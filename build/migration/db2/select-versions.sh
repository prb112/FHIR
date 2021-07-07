#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

# generate_diff_log - Generate log that highlights the changes
generate_diff_log() {
    DATASOURCE="${1}"
    TAG="${2}"
    PREVIOUS_TAG="${3}"

    # Schema Changes
    echo "# fhir-persistence-schema" > ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
    git diff --name-only ${TAG}..${PREVIOUS_TAG} fhir-persistence-schema/src/main | sort -u >> ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
    echo

    # Structural Changes to Database Support
    echo "# fhir-database-utils" >> ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
    git diff --name-only ${TAG}..${PREVIOUS_TAG} fhir-database-utils/src/main | sort -u >> ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
    echo

    # Automation Changes
    echo "# build/migration" >> ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
    git diff --name-only ${TAG}..${PREVIOUS_TAG} build/migration | sort -u >> ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
    echo

    # JDBC Changes (e.g. how we extract or retrieve the data)
    # important for reindexing (Extract Search Parameter Values)
    echo "# fhir-persistence-jdbc" >> ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
    git diff --name-only ${TAG}..${PREVIOUS_TAG} fhir-persistence-jdbc/src/main | sort -u >> ${WORKSPACE}/build/migration/${DATASOURCE}/workarea/version-diff-${TAG}-${PREVIOUS_TAG}.log
}

# pick_version - picks the version based on the minor. 
# From the Matrix, at least one is passed in: release: ['release', 'minor']
pick_version() {
    INPUT="${1}"
    if [ "release" == "${INPUT}" ]
    then
        VERSION=$(git tag --sort=-v:refname | grep -v '-' | head -1)
    elif [ "previous" == "${INPUT}" ]
    then
        MAJOR_VERSION=$(for TAG in  $(git tag --sort=-v:refname | grep -v '-')
        do
            MAJOR=$(echo $TAG | sed 's|\.| |g' | awk '{print $1}')
            MINOR=$(echo $TAG | sed 's|\.| |g' | awk '{print $2}')
            SEP='.'
            if [ -z "${MINOR}" ]
            then
                SEP=''
            fi
            echo $MAJOR$SEP$MINOR
        done | sort -ur | head -2 | tail -1)
        VERSION=$(git tag --sort=-v:refname | grep -v '-' |  grep  -i ${MAJOR_VERSION} | head -n 1)
    fi
    echo ${VERSION}
}


echo "Changes to 'fhir-persistence-schema' between tags"
PREVIOUS_TAG=HEAD
for TAG in $(git tag --sort=-v:refname)
do
    COUNT=$(git diff --name-only ${TAG}..${PREVIOUS_TAG} fhir-persistence-schema/src/main | sort -u | wc -l)
    echo "${PREVIOUS_TAG}..${TAG}|${COUNT}"
    PREVIOUS_TAG=${TAG}
done


echo "Changes to 'fhir-database-utils' between tags"
PREVIOUS_TAG=HEAD
for TAG in $(git tag --sort=-v:refname)
do
    DB_UTIL_COUNT=$(git log ${TAG}..${PREVIOUS_TAG} --oneline --name-only fhir-database-utils/src/main | grep -i fhir-database-utils | sort -u | wc -l)
    FPS_COUNT=$(git diff --name-only ${TAG}..${PREVIOUS_TAG} fhir-persistence-schema/src/main | sort -u | wc -l)
    echo "${PREVIOUS_TAG}..${TAG}|${DB_UTIL_COUNT}|${FPS_COUNT}"
    PREVIOUS_TAG=${TAG}
done

echo "Changes to 'build/migration' between tags"
PREVIOUS_TAG=HEAD
for TAG in $(git tag --sort=-v:refname)
do
    COUNT=$(git log ${TAG}..${PREVIOUS_TAG} --oneline --name-only build/migration | grep -i build/migration | sort -u | wc -l)
    echo "${PREVIOUS_TAG}..${TAG}|${COUNT}"
    PREVIOUS_TAG=${TAG}
done



['MINOR-1', 'MINOR-2']



git log $(git describe --tags --abbrev=0)..HEAD --oneline --name-only fhir-persistence-schema/src/main/java
git tag --sort=-v:refname

# figure out versions
wait_for_it(){
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
# Check if the workspace is set.
if [ -z "${WORKSPACE}" ]
then 
    echo "The WORKSPACE value is unset"
    exit -1
fi 

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the reindex/bin directory
cd "${WORKSPACE}"

run_tests "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################