/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata;

import java.io.InputStream;
import java.util.List;

import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.format.Format;
import com.ibm.fhir.model.parser.FHIRParser;
import com.ibm.fhir.model.resource.OperationDefinition;
import com.ibm.fhir.model.resource.Parameters;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.config.preflight.Preflight;
import com.ibm.fhir.operation.bulkdata.config.preflight.PreflightFactory;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.processor.BulkDataFactory;
import com.ibm.fhir.operation.bulkdata.util.BulkDataImportUtil;
import com.ibm.fhir.operation.bulkdata.util.CommonUtil;
import com.ibm.fhir.server.operation.spi.AbstractOperation;
import com.ibm.fhir.server.operation.spi.FHIROperationContext;
import com.ibm.fhir.server.operation.spi.FHIRResourceHelpers;

/**
 * BulkData Specification Proposal:
 * <a href= "https://github.com/smart-on-fhir/bulk-import/blob/master/import.md">$import</a>
 */
public class ImportOperation extends AbstractOperation {
    private static final String FILE = "import.json";

    private static final CommonUtil common = new CommonUtil();

    public ImportOperation() {
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
        common.checkAllowed(operationContext);

        if (!"POST".equals(operationContext.getProperty(FHIROperationContext.PROPNAME_METHOD_TYPE))) {
            throw buildExceptionWithIssue("Invalid call $import operation only POST allowed",
                IssueType.INVALID);
        }

        // Checks the Import Type
        checkImportType(operationContext.getType());

        BulkDataImportUtil util = new BulkDataImportUtil(operationContext, parameters);

        // Parameter: inputFormat
        String inputFormat = util.retrieveInputFormat();

        // Parameter: inputSource
        String inputSource = util.retrieveInputSource();

        // Parameter: input
        List<Input> inputs = util.retrieveInputs();

        // Parameter: storageDetail
        StorageDetail storageDetail = util.retrieveStorageDetails();

        Preflight preflight =  PreflightFactory.getInstance(operationContext, inputs, null, inputFormat);
        preflight.checkStorageAllowed(storageDetail);
        preflight.preflight();
        return BulkDataFactory.getInstance(operationContext)
                .importBulkData(inputFormat, inputSource, inputs, storageDetail, operationContext);
    }

    private void checkImportType(FHIROperationContext.Type type) throws FHIROperationException {
        // Check Import Type is System.  We only support system right now.

        if (!FHIROperationContext.Type.SYSTEM.equals(type)) {
            throw buildExceptionWithIssue("Invalid call $import operation call only system is allowed",
                    IssueType.INVALID);
        }
    }
}