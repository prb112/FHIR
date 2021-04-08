/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.jdbc.search.test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotEquals;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertNull;
import static org.testng.AssertJUnit.assertTrue;

import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.LogManager;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.database.utils.api.IConnectionProvider;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.model.resource.Location;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.persistence.MultiResourceResult;
import com.ibm.fhir.persistence.SingleResourceResult;
import com.ibm.fhir.persistence.context.FHIRPersistenceContext;
import com.ibm.fhir.persistence.context.FHIRPersistenceContextFactory;
import com.ibm.fhir.persistence.exception.FHIRPersistenceException;
import com.ibm.fhir.persistence.jdbc.FHIRPersistenceJDBCCache;
import com.ibm.fhir.persistence.jdbc.cache.CommonTokenValuesCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.FHIRPersistenceJDBCCacheImpl;
import com.ibm.fhir.persistence.jdbc.cache.NameIdCache;
import com.ibm.fhir.persistence.jdbc.dao.api.ICommonTokenValuesCache;
import com.ibm.fhir.persistence.jdbc.impl.FHIRPersistenceJDBCImpl;
import com.ibm.fhir.persistence.jdbc.test.util.DerbyInitializer;
import com.ibm.fhir.search.context.FHIRSearchContext;
import com.ibm.fhir.search.util.SearchUtil;

/**
 * <a href="https://www.hl7.org/fhir/r4/location.html#positional">FHIR
 * Specification: Positional Searching for Location Resource</a>
 * <br>
 * Original LONG/LAT
 *
 * <pre>
 *  "position": {
 *      "longitude": -83.6945691,
 *      "latitude": 42.25475478,
 *      "altitude": 0
 * }
 * </pre>
 */
public class JDBCSearchNearTest {
    private Properties testProps;

    protected Location savedResource;

    protected static FHIRPersistence persistence = null;

    @BeforeClass
    public void startup() throws Exception {
        LogManager.getLogManager().readConfiguration(
                new FileInputStream("../fhir-persistence/src/test/resources/logging.unitTest.properties"));

        FHIRConfiguration.setConfigHome("../fhir-persistence/target/test-classes");
        FHIRRequestContext.get().setTenantId("default");

        testProps = TestUtil.readTestProperties("test.jdbc.properties");

        DerbyInitializer derbyInit;
        PoolConnectionProvider connectionPool;
        String dbDriverName = this.testProps.getProperty("dbDriverName");
        if (dbDriverName != null && dbDriverName.contains("derby")) {
            derbyInit = new DerbyInitializer(this.testProps);
            IConnectionProvider cp = derbyInit.getConnectionProvider(false);
            connectionPool = new PoolConnectionProvider(cp, 1);
        } else {
            throw new IllegalStateException("dbDriverName must be set in test.jdbc.properties");
        }

        savedResource = TestUtil.readExampleResource("json/spec/location-example.json");

        ICommonTokenValuesCache rrc = new CommonTokenValuesCacheImpl(100, 100);
        FHIRPersistenceJDBCCache cache = new FHIRPersistenceJDBCCacheImpl(new NameIdCache<Integer>(), new NameIdCache<Integer>(), rrc);
        persistence   = new FHIRPersistenceJDBCImpl(this.testProps, connectionPool, cache);

        SingleResourceResult<Location> result =
                persistence.create(FHIRPersistenceContextFactory.createPersistenceContext(null), savedResource);
        assertTrue(result.isSuccess());
        assertNotNull(result.getResource());
        savedResource = result.getResource();

    }

    @AfterClass
    public void teardown() throws Exception {
        if (savedResource != null && persistence.isDeleteSupported()) {
            if (persistence.isTransactional()) {
                persistence.getTransaction().begin();
            }

            FHIRSearchContext ctx = SearchUtil.parseQueryParameters(Location.class, Collections.emptyMap(), true);
            FHIRPersistenceContext persistenceContext =
                    FHIRPersistenceContextFactory.createPersistenceContext(null, ctx);
            persistence.delete(persistenceContext, Location.class, savedResource.getId());
            if (persistence.isTransactional()) {
                persistence.getTransaction().end();
            }
        }
        FHIRRequestContext.get().setTenantId("default");
    }

    public MultiResourceResult<Resource> runQueryTest(String searchParamCode, String queryValue) throws Exception {
        Map<String, List<String>> queryParms = new HashMap<String, List<String>>(1);
        if (searchParamCode != null && queryValue != null) {
            queryParms.put(searchParamCode, Collections.singletonList(queryValue));
        }
        return runQueryTest(queryParms);
    }

    public MultiResourceResult<Resource> runQueryTestMultiples(String searchParamCode, String... queryValues)
            throws Exception {
        Map<String, List<String>> queryParms = new LinkedHashMap<String, List<String>>(queryValues.length);
        for (String queryValue : queryValues) {
            if (searchParamCode != null && queryValue != null) {
                queryParms.put(searchParamCode, Collections.singletonList(queryValue));
            }
        }
        System.out.println(queryParms);
        return runQueryTest(queryParms);
    }

    public MultiResourceResult<Resource> runQueryTest(Map<String, List<String>> queryParms) throws Exception {
        FHIRSearchContext ctx = SearchUtil.parseQueryParameters(Location.class, queryParms, true);
        FHIRPersistenceContext persistenceContext = FHIRPersistenceContextFactory.createPersistenceContext(null, ctx);
        MultiResourceResult<Resource> result = persistence.search(persistenceContext, Location.class);
        return result;
    }

    @BeforeMethod(alwaysRun = true)
    public void startTrx() throws Exception {
        if (persistence != null && persistence.isTransactional()) {
            persistence.getTransaction().begin();
        }
    }

    @AfterMethod(alwaysRun = true)
    public void commitTrx() throws Exception {
        if (persistence != null && persistence.isTransactional()) {
            persistence.getTransaction().end();
        }
    }

    @Test
    public void testSearchPositionSearchExactSmallRangeMatch() throws Exception {
        // Should match the loaded resource with a real range
        String searchParamCode = "near";
        String queryValue = "42.25475478|-83.6945691|10.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertFalse(result.getResource().size() == 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactLargeRangeMatch() throws Exception {
        // Should match the loaded resource with a real range
        String searchParamCode = "near";
        String queryValue = "42.25475478|0|10000.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResource().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchWithinSmallRange() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "42.0|-83.0|500.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResource().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchNotMatchingRange() throws Exception {
        // Should not match (opposite the loaded resource)
        String searchParamCode = "near";
        String queryValue = "-83.0|42.0|1.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResource().size() == 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchWithinRangeNot() throws Exception {
        // Difference to expected location is greater than 523.3km
        String searchParamCode = "near";
        String queryValue = "-79|40|523.3|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResource().size() == 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchWithinRange() throws Exception {
        // 40, -79
        // Difference to expected location is 1046.6km
        String searchParamCode = "near";
        String queryValue = "40|-79|1046.6|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResource().size(), 0);
    }

    @Test
    public void testSearchPositionSearchExactMatch() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "42.25475478|-83.6945691|0.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertNotEquals(result.getResource().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchMultiples() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue1 = "42.25475478|-83.6945691|0.0|km";
        String queryValue2 = "42.25475478|-83.6945691|0.0|km";

        MultiResourceResult<Resource> result = runQueryTestMultiples(searchParamCode, queryValue1, queryValue2);
        assertNotNull(result);
        assertNotEquals(result.getResource().size(), 0);
        assertNull(result.getOutcome());
    }

    @Test
    public void testSearchPositionSearchExactMatchNotMatching() throws Exception {
        // Should not match (opposite the loaded resource)
        String searchParamCode = "near";
        String queryValue = "83.6945691|-42.25475478|0.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertEquals(result.getResource().size(), 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchUnitMiles() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "42.25475478|-83.6945691|0.0|mi_us";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertFalse(result.getResource().size() == 0);
        assertNull(result.getOutcome());
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadPrefix() throws Exception {
        // Should not match (opposite the loaded resource)
        String searchParamCode = "near";
        String queryValue = "ap83.6945691|-42.25475478|0.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResource().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputLon() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "-42.25475478|FUDGESHOULDNOTMATCH|0.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResource().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputLat() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "FUDGESHOULDNOTMATCH|-42.25475478|0.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResource().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputRadius() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "-42.25475478|-42.25475478|FUDGESHOULDNOTMATCH|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResource().size() == 0);
    }

    @Test(expectedExceptions = { FHIRPersistenceException.class })
    public void testSearchPositionSearchBadInputUnit() throws Exception {
        // Bad Input - Latitude
        String searchParamCode = "near";
        String queryValue = "-42.25475478|-42.25475478|0.0|FUDGESHOULDNOTMATCH";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertTrue(result.getResource().size() == 0);
    }

    @Test
    public void testSearchPositionSearchExactMatchGoodPrefix() throws Exception {
        // Should match the loaded resource
        String searchParamCode = "near";
        String queryValue = "eq42.25475478|-83.6945691|0.0|km";

        MultiResourceResult<Resource> result = runQueryTest(searchParamCode, queryValue);
        assertNotNull(result);
        assertFalse(result.getResource().size() == 0);
        assertNull(result.getOutcome());
    }
}