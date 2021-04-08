/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service.provider;

import java.util.List;
import java.util.Set;
import java.util.function.Function;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.code.CodeSystemContentMode;
import com.ibm.fhir.term.exception.FHIRTermException;
import com.ibm.fhir.term.service.exception.FHIRTermServiceException;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;

/**
 * Registry-based implementation of the {@link FHIRTermServiceProvider} interface using {@link CodeSystemSupport}
 */
public class RegistryTermServiceProvider implements FHIRTermServiceProvider {
    @Override
    public Set<Concept> closure(CodeSystem codeSystem, Code code) {
        Concept concept = CodeSystemSupport.findConcept(codeSystem, code);
        return CodeSystemSupport.getConcepts(concept);
    }

    @Override
    public Concept getConcept(CodeSystem codeSystem, Code code) {
        Concept concept = CodeSystemSupport.findConcept(codeSystem, code);
        if (concept != null) {
            // child concepts are removed for consistency with the other providers
            return CodeSystemSupport.CONCEPT_NO_CHILDREN_FUNCTION.apply(concept);
        }
        return null;
    }

    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem) {
        return getConcepts(codeSystem, CodeSystemSupport.SIMPLE_CONCEPT_FUNCTION);
    }

    @Override
    public <R> Set<R> getConcepts(CodeSystem codeSystem, Function<Concept, ? extends R> function) {
        return CodeSystemSupport.getConcepts(codeSystem, function);
    }

    @Override
    public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
        return getConcepts(codeSystem, filters, CodeSystemSupport.SIMPLE_CONCEPT_FUNCTION);
    }

    @Override
    public <R> Set<R> getConcepts(CodeSystem codeSystem, List<Filter> filters, Function<Concept, ? extends R> function) {
        try {
            return CodeSystemSupport.getConcepts(codeSystem, filters, function);
        } catch (FHIRTermException e) {
            throw new FHIRTermServiceException(e.getMessage(), e, e.getIssues());
        }
    }

    @Override
    public boolean hasConcept(CodeSystem codeSystem, Code code) {
        return getConcept(codeSystem, code) != null;
    }

    @Override
    public boolean isSupported(CodeSystem codeSystem) {
        return CodeSystemContentMode.COMPLETE.equals(codeSystem.getContent());
    }

    @Override
    public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
        Concept concept = CodeSystemSupport.findConcept(codeSystem, codeA);
        return (CodeSystemSupport.findConcept(codeSystem, concept, codeB) != null);
    }
}
