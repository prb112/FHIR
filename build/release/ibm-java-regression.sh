#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# regression - on jdks without creating yet another build. 
# - OpenJDK 8, OpenJDK 11 - from https://adoptopenjdk.net/.
#   Skipped - 'openjdk11' is the default in `.travis.yml`
# - IBM SDK, Java Technology Edition, Version 8. 8.0.5.40
#   JDKS_VERS='https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/baseline/baseline_version.txt'
#   JDKS_META='https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/meta/sdk/linux/x86_64/index.yml'
# 
# Reference -> https://github.com/michaelklishin/jdk_switcher
#
# colon is in the following so we can skip (if already done, or provide an unexpected outcome)
JDKS=('openjdk11:skip', 'openjdk8:skip', 'ibmjdk8:ibm' )
function regression { 
    echo "Starting the REGRESSION tests on specified jdks"
    
    FILE='/home/travis/ibm-java/ibm-java-archive.bin'

    for JDK in "${JDKS[@]}"
    do 
        JDK_INTERIM=`echo $JDK | sed 's|:| |g' | awk '{print $1}'`
        JDK_TYPE=`echo $JDK | sed 's|:| |g' | awk '{print $2}'`
    
        if [[ "${JDK_TYPE}" = "default" ]]
        then 
            echo "AdoptOpenJDK JDK -> ${JDK_INTERIM}"
            #jdk_switcher use $JDK_INTERIM
        elif [[ "${JDK_TYPE}" = "ibm" ]]
        then 
            echo "IBM JDK -> ${JDK_INTERIM}"
            
            # Setup Repsonse.properties
            echo "INSTALLER_UI=silent" > /home/travis/response.properties
            echo "USER_INSTALL_DIR=/home/travis/ibm-java/java80" >> /home/travis/response.properties
            echo "LICENSE_ACCEPTED=TRUE" >> /home/travis/response.properties
            
            chmod +x ${FILE}

            # setup directory and install 
            mkdir -p /home/travis/ibm-java/java80
            ${FILE} -i silent -f /home/travis/response.properties 
            
            export JAVA_HOME="${USER_INSTALL_DIR}"
            export PATH="${JAVA_HOME}/bin:${PATH}"
        fi
       
        header_line
    done 
}

# downloads the jdk 
# - SHA_SUM - the sha_sum in the trusted variable that is passed in here. 
# - DOWNLOAD_URI - uri
#
# Example:
# 1.8.0_sr5fp40:
#    uri: https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/8.0.5.40/linux/x86_64/ibm-java-sdk-8.0-5.40-x86_64-archive.bin
#    sha256sum: bc53faf476655e565f965dab3db37f9258bfc16bb8c5352c93d43d53860b79d3
# Originally from https://public.dhe.ibm.com/ibmdl/export/pub/systems/cloud/runtimes/java/meta/sdk/linux/x86_64/index.yml 
function download {
    announce "${FUNCNAME[0]}"

    SHA_SUM="$1"
    DOWNLOAD_URI="$2"
    
    # Create the directory
    mkdir -p /home/travis/ibm-java

    FILE='/home/travis/ibm-java/ibm-java-archive.bin'
    if [ -f "${FILE}" ]
    then 
        # check sha256sum in the cached file
        LOCAL_SHA_SUM="$(sha256sum ${FILE} | awk '{print $1}')"
        echo "ACTUAL_SHA_SUM -> ${LOCAL_SHA_SUM}"
        echo "EXPECTED_SHA_SUM -> ${SHA_SUM}"
        if [ "${SHA_SUM}" != "${LOCAL_SHA_SUM}" ]
        then
            curl -o ${FILE} "${DOWNLOAD_URI}"
        fi

        # make sure it's executable. 
        chmod +x ${FILE}

    else 
        curl -o ${FILE} "${DOWNLOAD_URI}"
    fi 

}

# EOF