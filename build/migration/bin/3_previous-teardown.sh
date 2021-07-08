#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

shutdown_fhir(){
    docker-compose down fhir
}

###############################################################################
# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the migration/bin directory
cd ${WORKSPACE}/prev/build/migration/db2/"

shutdown_fhir

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################