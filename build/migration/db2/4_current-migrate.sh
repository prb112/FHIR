#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

###############################################################################

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the migration/bin directory
cd "fhir/build/migrate/db2"

echo ">>> Persistence >>> current is being run"
echo 'change-password' > tenant.key
java -jar ${WORKSPACE}/prev/fhir-persistence-schema/target/fhir-persistence-schema-*-cli.jar \
    --db-type db2 --prop db.host=localhost --prop db.port=50000 --prop db.database=fhirdb \
    --prop user=db2inst1 --prop password=change-password \
    --update-schema

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################