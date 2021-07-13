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

        # Date YYYY-MM-DDTHH:MM:SSZ
        DATE_ISO=$(date +%Y-%m-%dT-%H:%M:%SZ)
        status=$(curl -o reindex.json -s -k -i --max-time 5 -I -w "%{http_code}" -u 'fhiruser:change-password' 'https://localhost:9443/fhir-server/api/v4/$reindex' -d "{\"resourceType\": \"Parameters\",\"parameter\":[{\"name\":\"resourceCount\",\"valueInteger\":100},{\"name\":\"tstamp\",\"valueString\":\"${DATE_ISO}\"}]}" -H 'Content-Type: application/fhir+json' -H 'X-FHIR-TENANT-ID: default')
        while [ $status -ne 200 ]
        do
            i=$((i+1))
            if [ $(cat reindex.json | grep -c "Reindex complete") -eq 1 ]
            then
                echo "${i}"
                break
            fi
            status=$(curl -o reindex.json -s -k -i --max-time 5 -I -w "%{http_code}" -u 'fhiruser:change-password' 'https://localhost:9443/fhir-server/api/v4/$reindex' -d "{\"resourceType\": \"Parameters\",\"parameter\":[{\"name\":\"resourceCount\",\"valueInteger\":100},{\"name\":\"tstamp\",\"valueString\":\"${DATE_ISO}\"}]}" -H 'Content-Type: application/fhir+json' -H 'X-FHIR-TENANT-ID: default')
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