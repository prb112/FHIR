#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -e
set +x

# create compose
pre_integration(){
    PREVIOUS_VERSION="${1}"
    cleanup
    setup_docker
    config
    bringup "${PREVIOUS_VERSION}"
}

# setup_docker - setup docker
setup_docker(){
    pushd $(pwd) > /dev/null 
    cd ${WORKSPACE}/fhir/build/migration/postgres
    mkdir -p ${WORKSPACE}/fhir/build/migration/postgres/workarea/volumes/dist/db
    docker build -t test/fhir-postgres resources/
    popd
}

# config - update configuration
config(){
    DIST="${WORKSPACE}/fhir/build/migration/postgres/workarea/volumes/dist"

    echo "Create the db volume..."
    mkdir -p ${DIST}/db

    # Setup the Configurations for Migration
    echo "Copying fhir configuration files..."
    mkdir -p ${DIST}/config
    cp -pr ${WORKSPACE}/prev/fhir-server/liberty-config/config $DIST
    cp -pr ${WORKSPACE}/prev/fhir-server/liberty-config-tenants/config/* $DIST/config

    echo "Copying test artifacts to install location..."
    USERLIB="${DIST}/userlib"
    mkdir -p "${USERLIB}"
    find ${WORKSPACE}/prev/conformance -iname 'fhir-ig*.jar' -not -iname 'fhir*-tests.jar' -not -iname 'fhir*-test-*.jar' -exec cp -f {} ${USERLIB} \;
    find ${WORKSPACE}/prev/operation/fhir-operation-test/target -iname '*.jar' -exec cp -f {} ${USERLIB} \;

    echo "Copying over the overrides for the datasource"
    mkdir -p ${DIST}/overrides
    cp ${WORKSPACE}/prev/fhir-server/liberty-config/configDropins/disabled/datasource-postgres.xml ${DIST}/overrides
    cp -p ${WORKSPACE}/prev/fhir-server/liberty-config/configDropins/disabled/datasource-derby.xml ${DIST}/overrides

    # Move over the test configurations
    echo "Copying over the fhir-server-config.json and updating publishing"
    cp -f ${DIST}/config/default/fhir-server-config-postgres.json ${DIST}/config/default/fhir-server-config.json

    echo "Reporting the files in the 'userlib' folder:"
    find ${DIST}
    echo ""
}

# cleanup - cleanup existing docker
cleanup(){
    # Stand up a docker container running the fhir server configured for integration tests
    echo "Bringing down any containers that might already be running as a precaution"
    docker-compose kill
    docker-compose rm -f
}

# bringup
bringup(){
    PREVIOUS_VERSION="${1}"
    echo "Previous Version: ${PREVIOUS_VERSION}"
    echo "Bringing up containers >>> Current time: " $(date)
    # Startup db

    export IMAGE_VERSION="${PREVIOUS_VERSION}"
    IMAGE_VERSION="${PREVIOUS_VERSION}" docker-compose build
    docker-compose up --remove-orphans -d db
    cx=0
    echo "Debug Details >>> "
    docker container inspect postgres_db_1 | jq -r '.[] | select (.Config.Hostname == "postgres").State.Status'
    echo "Debug All Details >>> "
    docker container inspect postgres_db_1 | jq -r '.[]'

    while [ $(docker container inspect postgres_db_1 | jq -r '.[] | select (.Config.Hostname == "postgres").State.Status' | wc -l) -gt 0 ] && [ $(docker container inspect postgres_db_1 | jq -r '.[] | select (.Config.Hostname == "postgres").State.Health.Status' | grep starting | wc -l) -eq 1 ]
    do
        echo "Waiting on startup of db ${cx}"
        cx=$((cx + 1))
        if [ ${cx} -ge 300 ]
        then
            echo "Failed to start"
            break
        fi
        sleep 10
    done

    echo ">>> Persistence >>> previous is being run"
    echo 'change-password' > tenant.key
    java -jar ${WORKSPACE}/prev/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
        --db-type postgres --prop db.host=localhost --prop db.port=50000 --prop db.database=fhirdb \
        --prop user=postgresinst1 --prop password=change-password \
        --create-schemas
    java -jar ${WORKSPACE}/prev/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
        --db-type postgres --prop db.host=localhost --prop db.port=50000 --prop db.database=fhirdb \
        --prop user=postgresinst1 --prop password=change-password \
        --update-schema
    java -jar ${WORKSPACE}/prev/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
        --db-type postgres --prop db.host=localhost --prop db.port=50000 --prop db.database=fhirdb \
        --prop user=postgresinst1 --prop password=change-password \
        --allocate-tenant default --tenant-key-file tenant.key --grant-to fhirserver

    # Startup FHIR
    docker-compose up --remove-orphans -d fhir
    cx=0
    while [ $(docker container inspect postgres_fhir_1 | jq -r '.[] | select (.Config.Hostname == "fhir").State.Status' | wc -l) -gt 0 ] && [ $(docker container inspect postgres_fhir_1  | jq -r '.[] | select (.Config.Hostname == "fhir").State.Health.Status' | grep running | wc -l) -eq 1 ]
    do
        echo "Waiting on startup of fhir ${cx}"
        cx=$((cx + 1))
        if [ ${cx} -ge 300 ]
        then
            echo "Failed to start fhir"
        fi
        sleep 1
    done

    # Gather up all the server logs so we can trouble-shoot any problems during startup
    pre_it_logs=${WORKSPACE}/pre-it-logs
    rm -rf ${pre_it_logs} 2>/dev/null
    mkdir -p ${pre_it_logs}

    zip_file=${WORKSPACE}/pre-it-logs.zip
    rm -f ${zip_file}

    echo ""
    echo "Docker container status:"
    docker ps -a

    containerId=$(docker ps -a | grep postgres_fhir_1 | cut -d ' ' -f 1)
    if [[ -z "${containerId}" ]]
    then
        echo "Warning: Could not find the fhir container!!!"
    else
        echo "fhir container id: ${containerId}"

        # Grab the container's console log
        docker logs ${containerId} > ${pre_it_logs}/docker-console.txt

        echo "Gathering pre-test server logs from docker container: ${containerId}"
        docker cp -L ${containerId}:/logs ${pre_it_logs}
    fi

    # Wait until the fhir server is up and running...
    echo "Waiting for fhir-server to complete initialization..."
    healthcheck_url='https://localhost:9443/fhir-server/api/v4/$healthcheck'
    tries=0
    status=0
    while [ $status -ne 200 -a $tries -lt 30 ]; do
        tries=$((tries + 1))

        set +o errexit
        cmd="curl -k -o ${WORKSPACE}/health.json --max-time 5 -I -w "%{http_code}" -u fhiruser:change-password $healthcheck_url"
        echo "Executing[$tries]: $cmd"
        status=$($cmd)
        set -o errexit

        echo "Status code: $status"
        if [ $status -ne 200 ]
        then
            echo "Sleeping 30 secs..."
            sleep 30
        fi
    done

    if [ $status -ne 200 ]
    then
        echo "Could not establish a connection to the fhir-server within $tries REST API invocations!"
        exit 1
    fi

    echo "The fhir-server appears to be running..."
}

###############################################################################

cd build/migration/postgres
pre_integration "${1}"

# EOF
###############################################################################