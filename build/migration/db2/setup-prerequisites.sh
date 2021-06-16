#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex

# cache_schema_tool - cache schema tool
cache_schema_tool() {
    VERSION="${1}"
    curl -L --output workarea/fhir-persistence-schema-${VERSION}-cli.jar https://repo1.maven.org/maven2/com/ibm/fhir/fhir-persistence-schema/${VERSION}/fhir-persistence-schema-${VERSION}-cli.jar

    java -jar workarea/fhir-persistence-schema-${VERSION}-cli.jar
    RC="$(echo $?)"
    if [ "${RC}" != "0" ]
    then
        echo "Schema tool is invalid, short circuiting, and returning non-zero exit code"
        exit 1
    fi
}

# setup_db_docker - setup docker
setup_db_docker() {
    docker build -t test/fhir-db2 resources
}

# pull_fhir_docker - 
pull_fhir_docker() {
    VERSION="${1}"
    docker pull ibmcom/ibm-fhir-server:"${VERSION}"
}

cache_schema_tool ${1}

setup_db_docker

pull_fhir_docker ${1}

# EOF