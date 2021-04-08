/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.util.BulkDataExportUtil;
import com.ibm.fhir.operation.bulkdata.util.CommonUtil;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 * <a href="https://build.fhir.org/ig/HL7/bulk-data/index.html">BulkDataAccess IG: STU1 - Polling Response</a><br>
 * There are two specific operations
 * <li>status of a bulkdata export/import job</li>
 * <li>delete a bulkdata export/import job</li>
 */
public class StatusOperation extends AbstractOperation {
    private static final String FILE = "status.json";

    private static final CommonUtil common = new CommonUtil();
    private static final BulkDataExportUtil export = new BulkDataExportUtil();

    public StatusOperation() {
        super();
    }

    @Override
    protected OperationDefinition buildOperationDefinition() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(FILE);) {
            return FHIRParser.parser(Format.JSON).parse(in);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @Override
    protected Parameters doInvoke(FHIROperationContext operationContext, Class<? extends Resource> resourceType,
            String logicalId, String versionId, Parameters parameters, FHIRResourceHelpers resourceHelper)
            throws FHIROperationException {
        common.checkEnabled();
        if (logicalId == null && versionId == null && resourceType == null) {
            String method = (String) operationContext.getProperty(FHIROperationContext.PROPNAME_METHOD_TYPE);
            if ("DELETE".equalsIgnoreCase(method)) {
                String job = export.checkAndValidateJob(parameters);
                // For now, we're going to execute the status update, and check.
                // If Base, Export Status (Else Invalid)
                return BulkDataFactory.getInstance(operationContext).delete(job, operationContext);
            } else {
                // Assume GET or POST
                String job = export.checkAndValidateJob(parameters);

                // @implNote We don't need a preflight... we wouldn't have go here otherwise.

                // For now, we're going to execute the status update, and check.
                // If Base, Export Status (Else Invalid)
                return BulkDataFactory.getInstance(operationContext).status(job, operationContext);
            }
        } else {
            // Unsupported on Resource Type
            // Root operation is only supported, and we signal it back here.
            // Don't get fancy, just send it back.
            throw buildExceptionWithIssue("Invalid call $bulkdata-status operation call", IssueType.INVALID);
        }
    }
}