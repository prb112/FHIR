#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

# migration_pre - executes for each migration pre integration steps
migration_pre(){
    migration="${1}"
    
    echo "Building the current docker image and the current java artifacts"
    pushd $(pwd) > /dev/null
    cd "${WORKSPACE}"
    mvn -T2C -B install --file fhir-examples --no-transfer-progress
    mvn -T2C -B install --file fhir-parent -DskipTests -P include-fhir-igs,integration --no-transfer-progress
    docker build -t test/fhir-db2 resources/
    popd > /dev/null

    if [ ! -z "${migration}" ] && [ -f build/migration/${migration}/pre-integration-test.sh ]
    then 
        echo "Running [${migration}] pre-integration-test"
        bash build/migration/${migration}/pre-integration-test.sh
    fi
}

###############################################################################

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the migration_pre
cd "${WORKSPACE}"

migration_pre "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################