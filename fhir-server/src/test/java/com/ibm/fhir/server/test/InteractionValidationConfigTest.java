/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.ibm.fhir.server.test;

import static com.ibm.fhir.model.type.String.string;
import static org.junit.Assert.assertNotNull;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.util.List;

import javax.ws.rs.core.MultivaluedHashMap;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.config.FHIRConfiguration;
import com.ibm.fhir.config.FHIRRequestContext;
import com.ibm.fhir.core.HTTPReturnPreference;
import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.Bundle;
import com.ibm.fhir.model.resource.Bundle.Entry;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.OperationOutcome.Issue;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.Procedure;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Xhtml;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.EncounterStatus;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.model.type.code.NarrativeStatus;
import com.ibm.fhir.model.type.code.ProcedureStatus;
import com.ibm.fhir.persistence.FHIRPersistence;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIRRestOperationResponse;
import com.ibm.fhir.server.util.FHIRRestHelper;

public class InteractionValidationConfigTest {

    FHIRPersistence persistence;
    FHIRRestHelper helper;

    public static final OperationOutcome ALL_OK = OperationOutcome.builder()
            .issue(Issue.builder()
                .severity(IssueSeverity.INFORMATION)
                .code(IssueType.INFORMATIONAL)
                .details(CodeableConcept.builder()
                    .text(string("All OK"))
                    .build())
                .build())
            .build();

    @BeforeClass
    void setup() throws FHIRException {
        FHIRConfiguration.setConfigHome("src/test/resources");
        FHIRRegistry.getInstance().addProvider(new MockRegistryResourceProvider());
        persistence = new MockPersistenceImpl();
        helper = new FHIRRestHelper(persistence);
    }

    @AfterClass
    void tearDown() throws FHIRException {
        FHIRConfiguration.setConfigHome("");
        FHIRRequestContext.get().setTenantId("default");
    }

    /**
     * Test a create where create is valid for a specific resource type.
     */
    @Test
    public void testCreateValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doCreate("Patient", patient, null, null, false);
            assertEquals(ALL_OK, response.getOperationOutcome());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a create where create is valid for the generic resource type.
     */
    @Test
    public void testCreateValidGenericResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doCreate("Encounter", encounter, null, null, false);
            assertEquals(ALL_OK, response.getOperationOutcome());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a create where create is valid for any resource type.
     */
    @Test
    public void testCreateValidAnyResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doCreate("Encounter", encounter, null, null, false);
            assertEquals(ALL_OK, response.getOperationOutcome());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a create where create is not valid due to empty specific resource list.
     */
    @Test
    public void testCreateNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");
        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Patient", patient, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'create' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create where create is not valid due to empty generic resource list.
     */
    @Test
    public void testCreateNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Encounter", encounter, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'create' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create where create is not valid due to not in specified resource list.
     */
    @Test
    public void testCreateNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Procedure procedure = Procedure.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Procedure", procedure, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'create' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create where create is not valid due to not in generic resource list.
     */
    @Test
    public void testCreateNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");
        Procedure procedure = Procedure.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Procedure", procedure, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'create' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a create where create is not valid due to open is false.
     */
    @Test
    public void testCreateNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Practitioner practitioner = Practitioner.builder()
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doCreate("Practitioner", practitioner, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test a read where read is valid for a specific resource type.
     */
    @Test
    public void testReadValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Resource resource = helper.doRead("Patient", "1", false, false, null, null);
            assertNotNull(resource);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a read where read is valid for the generic resource type.
     */
    @Test
    public void testReadValidGenericResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Resource resource = helper.doRead("Encounter", "1", false, false, null, null);
            assertNotNull(resource);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a read where read is valid for any resource type.
     */
    @Test
    public void testReadValidAnyResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("default");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Resource resource = helper.doRead("Encounter", "1", false, false, null, null);
            assertNotNull(resource);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a read where read is not valid due to empty specific resource list.
     */
    @Test
    public void testReadNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doRead("Patient", "1", false, false, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'read' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a read where read is not valid due to empty generic resource list.
     */
    @Test
    public void testReadNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doRead("Encounter", "1", false, false, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'read' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a read where read is not valid due to not in specified resource list.
     */
    @Test
    public void testReadNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doRead("Procedure", "1", false, false, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'read' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a read where read is not valid due to not in generic resource list.
     */
    @Test
    public void testReadNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doRead("Procedure", "1", false, false, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'read' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a read where read is not valid due to open is false.
     */
    @Test
    public void testReadNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doRead("Practitioner", "1", false, false, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test a vread where vread is valid for a specific resource type.
     */
    @Test
    public void testVReadValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Resource resource = helper.doVRead("Patient", "1", "1", null);
            assertNotNull(resource);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a vread where vread is valid for the generic resource type.
     */
    @Test
    public void testVReadValidGenericResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Resource resource = helper.doVRead("Encounter", "1", "1", null);
            assertNotNull(resource);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a vread where vread is valid for any resource type.
     */
    @Test
    public void testVReadValidAnyResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("default");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Resource resource = helper.doVRead("Encounter", "1", "1", null);
            assertNotNull(resource);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a vread where vread is not valid due to empty specific resource list.
     */
    @Test
    public void testVReadNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doVRead("Patient", "1", "1", null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'vread' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a vread where vread is not valid due to empty generic resource list.
     */
    @Test
    public void testVReadNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doVRead("Encounter", "1", "1", null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'vread' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a vread where vread is not valid due to not in specified resource list.
     */
    @Test
    public void testVReadNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doVRead("Procedure", "1", "1", null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'vread' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a vread where vread is not valid due to not in generic resource list.
     */
    @Test
    public void testVReadNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doVRead("Procedure", "1", "1", null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'vread' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a vread where vread is not valid due to open is false.
     */
    @Test
    public void testVReadNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doVRead("Practitioner", "1", "1", null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test a history where history is valid for a specific resource type.
     */
    @Test
    public void testHistoryValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doHistory("Patient", "1", new MultivaluedHashMap<>(), "test", null);
            assertNotNull(bundle);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a history where history is valid for the generic resource type.
     */
    @Test
    public void testHistoryValidGenericResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doHistory("Encounter", "1", new MultivaluedHashMap<>(), "test", null);
            assertNotNull(bundle);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a history where history is valid for any resource type.
     */
    @Test
    public void testHistoryValidAnyResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("default");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doHistory("Encounter", "1", new MultivaluedHashMap<>(), "test", null);
            assertNotNull(bundle);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a history where history is not valid due to empty specific resource list.
     */
    @Test
    public void testHistoryNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doHistory("Patient", "1", new MultivaluedHashMap<>(), null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'history' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a history where history is not valid due to empty generic resource list.
     */
    @Test
    public void testHistoryNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doHistory("Encounter", "1", new MultivaluedHashMap<>(), null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'history' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a history where history is not valid due to not in specified resource list.
     */
    @Test
    public void testHistoryNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doHistory("Procedure", "1", new MultivaluedHashMap<>(), null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'history' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a history where history is not valid due to not in generic resource list.
     */
    @Test
    public void testHistoryNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doHistory("Procedure", "1", new MultivaluedHashMap<>(), null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'history' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a history where history is not valid due to open is false.
     */
    @Test
    public void testHistoryNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doHistory("Practitioner", "1", new MultivaluedHashMap<>(), null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test a search where search is valid for a specific resource type.
     */
    @Test
    public void testSearchValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doSearch("Patient", null, null, new MultivaluedHashMap<>(), "test", null, null);
            assertNotNull(bundle);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a search where search is valid for the generic resource type.
     */
    @Test
    public void testSearchValidGenericResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doSearch("Encounter", null, null, new MultivaluedHashMap<>(), "test", null, null);
            assertNotNull(bundle);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a search where search is valid for any resource type.
     */
    @Test
    public void testSearchValidAnyResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("default");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doSearch("Encounter", null, null, new MultivaluedHashMap<>(), "test", null, null);
            assertNotNull(bundle);
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a search where search is not valid due to empty specific resource list.
     */
    @Test
    public void testSearchNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doSearch("Patient", null, null, new MultivaluedHashMap<>(), null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'search' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a search where search is not valid due to empty generic resource list.
     */
    @Test
    public void testSearchNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doSearch("Encounter", null, null, new MultivaluedHashMap<>(), null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'search' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a search where search is not valid due to not in specified resource list.
     */
    @Test
    public void testSearchNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doSearch("Procedure", null, null, new MultivaluedHashMap<>(), null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'search' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a search where search is not valid due to not in generic resource list.
     */
    @Test
    public void testSearchNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doSearch("Procedure", null, null, new MultivaluedHashMap<>(), null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'search' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a search where search is not valid due to open is false.
     */
    @Test
    public void testSearchNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doSearch("Practitioner", null, null, new MultivaluedHashMap<>(), null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test an update where update is valid for a specific resource type.
     */
    @Test
    public void testUpdateValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Patient patient = Patient.builder()
                .id("1")
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doUpdate("Patient", "1", patient, null, null, null, false);
            assertEquals(ALL_OK, response.getOperationOutcome());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test an update where update is valid for the generic resource type.
     */
    @Test
    public void testUpdateValidGenericResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Encounter encounter = Encounter.builder()
                .id("1")
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doUpdate("Encounter", "1", encounter, null, null, null, false);
            assertEquals(ALL_OK, response.getOperationOutcome());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test an update where update is valid for any resource type.
     */
    @Test
    public void testUpdateValidAnyResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("default");
        Encounter encounter = Encounter.builder()
                .id("1")
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doUpdate("Encounter", "1", encounter, null, null, null, false);
            assertEquals(ALL_OK, response.getOperationOutcome());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test an update where update is not valid due to empty specific resource list.
     */
    @Test
    public void testUpdateNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");
        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Patient", "1", patient, null, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'update' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test an update where update is not valid due to empty generic resource list.
     */
    @Test
    public void testUpdateNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Encounter", "1", encounter, null, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'update' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test an update where update is not valid due to not in specified resource list.
     */
    @Test
    public void testUpdateNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");
        Encounter encounter = Encounter.builder()
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Encounter", "1", encounter, null, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'update' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test an update where update is not valid due to not in generic resource list.
     */
    @Test
    public void testUpdateNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");
        Procedure procedure = Procedure.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .status(ProcedureStatus.COMPLETED)
                .subject(Reference.builder()
                    .reference(string("urn:3"))
                    .build())
                .encounter(Reference.builder()
                    .reference(string("urn:1"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:5"))
                    .build())
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Procedure", "1", procedure, null, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'update' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test an update where update is not valid due to open is false.
     */
    @Test
    public void testUpdateNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Practitioner practitioner = Practitioner.builder()
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doUpdate("Practitioner", "1", practitioner, null, null, null, false);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test a patch where patch is not valid due to empty specific resource list.
     */
    @Test
    public void testPatchNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doPatch("Patient", "1", null, null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'patch' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a patch where patch is not valid due to empty generic resource list.
     */
    @Test
    public void testPatchNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doPatch("Encounter", "1", null, null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'patch' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a patch where patch is not valid due to not in specified resource list.
     */
    @Test
    public void testPatchNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doPatch("Procedure", "1", null, null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'patch' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a patch where patch is not valid due to not in generic resource list.
     */
    @Test
    public void testPatchNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doPatch("Encounter", "1", null, null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'patch' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a patch where patch is not valid due to open is false.
     */
    @Test
    public void testPatchNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");
        Practitioner practitioner = Practitioner.builder()
                .active(com.ibm.fhir.model.type.Boolean.TRUE)
                .build();

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doPatch("Practitioner", "1", null, null, null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test a delete where delete is valid for a specific resource type.
     */
    @Test
    public void testDeleteValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doDelete("Patient", "1", null, null);
            List<Issue> issues = response.getOperationOutcome().getIssue();
            assertEquals(1, issues.size());
            assertEquals("Deleted 1 Patient resource(s) with the following id(s): 1",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.INFORMATION, issues.get(0).getSeverity());
            assertEquals(IssueType.INFORMATIONAL, issues.get(0).getCode());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a delete where delete is valid for the generic resource type.
     */
    @Test
    public void testDeleteValidGenericResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doDelete("Encounter", "1", null, null);
            List<Issue> issues = response.getOperationOutcome().getIssue();
            assertEquals(1, issues.size());
            assertEquals("Deleted 1 Encounter resource(s) with the following id(s): 1",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.INFORMATION, issues.get(0).getSeverity());
            assertEquals(IssueType.INFORMATIONAL, issues.get(0).getCode());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a delete where delete is valid for any resource type.
     */
    @Test
    public void testDeleteValidAnyResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("default");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            FHIRRestOperationResponse response = helper.doDelete("Encounter", "1", null, null);
            List<Issue> issues = response.getOperationOutcome().getIssue();
            assertEquals(1, issues.size());
            assertEquals("Deleted 1 Encounter resource(s) with the following id(s): 1",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.INFORMATION, issues.get(0).getSeverity());
            assertEquals(IssueType.INFORMATIONAL, issues.get(0).getCode());
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test a delete where delete is not valid due to empty specific resource list.
     */
    @Test
    public void testDeleteNotValidEmptySpecificResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doDelete("Patient", "1", null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'delete' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a delete where delete is not valid due to empty generic resource list.
     */
    @Test
    public void testDeleteNotValidEmptyGenericResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("empty");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doDelete("Encounter", "1", null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'delete' is not allowed for resource type 'Encounter'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a delete where delete is not valid due to not in specified resource list.
     */
    @Test
    public void testDeleteNotValidNotSpecifiedResourceList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doDelete("Procedure", "1", null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'delete' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a delete where delete is not valid due to not in generic resource list.
     */
    @Test
    public void testDeleteNotValidNotSpecifiedGenericList() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doDelete("Procedure", "1", null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'delete' is not allowed for resource type 'Procedure'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test a delete where delete is not valid due to open is false.
     */
    @Test
    public void testDeleteNotValidOpenFalse() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        // Process request
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doDelete("Practitioner", "1", null, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested resource type 'Practitioner' is not found",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.NOT_FOUND, issues.get(0).getCode());
        }
    }

    /**
     * Test transaction bundle with create valid for specific resource type.
     */
    @Test
    public void testTransactionBundleWithCreateValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest1 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry1 = Bundle.Entry.builder()
                .resource(patient)
                .request(bundleEntryRequest1)
                .build();

        Encounter encounter = Encounter.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v3-ActCode"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(encounter)
                .request(bundleEntryRequest2)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry1, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doBundle(requestBundle, null);
            assertEquals(2, bundle.getEntry().size());
            for (Entry bundleEntry : bundle.getEntry()) {
                assertEquals(ALL_OK, bundleEntry.getResource().as(OperationOutcome.class));
            }
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test transaction bundle with create not valid for specific resource type.
     */
    @Test
    public void testTransactionBundleWithCreateNotValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest1 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry1 = Bundle.Entry.builder()
                .resource(patient)
                .request(bundleEntryRequest1)
                .build();

        Encounter encounter = Encounter.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v3-ActCode"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(encounter)
                .request(bundleEntryRequest2)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.TRANSACTION)
                .entry(bundleEntry1, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            helper.doBundle(requestBundle, null);
            fail();
        } catch (FHIROperationException e) {
            // Validate results
            List<Issue> issues = e.getIssues();
            assertEquals(1, issues.size());
            assertEquals("The requested interaction of type 'create' is not allowed for resource type 'Patient'",
                issues.get(0).getDetails().getText().getValue());
            assertEquals(IssueSeverity.ERROR, issues.get(0).getSeverity());
            assertEquals(IssueType.BUSINESS_RULE, issues.get(0).getCode());
        }
    }

    /**
     * Test batch bundle with create valid for specific resource type.
     */
    @Test
    public void testBatchBundleWithCreateValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest1");

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest1 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry1 = Bundle.Entry.builder()
                .resource(patient)
                .request(bundleEntryRequest1)
                .build();

        Encounter encounter = Encounter.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v3-ActCode"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(encounter)
                .request(bundleEntryRequest2)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.BATCH)
                .entry(bundleEntry1, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doBundle(requestBundle, null);
            assertEquals(2, bundle.getEntry().size());
            for (Entry bundleEntry : bundle.getEntry()) {
                assertEquals(ALL_OK, bundleEntry.getResource().as(OperationOutcome.class));
            }
        } catch (FHIROperationException e) {
            fail();
        }
    }

    /**
     * Test batch bundle with create not valid for specific resource type.
     */
    @Test
    public void testBatchBundleWithCreateNotValidSpecificResourceType() throws Exception {
        FHIRRequestContext.get().setTenantId("interactionConfigTest2");

        Patient patient = Patient.builder()
                .generalPractitioner(Reference.builder()
                    .reference(string("Practitioner/1"))
                    .build())
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest1 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Patient"))
                .build();
        Bundle.Entry bundleEntry1 = Bundle.Entry.builder()
                .resource(patient)
                .request(bundleEntryRequest1)
                .build();

        Encounter encounter = Encounter.builder()
                .text(Narrative.builder()
                    .div(Xhtml.of("<div xmlns=\"http://www.w3.org/1999/xhtml\">Some narrative</div>"))
                    .status(NarrativeStatus.GENERATED)
                    .build())
                .status(EncounterStatus.FINISHED)
                .clazz(Coding.builder()
                    .code(Code.of("AMB"))
                    .system(Uri.of("http://terminology.hl7.org/CodeSystem/v3-ActCode"))
                    .build())
                .reasonReference(Reference.builder()
                    .reference(string("urn:2"))
                    .build())
                .build();
        Bundle.Entry.Request bundleEntryRequest2 = Bundle.Entry.Request.builder()
                .method(HTTPVerb.POST)
                .url(Uri.of("Encounter"))
                .build();
        Bundle.Entry bundleEntry2 = Bundle.Entry.builder()
                .resource(encounter)
                .request(bundleEntryRequest2)
                .build();

        Bundle requestBundle = Bundle.builder()
                .id("bundle1")
                .type(BundleType.BATCH)
                .entry(bundleEntry1, bundleEntry2)
                .build();

        // Process bundle
        FHIRRequestContext.get().setOriginalRequestUri("test");
        FHIRRequestContext.get().setReturnPreference(HTTPReturnPreference.OPERATION_OUTCOME);
        try {
            Bundle bundle = helper.doBundle(requestBundle, null);
            assertEquals(2, bundle.getEntry().size());
            assertEquals("The requested interaction of type 'create' is not allowed for resource type 'Patient'",
                bundle.getEntry().get(0).getResource().as(OperationOutcome.class).getIssue().get(0).getDetails().getText().getValue());
            assertEquals(ALL_OK, bundle.getEntry().get(1).getResource().as(OperationOutcome.class));
        } catch (FHIROperationException e) {
            fail();
        }
    }

}
