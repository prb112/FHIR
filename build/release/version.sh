#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# set the version 

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

# Reset to Original Directory
popd > /dev/null

###############################################################################
# Function Declarations:

# set_version - set version per latest build_id 
# Parameters: 
#   PROJECT
#   NEW_VERISON
# Reference https://www.mojohaus.org/versions-maven-plugin/set-mojo.html
# use versions:revert - option to revert the change. 
function set_version { 
    announce "${FUNCNAME[0]} - started - ${PROJECT_PATH}"
    PROJECT_PATH="$0"
    OLD_VERSION='*'

    # If we need build_numbers added, uncomment the next. 
    NEW_VERSION="${BUILD_ID}"
    mvn ${THREAD_COUNT} versions:set -ntp -B -f ${PROJECT_PATH} -DoldVersion=${OLD_VERSION} -DnewVersion=${NEW_VERSION}
    check_and_fail $? "${FUNCNAME[0]} - stopped - ${PROJECT_PATH}"
}

# change_all_versions - change all versions 
function change_all_versions { 
    set_version "fhir-examples"
    set_version "fhir-tools"
    mvn ${THREAD_COUNT} clean package install -ntp -B -N -f fhir-parent 
    set_version "fhir-parent" 
}

###############################################################################
# check to see if mvn exists
if which mvn | grep -i mvn
then 
    debugging 'mvn is found!'
else 
    warn 'mvn is not found!'
fi

#RELEASE_CANDIDATE or RELEASE or SNAPSHOT or EXISTING
case $BUILD_TYPE in
    RELEASE_CANDIDATE) 
        change_all_versions
        header_line
    ;;
    RELEASE) 
        change_all_versions
        header_line
    ;;
    SNAPSHOT) 
        info "SNAPSHOT build is not set"
        header_line
    ;;
    EXISTING)
        info "EXISTING build is not set"
        header_line
    ;;
    *)
        warn "invalid function called, dropping through "
    ;;
esac

# EOF