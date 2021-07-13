#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set +x

# create compose
pre_integration(){
    config
    bringup
}

# config - update configuration
config(){
    DIST="${WORKSPACE}/fhir/build/migration/db2/workarea/volumes/dist"

    echo "Create the db volume..."
    mkdir -p ${DIST}/db

    # Setup the Configurations for Migration
    echo "Copying fhir configuration files..."
    rm -rf ${DIST}/config
    mkdir -p ${DIST}/config
    cp -pr ${WORKSPACE}/fhir/fhir-server/liberty-config/config $DIST
    cp -pr ${WORKSPACE}/fhir/fhir-server/liberty-config-tenants/config/* $DIST/config

    echo "Copying test artifacts to install location..."
    USERLIB="${DIST}/userlib"
    rm -rf ${DIST}/userlib
    mkdir -p "${USERLIB}"
    find ${WORKSPACE}/fhir/conformance -iname 'fhir-ig*.jar' -not -iname 'fhir*-tests.jar' -not -iname 'fhir*-test-*.jar' -exec cp -f {} ${USERLIB} \;
    find ${WORKSPACE}/fhir/operation/fhir-operation-test/target -iname '*.jar' -exec cp -f {} ${USERLIB} \;

    echo "Copying over the overrides for the datasource"
    mkdir -p ${DIST}/overrides
    rm -rf ${DIST}/overrides
    cp ${WORKSPACE}/fhir/fhir-server/liberty-config/configDropins/disabled/datasource-db2.xml ${DIST}/overrides
    cp -p ${WORKSPACE}/fhir/fhir-server/liberty-config/configDropins/disabled/datasource-derby.xml ${DIST}/overrides

    # Move over the test configurations
    echo "Copying over the fhir-server-config.json and updating publishing"
    jq '.fhirServer.notifications.nats.enabled = false' ${DIST}/config/default/fhir-server-config-db2.json > ${DIST}/config/default/fhir-server-config-t.json
    jq '.fhirServer.persistence.datasources.default.tenantKey = "change-password"' ${DIST}/config/default/fhir-server-config-t.json > ${DIST}/config/default/fhir-server-config.json

    echo "Reporting the files in the 'userlib' folder:"
    find ${DIST}
    echo ""
}

# bringup
bringup(){
    echo "Bringing up containers >>> Current time: " $(date)
    # Startup db
    IMAGE_VERSION=latest docker-compose build
    docker-compose up --remove-orphans -d db
    cx=0
    echo "Debug Details >>> "
    docker container inspect db2_db_1 | jq -r '.[] | select (.Config.Hostname == "db2").State.Status'
    echo "Debug All Details >>> "
    docker container inspect db2_db_1 | jq -r '.[]'

    while [ $(docker container inspect db2_db_1 | jq -r '.[] | select (.Config.Hostname == "db2").State.Status' | wc -l) -gt 0 ] && [ $(docker container inspect db2_db_1 | jq -r '.[] | select (.Config.Hostname == "db2").State.Health.Status' | grep starting | wc -l) -eq 1 ]
    do
        echo "Waiting on startup of db ${cx}"
        cx=$((cx + 1))
        if [ ${cx} -ge 300 ]
        then
            echo "Failed to start"
            exit 100
        fi
        sleep 10
    done

    # Startup FHIR
    docker-compose up --remove-orphans -d fhir
    cx=0
    while [ $(docker container inspect db2_fhir_1 | jq -r '.[] | select (.Config.Hostname == "fhir").State.Status' | wc -l) -gt 0 ] && [ $(docker container inspect db2_fhir_1  | jq -r '.[] | select (.Config.Hostname == "fhir").State.Health.Status' | grep running | wc -l) -eq 1 ]
    do
        echo "Waiting on startup of fhir ${cx}"
        cx=$((cx + 1))
        if [ ${cx} -ge 300 ]
        then
            echo "Failed to start fhir"
        fi
        sleep 1
    done
}

###############################################################################

cd ${WORKSPACE}/build/migration/db2
pre_integration

# EOF
###############################################################################