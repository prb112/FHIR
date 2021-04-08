/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.persistence.test.common;

import static com.ibm.fhir.model.type.String.string;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;

import java.util.List;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.fhir.model.resource.Device;
import com.ibm.fhir.model.resource.Encounter;
import com.ibm.fhir.model.resource.Observation;
import com.ibm.fhir.model.resource.Patient;
import com.ibm.fhir.model.resource.Practitioner;
import com.ibm.fhir.model.resource.RelatedPerson;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.test.TestUtil;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.util.ModelSupport;

/**
 * This class contains a collection of tests that will be run against
 * each of the various persistence layer implementations.
 * There will be a subclass in each persistence project.
 */
public abstract class AbstractCompartmentTest extends AbstractPersistenceTest {
    Patient savedPatient;
    Device savedDevice;
    Encounter savedEncounter;
    Practitioner savedPractitioner;
    RelatedPerson savedRelatedPerson;
    Observation savedObservation;
    Observation savedObservation2;

    /**
     * Builds and saves an Observation with the following references:
     *
     * Observation.subject = Patient
     * Observation.device = Device
     * Observation.encounter = Encounter
     * Observation.performer[0] = Patient
     * Observation.performer[1] = Practitioner
     * Observation.performer[2] = RelatedPerson
     *
     * Builds and saves an Observation with the following logical ID-only reference:
     *
     * Observation.subject = Patient
     */
    @BeforeClass
    public void createResources() throws Exception {
        Observation.Builder observationBuilder = ((Observation) TestUtil.readExampleResource("json/ibm/minimal/Observation-1.json")).toBuilder();
        Observation.Builder observation2Builder = ((Observation) TestUtil.readExampleResource("json/ibm/minimal/Observation-1.json")).toBuilder();

        Patient patient = TestUtil.readExampleResource("json/ibm/minimal/Patient-1.json");
        savedPatient = persistence.create(getDefaultPersistenceContext(), patient).getResource();
        observationBuilder.subject(buildReference(savedPatient));
        observationBuilder.performer(buildReference(savedPatient));
        observation2Builder.subject(Reference.builder().reference(string(savedPatient.getId())).build());

        Device device = TestUtil.readExampleResource("json/ibm/minimal/Device-1.json");
        savedDevice = persistence.create(getDefaultPersistenceContext(), device).getResource();
        observationBuilder.device(buildReference(savedDevice));

        Encounter encounter = TestUtil.readExampleResource("json/ibm/minimal/Encounter-1.json");
        savedEncounter = persistence.create(getDefaultPersistenceContext(), encounter).getResource();
        observationBuilder.encounter(buildReference(savedEncounter));

        Practitioner practitioner = TestUtil.readExampleResource("json/ibm/minimal/Practitioner-1.json");
        savedPractitioner = persistence.create(getDefaultPersistenceContext(), practitioner).getResource();
        observationBuilder.performer(buildReference(savedPractitioner));

        RelatedPerson relatedPerson = TestUtil.readExampleResource("json/ibm/minimal/RelatedPerson-1.json");
        savedRelatedPerson = persistence.create(getDefaultPersistenceContext(), relatedPerson).getResource();
        observationBuilder.performer(buildReference(savedRelatedPerson));

        savedObservation = persistence.create(getDefaultPersistenceContext(), observationBuilder.build()).getResource();
        assertNotNull(savedObservation);
        assertNotNull(savedObservation.getId());
        assertNotNull(savedObservation.getMeta());
        assertNotNull(savedObservation.getMeta().getVersionId().getValue());
        assertEquals("1", savedObservation.getMeta().getVersionId().getValue());

        savedObservation2 = persistence.create(getDefaultPersistenceContext(), observation2Builder.build()).getResource();
        assertNotNull(savedObservation2);
        assertNotNull(savedObservation2.getId());
        assertNotNull(savedObservation2.getMeta());
        assertNotNull(savedObservation2.getMeta().getVersionId().getValue());
        assertEquals("1", savedObservation2.getMeta().getVersionId().getValue());
    }

    private Reference buildReference(Resource resource) {
        assertNotNull(resource);
        assertNotNull(resource.getId());

        String resourceTypeName = ModelSupport.getTypeName(resource.getClass());
        return Reference.builder()
                        .reference(string(resourceTypeName + "/" + resource.getId()))
                        .build();
    }

    @Test
    public void testPatientCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Patient", savedPatient.getId(),
                                    Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testPatientCompartmentViaLogicalId() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Patient", savedPatient.getId(),
                                    Observation.class, "_id", savedObservation2.getId());
        assertEquals(0, results.size());
    }

    @Test
    public void testDeviceCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Device", savedDevice.getId(),
                                    Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testEncounterCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Encounter", savedEncounter.getId(),
                                    Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testPractitionerCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("Practitioner", savedPractitioner.getId(),
                                    Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }

    @Test
    public void testRelatedPersonCompartment() throws Exception {
        List<Resource> results = runCompartmentQueryTest("RelatedPerson", savedRelatedPerson.getId(),
                                    Observation.class, "_id", savedObservation.getId());
        assertEquals(1, results.size());
    }
}
