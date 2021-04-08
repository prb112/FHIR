/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.server.test.bulkdata;

import static com.ibm.fhir.model.type.String.string;
import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertEquals;
import static org.testng.AssertJUnit.assertFalse;
import static org.testng.AssertJUnit.assertNotNull;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.generator.FHIRGenerator;
import com.ibm.fhir.model.generator.exception.FHIRGeneratorException;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.parser.exception.FHIRParserException;
import com.ibm.fhir.model.resource.Condition;
import com.ibm.fhir.model.resource.Group;
import com.ibm.fhir.model.resource.Group.Member;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Parameters.Parameter;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.server.test.FHIRServerTestBase;

/**
 * These tests exercise the $export operation, a BulkData specification defined operation
 */
public class ExportOperationTest extends FHIRServerTestBase {

    private static final String CLASSNAME = ExportOperationTest.class.getName();
    private static final Logger log = Logger.getLogger(CLASSNAME);

    private static final int TIMEOUT = 10000;

    private static final HttpRequestRetryHandler rh = new HttpRequestRetryHandler(){
        @Override
        public boolean retryRequest(IOException exception, int executionCount,HttpContext context){
            return executionCount<2;
        }
    };

    private static final RequestConfig config = RequestConfig.custom()
            .setConnectTimeout(TIMEOUT)
            .setConnectionRequestTimeout(TIMEOUT)
            .setSocketTimeout(TIMEOUT)
            .build();
    public static final String TEST_GROUP_NAME = "export-operation";
    public static final String PATIENT_VALID_URL = "Patient/$export";
    public static final String GROUP_VALID_URL = "Group/?/$export";
    public static final String BASE_VALID_URL = "/$export";
    public static final String BASE_VALID_STATUS_URL = "/$bulkdata-status";
    public static final String FORMAT_NDJSON = "application/fhir+ndjson";
    public static final String FORMAT_PARQUET = "application/fhir+parquet";
    private final String tenantName = "default";
    private final String dataStoreId = "default";

    // Disabled by default
    private static boolean ON = false;

    public static final boolean DEBUG = false;
    private String exportStatusUrl;
    private String savedPatientId, savedPatientId2;
    private String savedGroupId, savedGroupId2;
    private String path;

    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);

    @BeforeClass
    public void setup() throws Exception {
        Properties testProperties = TestUtil.readTestProperties("test.properties");
        ON = Boolean.parseBoolean(testProperties.getProperty("test.bulkdata.export.enabled", "false"));
        path = testProperties.getProperty("test.bulkdata.path");
        /*
         * isUseMinio = Boolean.parseBoolean(testProperties.getProperty("test.bulkdata.useminio", "false"));
         * minioUserName = testProperties.getProperty("test.bulkdata.minio.username");
         * minioPassword = testProperties.getProperty("test.bulkdata.minio.password");
         * isUseMinioInBuildPipeline =
         * Boolean.parseBoolean(testProperties.getProperty("test.bulkdata.useminio.inbuildpipeline", "false"));
         */
    }

    public Response doPost(String path, String mimeType, String outputFormat, Instant since, List<String> types, List<String> typeFilters, String provider,
        String outcome)
        throws FHIRGeneratorException, IOException {
        WebTarget target = getWebTarget();
        target = target.path(path);
        target = addQueryParameter(target, "_outputFormat", outputFormat);
        // target = addQueryParameter(target, "_since", since);
        target = addQueryParameterList(target, "_type", types);
        target = addQueryParameterList(target, "_typeFilter", typeFilters);
        if (DEBUG) {
            System.out.println("URL -> " + target.getUri());
        }
        Parameters parameters = generateParameters(outputFormat, since, types, null);
        Entity<Parameters> entity = Entity.entity(parameters, FHIRMediaType.APPLICATION_FHIR_JSON);

        //@formatter:off
        return target
                .request(mimeType)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .header("X-FHIR-BULKDATA-PROVIDER", provider)
                .header("X-FHIR-BULKDATA-PROVIDER-OUTCOME", outcome)
                .post(entity, Response.class);
        //@formatter:on
    }

    /**
     * @param outputFormat
     * @param since
     * @param types
     * @param typeFilters
     * @return
     * @throws FHIRGeneratorException
     * @throws IOException
     */
    private Parameters generateParameters(String outputFormat, Instant since, List<String> types, List<String> typeFilters)
        throws FHIRGeneratorException, IOException {
        List<Parameter> parameters = new ArrayList<>();

        if (outputFormat != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_outputFormat"))
                    .value(string(outputFormat))
                    .build());
            //@formatter:on
        }

        if (since != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_since"))
                    .value(since)
                    .build());
            //@formatter:on
        }

        if (types != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_type"))
                    .value(string(types.stream().collect(Collectors.joining(","))))
                    .build());
            //@formatter:on
        }

        if (typeFilters != null) {
            //@formatter:off
            parameters.add(
                Parameter.builder()
                    .name(string("_typeFilters"))
                    .value(string(types.stream().collect(Collectors.joining(","))))
                    .build());
            //@formatter:on
        }

        Parameters.Builder builder = Parameters.builder();
        builder.id(UUID.randomUUID().toString());
        builder.parameter(parameters);
        Parameters ps = builder.build();

        try (StringWriter writer = new StringWriter();) {
            FHIRGenerator.generator(Format.JSON, true).generate(ps, writer);
            if (DEBUG) {
                System.out.println(writer.toString());
            }
        }
        return ps;
    }

    /**
     * add query parameter list
     */
    public WebTarget addQueryParameterList(WebTarget target, String header, List<String> vals) {
        if (header != null && vals != null && !vals.isEmpty()) {
            target = target.queryParam(header, vals.stream().collect(Collectors.joining(",")));
        }
        return target;
    }

    /**
     * adds the query parameter
     */
    public WebTarget addQueryParameter(WebTarget target, String header, String val) {
        if (header != null && val != null) {
            target = target.queryParam(header, val);
        }
        return target;
    }

    public Response doGet(String path, String mimeType, String provider, String outcome) {
        WebTarget target = getWebTarget();
        target = target.path(path);
        // @formatter:off
        return target.request(mimeType)
                     .header("X-FHIR-TENANT-ID", tenantName)
                     .header("X-FHIR-DSID", dataStoreId)
                     .header("X-FHIR-BULKDATA-PROVIDER", provider)
                     .header("X-FHIR-BULKDATA-PROVIDER-OUTCOME", outcome)
                     .get(Response.class);
        // @formatter:on
    }

    private void checkExportStatus(boolean isCheckPatient, boolean s3, List<String> types) throws Exception {
        Response response;
        do {
            response = doGet(exportStatusUrl, FHIRMediaType.APPLICATION_FHIR_JSON, "default", "default");
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            assertEquals(Status.Family.familyOf(response.getStatus()), Status.Family.SUCCESSFUL);
            Thread.sleep(1000);
        } while (response.getStatus() == Response.Status.ACCEPTED.getStatusCode());

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        // Export finished successfully, we should be about to find the "output" part in the message body
        // which includes all the COS objects download urls.
        String body = response.readEntity(String.class);
        if (DEBUG) {
            System.out.println(body);
        }

        if (isCheckPatient) {
            assertTrue(body.contains("Patient_1.ndjson"));
        }

        assertTrue(body.contains("output"));
        verifyUrl(body, s3, types);
    }

    public void verifyUrl(String body, boolean s3, List<String> types) throws Exception {
        List<String> tmpTypes = new ArrayList<>();
        try (ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
                JsonReader jsonReader = JSON_READER_FACTORY.createReader(bais, StandardCharsets.UTF_8)) {
            JsonObject object = jsonReader.readObject();
            JsonArray arr = object.getJsonArray("output");
            assertNotNull(arr);
            assertTrue(arr.size() > 0);
            for (int i = 0; i < arr.size(); i++) {
                JsonObject obj = arr.get(i).asJsonObject();
                String str = obj.getString("url");
                assertNotNull(str);
                assertTrue(str.contains(".ndjson"));
                String resourceType = obj.getString("type").trim();
                tmpTypes.add(resourceType);
                if (!s3) {
                    verifyFileLines(str, obj.getInt("count"), resourceType);
                } else {
                    verifyS3Lines(str, obj.getInt("count"));
                }
            }
        }
        if (true) {
            System.out.println("The list of resources is " + types);
            System.out.println("The actual list of resources is " + tmpTypes);
        }

        // Check the Types that we retrieved are the ones we requested.
        for (String type : tmpTypes) {
            System.out.println(type + " | " + types.contains(type));
            assertTrue(types.contains(type));
        }

        // Check the actual types that we retrieved are the ones we requested.
        for (String type : types) {
            assertTrue(tmpTypes.contains(type));
        }
    }

    public void verifyS3Lines(String workItem, int count) throws IOException {
        CloseableHttpClient cli = getHttpClient();
        HttpGet get = new HttpGet(workItem);
        CloseableHttpResponse getResponse = cli.execute(get);

        int actual = -1;
        try {
            HttpEntity entity = getResponse.getEntity();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent()))) {
                List<String> str = br.lines().collect(Collectors.toList());
                actual = str.size();
            }
            EntityUtils.consume(entity);
        } finally {
            get.releaseConnection();
            getResponse.close();
        }
        assertEquals(actual, count);
    }

    /**
     * generates a static SSL Connection socket factory.
     * @return
     */
    public static SSLConnectionSocketFactory generateSSF() {
        try {
            org.apache.http.ssl.SSLContextBuilder sslContextBuilder = org.apache.http.ssl.SSLContextBuilder.create();

            HostnameVerifier                 verifier = new org.apache.http.conn.ssl.NoopHostnameVerifier();
                TrustStrategy strategy = new org.apache.http.conn.ssl.TrustAllStrategy();
                sslContextBuilder.loadTrustMaterial(strategy);

            SSLContext sslContext = sslContextBuilder.build();

            return new SSLConnectionSocketFactory(sslContext, verifier);
        }catch(NoSuchAlgorithmException|KeyStoreException|KeyManagementException e){
            log.warning("Default Algorithm for Http Client not found " + e.getMessage());
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    public CloseableHttpClient getHttpClient() {
        return HttpClients.custom()
                .setSSLSocketFactory(generateSSF())
                .setRetryHandler(rh)
                .setDefaultRequestConfig(config)
                .build();
    }

    public void verifyFileLines(String workItem, int count, String resourceType) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(path + "/" + workItem));
        for (String line : lines) {
            try (ByteArrayInputStream bais = new ByteArrayInputStream(line.getBytes())) {
                // Checks to see if it's a single type.
                com.ibm.fhir.model.resource.Resource r = FHIRParser.parser(Format.JSON).parse(bais);
                assertEquals(resourceType, r.getClass().getSimpleName());
            } catch (FHIRParserException e) {
                e.printStackTrace();
                Assert.fail();
            }
        }
        int actual = lines.size();
        System.out.println(" Verfied the Export Test [" + actual + "] [" + count + "]");
        assertEquals(actual, count);
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testCreateTwoPatients() throws Exception {
        WebTarget target = getWebTarget();
        // Build a new Patient and then call the 'create' API.
        Patient patient = TestUtil.readLocalResource("Patient_JohnDoe.json");
        Entity<Patient> entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        Response response =
                target.path("Patient")
                    .request()
                    .header("X-FHIR-TENANT-ID", tenantName)
                    .header("X-FHIR-DSID", dataStoreId)
                    .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the patient's logical id value.
        savedPatientId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        //@formatter:off
        response = target.path("Patient/"+ savedPatientId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Create 2nd Patient.
        entity = Entity.entity(patient, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        response = target.path("Patient")
                            .request()
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the 2nd patient's logical id value.
        savedPatientId2 = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new patient and verify it.
        //@formatter:off
        response = target.path("Patient/"+ savedPatientId2)
                .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                .header("X-FHIR-TENANT-ID", tenantName)
                .header("X-FHIR-DSID", dataStoreId)
                .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testCreateTwoPatients" })
    public void testCreateResourceForPatients() throws Exception {
        WebTarget target = getWebTarget();

        // Next, create an Observation for patient1.
        Observation observation = TestUtil.buildPatientObservation(savedPatientId, "Observation1.json");
        Entity<Observation> obs = Entity.entity(observation, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        Response response =
                target.path("Observation")
                      .request()
                      .header("X-FHIR-TENANT-ID", tenantName)
                      .header("X-FHIR-DSID", dataStoreId)
                      .post(obs, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String observationId = getLocationLogicalId(response);
        //@formatter:off
        response = target.path("Observation/"+ observationId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Create a Condition for patient2.
        Condition condition = buildCondition(savedPatientId2, "Condition.json");
        Entity<Condition> cdt = Entity.entity(condition, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        response = target.path("Condition")
                            .request()
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .post(cdt, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());

        String conditionId = getLocationLogicalId(response);
        //@formatter:off
        response = target.path("Condition/"+ conditionId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testCreateResourceForPatients" })
    public void testGroup() throws Exception {
        WebTarget target = getWebTarget();

        // (1) Build a new Group.
        Group group = TestUtil.readExampleResource("json/spec/group-example-member.json");
        Entity<Group> entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        Response response =
                target.path("Group")
                        .request()
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        URI location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        savedGroupId = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new group and verify it.
        //@formatter:off
        response = target.path("Group/" + savedGroupId)
                            .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .get();
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());
        Group responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 4);

        // (2) Build a new Group with patient2 and the above group only as members.
        ArrayList<Member> members = new ArrayList<>();
        group = group.toBuilder().member(members).build();
        //@formatter:off
        Member member = Member.builder()
                                .entity(
                                    Reference.builder()
                                        .reference(string("Patient/" + savedPatientId2))
                                        .build())
                                .build();
        //@formatter:on
        members.add(member);
        //@formatter:off
        member = Member.builder()
                            .entity(Reference.builder()
                                        .reference(string("Group/" + savedGroupId))
                                        .build())
                            .build();
        //@formatter:on
        members.add(member);
        group = group.toBuilder().member(members).build();
        entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:off
        response = target.path("Group")
                            .request()
                            .header("X-FHIR-TENANT-ID", tenantName)
                            .header("X-FHIR-DSID", dataStoreId)
                            .post(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.CREATED.getStatusCode());
        location = response.getLocation();
        assertNotNull(location);
        assertNotNull(location.toString());
        assertFalse(location.toString().isEmpty());

        // Get the group logical id value.
        savedGroupId2 = getLocationLogicalId(response);

        // Next, call the 'read' API to retrieve the new group and verify it.
        //@formatter:off
        response = target.path("Group/" + savedGroupId2)
                        .request(FHIRMediaType.APPLICATION_FHIR_JSON)
                        .header("X-FHIR-TENANT-ID", tenantName)
                        .header("X-FHIR-DSID", dataStoreId)
                        .get();
        //@formatter:off
        assertResponse(response, Response.Status.OK.getStatusCode());
        responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 2);

        // (3) Modify the first group to contain the second group and patient1 only, this creates a circle reference
        // situation.
        members.clear();
        //@formatter:off
        member = Member.builder()
                    .entity(Reference.builder()
                        .reference(string("Patient/" + savedPatientId))
                        .build())
                    .build();
        //@formatter:on
        members.add(member);

        //@formatter:off
        member = Member.builder()
                    .entity(Reference.builder()
                        .reference(string("Group/" + savedGroupId2))
                        .build())
                    .build();
        //@formatter:on
        members.add(member);
        group = group.toBuilder().id(savedGroupId).member(members).build();

        // Update the patient and verify the response.
        entity = Entity.entity(group, FHIRMediaType.APPLICATION_FHIR_JSON);
        //@formatter:on
        response = target.path("Group/"
                + savedGroupId).request().header("X-FHIR-TENANT-ID", tenantName).header("X-FHIR-DSID", dataStoreId).put(entity, Response.class);
        //@formatter:on
        assertResponse(response, Response.Status.OK.getStatusCode());

        // Next, call the 'read' API to retrieve the new group and verify it.
        //@formatter:on
        response = target.path("Group/"
                + savedGroupId).request(FHIRMediaType.APPLICATION_FHIR_JSON).header("X-FHIR-TENANT-ID", tenantName).header("X-FHIR-DSID", dataStoreId).get();
        //@formatter:off
        assertResponse(response, Response.Status.OK.getStatusCode());
        responseGroup = response.readEntity(Group.class);
        assertNotNull(responseGroup);
        assertTrue(responseGroup.getMember().size() == 2);
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testBaseExport() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Patient");
            Response response = doPost(
                    BASE_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(false, false, types);
        } else {
            System.out.println("Base Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testBaseExportWithBadResourceType() throws Exception {
        if (ON) {
            Response response =
                doPost(BASE_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("DomainResource"), null, "default", "default");
            assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());

            response =
                doPost(BASE_VALID_URL, FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Resource"), null, "default", "default");
            assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testGroupWhichIsOutsidePatientCompartment() throws Exception {
        if (ON) {
            Response response =
                    doPost(GROUP_VALID_URL.replace("?", "1234"), FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Binary"), null, "default", "default");
            assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    @Test(groups = { TEST_GROUP_NAME })
    public void testPatientWhichIsOutsidePatientCompartment() throws Exception {
        if (ON) {
            Response response =
                    doPost(PATIENT_VALID_URL.replace("?", "1234"), FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON, Instant.of("2019-01-01T08:21:26.94-04:00"), Arrays.asList("Binary"), null, "default", "default");
            assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode());
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" }, enabled = true)
    public void testBaseExportToS3() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Patient");
            Response response = doPost(
                    BASE_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON,
                    FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "minio",
                    "minio");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(false, true, types);
        } else {
            System.out.println("Base Export Test Disabled, Skipping");
        }
    }

    /**
     * Ensure that export to parquet returns a reasonable error when its disabled on the server
     */
    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" }, enabled = true)
    public void testExportToParquetResponse() throws Exception {
        if (ON) {
            Response response = doPost(
                    BASE_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_PARQUET,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    Arrays.asList("Patient"),
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.BAD_REQUEST.getStatusCode(), "Response status");
        } else {
            System.out.println("Base Export Test Disabled, Skipping");
        }
    }

    /**
     * Disabled due to limitations with writing parquet to minio
     * https://stackoverflow.com/questions/63174444/how-to-write-parquet-to-minio-from-spark
     */
    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" }, enabled = false)
    public void testBaseExportToParquet() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Patient");
            Response response = doPost(
                    BASE_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_PARQUET,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(false, false, types);
        } else {
            System.out.println("Base Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testPatientExport() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Observation", "Condition", "Patient");
            Response response = doPost(
                    PATIENT_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(true, false, types);
        } else {
            System.out.println("Patient Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testPatientExportToS3() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Observation", "Condition", "Patient");
            Response response = doPost(
                    PATIENT_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "minio",
                    "minio");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(true, true, types);
        } else {
            System.out.println("Patient Export Test Disabled, Skipping");
        }
    }

    /**
     * Disabled due to limitations with writing parquet to minio
     * https://stackoverflow.com/questions/63174444/how-to-write-parquet-to-minio-from-spark
     */
    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" }, enabled = false)
    public void testPatientExportToParquet() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Observation", "Condition", "Patient");
            Response response = doPost(
                    PATIENT_VALID_URL,
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_PARQUET,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkExportStatus(true, false, types);
        } else {
            System.out.println("Patient Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testGroupExport() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Patient", "Group", "Condition", "Observation");
            Response response = doPost(
                    GROUP_VALID_URL.replace("?", savedGroupId2),
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkGroupExportStatus(false, types);
        } else {
            System.out.println("Group Export Test Disabled, Skipping");
        }
    }

    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" })
    public void testGroupExportToS3() throws Exception {
        if (ON) {
            List<String> types = Arrays.asList("Patient", "Group", "Condition", "Observation");
            Response response = doPost(
                    GROUP_VALID_URL.replace("?", savedGroupId2),
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_NDJSON,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    types,
                    null,
                    "minio",
                    "minio");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkGroupExportStatus(true, types);
        } else {
            System.out.println("Group Export Test Disabled, Skipping");
        }
    }

    /**
     * Disabled due to limitations with writing parquet to minio
     * https://stackoverflow.com/questions/63174444/how-to-write-parquet-to-minio-from-spark
     */
    @Test(groups = { TEST_GROUP_NAME }, dependsOnMethods = { "testGroup" }, enabled = false)
    public void testGroupExportToParquet() throws Exception {
        if (ON) {
            List<String> types = new ArrayList<>();
            Response response = doPost(
                    GROUP_VALID_URL.replace("?", savedGroupId2),
                    FHIRMediaType.APPLICATION_FHIR_JSON, FORMAT_PARQUET,
                    Instant.of("2019-01-01T08:21:26.94-04:00"),
                    null,
                    null,
                    "default",
                    "default");
            assertEquals(response.getStatus(), Response.Status.ACCEPTED.getStatusCode());

            // check the content-location that's returned.
            String contentLocation = response.getHeaderString("Content-Location");
            if (DEBUG) {
                System.out.println("Content Location: " + contentLocation);
            }

            assertTrue(contentLocation.contains(BASE_VALID_STATUS_URL));
            exportStatusUrl = contentLocation;
            checkGroupExportStatus(false, types);
        } else {
            System.out.println("Group Export Test Disabled, Skipping");
        }
    }

    private void checkGroupExportStatus(boolean s3, List<String> types) throws Exception {
        Response response;
        do {
            response = doGet(exportStatusUrl, FHIRMediaType.APPLICATION_FHIR_JSON, "default", "default");
            // 202 accept means the request is still under processing
            // 200 mean export is finished
            assertEquals(Status.Family.familyOf(response.getStatus()), Status.Family.SUCCESSFUL);
            Thread.sleep(5000);
        } while (response.getStatus() == Response.Status.ACCEPTED.getStatusCode());

        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        // Export finished successfully, we should be about to find the "output" part in the message body
        // which includes all the COS objects download urls.
        String body = response.readEntity(String.class);
        JsonObject jsonObject = JSON_READER_FACTORY.createReader(new StringReader(body)).readObject();
        assertTrue(jsonObject.containsKey("output"));

        verifyUrl(body, s3, types);
    }
}
