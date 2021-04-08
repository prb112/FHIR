/*
 * (C) Copyright IBM Corp. 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.operation.bulkdata.config.preflight.impl;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.ibm.fhir.core.FHIRMediaType;
import com.ibm.fhir.exception.FHIROperationException;
import com.ibm.fhir.model.type.code.IssueType;
import com.ibm.fhir.operation.bulkdata.OperationConstants;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationAdapter;
import com.ibm.fhir.operation.bulkdata.config.ConfigurationFactory;
import com.ibm.fhir.operation.bulkdata.model.type.Input;
import com.ibm.fhir.operation.bulkdata.model.type.StorageDetail;
import com.ibm.fhir.operation.bulkdata.model.type.StorageType;

/**
 * Preflight is a health check prior to executing the calls on BulkData.
 */
public class FilePreflight extends NopPreflight {

    public FilePreflight(String source, String outcome, List<Input> inputs, OperationConstants.ExportType exportType, String format) {
        super(source, outcome, inputs, exportType, format);
    }

    @Override
    public void preflight() throws FHIROperationException {
        super.preflight();
        ConfigurationAdapter adapter = ConfigurationFactory.getInstance();
        String base = adapter.getBaseFileLocation(getSource());
        checkFile(base);
        checkFormat();
        if (adapter.shouldStorageProviderCollectOperationOutcomes(getSource())) {
            checkFile(adapter.getBaseFileLocation(getOutcome()));
        }
    }

    private void checkFile(String base) throws FHIROperationException {
        if (base != null) {
            Path p = Paths.get(base);
            boolean accessible = Files.isReadable(p);
            if (getInputs() == null) {
                // This is an export
                accessible = Files.isWritable(p);
            } else {
                // This is an import
                for (Input input : getInputs()) {

                    // We want to append the input path on the base, we don't want to allow everything.
                    Path p1 = Paths.get(base, input.getUrl()).normalize();
                    if (!p1.startsWith(p)) {
                        throw util.buildExceptionWithIssue("The path is outside the accepted base path", IssueType.INVALID);
                    }

                    accessible = Files.isReadable(p1);
                    if (!accessible) {
                        // Skip out of the for loop
                        return;
                    }
                }
            }
            if (!accessible) {
                throw util.buildExceptionWithIssue("The location for the bulkdata operation is not found", IssueType.NO_STORE);
            }
        } else {
            throw util.buildExceptionWithIssue("No File Base Configured for FHIR bulkdata operation", IssueType.INVALID);
        }
    }

    private void checkFormat() throws FHIROperationException {
        if (!FHIRMediaType.APPLICATION_NDJSON.equals(getFormat())) {
            throw util.buildExceptionWithIssue("File: the requested storageProvider '" + getSource() +
                    "' does not support format '" + getFormat() + "'", IssueType.INVALID);
        }
    }

    @Override
    public void checkStorageAllowed(StorageDetail storageDetail) throws FHIROperationException {
        if (storageDetail != null && !StorageType.FILE.value().equals(storageDetail.getType())){
            throw util.buildExceptionWithIssue("File: Configuration not set to import from storageDetail '" +
                    getSource() + "'", IssueType.INVALID);
        }
    }
}
