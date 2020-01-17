#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2019
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Manages the fhir build

# Global Settings are: 
# BUILD_TYPE for logging / execution purposes
# THREAD_COUNT = (described in more detail below)
# ACTION = Action Specific settings are covered in the case statement
# ACTION_PARAMS = list of parameters (described in case statement)

# BUILD_DATETIME
BUILD_DATETIME=`date +%FT%T`

# BUILD_TYPES
# - BRANCH - builds a specific branch (no-release)
# - PR - builds a specific pull_request (no-release)
# - TAG - builds from current tag to prior tag (release)
#       - MUST be since last SUCCESSFUL TAG build
# - REBUILD_RELEASE
BUILD_TYPE="$1"

# SOURCE DIRS CAPTURES THE DIRECTORIES WHICH ARE GOING TO BE USED 
# in deployment
SOURCE_DIRS=()



# build_source - Build source from a Project PATH
# Reference https://maven.apache.org/plugins/maven-source-plugin/
function build_source { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_source.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    mvn ${THREAD_COUNT} source:jar source:test-jar -f ${PROJECT_PATH} --log-file ${LOG_DIR}/${PROJECT_NAME}-build_source.log
    check_and_fail $? "build_source - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_source.log
}

# build_javadoc_jar - Build javadoc jar from a Project PATH
# Reference https://maven.apache.org/plugins/maven-javadoc-plugin/
function build_javadoc_jar { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_javadoc_jar.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    # intentionally not stopping on warnings... 
    mvn ${THREAD_COUNT} javadoc:jar javadoc:test-jar -f ${PROJECT_PATH} -DadditionalJOption=-Xdoclint:none --log-file ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_jar.log
    check_and_fail $? "build_javadoc_jar - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_jar.log
}

# build_javadoc_site - Build javadoc site aggregate from a Project PATH
# Reference https://maven.apache.org/plugins/maven-javadoc-plugin/
# 
# This function can take a long while (greater than 10 mintues)
# Add this to Travis - travis_wait 30 
function build_javadoc_site { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_javadoc_site.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    mvn ${THREAD_COUNT} javadoc:javadoc javadoc:test-javadoc -f ${PROJECT_PATH} -DadditionalJOption=-Xdoclint:none --log-file ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_site.log
    check_and_fail $? "build_javadoc_jar - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_javadoc_site.log
}

# build_source_javadoc - Build javadoc, site and source aggregate from a Project PATH
# Reference https://maven.apache.org/plugins/maven-javadoc-plugin/
function build_source_javadoc { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    # running fixup. 
    mvn ${THREAD_COUNT} javadoc:fix javadoc:test-fix -f ${PROJECT_PATH} --log-file ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc_fixup.log -DadditionalJOption=-Xdoclint:none -DfixTags=link
    check_and_fail $? "build_source_javadoc - fixup - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc_fixup.log

    # goals are combined to simplify the build 
    # should it be necesarry the above functions divide and conquer. 
    mvn ${THREAD_COUNT} source:jar source:test-jar javadoc:javadoc javadoc:test-javadoc javadoc:jar javadoc:test-jar -f ${PROJECT_PATH} -DadditionalJOption=-Xdoclint:none --log-file ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc.log
    check_and_fail $? "build_source_javadoc - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_source_javadoc.log
}

# build_install - Build install from a Project PATH
# Reference https://maven.apache.org/plugins/maven-install-plugin/
function build_install { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-build_install.log"

    PROJECT_PATH="$1"
    PROJECT_NAME="$2"

    # clean and install (do both now)
    mvn ${THREAD_COUNT} clean install -f ${PROJECT_PATH} --log-file ${LOG_DIR}/${PROJECT_NAME}-build_install.log
    check_and_fail $? "build_install - ${PROJECT_PATH}" ${LOG_DIR}/${PROJECT_NAME}-build_install.log
}


# files_changed - between two branches or two tags
# Parameters: 
#   TARGET_BRANCH
#   SOURCE_BRANCH 
function files_changed { 
    announce "${FUNCNAME[0]}" "${LOG_DIR}/${PROJECT_NAME}-files_changed.log"

    TARGET_BRANCH="${1}"
    SOURCE_BRANCH="${2}"
    CHANGED_FILES=`git log ${TARGET_BRANCH}..${SOURCE_BRANCH} --name-only --pretty=format:`
    for CHANGED_FILE in `echo ${CHANGED_FILES}`
    do 
        echo "Changed File: ${CHANGED_FILE}"
    done
}

# files_changed_by_source_folder - between two branches or two tags
# Parameters: 
#   TARGET_BRANCH
#   SOURCE_BRANCH 
function files_changed_by_source_folder { 
    echo "[Identifying the changed files][START]: "
    TARGET_BRANCH="${1}"
    SOURCE_BRANCH="${2}"
    
    pushd `pwd`
    cd ${TRAVIS_BUILD_DIR}
    git fetch origin ${SOURCE_BRANCH}:${SOURCE_BRANCH}
    for SOURCE_DIR in `find . -type d ${BUILD_ENV_TYPE_DEPTH} | grep -v '.git' | grep -v 'build' | grep -v '/docs'`
    do 
        #echo "Processing ${SOURCE_DIR}"
        SOURCE_DIR=`echo ${SOURCE_DIR} | sed 's|./||g'`
        FILES=`git log --relative=${SOURCE_DIR}/ --name-only ${TARGET_BRANCH}..${SOURCE_BRANCH} --pretty=format:`
        
        COUNT=0
        for FILE in ${FILES}
        do 
            #echo "- ${FILE}"
            COUNT=$((COUNT+1))
        done

        if [ ${COUNT} -ne 0 ]
        then 
            # populate the source_dirs 
            echo "[SOURCE_DIR] -> [${SOURCE_DIR}][FILES: ${COUNT}]"
            SOURCE_DIRS+=("${SOURCE_DIR}")
        fi 
    done 
    popd

    echo "[Identifying the changed files][DONE] "
    echo "[SOURCE_DIRS] changed : [${SOURCE_DIRS[@]}]"
    
}

# tag_release - create a tag 
# Parameters: 
#   VERSION - MAJOR.MINOR.INCREMENTALVERSION
# Reference 
#   https://maven.apache.org/pom.html#Dependency_Version_Requirement_Specification
#   https://www.mojohaus.org/versions-maven-plugin/version-rules.html
#   https://cwiki.apache.org/confluence/display/MAVENOLD/Versioning
function tag_release { 
    announce "${FUNCNAME[0]}" "tag_release"

    VERSION="${1}"
    git tag "release-${VERSION}" -m "Releasing version ${VERSION} - ${BUILD_DATETIME}"
    git push --tags 
}





# shortcut_doc_only_update - shortcuts the build if there are only files 
function shortcut_doc_only_update {
    echo "Checking if we need to shortcut"
}

# build_type - set the global build type
function build_type { 
    TYPE="${1}"
    echo "Checking Type - ${1}"
    TAGS=`git tag --list`
}





# comment_on_pull_request - adds a comment on the pull request.
# the token is encrypted following https://medium.com/@preslavrachev/using-travis-for-secure-building-and-deployment-to-github-5a97afcac113
function comment_on_pull_request {
    COMMENT_IN=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.com"
    
    COMMENT="{\"body\": \"${COMMENT_IN}\"}"
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/comments"

    if [[ "${TRAVIS_PULL_REQUEST_BRANCH}" != "" && "${TRAVIS_EVENT_TYPE}" == "pull_request" && "${TRAVIS_PULL_REQUEST}" != "false" ]]
    then
        curl -H "Authorization: token ${GITHUB_OAUTH_TOKEN}" -X POST -d "${COMMENT}" "${API_URL}"
    fi
}

# label_pr_with_status - labels the build with a current status
#
# URL: https://developer.github.com/v3/issues/labels/#add-labels-to-an-issue
# POST /repos/:owner/:repo/issues/:issue_number/labels
#
# Parameter: 
# - BUILD_LABEL 
function label_pr_with_status {
    BUILD_LABEL=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.com"
    if [[ "${TRAVIS_APP_HOST}" == *"ibm.com"* ]]
    then
        BASE_URL="api.github.com"
    fi

    LABEL="{  \
            \"labels\": [ \
                ${BUILD_LABEL}
                ] \
        }"
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/labels"

    if [[ "${TRAVIS_EVENT_TYPE}" == "pull_request" ]]
    then
        echo "API_URL: ${API_URL}"
        echo "LABEL: ${LABEL}"

        curl -H "Authorization: token ${GITHUB_OAUTH_TOKEN}" -H "User-Agent: ibm-fhir-cicd" -X POST -d "${LABEL}" "${API_URL}"
    fi
}

# comment_on_pull_request_with_log - adds a comment on the pull request.
# the token is encrypted following https://medium.com/@preslavrachev/using-travis-for-secure-building-and-deployment-to-github-5a97afcac113
function comment_on_pull_request_with_log {
    COMMENT_IN=$1

    # quick switch between github.com and ibm.com domains.
    BASE_URL="api.github.com"

    # Log File -> Comment is wrapped. 
    # 50K chars seem enough
    LOG_FILE="build/logs/$(ls -A1tr build/logs | grep -v diag.log | tail -n 1)"

    # Expressions
    # - -e 's|"|\&#39;|g'
    # - -e 's|'|\&#39;|g'
    #LOG_DETAIL=`tail -c 50000 ${LOG_FILE} | sed -e 's|&|\&amp;|g'  -e 's|<|\&lt;|g' -e 's|>|\&gt;|g' -e 's|\"|\&quot;|g' -e "s|\'|\&quot;|g" -e 's|\n|\\\\r\\\\n|g' -e "s|'||g" -e 's|"||g' | tr '\t' '    ' | sed 's|$|\\\\r\\\\n|g'| awk ' /$/ {print}' ORS=' '`
    LOG_DETAIL="Please refer to build log. "
    COMMENT_UPDATE="${COMMENT_IN}<br><hr>\r\n\`\`\`${LOG_DETAIL}\`\`\`"
    COMMENT=`echo "{\"body\": \"${COMMENT_UPDATE}\"}"  `
    
    # for debug - use --- 
    # echo "--> [DEBUG JSON --> " 
    # echo "${COMMENT}"
    
    API_URL="https://${BASE_URL}/repos/${TRAVIS_REPO_SLUG}/issues/${TRAVIS_PULL_REQUEST}/comments"

    if [[ "${TRAVIS_PULL_REQUEST_BRANCH}" != "" && "${TRAVIS_EVENT_TYPE}" == "pull_request" && "${TRAVIS_PULL_REQUEST}" != "false" ]]
    then
        echo "API_URL: ${API_URL}"
        echo "LABEL: ${COMMENT}"
        curl -H "Authorization: token ${GITHUB_OAUTH_TOKEN}" -H "User-Agent: ibm-fhir-cicd" -X POST -d "${COMMENT}" "${API_URL}"
    fi
}



###############################################################################
# Menu
ACTION="$2"
case $ACTION in
    setup) 
        mvn_setup
    ;;
    diagnostics) 
        diagnostic_details
        header_line
    ;;
    build_install) 
        PROJECT_POM_ARGS="$3"
        PROJECT_NAME="$4"
        build_install "${PROJECT_POM_ARGS}" "${PROJECT_NAME}"
        header_line
    ;;
    build_clean)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_clean ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    set_version)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        PROJECT_VERSION="$5"
        set_version ${PROJECT_POM} ${PROJECT_NAME} ${PROJECT_VERSION}
        header_line
    ;;
    build_security_check) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_security_check ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    build_javadoc) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_javadoc_jar ${PROJECT_POM} ${PROJECT_NAME}
        build_javadoc ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    build_source) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_source ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    build_source_javadoc)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        build_source_javadoc ${PROJECT_POM} ${PROJECT_NAME}
        header_line
    ;;
    tag_release) 
        VERSION=$2
        tag_release ${VERSION}
    ;;
    assemble)
        # identify the projects which changed  
        files_changed_by_source_folder "${TRAVIS_BRANCH}" "${TRAVIS_PULL_REQUEST_BRANCH}"
        header_line
    ;;
    release) 
        echo "BIN TRAY"
    ;;
    sync_to_maven_central)
        echo "maven central"
    ;; 
    regression)
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        regression        
        build_clean "${PROJECT_POM}" "${PROJECT_NAME}"
        build_install "${PROJECT_POM}" "${PROJECT_NAME}"
        header_line
    ;;
    download) 
        PROJECT_POM="$3"
        PROJECT_NAME="$4"
        download "${PROJECT_POM}" "${PROJECT_NAME}"
        header_line
    ;;
    comment_on_pull_request) 
        comment_on_pull_request "${3}"
        header_line
    ;;
    comment_on_pull_request_with_log)
        comment_on_pull_request_with_log "${3}"
        header_line
    ;;
    label_pr_with_status)
        LABELS="${3}"
        label_pr_with_status 
    ;;
    *)
        echo "invalid function called, dropping through "
    ;;
esac 
# EOF 