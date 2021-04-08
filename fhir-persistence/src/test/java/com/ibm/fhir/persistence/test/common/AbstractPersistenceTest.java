/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.InputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.context.FHIRHistoryContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.parameters.QueryParameter;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * This is a common abstract base class for all persistence-related tests.
 *
 * Abstract subclasses in this package implement the logic of the tests and should
 * be extended by concrete subclasses in each persistence layer implementation.
 *
 * @implNote {@link FHIRConfiguration} requires a path to the root of the configuration directory
 * and this class passes the Maven target of the fhir-persistence project via a relative URL
 * ("../fhir-persistence/target/test-classes"). This means that:
 * <ul>
 *   <li>persistence layers under test must be configured outside of this mechanism
 *   <li>persistence layer projects must be peer to this project for the tests to properly read their config info
 * </ul>
 */
public abstract class AbstractPersistenceTest {
    // common logger to make things a little easier on subclass implementations
    protected static final Logger logger = Logger.getLogger(AbstractPersistenceTest.class.getName());

    // The common base URI used for all the search/persistence tests
    public static final String BASE = "https://example.com/";

    // The persistence layer instance to be used by the tests.
    protected static FHIRPersistence persistence = null;

    // Each concrete subclass needs to implement this to obtain the appropriate persistence layer instance.
    protected abstract FHIRPersistence getPersistenceImpl() throws Exception;

    // A hook for subclasses to override and provide specific test database setup functionality if required.
    protected void bootstrapDatabase() throws Exception {}

    // A hook for subclasses to override and provide specific test database shutdown functionality if required.
    protected void shutdownDatabase() throws Exception {}

    // A hook for subclasses to close any pools they may have created. Called after all tests in the class have been run
    protected void shutdownPools() throws Exception {}

    // A hook for subclasses to debug locks in the case of a lock timeout in the current transaction
    protected void debugLocks() {}

    // The following persistence context-related methods can be overridden in subclasses to
    // provide a more specific instance of the FHIRPersistenceContext if necessary.
    // These default versions just provide the minimum required by the FHIR Server persistence layers.
    protected FHIRPersistenceContext getDefaultPersistenceContext() throws Exception {
        return FHIRPersistenceContextFactory.createPersistenceContext(null);
    }
    protected FHIRPersistenceContext getPersistenceContextForSearch(FHIRSearchContext ctxt) {
        return FHIRPersistenceContextFactory.createPersistenceContext(null, ctxt);
    }
    protected FHIRPersistenceContext getPersistenceContextForHistory(FHIRHistoryContext ctxt) {
        return FHIRPersistenceContextFactory.createPersistenceContext(null, ctxt);
    }

    @BeforeClass
    public void configureLogging() throws Exception {
        final InputStream inputStream = AbstractPersistenceTest.class.getResourceAsStream("/logging.unitTest.properties");
        LogManager.getLogManager().readConfiguration(inputStream);
    }

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        bootstrapDatabase();
        persistence = getPersistenceImpl();
        // Note: this assumes that the concrete test classes will be in a project that is peer to the fhir-persistence module
        // TODO: it would be better for our unit tests if we could load config files from the classpath
        FHIRConfiguration.setConfigHome("../fhir-persistence/target/test-classes");
    }

    @BeforeMethod(alwaysRun = true)
    public void startTrx() throws Exception{
        // Configure the request context for our search tests
        FHIRRequestContext context = FHIRRequestContext.get();
        if (context == null) {
            context = new FHIRRequestContext();
        }
        context.setOriginalRequestUri(BASE);

        FHIRRequestContext.set(context);
        if (persistence != null && persistence.isTransactional()) {
            persistence.getTransaction().begin();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void commitTrx() throws Exception{
        if (persistence != null && persistence.isTransactional()) {
            persistence.getTransaction().end();
        }
    }

    @AfterClass(alwaysRun = true)
    public void closeClass() throws Exception {
        shutdownPools();
    }

    @AfterSuite(alwaysRun = true)
    public void tearDown() throws Exception {
        shutdownDatabase();
    }

    protected List<Resource> runQueryTest(Class<? extends Resource> resourceType, String parmName, String parmValue) throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>(1);
        if (parmName != null && parmValue != null) {
            queryParms.put(parmName, Collections.singletonList(parmValue));
        }
        return runQueryTest(resourceType, queryParms);
    }

    protected List<Resource> runQueryTest(Class<? extends Resource> resourceType, String parmName, String parmValue, Integer maxPageSize) throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>(1);
        if (parmName != null && parmValue != null) {
            queryParms.put(parmName, Collections.singletonList(parmValue));
        }
        return runQueryTest(resourceType, queryParms, maxPageSize);
    }

    protected List<Resource> runQueryTest(Class<? extends Resource> resourceType, Map<String, List<String>> queryParms) throws Exception {
        return runQueryTest(resourceType, queryParms, null);
    }

    protected List<Resource> runQueryTest(Class<? extends Resource> resourceType, Map<String, List<String>> queryParms, Integer maxPageSize) throws Exception {
        return runQueryTest(SearchUtil.parseQueryParameters(resourceType, queryParms), resourceType, queryParms, maxPageSize).getResource();
    }

    protected MultiResourceResult<Resource> runQueryTest(FHIRSearchContext searchContext, Class<? extends Resource> resourceType, Map<String, List<String>> queryParms, Integer maxPageSize) throws Exception {
        // ensure that all the query parameters were processed into search parameters (needed because the server ignores invalid params by default)
        int expectedCount = 0;
        for (String key : queryParms.keySet()) {

            expectedCount++;
            if (!SearchUtil.isSearchResultParameter(key) && !SearchUtil.isGeneralParameter(key)) {
                String paramName = key;
                if (SearchUtil.isReverseChainedParameter(key)) {
                    // ignore the reference type and just verify the reference param is there
                    paramName = key.split(":")[2];
                } else if (SearchUtil.isChainedParameter(key)) {
                    // ignore the chained part and just verify the reference param is there
                    paramName = key.split("\\.")[0];
                }
                // strip any modifiers
                final String finalParamName = paramName.split(":")[0];

                assertTrue(searchContext.getSearchParameters().stream().anyMatch(t -> t.getCode().equals(finalParamName)),
                    "Search parameter '" + key + "' was not successfully parsed into a search parameter");
            }
        }
        assertEquals(queryParms.keySet().size(), expectedCount);
        if (maxPageSize != null) {
            searchContext.setPageSize(maxPageSize);
        }
        FHIRPersistenceContext persistenceContext = getPersistenceContextForSearch(searchContext);

        try {
            MultiResourceResult<Resource> result = persistence.search(persistenceContext, resourceType);
            assertNotNull(result.getResource());
            return result;
        } catch (Throwable t) {
            debugLocks();
            throw t;
        }
    }

    protected List<Resource> runCompartmentQueryTest(String compartmentName, String compartmentLogicalId, Class<? extends Resource> resourceType, String parmName, String parmValue) throws Exception {
        return runCompartmentQueryTest(compartmentName, compartmentLogicalId, resourceType, parmName, parmValue, null);
    }

    protected List<Resource> runCompartmentQueryTest(String compartmentName, String compartmentLogicalId, Class<? extends Resource> resourceType, String parmName, String parmValue, Integer maxPageSize) throws Exception {
        Map<String, List<String>> queryParms = new HashMap<>(1);
        if (parmName != null && parmValue != null) {
            queryParms.put(parmName, Collections.singletonList(parmValue));
        }
        FHIRSearchContext searchContext = SearchUtil.parseCompartmentQueryParameters(compartmentName, compartmentLogicalId, resourceType, queryParms);

        return executeCompartmentQuery(resourceType, maxPageSize, searchContext);
    }

    protected List<Resource> runCompartmentQueryTest(String compartmentName, List<String> compartmentLogicalIds, Class<? extends Resource> resourceType, Map<String, List<String>> queryParms, Integer maxPageSize) throws Exception {
        FHIRSearchContext searchContext = SearchUtil.parseQueryParameters(resourceType, queryParms);
        QueryParameter inclusionCriteria = SearchUtil.buildInclusionCriteria(compartmentName, compartmentLogicalIds, resourceType.getSimpleName());
        if (inclusionCriteria != null) {
            searchContext.getSearchParameters().add(0, inclusionCriteria);
        }

        return executeCompartmentQuery(resourceType, maxPageSize, searchContext);
    }

    private List<Resource> executeCompartmentQuery(Class<? extends Resource> resourceType, Integer maxPageSize, FHIRSearchContext searchContext)
            throws FHIRPersistenceException {
        if (maxPageSize != null) {
            searchContext.setPageSize(maxPageSize);
        }
        FHIRPersistenceContext persistenceContext = getPersistenceContextForSearch(searchContext);
        MultiResourceResult<Resource> result = persistence.search(persistenceContext, resourceType);
        assertNotNull(result.getResource());
        return result.getResource();
    }
}
