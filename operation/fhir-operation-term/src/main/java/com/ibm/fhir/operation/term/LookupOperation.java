/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.term;

import static com.ibm.fhir.model.type.String.string;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.OperationOutcome;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.code.IssueSeverity;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.registry.FHIRRegistry;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;
import com.ibm.fhir.server.util.FHIRRestHelper;
import com.ibm.fhir.term.service.LookupOutcome;
import com.ibm.fhir.term.service.LookupParameters;
import com.ibm.fhir.term.service.exception.FHIRTermServiceException;

public class LookupOperation extends AbstractTermOperation {
    @Override
    protected OperationDefinition buildOperationDefinition() {
        return FHIRRegistry.getInstance().getResource("http://hl7.org/fhir/OperationDefinition/CodeSystem-lookup",
                OperationDefinition.class);
    }

    @Override
    protected Parameters doInvoke(
            FHIROperationContext operationContext,
            Class<? extends Resource> resourceType,
            String logicalId,
            String versionId,
            Parameters parameters,
            FHIRResourceHelpers resourceHelper) throws FHIROperationException {
        Coding coding = getCoding(parameters, "coding", "code");
        LookupOutcome outcome = null;
        try {
            outcome = service.lookup(coding, LookupParameters.from(parameters));
        } catch (FHIRTermServiceException e) {
            throw new FHIROperationException(e.getMessage(), e.getCause()).withIssue(e.getIssues());
        } catch(Exception e) {
            throw new FHIROperationException("An error occurred during the CodeSystem lookup operation", e);
        }
        if (outcome == null) {
            throw new FHIROperationException("Coding not found")
                .withIssue(OperationOutcome.Issue.builder()
                    .severity(IssueSeverity.ERROR)
                    .code(IssueType.NOT_FOUND.toBuilder()
                        .extension(Extension.builder()
                            .url(FHIRRestHelper.EXTENSION_URL + "/not-found-detail")
                            .value(Code.of("coding"))
                            .build())
                        .build())
                    .details(CodeableConcept.builder()
                        .text(string(String.format("Code '%s' not found in system '%s'", coding.getCode().getValue(), coding.getSystem().getValue())))
                        .build())
                    .build());
        }
        return outcome.toParameters();
    }
}
