#!/usr/bin/env bash

###############################################################################
# (C) Copyright IBM Corp. 2021
#
# SPDX-License-Identifier: Apache-2.0
###############################################################################

if [ -d ~/fhir/build/migration/.cache ]
then
    echo "Listing the files in the cache: "
    find ~/fhir/build/migration/.cache
fi

if [ ! -f ~/fhir/build/migration/.cache/db.tgz ]
then
    echo "Skipping as the cache'd file does not exist"
    echo "env.migration_skip=true" >> $GITHUB_ENV
    exit 0
fi

if [ ! -z ~/fhir/build/migration/.cache/db.tgz ]
then
    echo "The cached file is empty"
    echo "env.migration_skip=true" >> $GITHUB_ENV
    exit 0
fi

if [ "$(file somefile1.tar.gz | grep -q 'gzip compressed data' && echo yes || echo no)" == "no" ]
then
    echo "Invalid tgz format"
    echo "env.migration_skip=true" >> $GITHUB_ENV
    exit 0
fi

if [ -f "$GITHUB_ENV" ]
then
    echo "Output of the Environment variables: "
    cat "$GITHUB_ENV"
fi

# EOF
###############################################################################