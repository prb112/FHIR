## IBM FHIR Server
The IBM® FHIR® Server is a modular Java implementation of version 4 of the [HL7 FHIR specification](https://www.hl7.org/fhir/r4/http.html) with a focus on performance and configurability.

For a detailed description of FHIR conformance, see https://ibm.github.io/FHIR/Conformance.

The server can be packaged as a set of jar files, a web application archive (war), an application installer, or a Docker image.

### Running the IBM FHIR Server
The IBM FHIR Server is available from the [Releases tab](https://github.com/IBM/FHIR/releases) as a zip file with installation scripts for Mac/Linux and Windows or as a docker image at https://hub.docker.com/r/ibmcom/ibm-fhir-server.

Note: The Docker image [ibmcom/ibm-fhir-schematool](https://hub.docker.com/r/ibmcom/ibm-fhir-schematool) is an early technology preview and is experimental.

More information on installing and running the server is available in the User Guide at https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide.

### Building on top of the IBM FHIR Server Modules
Each of the IBM FHIR Server modules are published to Maven Central [repo](https://repo1.maven.org/maven2/com/ibm/fhir/)

For Version 4.6.1 and earlier, each of the IBM FHIR Server modules are published as an archive of the repository.

After Version 4.6.1, you may declare the dependency without a repository as it points to [Maven Central] [https://repo1.maven.org/maven2/com/ibm/fhir]

To use the artifacts from a Maven project, declare the dependencies. For example, to use our visitable, thread-safe FHIR R4 object model (including our high-performance parsers and generators), declare a dependency on the `fhir-model` module:

    ```
    ...
    <dependencies>
        <dependency>
          <groupId>com.ibm.fhir</groupId>
          <artifactId>fhir-model</artifactId>
          <version>${fhir.version}</version>
        </dependency>
        ...
    ```

Note, if you are using a local repository or private host, you must add the repository to your pom.xml:

    ```
    <repositories>
        <repository>
            <id>ibm-fhir</id>
            <url>https://myhost.com/ibm-fhir-server-releases</url>
        </repository>
        ...
    ```

### IBM FHIR Server Module Catalog
The IBM FHIR Server is modular and extensible. The following tables provide an overview of all the IBM FHIR modules, along with an indicator of the stability of the Java APIs defined in each module. This indicator is only applicable to the direct usage of the modules, not for usage of the IBM FHIR Server as a whole.

#### Core
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-parent|The parent project for all projects which make up the IBM FHIR Server|false|
|fhir-core|Core helpers and utilities|false|

#### Model and Profile Support
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-model|An object model generated from the FHIR R4 specification and corresponding parsers and generators for XML and JSON|true|
|fhir-registry|A resource registry, registry provider interfaces, and pre-registered resources shipped with the FHIR specification|false|
|fhir-term|A terminology service provider interface with a default implementation that implements terminology services from fully-defined CodeSystems in the registry|false|
|fhir-profile|Helper methods for validating ValueSet membership and Profile conformance|false|
|fhir-path|An implementation of version 2.0.0 of the FHIRPath specification assumed by FHIR R4|false|
|fhir-validation|Validation utility for validating resource instances against the base specification and/or configured profiles|false|
|fhir-ig-us-core|A packaging of the US Core Implementation Guide for extending the IBM FHIR Server with US Core Profile validation|false|
|fhir-ig-mcode|A packaging of the minimal Common Oncology Data Elements for extending the IBM FHIR Server with minimal Common Oncology Data Elements Profile validation|false|
|fhir-ig-carin-bb|A packaging of the Consumer-Directed Payer Data Exchange Guide for extending the IBM FHIR Server with  Consumer-Directed Payer Data Exchange Profile validation|false|
|fhir-ig-davinci-pdex|A packaging of the Da Vinci Payer Data Exchange (PDEX) Implementation Guide for extending the IBM FHIR Server with DaVinci Payer Data Exchange (PDEX) Profile validation|false|
|fhir-ig-davinci-hrex|A packaging of the Da Vinci Health Record Exchange (HREX) Implementation Guide for extending the IBM FHIR Server with DaVinci Health Record Exchange (HREX) Profile validation|false|
|fhir-ig-davinci-pdex-plan-net|A packaging of the Da Vinci Payer Data Exchange (PDEX) Plan Net Implementation Guide for extending the IBM FHIR Server with DaVinci Payer Data Exchange (PDEX) Plan Net Profile validation|false|
|fhir-ig-davinci-pdex-formulary|A packaging of the Da Vinci Payer Data Exchange (PDex) US Drug Formulary Implementation Guide for extending the IBM FHIR Server with DaVinci Payer Data Exchange (PDex) US Drug Formulary validation|false|

#### Server
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-config|Configuration property definitions and helpers for working with the fhir-server-config.json config files and multi-tenancy|false|
|fhir-audit|Audit-related interfaces and implementations including 1) a No-op AuditLogService and 2) an AuditLogService that writes audit events to Apache Kafka in the Cloud Auditing Data Federation (CADF) JSON format|false|
|fhir-search|Utilities for working with the FHIR search specification|false|
|fhir-persistence|Interfaces, helpers, and tests for implementing a persistence layer or persistence interceptors for the IBM FHIR Server|false|
|fhir-persistence-schema|Classes for deploying and updating the IBM FHIR Server relational database schema|false|
|fhir-persistence-jdbc|A relational FHIRPersistence implementation that uses JDBC to store and query FHIR resources|false|
|fhir-persistence-scout|A scale out persistence layer to store and query FHIR resources *experimental* |false|
|fhir-persistence-proxy|A custom XADataSource implementation for managing distributed transactions across multiple backends **No longer included in build**|false|
|fhir-provider|JAX-RS Providers for FHIR XML and JSON and related patch formats|false|
|fhir-notification|[Subscription](https://www.hl7.org/fhir/R4/subscription.html) and notification interfaces and helpers|false|
|fhir-notification-websocket|A fhir-notification implementation that uses WebSockets as described at https://www.hl7.org/fhir/R4/subscription.html#2.46.7.2 |false|
|fhir-notification-kafka|An experimental fhir-notification implementation that uses Apache Kafka instead of WebSockets|false|
|fhir-notification-nats|An experimental fhir-notification implementation that uses [NATS](https://nats.io/) instead of WebSockets|false|
|fhir-server|JAX-RS resources and related classes for implementing the FHIR REST API and extended operations|false|
|fhir-server-webapp|A web application that packages the fhir-server with a set of built-in extended operations|false|
|fhir-server-test|End-to-end integration tests for testing a running server|false|

#### Extended Operations
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-operation-test|Sample operations for testing Extended Operations as describe at https://www.hl7.org/fhir/R4/operations.html |false|
|fhir-operation-apply|A naive implementation of the `$apply` operation defined at https://www.hl7.org/fhir/operation-activitydefinition-apply.html |false|
|fhir-operation-bulkdata|`$import` and `$export` implementations which translate bulk data requests into JSR352 Java Batch jobs|false|
|fhir-bulkdata-webapp|Standalone web application for serving bulk import and export requests via JSR352 Java Batch jobs|false|
|fhir-operation-convert|A limited implementation of the FHIR [$convert operation](https://www.hl7.org/fhir/R4/resource-operation-convert.html), able to convert between JSON and XML but *not* between FHIR versions|false|
|fhir-operation-document|Basic support for the Composition `$document` operation defined at https://www.hl7.org/fhir/operation-composition-document.html |false|
|fhir-operation-healthcheck|The `$healthcheck` operation checks for a valid connection to the database and returns the server status|false|
|fhir-operation-term|[Terminology service](https://www.hl7.org/fhir/terminology-service.html) operations which use the default fhir-term TerminologyServiceProvider to implement $expand, $lookup, $subsumes, $closure, $validate and $translate|false|
|fhir-operation-validate|An implementation of the FHIR resource [$validate operation](https://www.hl7.org/fhir/R4/operation-resource-validate.html)|false|
|fhir-operation-everything|An implementation of the FHIR patient [`$everything`](https://www.hl7.org/fhir/operation-patient-everything.html) operation|false|

#### Client
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-client|A FHIR Client that re-uses the IBM FHIR Server model and its JAX-RS Providers|false|
|fhir-cli|Experimental command line interface utility for working with the IBM FHIR Server client from the command line|false|

#### Tools and Utilities
|Module|Description|Java API-stable|
|------|-----------|----------|
|fhir-tools|Code generation tools and logic for generating the FHIR object model, XML and JSON parsers, and the DefaultVisitor base class|false|
|fhir-database-utils|Generic database utilities for working with Apache Derby and IBM Db2 relational database management systems|false|
|fhir-examples-generator|A utility for generating resource examples which range from minimal (only required fields) to complete (every field present)|false|
|fhir-examples|A set of FHIR resource examples including 1) all examples from the FHIR Specification 2) a set of generated examples for test purposes|false|
|fhir-swagger-generator|Utilities for generating Swagger 2.0 and OpenAPI 3.0 definitions for a subset of the FHIR HTTP interface|false|
|fhir-openapi|A web application that provides a simplified OpenAPI 3.0 definition of the FHIR HTTP interface|false|
|fhir-install|Packaging and installation scripts for creating the fhir-distribution zip and the corresponding IBM FHIR Server Docker image|false|
|fhir-benchmark|Java Microbenchmark Harness (JMH) tests for measuring read/write/validation performance for the IBM FHIR Server and the HL7 FHIR Java Reference Implementation|false|
|fhir-bucket|Scans cloud object storage buckets and uploads data using the FHIR REST API|false|

### Contributing to the IBM FHIR Server
The IBM FHIR Server is under active development. To help develop the server, clone or download the project and build it using Maven.
See [Setting up for development](https://github.com/IBM/FHIR/wiki/Setting-up-for-development) for more information.

See [CONTRIBUTING.md](CONTRIBUTING.md) for contributing your changes back to the project.

See [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) for code of conduct.

### License
The IBM FHIR Server is licensed under the Apache 2.0 license. Full license text is
available at [LICENSE](LICENSE).

FHIR® is the registered trademark of HL7 and is used with the permission of HL7. Use of the FHIR trademark does not constitute endorsement of this product by HL7.
IBM and the IBM logo are trademarks of International Business Machines Corporation, registered in many jurisdictions worldwide. Other product and service names might be trademarks of IBM or other companies. A current list of IBM trademarks is available on [https://ibm.com/trademark](https://ibm.com/trademark). 