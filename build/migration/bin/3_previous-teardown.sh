#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

set -ex
set -o pipefail

# check_if_should_run - check if this (N-VERSION) should run.
check_if_should_run(){
    git tag --sort=-v:refname
    
    N_VERISON="${1}"
12:14:34-paulbastide@pauls-mbp:~/git/wffh/2021/fhir$ git log $(git describe --tags --abbrev=0)..HEAD --oneline --name-only fhir-persistence-schema/src/main/java
a1c1e1fd43 Merge pull request #2487 from IBM/issue-2354
3ea5c86f24 Cleanly handle tablespace removal error
fhir-persistence-schema/src/main/java/com/ibm/fhir/schema/app/Main.java
88319aa9f0 (origin/issue-2211, issue-2211) Support revocation of tenant keys #2211
fhir-persistence-schema/src/main/java/com/ibm/fhir/schema/app/Main.java
fhir-persistence-schema/src/main/java/com/ibm/fhir/schema/app/util/CommonUtil.java
}

###############################################################################
# Check if the workspace is set.
if [ -z "${WORKSPACE}" ]
then 
    echo "The WORKSPACE value is unset"
    exit -1
fi 

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the reindex/bin directory
cd "${WORKSPACE}"

check_if_should_run "${1}"

# Reset to Original Directory
popd > /dev/null

# EOF
###############################################################################