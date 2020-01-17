#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

###############################################################################
# Function Declaration

# set_build_variable - take 3 parameters 
# the three parameters are 
function set_build_variable {
    if [ ! -z "${3}" ] # check to see if the variable is already not empty.
    then 
        if [ ! -z "${3}" ]
        then 
            export $1=$3
        else
            export $1=$2
        fi 
        debugging "Environment Variable is set [$(env | grep -i ${1})]"
    else 
        debugging "Environment Variable is already set [$(env | grep -i ${1})]"
    fi
    
}

# set_build_type - sets the type of the build and the build id
# 1 - tag 
# 2 - github_ref
# 3 - github_sha 
function set_build_type {
    if [ "${BUILD_ID}" == *"Integration"* ] || [ -z "${BUILD_ID}" ]
    then
        if [ ! [ "${1}" == "--" ] ]
        then 
            # not empty and is tag build
            if [[ "${1}" == *"RC"* ]]
            then
                export BUILD_TYPE="RELEASE_CANDIDATE"
            else
                export BUILD_TYPE="RELEASE"
            fi
            set_build_variable BUILD_ID "" "${1}"
        fi
    else 
        # SNAPSHOT build
        set_build_variable BUILD_ID "" "Integration_${2}_${3}"
        export BUILD_TYPE="SNAPSHOT"
    fi
}

###############################################################################
# Setup Maven Options 

# Not all JVMs support - -XX:MaxPermSize=512m (don't set it)
if [ ! -f ~/.mavenrc ]
then 
    echo "MAVEN_OPTS='-Xmx2G -Xms1G -Djava.awt.headless=true '" > ~/.mavenrc
fi

# Threads that are allowed to run
# which are passed in with BUILD_THREAD
# we don't want to embed this in maven_opts as certain goals are not thread safe.
#
# Examples: 
#   1C (1Core)
#   4  (4 Threads)
THREAD_COUNT=
if [ ! -z "${BUILD_THREAD}" ]
then 
    THREAD_COUNT="-T ${BUILD_THREAD}"
fi

###############################################################################
# Setup Git - makes sure there is a gitignore which puts the log in this folder. 
debugging "Current directory is $(pwd)"

GIT_IGNORE='.gitignore'
if [ ! -f ${GIT_IGNORE} ]
then 
    echo 'logs/' > ${GIT_IGNORE} 
    echo '**/.cache' >> ${GIT_IGNORE} 
fi

###############################################################################
# Setup Default Variables
set_build_variable BUILD_VERSION "" "${TRAVIS_TAG}"
set_build_variable BUILD_VERSION "development" "$(echo ${GITHUB_REF} | sed 's|refs/heads/||g')"

set_build_variable GIT_BRANCH "${TRAVIS_PULL_REQUEST_BRANCH}" "${TRAVIS_BRANCH}"
set_build_variable GIT_BRANCH "${GITHUB_REF}" "${GITHUB_REF}"

set_build_variable GIT_COMMIT "${GITHUB_SHA}" "${GITHUB_SHA}"
set_build_variable GIT_COMMIT "${TRAVIS_COMMIT}" "${TRAVIS_COMMIT}"

# Embedded in the 
set_build_variable GIT_URL "" ${TRAVIS_REPO_SLUG}
set_build_variable GIT_URL "development" "${GITHUB_REPOSITORY}"

# Create Build ID - Check if integration build
# sets BUILD_TYPE and BUILD_ID (naturally one of these cannot be empty to hold true.)
# - Check type for Travis build
set_build_type "-${TRAVIS_TAG}-" "${GITHUB_REF}" "${GITHUB_SHA}"

# - Check type for GitHub Actions build
set_build_type "-${TRAVIS_TAG}-" "${TRAVIS_BUILD_NUMBER}" "${TRAVIS_BRANCH}"

# - Check type for LOCAL build
set_build_type "-${FHIR_GIT_TAG}-" "${FHIR_GIT_BUILD_NUMBER}" "${FHIR_GIT_BRANCH}"

export BUILD_DISPLAY_NAME = "${BUILD_ID}"

# Outputting JAVA_HOME
debugging "JAVA_HOME is [${JAVA_HOME}]"

# Reset to Original Directory
popd > /dev/null

# EOF