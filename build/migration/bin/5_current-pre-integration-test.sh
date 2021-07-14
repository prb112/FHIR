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

    if [ ! -z "${migration}" ] && [ -f ${WORKSPACE}/fhir/build/migration/${migration}/5_current-pre-integration-test.sh ]
    then 
        echo "Running [${migration}] pre-integration-test"
        bash ${WORKSPACE}/fhir/build/migration/${migration}/5_current-pre-integration-test.sh
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