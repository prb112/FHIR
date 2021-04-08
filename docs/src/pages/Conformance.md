---
layout: post
title:  Conformance
description: Notes on the Conformance of the IBM FHIR Server
date:   2021-04-06 12:00:00 -0400
permalink: /conformance/
---

# Conformance to the HL7 FHIR Specification
The IBM FHIR Server aims to be a conformant implementation of the HL7 FHIR specification, version 4.0.1 (R4). However, the FHIR specification is very broad and not all implementations are expected to implement every feature. We prioritize performance and configurability over spec coverage.

## Capability statement
The HL7 FHIR specification defines [an interaction](https://www.hl7.org/fhir/R4/http.html#capabilities) for retrieving a machine-readable description of the server's capabilities via the `[base]/metadata` endpoint. The IBM FHIR Server implements this interaction and generates a `CapabilityStatement` resource based on the current server configuration. While the `CapabilityStatement` resource is ideal for certain uses, this markdown document provides a human-readable summary of important details, with a special focus on limitations of the current implementation and deviations from the specification.

The IBM FHIR Server supports only version 4.0.1 of the specification and ignores the optional MIME-type parameter `fhirVersion`.

## FHIR HTTP API
The HL7 FHIR specification is more than just a data format. It defines an [HTTP API](https://www.hl7.org/fhir/R4/http.html) for creating, reading, updating, deleting, and searching over FHIR resources. The IBM FHIR Server implements the full API for every resource defined in the specification, with the following exceptions:
* history is not supported at the resource-type level (only the resource instance level and the whole-system level)
* whole-system history is not conformant to the HL7 FHIR specification; we chose not to include the resource in the history response bundle
* there are parts of the FHIR search specification which are not fully implemented as documented in the following section

The IBM FHIR Server implements a linear versioning scheme for resources and fully implements the `vread` and `history-instance` interactions, as well as version-aware updates.

By default, the IBM FHIR Server allows all supported API interactions (`create`, `read`, `vread`, `history`, `search`, `update`, `patch`, `delete`). However, it is possible to configure which of these interactions are allowed on a per resource basis through a set of interaction rules. See the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#412-fhir-rest-api) for details.

### HTTP Headers
In addition to the content negotiation headers required in the FHIR specification, the IBM FHIR Server supports two client preferences via the `Prefer` header:
* [return preference](https://www.hl7.org/fhir/http.html#ops)
* [handling preference](https://www.hl7.org/fhir/search.html#errors)

The default return preference is `minimal`.
The default handling preference is configurable via the server's `fhirServer/core/defaultHandling` config property, but defaults to `strict`.
Additionally, server administrators can configure whether or not to honor the client's handling preference by setting `fhirServer/core/allowClientHandlingPref` which defaults to `true`.

For example, to ask the server to be lenient in processing a given request, but to return warnings for non-fatal errors, a client should set the Prefer header as follows:
```
Prefer: return=OperationOutcome; handling=lenient
```

In `lenient` mode, the client must [check the self uri](https://www.hl7.org/fhir/search.html#conformance) of a search response to determine which parameters were used in computing the response.

Note: In addition to controlling whether or not the server returns an error for unexpected search parameters, the handling preference is also used to control whether or not the server will return an error for unexpected elements in the JSON representation of a Resource as defined at https://www.hl7.org/fhir/json.html.

Finally, the IBM FHIR Server supports multi-tenancy through custom headers as defined at https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#49-multi-tenancy. By default, the server will look for a tenantId in a `X-FHIR-TENANT-ID` header and a datastoreId in the `X-FHIR-DSID` header, and use `default` for either one if the headers are not present.

### General parameters
The `_format` parameter is supported and provides a useful mechanism for requesting a specific format (`XML` or `JSON`) in requests made from a browser. In the absence of either an `Accept` header or a `_format` query parameter, the server defaults to `application/fhir+json`.

The `_pretty` parameter is also supported.

## Whole System History
The whole system history interaction can be used to obtain a list of changes (create, update, delete) to resources in the IBM FHIR Server. This may be useful for other systems to reliably track these changes and keep themselves in-sync.

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history'
```

The response is a history bundle as described by the [FHIR specification](https://www.hl7.org/fhir/http.html#history) with one exception. Whereas the specification requires each entry in the history bundle to contain the full contents of the resource (at least for entries with a `entry.request.method` of PUT or POST), the IBM FHIR Server whole-system history response bundle contains only references to the resources. Clients may choose which resources they fetch. They may also fetch the resources in parallel, which may greatly improve throughput.

To return all changes that have occurred since a known point in time, use the `_since` query parameter:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_since=2021-02-21T00:00:00Z'
```

By default, the returned bundle will contain up to 100 resource references. The number of resources can be increased (up to 1000) using the `_count` parameter. Specifying `_count` values larger than 1000 will return no more than 1000 resource references:
```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_count=1000'
```

Client applications can use the `_since` parameter to scan sequentially through all the changes recorded by the IBM FHIR Server. Each result bundle contains a `next` link which can be used to fetch the next set of data. To simplify client implementations, the IBM FHIR Server also includes a custom attribute `_afterHistoryId`. This value can be used by a client to checkpoint where they are in the list of changes, and ask for only changes that come after the given id. This is convenient, because the `_afterHistoryId` is based on a unique identifier. The client does not need to worry about duplicates which otherwise occur when using `_since` to sequentially fetch history:

```
    curl -k -u '<username>:<password>' 'https://<host>:<port>/fhir-server/api/v4/_history?_count=1000&_afterHistoryId=3004'
```

Clients cannot use both `_afterHistoryId` and `_since` together nor should they switch to using the other without understanding how concurrency, transaction boundaries and cluster clock synchronization issues can affect how ids and timestamps are assigned during ingestion. If `_since` is specified, the `next` link in the returned bundle will include the `_since` parameter using the greatest `lastModified` from the returned resources as its value. If `_afterHistoryId` is specified, the `next` link in the returned bundle will include the `_afterHistoryId` parameter using the greatest internal id from the returned resources as its value. If you do not specify any filter parameter, the IBM FHIR Server uses `_afterHistoryId=0` as the initial start point and so will use `_afterHistoryId` for the subsequent `next` links.

In a highly concurrent system, several resources could share the same timestamp. Also, the internal id used to identify individual resource changes may not correlate perfectly with the `lastModified` time. Take the following, for example:

| logical-id | version |  time | change-id |  change-type |
| ---------- | ------- | ----- | --------- | ------------ |
| patient-1  |       1 | 12:00 |         1 | CREATE |
| patient-2  |       1 | 12:05 |         2 | CREATE |
| patient-3  |       1 | 12:05 |         3 | CREATE |
| patient-4  |       1 | 12:04 |         4 | CREATE |
| patient-1  |       2 | 12:06 |         5 | UPDATE |
| patient-3  |       2 | 12:06 |         6 | DELETE |

Note how the change time for `patient-3` and `patient-4` is the same, although they have different change ids. Significantly, `patient-4` has a change time before `patient-2` even though its id is greater. This can happen if the clocks in a cluster are not perfectly synchronized. This only applies to different resources - changes can _never_ appear out of order for a specific resource because the IBM FHIR Server uses database locking to ensure consistency.

Clients must also exercise caution when reading recently ingested resources. When processing large bundles in parallel, an id may be assigned by the database but ACID isolation means that the record will not be visible to a reader until the transaction is committed. This could be up to 120s or longer if a larger transaction-timeout property has been defined. If a smaller bundle starts after the larger bundle and its transaction is committed first, its change ids and timestamps will be visible to readers before the resources from the other bundle, which will have earlier change ids and timestamps. If clients do not take this into account, they may miss some resources. This behavior is a common concern in databases and not specific to the IBM FHIR Server.

To guarantee no data is skipped, clients should not process resources with a `lastModified` timestamp which is after `{current_time} - {transaction_timeout} - {max_cluster_clock_drift}`. By waiting for this time window to close, the client can be sure the data being returned is complete and in order, and new data will not appear. The default value for transaction timeout is 120s but this is configurable. A value of 2 seconds is a reasonable default to consider for `max_cluster_clock_drift` in lieu of specific information. Implementers should check with server administrators on the appropriate values to use.

## Search
The IBM FHIR Server supports all search parameter types defined in the specification:
* `Number`
* `Date/DateTime`
* `String`
* `Token`
* `Reference`
* `Composite`
* `Quantity`
* `URI`
* `Special` (Location-near)

### Search parameters
Search parameters defined in the specification can be found by browsing the R4 FHIR specification by resource type. For example, to find the search parameters for the Patient resource, navigate to https://www.hl7.org/fhir/R4/patient.html and scroll to the Search Parameters section near the end of the page.

In addition, the following search parameters are supported on all resources:
* `_id`
* `_lastUpdated`
* `_tag`
* `_profile`
* `_security`
* `_source`
* `_type`
* `_has`

These parameters can be used while searching any single resource type or while searching across resource types (whole system search).

The `_type` parameter has two restrictions:
* It may only be used with whole system search.
* It may only be specified once in a search. In `lenient` mode, only the first occurrence is used; additional occurrences are ignored.

The `_has` parameter has two restrictions:
* It cannot be used with whole system search.
* The search parameter specified at the end of its chain cannot be a search result parameter.

The `_text`, `_content`, `_list`, `_query`, and `_filter` parameters are not supported at this time.

Finally, the specification defines a set of "Search result parameters" for controlling the search behavior. The IBM FHIR Server supports the following:
* `_sort`
* `_count`
* `_include`
* `_revinclude`
* `_summary`
* `_elements`
* `_total`

The `_sort`, `_count`, `_summary`, `_elements`, and `_total` parameters may each only be specified once in a search. In `lenient` mode, only the first occurrence of each of these parameters is used; additional occurrences are ignored.

The `_count` parameter can be used to request up to 1000 resources matching the search criteria. An attempt to exceed this `_count` limit will not be honored and returned resources will be capped at 1000. Any associated `_include` or `_revinclude` resources are not considered in the `_count` limit.

The `_include` and `_revinclude` parameters can be used to return resources related to the primary search results, in order to reduce the overall network delay of repeated retrievals of related resources. The number of `_include` or `_revinclude` resources returned for a single page of primary search results will be limited to 1000. If the number of included resources to be returned exceeds 1000, the search will fail. For example, if the primary search result is one resource and the number of included resources is 1000, the search will succeed. However, if the primary search result is one resource and the number of included resources is 1001, the search will fail. It is possible that an included resource could be referenced by more than one primary search result. Duplicate included resources will be removed before search results are returned, so a resource will not appear in the search results more than once. A resource is considered a duplicate if a primary resource or another included resource with the same logical ID and version already exists in the search results.

The `:iterate` modifier is supported for the `_include` and `_revinclude` parameters. The number of iterations is limited to 1. This means the iteration depth will be limited to one level beyond the depth of the resources being iterated against, whether primary search resources or included resources. One exception to this is the case where an iterative `_include` or `_revinclude` is specified that will return the same resource type as the primary search resource type (for example `.../Patient?_include:iterate=Patient:link:Patient`). In this case, the iteration depth will be limited to a maximum of two levels beyond the primary search resource type.

The `_contained` and `_containedType` parameters are not supported at this time.

### Custom search parameters
Custom search parameters are search parameters that are not defined in the FHIR R4 specification, but are configured for search on the IBM FHIR Server. You can configure custom parameters for either extension elements or for elements that are defined in the specification but without a corresponding search parameter.

For information on how to specify custom search parameters, see [FHIRSearchConfiguration.md](https://ibm.github.io/FHIR/guides/FHIRSearchConfiguration).

### Search modifiers
FHIR search modifiers are described at https://www.hl7.org/fhir/R4/search.html#modifiers and vary by search parameter type. The IBM FHIR Server implements a subset of the spec-defined search modifiers that is defined in the following table:

|FHIR Search Parameter Type|Supported Modifiers|"Default" search behavior when no Modifier or Prefix is present|
|--------------------------|-------------------|---------------------------------------------------------------|
|String                    |`:exact`,`:contains`,`:missing`    |"starts with" search that is case-insensitive and accent-insensitive|
|Reference                 |`:[type]`,`:missing`,`:identifier` |exact match search and targets are implicitly added|
|URI                       |`:below`,`:above`,`:missing`       |exact match search|
|Token                     |`:missing`,`:not`,`:of-type`,`:in`,`:not-in`,`:text`,`:above`,`:below`       |exact match search|
|Number                    |`:missing`                         |implicit range search (see http://hl7.org/fhir/R4/search.html#number)|
|Date                      |`:missing`                         |implicit range search (see https://www.hl7.org/fhir/search.html#date)|
|Quantity                  |`:missing`                         |implicit range search (see http://hl7.org/fhir/R4/search.html#quantity)|
|Composite                 |`:missing`                         |processes each parameter component according to its type|
|Special (near)            | none                              |searches a bounding area according to the value of the `fhirServer/search/useBoundingRadius` property|

Due to performance implications, the `:exact` modifier should be used for String search parameters when possible.

The `:above` and `:below` modifiers for Token search parameters are supported, with the following restrictions:
* The search parameter value must have the form of `[system]|[code]`.
* The referenced code system must exist in the FHIR registry.

The `:in` and `:not-in` modifiers for Token search parameters are supported, with the following restrictions:
* The search parameter value must be an absolute URI that identifies a value set.
* The referenced value set must exist in the FHIR registry and must be expandable.

### Search prefixes
FHIR search prefixes are described at https://www.hl7.org/fhir/R4/search.html#prefix.

As defined in the specification, the following prefixes are supported for Number, Date, and Quantity search parameters:
* `eq`
* `ne`
* `gt`
* `lt`
* `gt`
* `le`
* `sa`
* `eb`
* `ap`

For range targets (parameter values extracted from Range, Date/Period, and DateTime elements without fractional seconds), the prefixes are interpreted as according to https://www.hl7.org/fhir/R4/search.html#prefix.

For example, a search like `Observation?date=2018-10-29T12:00:00Z` would *not* match an Observation with an effectivePeriod of `start=2018-10-29` and `end=2018-10-30` because "the search range does not fully contain the range of the target value." Similarly, a search like `range=5||mg` would not match a range value with `low = 1 mg` and `high = 10 mg`. To obtain all range values which contain a specific value, use the `ap` prefix which is defined to match when "the range of the search value overlaps with the range of the target value."

If not specified on a query string, the default prefix is `eq`.

### Searching on Date
The IBM FHIR Server implements date search as according to the specification.

The server supports up to 6 fractional seconds (microsecond granularity) for Instant and DateTime values and all extracted parameter values are stored in the database in UTC in order to improve data portability.

Dates and DateTimes which are expressed without timezones are assumed to be in the local timezone of the application server at the time of parameter extraction.
Similarly, query parameter date values with no timezone are assumed to be in the local timezone of the server at the time the search is invoked.
To ensure consistency of search results, clients are recommended to include the timezone on all search query values that include a time.

Finally, the server extends the specified capabilities with support for "exact match" semantics on fractional seconds.

Query parameter values without fractional seconds are handled as an implicit range. For example, a search like `Observatoin?date=2019-01-01T12:00:00Z` would return resources with the following effectiveDateTime values:
* 2019-01-01T12:00:00Z
* 2019-01-01T12:00:00.1Z
* 2019-01-01T12:00:00.999999Z

Query parameter values with fractional seconds are handled with exact match semantics (ignoring precision). For example, a search like `Patient?birthdate=2019-01-01T12:00:00.1Z` would include resources with the following effectiveDateTime values:
* 2019-01-01T12:00:00.1Z
* 2019-01-01T12:00:00.100Z
* 2019-01-01T12:00:00.100000Z

Indexing fields of type `Timing` is not well-defined in the specification and is not supported in this version of the IBM FHIR Server.

### Searching on Token
For search parameters of type token, resource values are not indexed unless the resource instance contains both a `system` **and** `code`. The server implements the following variations of token search defined in the specification:
* `[parameter]=[code]`
* `[parameter]=[system]|[code]`
* `[parameter]=|[code]`

However, the `|[code]` variant currently behaves like the `[code]` option, matching code values irrespective of the system instead of matching only on elements with missing/null system values as defined in the spec.

The IBM FHIR Server does not yet support searching a token value by codesystem, irrespective of the value (`|[system]|`).

For search parameters of type token that are defined on data fields of type `ContactPoint`, the FHIR server currently uses the `ContactPoint.system` and the `ContactPoint.value` instead of the `ContactPoint.use` field as described in the specification.

Searching string values via a token search parameter is not currently supported.

### Searching on Number
For fields of type `decimal`, the IBM FHIR Server computes an implicit range when the query parameter value has a prefix of `eq` (the default), `ne`, or `ap`. The computed range is based on the number of significant figures passed in the query string and further information can be found at https://www.hl7.org/fhir/R4/search.html#number.
For searches with the `ap` prefix, we use the range `[implicitLowerBound - searchQueryValue * .1, implicitUpperBound + searchQueryValue * .1)` to ensure that the `ap` range is broader than the implicit range of `eq`.

### Searching on Quantity
Quantity elements are not indexed unless they include either a valid `system` **and** `code` for their unit **or** a human-readable `unit` field.
If a Quantity element contains both a coded unit **and** a display unit, then both will be indexed. Quantities that don't include a `value` element are also skipped.

The FHIR server does not perform any unit conversion or unit manipulation at this time. Quantity values should be searched using the same unit `code` that is included in the original resource.

Similar to Numeric searches, the FHIR Server computes an implicit range for search query values with no range prefix (e.g. `eq`, `ne`, `ap`) based on the number of significant figures passed in the query string.
For searches with the `ap` prefix, we use the range `[implicitLowerBound - searchQueryValue * .1, implicitUpperBound + searchQueryValue * .1)` to ensure that the `ap` range is broader than the implicit range of `eq`.

The IBM FHIR Server does not consider the `Quantity.comparator` field as part of search processing at this time.

### Searching on URI
URI searches on the IBM FHIR Server are case-sensitive with "exact-match" semantics. The `above` and `below` prefixes can be used to perform path-based matching that is based on the `/` delimiter.

### Searching on Reference
Reference searches on the IBM FHIR Server support search on:

* a relative reference - `1` where it is reflexsively determined to be a subset of possible targets, such as `Patient/1`, `Group/1`
* a logical reference - `Patient/1` where it is explicitly set
* uri searches - where it is explicitly searched using a URI on the server, such as `reference=http://example.org/fhir/Patient/123`

We recommend using logical reference where possible.

Elements of type Reference may contain a versioned reference, such as `Patient/123/_history/2`. When performing chained, reverse chained (`_has`), `_include`, or `_revinclude` searches on versioned references, the following rules apply:

* **Chained search**: If a resource has a reference that is versioned, and a chained search is performed using the element containing the versioned reference, the search criteria will be evaluated against the current version of the referenced resource, regardless of the version specified.
    * This is because the IBM FHIR Server only stores search index information for the current versions of resources. In the case where a chained search does not act on the referenced version of a resource, the search results will contain an `OperationOutcome` with a warning that indicates the logical id of the resource and the element containing the versioned reference.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/1` in its `subject` element, and the current version of the `Patient/123` resource is 2, and a search of `Condition?subject.name=Jane` is performed. In this case, the search criteria will be evaluated against the current version of the `Patient/123` resource rather than the specified version of `1`.
* **Reverse chained search**: If a resource has a reference that is versioned, and a reverse chain search is performed using the element containing the versioned reference, then the referenced resource can only be returned as a match if the version specified is the referenced resource's current version.
    * This is because the IBM FHIR Server will only return the current version of `match` resources in search results.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/2` in its `subject` element, and the current version of the `Patient/123` resource is `2`, and a search of `Patient?_has:Condition:patient:code=1234-5` is performed. If the `Condition` resource meets the search criteria, then the `Patient/123` resource will be returned as a match since the version specified in the reference is the current version of the `Patient/123` resource. However, if the current version of the `Patient/123` resource happens to be `3`, then the `Condition` resource will not be returned as a match in the search results.
* **Include search**: If a resource has a reference that is versioned, and an `_include` search is performed using the element containing the versioned reference, then the referenced resource with the specified version will be returned as an `include` resource in the search results, assuming the version is valid.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/1` in its `subject` element, and the current version of the `Patient/123` resource is `2`, and a search of `Condition?_include=Condition:subject` is performed. Version `1` of the `Patient/123` resource will be returned as an `include` resource in the search results.
* **Revinclude search**: If a resource has a reference that is versioned, and a `_revinclude` search is performed using the element containing the versioned reference, then the resource containing the versioned reference is returned as an `include` resource only if the version specified in the reference is the referenced resource's current version.
    * This is because the IBM FHIR Server will only return the current version of `match` resources in search results. A reference to a non-current version of the resource is not considered to have met the search criteria, thus the resource containing the reference is not considered a valid `include` resource.
    * Example: A `Condition` resource contains a reference of `Patient/123/_history/2` in its `subject` element, and the current version of the `Patient/123` resource is `2`, and a search of `Patient?_revinclude=Condition:subject` is performed. The `Condition` resource will be returned as an `include` resource since the version of the `Patient` resource specified in the `subject` element is the current version of the `Patient` resource. If the current version of the `Patient/123` resource is `3`, then the `Condition` resource will not be returned as an `include` resource in the search results.

### Searching on Special Positional Search
Positional Search uses [UCUM units](https://unitsofmeasure.org/ucum.html) of distance measure along with common variants:

| Unit of Measure | Variant |
|-----------------|---------|
|KILOMETERS|km, kms, kilometer, kilometers|
|MILES|mi, mis, mile, miles|
|METERS|m, ms, meter, meters|
|FEET|ft, fts, foot|

Note, the use of the surrounding bracket, such as `[mi_us]` is optional; `mi_us` is also valid.

## Extended operations
The HL7 FHIR specification also defines a mechanism for extending the base API with [extended operations](https://www.hl7.org/fhir/R4/operations.html).
The IBM FHIR Server implements a handful of extended operations and provides extension points for users to extend the server with their own.

Operations are invoked via HTTP POST.
* All operations can be invoked by passing a Parameters resource instance in the body of the request.
* For operations with no input parameters, no body is required.
* For operations with a single input parameter named "resource", the Parameters wrapper can be omitted.

Alternatively, for operations with only primitive input parameters (i.e. no complex datatypes like 'Identifier' or 'Reference'), operations can be invoked via HTTP GET by passing the parameters in the URL.

### System operations
System operations are invoked at `[base]/$[operation]`

|Operation|Short Description|Notes|
|---------|-----------------|-----|
| [$convert](https://hl7.org/fhir/R4/resource-operation-convert.html) | Takes a resource in one form and returns it in another | Converts between JSON and XML but *not* between FHIR versions |
| [$export](https://hl7.org/fhir/uv/bulkdata/STU1/OperationDefinition-export.html) | Export data from the server | exports to an S3-compatible data store; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#4101-bulk-data-export) for config info |
| [$import](https://github.com/smart-on-fhir/bulk-import/blob/master/import.md) | Import FHIR Resources from a source| see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide#4101-bulk-data-export) for config info. This implementation is based on the proposed operation.|
| [$healthcheck](https://github.com/IBM/FHIR/blob/main/operation/fhir-operation-healthcheck/src/main/resources/healthcheck.json) | Check the health of the server | Checks for a valid connection to the database |

### Type operations
Type operations are invoked at `[base]/[resourceType]/$[operation]`

|Operation|Type|Short Description|Notes|
|---------|----|-----------------|-----|
| [$validate](https://hl7.org/fhir/R4/operation-resource-validate.html) | * | Validate a passed resource instance | Uses fhir-validate |
| [$export](https://hl7.org/fhir/uv/bulkdata/OperationDefinition-patient-export.html) | Patient | Obtain a set of resources pertaining to all patients | exports to an S3-compatible data store; see the [user guide](https://ibm.github.io/FHIR/guides/FHIRServerUsersGuide/#410-bulk-data-operations) for config info |
| [$document](https://hl7.org/fhir/R4/operation-composition-document.html) | Composition | Generate a document | Prototype-level implementation |
| [$apply](https://hl7.org/fhir/R4/operation-plandefinition-apply.html) | PlanDefinition | Applies a PlanDefinition to a given context | A prototype implementation that performs naive conversion |
| [$everything](https://www.hl7.org/fhir/operation-patient-everything.html) | Patient | Obtain all resources pertaining to a patient | Current implementation supports obtaining all resources for a patient up to an aggregate total of 10,000 resources (at which point it is recommended to use the `$export` operation). This implementation does not currently support using the `_since` and `_count` query parameters. Pagination is not currently supported. |

Note: the `$everything` operation is not currently packaged with the IBM FHIR Server. To add it to an existing installation, you must build or download the jar from [Bintray](https://bintray.com/ibm-watson-health/ibm-fhir-server-releases/fhir-operation-everything) and add it to your server's `userlib` directory.

### Instance operations
Instance operations are invoked at `[base]/[resourceType]/[id]/$[operation]`

|Operation|Type|Short Description|Notes|
|---------|----|-----------------|-----|
| [$validate](https://hl7.org/fhir/R4/operation-resource-validate.html) | * | Validate a resource instance | Uses fhir-validate |
| [$export](https://hl7.org/fhir/uv/bulkdata/OperationDefinition-group-export.html) | Group | Obtain a set resources pertaining to patients in a specific Group | Only supports static membership; does not resolve inclusion/exclusion criteria |
| [$document](https://hl7.org/fhir/R4/operation-composition-document.html) | Composition | Generate a document | Prototype-level implementation |
| [$apply](https://hl7.org/fhir/R4/operation-plandefinition-apply.html) | PlanDefinition | Applies a PlanDefinition to a given context | A prototype implementation that performs naive conversion |

## HL7 FHIR R4 (v4.0.1) errata
We add information here as we find issues with the artifacts provided with this version of the specification.

---

FHIR® is the registered trademark of HL7 and is used with the permission of HL7.
