#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2020
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

# Release enables tag release with 
# - Travis CI
# - GitHub Actions 

export JAVA_HOME=/Library/Java/JavaVirtualMachines/adoptopenjdk-11.jdk/Contents/Home/
GIT_TAG=""

# if release-

# if snapshot-

# clean

# set release versions 

# source

# javadoc

# site
mvn site -f fhir-parent

# deploy

# jacoco

# Synch to Maven Central 

# regression

# catch any exception 
diagnostic_details

# EOF