#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -e
set -x

# startup_database - startup database
startup_database(){
    migration="${1}"
    if [ -f "${WORKSPACE}/fhir/build/migration/${migration}/2_compose.sh" ]
    then
        echo "Running [${migration}] setting up prerequisites"
        bash ${WORKSPACE}/fhir/build/migration/${migration}/2_compose.sh
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "${WORKSPACE}/fhir"

startup_database "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF 
###############################################################################