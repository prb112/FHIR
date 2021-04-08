/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.operation.everything;

import static com.ibm.fhir.model.util.ModelSupport.FHIR_DATE;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_INSTANT;
import static com.ibm.fhir.model.util.ModelSupport.FHIR_STRING;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;

import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPHandlingPreference;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Bundle.Entry.Search;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.SearchEntryMode;
import com.ibm.fhir.persistence.exception.FHIRPersistenceResourceDeletedException;
import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.compartment.CompartmentUtil;
import com.ibm.fhir.search.exception.FHIRSearchException;
import com.ibm.fhir.search.exception.SearchExceptionUtil;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.util.FHIROperationUtil;


/**
 * This class implements the <a href="https://www.hl7.org/fhir/operation-patient-everything.html">$everything</a> operation
 * which is used to return all the information related to one or more patients described in the resource or context on
 * which this operation is invoked.
 *
 */
public class EverythingOperation extends AbstractOperation {

    private static final Logger LOG = java.util.logging.Logger.getLogger(EverythingOperation.class.getName());

    /**
     * The <a href="https://www.hl7.org/fhir/search.html#prefix">prefix</a> used to indicate the start date for the $everything resources
     */
    protected static final String STARTING_FROM = SearchConstants.Prefix.GE.value();

    /**
     * The <a href="https://www.hl7.org/fhir/search.html#prefix">prefix</a> used to indicate the end date for the $everything resources
     */
    protected static final String UP_UNTIL = SearchConstants.Prefix.LE.value();

    /**
     * The "date" query parameter used in the underlying search operation.
     */
    protected static final String DATE_QUERY_PARAMETER = "date";

    /**
     * The "_lastUpdated" query parameter used in the underlying search operation.
     */
    protected static final String LAST_UPDATED_QUERY_PARAMETER = "_lastUpdated";

    /**
     * The query parameter to indicate a start date for the $everything operation
     */
    protected static final String START_QUERY_PARAMETER = "start";

    /**
     * The query parameter to indicate a stop date for the $everything operation
     */
    protected static final String END_QUERY_PARAMETER = "end";

    /**
     * The query parameter to only return resources last update since a date for the $everything operation
     */
    protected static final String SINCE_QUERY_PARAMETER = "_since";

    /**
     * The patient resource name
     */
    private static final String PATIENT = Patient.class.getSimpleName();

    /**
     * The file with the operation definition
     */
    private static final String OPERATION_DEFINITION_FILE = "everything.json";

    /**
     * The maximum number of cumulative resources from all compartments for a given patient.
     */
    private static final int MAX_OVERALL_RESOURCES = 10000;

    /**
     * The list of resources for which the <code>date</code> query parameter can be used
     */
    private static final Set<String> SUPPORT_CLINICAL_DATE_QUERY = new HashSet<>(Arrays.asList(
        "AllergyIntolerance",
        "CarePlan",
        "CareTeam",
        "ClinicalImpression",
        "Composition",
        "Consent",
        "DiagnosticReport",
        "Encounter",
        "EpisodeOfCare",
        "FamilyMemberHistory",
        "Flag",
        "Immunization",
        "List",
        "Observation",
        "Procedure",
        "RiskAssessment",
        "SupplyRequest"));

    private List<String> defaultResourceTypes;

    /**
     * Initialize the operation and load the sub-resources that will be retrieved.
     */
    public EverythingOperation() {
        try {
            defaultResourceTypes = getDefaultIncludedResourceTypes();
        } catch (FHIRSearchException e) {
            throw new Error("There has been an error retrieving the list of included resources of the $everything operation.", e);
        }
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(OPERATION_DEFINITION_FILE)) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (FHIRParserException e) {
            throw new Error("There has been a parsing error in the operation definition of the $everything operation.", e);
        } catch (IOException e) {
            throw new Error("There has been an IO error reading the operation definition of the $everything operation.", e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType, String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        LOG.entering(this.getClass().getName(), "doInvoke");

        Patient patient = null;
        try {
            patient = (Patient) resourceHelper.doRead(PATIENT, logicalId, false, false, null, null);
        } catch (FHIRPersistenceResourceDeletedException fde) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Patient with ID '" + logicalId + "' does not exist.", IssueType.NOT_FOUND);
            throw exceptionWithIssue;
        } catch (Exception e) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("An unexpected error occurred while reading patient '" + logicalId + "'", IssueType.EXCEPTION);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }
        if (patient == null) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Patient with ID '" + logicalId + "' does not exist.", IssueType.NOT_FOUND);
            throw exceptionWithIssue;
        }

        Entry patientEntry = buildPatientEntry(operationContext, patient);
        List<Entry> allEntries = new ArrayList<>(SearchConstants.MAX_PAGE_SIZE);
        allEntries.add(patientEntry);

        // We can't always use the "date" query parameter to query by clinical date, only with some resources.
        // Initial list obtained from the github issue: https://github.com/IBM/FHIR/issues/1044#issuecomment-769788097
        // Otherwise the search throws an exception. We create a params map with and without and use as needed
        MultivaluedMap<String, String> queryParameters = parseQueryParameters(parameters);
        MultivaluedMap<String, String> queryParametersWithoutDates = new MultivaluedHashMap<String,String>(queryParameters);
        boolean startOrEndProvided = queryParametersWithoutDates.remove(DATE_QUERY_PARAMETER) != null;

        List<String> resourceTypesOverride = getOverridenIncludedResourceTypes(parameters);
        List<String> resourceTypes = resourceTypesOverride.isEmpty() ? defaultResourceTypes : resourceTypesOverride;

        int totalResourceCount = 0;
        for (String compartmentType : resourceTypes) {
            MultivaluedMap<String, String> searchParameters = queryParameters;
            if (startOrEndProvided  && !SUPPORT_CLINICAL_DATE_QUERY.contains(compartmentType)) {
                LOG.finest("The request specified a '" + START_QUERY_PARAMETER + "' and/or '" + END_QUERY_PARAMETER + "' query parameter. They are not valid for resource type '" + compartmentType + "', so will be ignored.");
                searchParameters = queryParametersWithoutDates;
            }
            Bundle results = null;
            int currentResourceCount = 0;
            try {
                results = resourceHelper.doSearch(compartmentType, PATIENT, logicalId, searchParameters, null, null, null);
                currentResourceCount = results.getTotal().getValue();
                totalResourceCount += currentResourceCount;
                LOG.finest("Got " + compartmentType + " resources " + currentResourceCount + " for a total of " + totalResourceCount);
            } catch (Exception e) {
                FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Error retrieving $everything resources of type '" + compartmentType + "' for patient " + logicalId, IssueType.EXCEPTION);
                LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
                throw exceptionWithIssue;
            }
            // If retrieving all these resources exceeds the maximum number of resources allowed for this operation the operation is failed
            if (totalResourceCount > MAX_OVERALL_RESOURCES) {
                FHIROperationException exceptionWithIssue = buildExceptionWithIssue("The maximum number of resources allowed for the $everything operation (" + MAX_OVERALL_RESOURCES + ") has been exceeded for patient '" + logicalId + "'. Try using the bulkexport feature.", IssueType.TOO_COSTLY);
                LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
                throw exceptionWithIssue;
            }
            allEntries.addAll(results.getEntry());

            // We are retrieving sub-resources MAX_PAGE_SIZE items at a time, but there could be more so we need to retrieve the rest of the pages for the last resource if needed
            if (currentResourceCount > SearchConstants.MAX_PAGE_SIZE) {
                // We already retrieved page 1 so we account for that and start retrieving the rest of the pages
                int page = 2;
                while ((currentResourceCount -= SearchConstants.MAX_PAGE_SIZE) > 0) {
                    LOG.finest("Retrieving page " + page + " of the " + compartmentType + " resources for patient " + logicalId);
                    try {
                        searchParameters.putSingle(SearchConstants.PAGE, page++ + "");
                        results = resourceHelper.doSearch(compartmentType, PATIENT, logicalId, searchParameters, null, null, null);
                    } catch (Exception e) {
                        FHIROperationException exceptionWithIssue = buildExceptionWithIssue("Error retrieving $everything resources page '" + page + "' of type '" + compartmentType + "' for patient " + logicalId, IssueType.EXCEPTION);
                        LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
                        throw exceptionWithIssue;
                    }
                    allEntries.addAll(results.getEntry());
                }
            }
        }

        Bundle.Builder bundleBuilder = Bundle.builder()
                .type(BundleType.SEARCHSET)
                .id(UUID.randomUUID().toString())
                .entry(allEntries)
                .total(UnsignedInt.of(allEntries.size()));

        Parameters outputParameters;
        try {
            outputParameters = FHIROperationUtil.getOutputParameters(bundleBuilder.build());
        } catch (Exception e) {
            FHIROperationException exceptionWithIssue = buildExceptionWithIssue("An unexpected error occurred while creating the operation output parameters for the resulting Bundle.", IssueType.EXCEPTION);
            LOG.throwing(this.getClass().getName(), "doInvoke", exceptionWithIssue);
            throw exceptionWithIssue;
        }
        LOG.exiting(this.getClass().getName(), "doInvoke", outputParameters);
        return outputParameters;
    }

    /**
     * Parse the parameters and turn them into a {@link MultivaluedMap} to pass to the search service
     *
     * @param parameters the operation parameters
     * @return the {@link MultivaluedMap} for the search service built from the parameters
     */
    protected MultivaluedMap<String, String> parseQueryParameters(Parameters parameters) {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        Parameter countParameter = getParameter(parameters, SearchConstants.COUNT);
        if (countParameter != null) {
            LOG.fine("The `count` parameter is currently not supported by the $everything operation, it will be ignored.");
        }
        // We will try to grab all resources of a given type, in order to reduce the number of pages we will get the max page size
        queryParameters.add(SearchConstants.COUNT, SearchConstants.MAX_PAGE_SIZE + "");
        // We use gt/lt here in an effort to be more liberal in terms of what we return
        // https://ibm-watsonhealth.slack.com/archives/C14JTTR6C/p1614181836050500?thread_ts=1614180853.047300&cid=C14JTTR6C
        Parameter startParameter = getParameter(parameters, START_QUERY_PARAMETER);
        if (startParameter != null) {
            queryParameters.add(DATE_QUERY_PARAMETER, STARTING_FROM + startParameter.getValue().as(FHIR_DATE).getValue());
        }
        Parameter endParameter = getParameter(parameters, END_QUERY_PARAMETER);
        if (endParameter != null) {
            queryParameters.add(DATE_QUERY_PARAMETER, UP_UNTIL + endParameter.getValue().as(FHIR_DATE).getValue());
        }
        Parameter sinceParameter = getParameter(parameters, SINCE_QUERY_PARAMETER);
        if (sinceParameter != null) {
            queryParameters.add(LAST_UPDATED_QUERY_PARAMETER, STARTING_FROM + sinceParameter.getValue().as(FHIR_INSTANT).getValue());
        }
        return queryParameters;
    }

    /**
     * @param parameters the {@link Parameters} object
     * @return the list of patient subresources that will be included in the $everything operation, as provided by the user
     * @throws FHIRSearchException
     */
    protected List<String> getOverridenIncludedResourceTypes(Parameters parameters) throws FHIRSearchException {
        List<String> typeOverrides = new ArrayList<>();
        Parameter typesParameter = getParameter(parameters, SearchConstants.RESOURCE_TYPE);
        if (typesParameter == null) {
            return typeOverrides;
        }
        String typeOverridesParam = typesParameter.getValue().as(FHIR_STRING).getValue();
        String[] typeOverridesList = typeOverridesParam.split(SearchConstants.JOIN_STR);
        List<String> unknownTypes= new ArrayList<>();
        for (String typeOverride : typeOverridesList) {
            if (!defaultResourceTypes.contains(typeOverride)) {
                unknownTypes.add(typeOverride);
            } else {
                typeOverrides.add(typeOverride.trim());
            }
        }
        FHIRRequestContext requestContext = FHIRRequestContext.get();
        if (!unknownTypes.isEmpty()) {
            String msg = "The following resource types are not supported by this operation: " + String.join(",", unknownTypes);
            if (HTTPHandlingPreference.LENIENT.equals(requestContext.getHandlingPreference())) {
                LOG.fine(msg);
            } else {
                throw SearchExceptionUtil.buildNewInvalidSearchException(msg);
            }
        }
        return typeOverrides;
    }

    /**
     * @return the list of patient subresources that will be included in the $everything operaetion
     * @throws FHIRSearchException
     */
    private List<String> getDefaultIncludedResourceTypes() throws FHIRSearchException {
        List<String> resourceTypes = new ArrayList<>(CompartmentUtil.getCompartmentResourceTypes(PATIENT));
        // TODO: Practitioner and Organization are not included in the getCompartmentReourceTypes() by default but it seems
        // like a couple of good additional resources to include and they are even mentioned as examples of resources
        // to include in the docs: https://www.hl7.org/fhir/operation-patient-everything.html
        // resourceTypes.add(Practitioner.class.getSimpleName());
        // resourceTypes.add(Organization.class.getSimpleName());
        return resourceTypes;
    }

    /**
     * Builds an {@link Entry} out of the given {@link Patient} resource including its fullURL
     *
     * @param operationContext the {@link FHIROperationContext} to get the base URI
     * @param patient the patient to wrap
     * @return the entry with URL
     */
    private Entry buildPatientEntry(FHIROperationContext operationContext, Patient patient) {
        Uri patientURL = uri(operationContext, PATIENT + "/" + patient.getId());
        Entry patientEntry = Entry.builder()
                .resource(patient)
                .fullUrl(patientURL)
                .search(Search.builder()
                    .mode(SearchEntryMode.MATCH)
                    .score(Decimal.of("1"))
                    .build())
                .build();
        return patientEntry;
    }

    /**
     * Builds a URI with the base URI from the given {@link FHIROperationContext} and then provided URI path.
     *
     * @param operationContext the {@link FHIROperationContext} to get the base URI
     * @param uriPath the path to append to the base URI
     * @return the {@link Uri}
     */
    private static Uri uri(FHIROperationContext operationContext, String uriPath) {
        String requestBaseURI = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_REQUEST_BASE_URI);
        return Uri.builder()
                .value(requestBaseURI + "/" + uriPath)
                .build();
    }
}