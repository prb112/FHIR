# Integration Test Framework for the Database Migration

This document outlines the end-to-end migration automation framework. 

The automation runs with these steps: 

- **Checkout source code** - Checks out the git code and populates the `github` environment variables.
- **Setup Java** - Downloads and setup for Java 11
- **Should this workflow run** - Checks to see if this is required to run, and subsequent executions are skipped.
- **Setup prerequisites** - This step builds the required artifacts necessary to test the build with the reindex. 
- 

- **Integration Tests** - The step executes the pre-integration, then integration-test and runs the post-integration scripts.
- **Gather error logs** - This step only runs upon a failure condition. 
- **Upload logs** - The step uploads the results of the integration tests and the operational logs are posted to the job. 

The GitHub Action is parameterized with a matrix for each new `$reindex` tests. Each additional entry in the array ends up creating multiple automation steps which must complete successfully for the workflow.

``` yaml
strategy:
  matrix:
    datastore: [ 'db2', 'postgres' ]
    version: [ 'RELEASE-1', 'RELEASE-2' ]
```

Each datastore layer that is tested as part of the framework uses the default build files and the files that match the `matrix.datastore` name added to the `reindex.yml`.

|Filename|Purpose|
|----------|----------------|
|bin/gather-logs.sh|Gathers the logs from the build|
|bin/integration-test.sh|Run after the tests complete to release resources and package tests results|
|bin/setup-prerequisites.sh|Builds the fhir-server|
|bin/pre-integration-test.sh|Call the pre-integration-test step for `<datastore>` and `version`|
|bin/post-integration-test.sh|Call the post-integration-test step for `<datastore>` and `version`|
|`<datastore>`/integration-test.sh|if exists, overrides bin/integration-test.sh, replacing the prior test behavior.|
|`<datastore>`/pre-integration-test.sh|Run before integration-test.sh to startup image and services for the integration testing|
|`<datastore>`/post-integration-test.sh|if exists, runs after integration-test.sh to stop image and services from the integration testing|
|`<datastore>`/.gitignore|Ignores files related to the reindex layer's tests|
|`README.md`|This file describing the reindex framework|

Note, `<datastore>` is replaced with your reindex layer such as `db2`. 

Transaction Timeout is 300 seconds.

Consult the reference implementation (`db2`) to start a new migration test. The minimum that must be implemented are the `pre-integration-test.sh` and `.gitignore`.

## Test the Automation

To test the build, be sure to pre-set the environment variable `WORKSPACE` with `export WORKSPACE=$(pwd)`.
You must also start Docker, so the image is built that supports the IBM FHIR Server.

If you have any questions, please reach out on Zulip.