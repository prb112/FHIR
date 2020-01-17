#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2019, 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Sets up the details for bintray modules/packages on BinTray
# You must set to use your API Key and API user in the curl request. 
# BINTRAY_USER=change-me
# BINTRAY_PASSWORD=change-me

BINTRAY_REPO_OWNER=ibm-watson-health

# Distination Repository Name (can also be ibm-fhir-server-snapshots)
BINTRAY_REPO_NAME=ibm-fhir-server-releases
BINTRAY_VERSION=4.0.0

PKGS=$(find . -type d  -name 'fhir-*' -maxdepth 1 | sed 's|.\/||g')

# If you need to create a single module, then do something like the following. 
# PKGS=( "fhir-parent" )
for BINTRAY_PKG_NAME in ${PKGS[@]}
do 
echo ${BINTRAY_PKG_NAME}

# Setup the JSON Data
JSON_DATA=`
cat << EOF
{
  "name": "${BINTRAY_PKG_NAME}",
  "desc": "This package is part of the IBM FHIR Server project.",
  "labels": ["ibm-fhir-server", "fhir"],
  "licenses": ["Apache-2.0"],
  "custom_licenses": [],
  "vcs_url": "https://github.com/IBM/FHIR.git",
  "website_url": "http://ibm.github.io/FHIR",
  "issue_tracker_url": "https://github.com/IBM/FHIR/issues",
  "github_repo": "IBM/FHIR",
  "public_download_numbers": false,
  "public_stats": false
}
EOF
`

curl -XPOST -u${BINTRAY_USER}:${BINTRAY_PASSWORD} https://api.bintray.com/packages/ibm-watson-health/${BINTRAY_REPO_NAME}/ --data "${JSON_DATA}" -H "Content-Type: application/json"

done 

# EOF