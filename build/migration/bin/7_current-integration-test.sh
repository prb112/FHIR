#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

run_tests(){
    # The integration tests may be overriden completely, or fall through to the default. 
    migration="${1}"

    if [ ! -z "${migration}" ] && [ -f "build/migration/${migration}/7_current-integration-test.sh" ]
    then 
        echo "Running [${migration}] specific integration tests"
        bash build/migration/${migration}/7_current-integration-test.sh
    else
        # Runs the migration tests
        mvn -B test -f fhir-server-test -DskipWebSocketTest=true --no-transfer-progress \
            -DskipTests=false | tee build/migration/${migration}/workarea/${migration}-current.log
    fi
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

export BASE="$(pwd)"

# Change to the migration/bin directory
cd "fhir/"

$BASE/build/common/wait_for_it.sh
run_tests "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################