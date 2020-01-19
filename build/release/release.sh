
set -eu -o pipefail

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# deploys the binaries to bintray

# Store the current directory to reset to
pushd $(pwd) > /dev/null

# Change to the release directory
cd "$(dirname ${BASH_SOURCE[0]})"

# Import Scripts
source "$(dirname '$0')/logging.sh"
source "$(dirname '$0')/release.properties"

# Basic information
SCRIPT_NAME="$(basename ${BASH_SOURCE[0]})"
debugging "Script Name is ${SCRIPT_NAME}"

# Reset to Original Directory
popd > /dev/null

###############################################################################
# Function Declarations:

# deploy_bintray - executes mvn with a set of goals
function deploy_bintray { 
    announce "${FUNCNAME[0]}"
    PROJECT_PATH="$1"
    PROFILES="-Pdeploy-bintray,fhir-javadocs"
    TYPE="${2}"

    mvn ${THREAD_COUNT} -ntp -B ${PROFILES} deploy -f ${PROJECT_PATH} -Dbintray.repo=ibm-fhir-server-${TYPE} -DskipTests -s build/release/.m2/settings.xml
    check_and_fail $? "${FUNCNAME[0]} - stopped - ${PROJECT_PATH}"
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
        TYPE="snapshots"
        deploy_bintray "fhir-examples" "${TYPE}"
        deploy_bintray "fhir-tools" "${TYPE}"
        deploy_bintray "fhir-parent" "${TYPE}"
        header_line
    ;;
    RELEASE) 
        TYPE="releases"
        deploy_bintray "fhir-examples" "${TYPE}"
        deploy_bintray "fhir-tools" "${TYPE}"
        deploy_bintray "fhir-parent" "${TYPE}"
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

# Synch to Maven Central 

# EOF