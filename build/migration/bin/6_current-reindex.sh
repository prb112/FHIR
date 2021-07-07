#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

run_reindex(){
    migration="${1}"

    if [ ! -z "${migration}" ] && [ -f "build/migration/${migration}/6_current-reindex.sh" ]
    then 
        echo "Running [${migration}] specific integration tests"
        bash build/migration/${migration}/6_current-reindex.sh
    else
        # Run the $reindex
        i=1
        until curl -s -k -i -u 'fhiruser:change-password' 'https://localhost:9443/fhir-server/api/v4/$reindex' -d '{"resourceType": "Parameters","parameter":[{"name":"resourceCount","valueInteger":100},{"name":"tstamp","valueString":"2021-07-02T16:40:00Z"}]}' -H 'Content-Type: application/fhir+json' -H 'X-FHIR-TENANT-ID: pglocal' | grep -q "Reindex complete"
        do
            echo $((++i))
        done
    fi
}

###############################################################################

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the migration/bin directory
cd "fhir/"

run_reindex "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################