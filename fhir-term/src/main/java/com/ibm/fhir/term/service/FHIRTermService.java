/*
 * (C) Copyright IBM Corp. 2020, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.term.service;

import static com.ibm.fhir.model.type.String.string;
import static com.ibm.fhir.model.util.FHIRUtil.STRING_DATA_ABSENT_REASON_UNKNOWN;
import static com.ibm.fhir.term.util.CodeSystemSupport.normalize;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.ibm.fhir.model.resource.CodeSystem;
import com.ibm.fhir.model.resource.CodeSystem.Concept;
import com.ibm.fhir.model.resource.ConceptMap;
import com.ibm.fhir.model.resource.ConceptMap.Group;
import com.ibm.fhir.model.resource.ConceptMap.Group.Element;
import com.ibm.fhir.model.resource.ConceptMap.Group.Element.Target;
import com.ibm.fhir.model.resource.ValueSet;
import com.ibm.fhir.model.resource.ValueSet.Compose.Include.Filter;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.CodeSystemHierarchyMeaning;
import com.ibm.fhir.model.type.code.ConceptSubsumptionOutcome;
import com.ibm.fhir.term.service.LookupOutcome.Designation;
import com.ibm.fhir.term.service.LookupOutcome.Property;
import com.ibm.fhir.term.service.TranslationOutcome.Match;
import com.ibm.fhir.term.service.provider.RegistryTermServiceProvider;
import com.ibm.fhir.term.spi.FHIRTermServiceProvider;
import com.ibm.fhir.term.util.CodeSystemSupport;
import com.ibm.fhir.term.util.ValueSetSupport;

public class FHIRTermService {
    private static final FHIRTermService INSTANCE = new FHIRTermService();
    private static final FHIRTermServiceProvider NULL_TERM_SERVICE_PROVIDER = new FHIRTermServiceProvider() {
        @Override
        public Set<Concept> closure(CodeSystem codeSystem, Code code) {
            return Collections.emptySet();
        }

        @Override
        public Concept getConcept(CodeSystem codeSystem, Code code) {
            return null;
        }

        @Override
        public Set<Concept> getConcepts(CodeSystem codeSystem) {
            return Collections.emptySet();
        }

        @Override
        public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
            return Collections.emptySet();
        }

        @Override
        public boolean hasConcept(CodeSystem codeSystem, Code code) {
            return false;
        }

        @Override
        public boolean isSupported(CodeSystem codeSystem) {
            return false;
        }

        @Override
        public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
            return false;
        }
    };
    private final List<FHIRTermServiceProvider> providers;

    private FHIRTermService() {
        providers = new CopyOnWriteArrayList<>(loadProviders());
    }

    /**
     * Add the given {@link FHIRTermServiceProvider} to the service
     *
     * @param provider
     *     the term service provider
     */
    public void addProvider(FHIRTermServiceProvider provider) {
        Objects.requireNonNull(provider);
        providers.add(provider);
    }

    public Set<Concept> closure(CodeSystem codeSystem, Code code) {
        return findProvider(codeSystem).closure(codeSystem, code);
    }

    /**
     * Generate the transitive closure for the code system concept represented by the given coding
     *
     * @param coding
     *     the coding
     * @return
     *     a set containing the transitive closure for the code system concept represented by the given coding
     */
    public Set<Concept> closure(Coding coding) {
        Uri system = coding.getSystem();
        java.lang.String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
        Code code = coding.getCode();

        if (system != null && code != null) {
            java.lang.String url = (version != null) ? system.getValue() + "|" + version : system.getValue();
            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(url);
            if (codeSystem != null &&
                    (CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) ||
                            codeSystem.getHierarchyMeaning() == null)) {
                FHIRTermServiceProvider provider = findProvider(codeSystem);
                if (provider.hasConcept(codeSystem, code)) {
                    return provider.closure(codeSystem, code);
                }
            }
        }

        return Collections.emptySet();
    }

    /**
     * Generate a map containing the transitive closures for the code system concepts represented by the giving codings
     *
     * @param codings
     *     the codings
     * @return
     *     a map of sets containing the transitive closures for the code system concepts represented by the given codings
     */
    public Map<Coding, Set<Concept>> closure(Set<Coding> codings) {
        Map<Coding, Set<Concept>> result = new LinkedHashMap<>();

        Map<CodeSystem, Set<Code>> codeSetMap = new LinkedHashMap<>();
        Map<CodeSystem, Map<Code, Coding>> codingMapMap = new LinkedHashMap<>();

        for (Coding coding : codings) {
            Uri system = coding.getSystem();
            java.lang.String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
            Code code = coding.getCode();

            if (system == null || code == null) {
                return Collections.emptyMap();
            }

            java.lang.String url = (version != null) ? system.getValue() + "|" + version : system.getValue();

            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(url);

            if (codeSystem == null ||
                    (!CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning()) &&
                            codeSystem.getHierarchyMeaning() != null)) {
                return Collections.emptyMap();
            }

            codeSetMap.computeIfAbsent(codeSystem, k -> new LinkedHashSet<>()).add(code);
            codingMapMap.computeIfAbsent(codeSystem, k -> new LinkedHashMap<>()).put(code, coding);
        }

        for (CodeSystem codeSystem : codeSetMap.keySet()) {
            Set<Code> codes = codeSetMap.get(codeSystem);

            FHIRTermServiceProvider provider = findProvider(codeSystem);
            if (!provider.hasConcepts(codeSystem, codes)) {
                return Collections.emptyMap();
            }

            Map<Code, Set<Concept>> closureMap = provider.closure(codeSystem, codes);

            for (Code code : closureMap.keySet()) {
                Coding coding = codingMapMap.get(codeSystem).get(code);
                Set<Concept> closure = closureMap.get(code);
                result.put(coding, closure);
            }
        }

        return result;
    }

    /**
     * Get a map of sets containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened
     *
     * @param codeSystem
     *     the code system
     * @param codes
     *     the set of roots of hierarchies containing the Concept instances to be flattened
     * @return
     *     a map containing flattened sets of Concept instances for the given trees
     */
    public Map<Code, Set<Concept>> closure(CodeSystem codeSystem, Set<Code> codes) {
        return findProvider(codeSystem).closure(codeSystem, codes);
    }

    /**
     * Expand the given value set
     *
     * @param valueSet
     *     the value set to expand
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    public ValueSet expand(ValueSet valueSet) {
        return ValueSetSupport.expand(valueSet);
    }

    /**
     * Expand the given value set and expansion parameters
     *
     * @param valueSet
     *     the value set to expand
     * @param parameters
     *     the expansion parameters
     * @return
     *     the expanded value set, or the original value set if already expanded or unable to expand
     */
    public ValueSet expand(ValueSet valueSet, ExpansionParameters parameters) {
        if (!ExpansionParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Expansion parameters are not supported");
        }
        return ValueSetSupport.expand(valueSet);
    }

    /**
     * Get the concept in the provided code system with the specified code.
     *
     * @param codeSystem
     *     the code system to search
     * @param code
     *     the code to match
     * @return
     *     the code system concept with the specified code, or null if no such concept exists
     */
    public Concept getConcept(CodeSystem codeSystem, Code code) {
        return findProvider(codeSystem).getConcept(codeSystem, code);
    }

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened.
     *
     * @param codeSystem
     *     the code system containing the set of Concept instances to be flattened
     * @return
     *     flattened set of Concept instances for the given code system
     */
    public Set<Concept> getConcepts(CodeSystem codeSystem) {
        return findProvider(codeSystem).getConcepts(codeSystem);
    }

    /**
     * Get a set containing {@link R} instances mapped from concepts where all structural
     * hierarchies have been flattened.
     *
     * @param <R>
     *     the element type of the result set
     * @param codeSystem
     *     the code system containing the set of Concept instances to be flattened
     * @param function
     *     the function to apply to each element of the result set
     * @return
     *     flattened set of {@link R} instances mapped from concepts for the given code system
     */
    public <R> Set<R> getConcepts(CodeSystem codeSystem, Function<Concept, ? extends R> function) {
        return findProvider(codeSystem).getConcepts(codeSystem, function);
    }

    /**
     * Get a set containing {@link CodeSystem.Concept} instances where all structural
     * hierarchies have been flattened and filtered by the given set of value set include filters.
     *
     * @param codeSystem
     *     the code system
     * @param filters
     *     the value set include filters
     * @return
     *     flattened / filtered list of Concept instances for the given code system
     */
    public Set<Concept> getConcepts(CodeSystem codeSystem, List<Filter> filters) {
        return findProvider(codeSystem).getConcepts(codeSystem, filters);
    }

    /**
     * Get a set containing {@link R} instances mapped from concepts where all structural
     * hierarchies have been flattened and filtered by the given set of value set include filters.
     *
     * @param <R>
     *     the element type of the result set
     * @param codeSystem
     *     the code system containing the set of Concept instances to be flattened / filtered
     * @param filters
     *     the value set include filters
     * @param function
     *     the function to apply to each element of the result set
     * @return
     *     flattened / filtered set of {@link R} instances mapped from concepts for the given code system
     */
    public <R> Set<R> getConcepts(CodeSystem codeSystem, List<Filter> filters, Function<Concept, ? extends R> function) {
        return findProvider(codeSystem).getConcepts(codeSystem, filters, function);
    }

    /**
     * Indicates whether the given code system contains a concept with the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the code
     * @return
     *     true if the given code system contains a concept with the specified code, false otherwise
     */
    public boolean hasConcept(CodeSystem codeSystem, Code code) {
        return findProvider(codeSystem).hasConcept(codeSystem, code);
    }

    /**
     * Indicates whether the given code system contains a concept for each of the specified codes.
     *
     * @param codeSystem
     *     the code system
     * @param codes
     *     the codes
     * @return
     *     true if the given code system contains a concept for each of the specified codes, false otherwise
     */
    public boolean hasConcepts(CodeSystem codeSystem, Set<Code> codes) {
        return findProvider(codeSystem).hasConcepts(codeSystem, codes);
    }

    /**
     * Indicates whether the given value set is expandable
     *
     * @param valueSet
     *     the value set
     * @return
     *     true if the given value set is expandable, false otherwise
     */
    public boolean isExpandable(ValueSet valueSet) {
        return ValueSetSupport.isExpandable(valueSet);
    }

    /**
     * Indicates whether the given code system is supported.
     *
     * @param codeSystem
     *     the code system
     * @return
     *     true if the given code system is supported, false otherwise
     */
    public boolean isSupported(CodeSystem codeSystem) {
        if (codeSystem != null) {
            for (FHIRTermServiceProvider provider : providers) {
                if (provider.isSupported(codeSystem)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Lookup the code system concept for the given coding
     *
     * @param coding
     *     the coding to lookup
     * @return
     *     the outcome of the lookup
     */
    public LookupOutcome lookup(Coding coding) {
        return lookup(coding, LookupParameters.EMPTY);
    }

    /**
     * Lookup the code system concept for the given coding and lookup parameters
     *
     * @param coding
     *     the coding to lookup
     * @param parameters
     *     the lookup parameters
     * @return
     *     the outcome of the lookup
     */
    public LookupOutcome lookup(Coding coding, LookupParameters parameters) {
        if (!LookupParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Lookup parameters are not suppored");
        }
        Uri system = coding.getSystem();
        Code code = coding.getCode();
        if (system != null && code != null) {
            java.lang.String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
            java.lang.String url = (version != null) ? system.getValue() + "|" + version : system.getValue();
            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(url);
            if (codeSystem != null) {
                Concept concept = findProvider(codeSystem).getConcept(codeSystem, code);
                if (concept != null) {
                    return LookupOutcome.builder()
                            .name((codeSystem.getName() != null) ? codeSystem.getName() : STRING_DATA_ABSENT_REASON_UNKNOWN)
                            .version(codeSystem.getVersion())
                            .display((concept.getDisplay() != null) ? concept.getDisplay() : STRING_DATA_ABSENT_REASON_UNKNOWN)
                            .property(concept.getProperty().stream()
                                .map(property -> Property.builder()
                                    .code(property.getCode())
                                    .value(property.getValue())
                                    .build())
                                .collect(Collectors.toList()))
                            .designation(concept.getDesignation().stream()
                                .map(designation -> Designation.builder()
                                    .language(designation.getLanguage())
                                    .use(designation.getUse())
                                    .value(designation.getValue())
                                    .build())
                                .collect(Collectors.toList()))
                            .build();
                }
            }
        }
        return null;
    }

    /**
     * Lookup the code system concept for the given system, version, and code
     *
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @return
     *     the outcome of the lookup
     */
    public LookupOutcome lookup(Uri system, String version, Code code) {
        return lookup(system, version, code, LookupParameters.EMPTY);
    }

    /**
     * Lookup the code system concept for the given system, version, code and lookup parameters
     *
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param parameters
     *     the lookup parameters
     * @return
     *     the outcome of the lookup
     */
    public LookupOutcome lookup(Uri system, String version, Code code, LookupParameters parameters) {
        if (!LookupParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Lookup parameters are not suppored");
        }
        Coding coding = Coding.builder()
                .system(system)
                .version(version)
                .code(code)
                .build();
        return lookup(coding, parameters);
    }

    /**
     * Find the concept in tree rooted by the provided concept that matches the specified code.
     *
     * @param codeSystem
     *     the code system
     * @param codeA
     *     the code "A"
     * @param codeB
     *     the code "B"
     * @return
     *     true if the concept represented by code "A" subsumes the concept represented by code "B", false otherwise
     */
    public boolean subsumes(CodeSystem codeSystem, Code codeA, Code codeB) {
        return findProvider(codeSystem).subsumes(codeSystem, codeA, codeB);
    }

    /**
     * Perform a subsumption test to determine if the code system concept represented by the given coding "A" subsumes
     * the code system concept represented by the given coding "B"
     *
     * @param codingA
     *     the coding "A"
     * @param codingB
     *     the coding "B"
     * @return
     *     the outcome of the subsumption test, or null if the relationship could not be tested
     */
    public ConceptSubsumptionOutcome subsumes(Coding codingA, Coding codingB) {
        Uri systemA = codingA.getSystem();
        java.lang.String versionA = (codingA.getVersion() != null) ? codingA.getVersion().getValue() : null;
        Code codeA = codingA.getCode();

        Uri systemB = codingB.getSystem();
        java.lang.String versionB = (codingB.getVersion() != null) ? codingB.getVersion().getValue() : null;
        Code codeB = codingB.getCode();

        if (systemA != null && systemB != null && codeA != null && codeB != null && systemA.equals(systemB)) {
            java.lang.String url = systemA.getValue();

            if (versionA != null || versionB != null) {
                if (versionA != null && versionB != null && !versionA.equals(versionB)) {
                    return null;
                }
                url = (versionA != null) ? (url + "|" + versionA) : (url + "|" + versionB);
            }

            CodeSystem codeSystem = CodeSystemSupport.getCodeSystem(url);
            if (codeSystem != null && CodeSystemHierarchyMeaning.IS_A.equals(codeSystem.getHierarchyMeaning())) {
                FHIRTermServiceProvider provider = findProvider(codeSystem);
                if (provider.hasConcept(codeSystem, codeA) && provider.hasConcept(codeSystem, codeB)) {
                    if (provider.subsumes(codeSystem, codeA, codeB)) {
                        return (codeA.equals(codeB) || (!CodeSystemSupport.isCaseSensitive(codeSystem) && normalize(codeA.getValue()).equals(normalize(codeB.getValue())))) ?
                                ConceptSubsumptionOutcome.EQUIVALENT : ConceptSubsumptionOutcome.SUBSUMES;
                    }
                    return provider.subsumes(codeSystem, codeB, codeA) ? ConceptSubsumptionOutcome.SUBSUMED_BY : ConceptSubsumptionOutcome.NOT_SUBSUMED;
                }
            }
        }

        return null;
    }

    /**
     * Translate the given coding using the provided concept map
     *
     * @param conceptMap
     *     the concept map
     * @param codeable concept
     *     the codeable concept
     * @return
     *     the outcome of translation
     */
    public TranslationOutcome translate(ConceptMap conceptMap, CodeableConcept codeableConcept) {
        return translate(conceptMap, codeableConcept, TranslationParameters.EMPTY);
    }

    /**
     * Translate the given codeable concept using the provided concept map and translation parameters
     *
     * @param conceptMap
     *     the concept map
     * @param codeableConcept
     *     the codeable concept
     * @param parameters
     *     the translation parameters
     * @return
     *     the outcome of translation
     */
    public TranslationOutcome translate(ConceptMap conceptMap, CodeableConcept codeableConcept, TranslationParameters parameters) {
        if (!TranslationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Translation parameters are not supported");
        }
        for (Coding coding : codeableConcept.getCoding()) {
            TranslationOutcome outcome = translate(conceptMap, coding, parameters);
            if (Boolean.TRUE.equals(outcome.getResult())) {
                return outcome;
            }
        }
        return TranslationOutcome.builder()
                .result(Boolean.FALSE)
                .message(string("No matches found"))
                .build();
    }

    /**
     * Translate the given coding using the provided concept map
     *
     * @param conceptMap
     *     the concept map
     * @param coding
     *     the coding
     * @return
     *     the outcome of translation
     */
    public TranslationOutcome translate(ConceptMap conceptMap, Coding coding) {
        return translate(conceptMap, coding, TranslationParameters.EMPTY);
    }

    /**
     * Translate the given coding using the provided concept map and translation parameters
     *
     * @param conceptMap
     *     the concept map
     * @param coding
     *     the coding
     * @param parameters
     *     the translation parameters
     * @return
     *     the outcome of translation
     */
    public TranslationOutcome translate(ConceptMap conceptMap, Coding coding, TranslationParameters parameters) {
        if (!TranslationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Translation parameters are not supported");
        }
        Uri source = getSource(conceptMap);
        List<Match> match = new ArrayList<>();
        for (Group group : conceptMap.getGroup()) {
            if (group.getSource() == null || !group.getSource().equals(coding.getSystem())) {
                continue;
            }
            if (group.getSourceVersion() != null && coding.getVersion() != null && !group.getSourceVersion().equals(coding.getVersion())) {
                continue;
            }
            for (Element element : group.getElement()) {
                if (element.getCode() == null || !element.getCode().equals(coding.getCode())) {
                    // TODO: handle unmatched codes here
                    continue;
                }
                for (Target target : element.getTarget()) {
                    match.add(Match.builder()
                        .equivalence(target.getEquivalence())
                        .concept(Coding.builder()
                            .system(group.getTarget())
                            .version(group.getTargetVersion())
                            .code(target.getCode())
                            .display(target.getDisplay())
                            .build())
                        .source(source)
                        .build());
                }
            }
        }
        return TranslationOutcome.builder()
                .result(match.isEmpty() ? Boolean.FALSE : Boolean.TRUE)
                .message(match.isEmpty() ? string("No matches found") : null)
                .match(match)
                .build();
    }

    /**
     * Translate the given system, version and code using the provided concept map
     *
     * @param conceptMap
     *     the concept map
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @return
     *     the outcome of translation
     */
    public TranslationOutcome translate(ConceptMap conceptMap, Uri system, String version, Code code) {
        return translate(conceptMap, system, version, code, TranslationParameters.EMPTY);
    }

    /**
     * Translate the given system, version and code using the provided concept map and translation parameters
     *
     * @param conceptMap
     *     the concept map
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param parameters
     *     the translation parameters
     * @return
     *     the outcome of translation
     */
    public TranslationOutcome translate(ConceptMap conceptMap, Uri system, String version, Code code, TranslationParameters parameters) {
        if (!TranslationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Translation parameters are not supported");
        }
        Coding coding = Coding.builder()
                .system(system)
                .version(version)
                .code(code)
                .build();
        return translate(conceptMap, coding, parameters);
    }

    /**
     * Validate a codeable concept using the provided code system
     *
     * @param codeableConcept
     *     the codeable concept
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(CodeSystem codeSystem, CodeableConcept codeableConcept) {
        return validateCode(codeSystem, codeableConcept, ValidationParameters.EMPTY);
    }

    /**
     * Validate a codeable concept using the provided code system and validation parameters
     *
     * @param codeSystem
     *     the code system
     * @param codeableConcept
     *     the codeable concept
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(CodeSystem codeSystem, CodeableConcept codeableConcept, ValidationParameters parameters) {
        if (!ValidationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Validation parameters are not supported");
        }
        for (Coding coding : codeableConcept.getCoding()) {
            ValidationOutcome outcome = validateCode(codeSystem, coding, parameters);
            if (Boolean.TRUE.equals(outcome.getResult())) {
                return outcome;
            }
        }
        return validateCode(null, false, null);
    }

    /**
     * Validate a coding using the provided code system
     *
     * @param codeSystem
     *     the codeSystem
     * @param coding
     *     the coding
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(CodeSystem codeSystem, Coding coding) {
        return validateCode(codeSystem, coding, ValidationParameters.EMPTY);
    }

    /**
     * Validate a coding using the provided code system and validation parameters
     *
     * @param codeSystem
     *     the code system
     * @param coding
     *     the coding
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(CodeSystem codeSystem, Coding coding, ValidationParameters parameters) {
        if (!ValidationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Validation parameters are not supported");
        }
        LookupOutcome outcome = lookup(coding, LookupParameters.EMPTY);
        return validateCode(codeSystem, coding, (outcome != null), outcome);
    }

    /**
     * Validate a code and display using the provided code system
     *
     * @param code system
     *     the code system
     * @param code
     *     the code
     * @param display
     *     the display
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(CodeSystem codeSystem, Code code, String display) {
        return validateCode(codeSystem, code, display, ValidationParameters.EMPTY);
    }

    /**
     * Validate a code and display using the provided code system and validation parameters
     *
     * @param codeSystem
     *     the code system
     * @param code
     *     the code
     * @param display
     *     the display
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(CodeSystem codeSystem, Code code, String display, ValidationParameters parameters) {
        if (!ValidationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Validation parameters are not supported");
        }
        Coding coding = Coding.builder()
                .system(codeSystem.getUrl())
                .version(codeSystem.getVersion())
                .code(code)
                .display(display)
                .build();
        return validateCode(codeSystem, coding, parameters);
    }

    /**
     * Validate a code using the provided value set
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param code
     *     the code
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, Code code) {
        return validateCode(valueSet, code, ValidationParameters.EMPTY);
    }

    /**
     * Validate a code using the provided value set and validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param code
     *     the code
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, Code code, ValidationParameters parameters) {
        if (!ValidationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Validation parameters are not supported");
        }
        boolean result = ValueSetSupport.validateCode(valueSet, code);
        return validateCode(null, Coding.builder().code(code).build(), result, null);
    }

    /**
     * Validate a codeable concept using the provided value set
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param codeable concept
     *     the codeable concept
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, CodeableConcept codeableConcept) {
        return validateCode(valueSet, codeableConcept, ValidationParameters.EMPTY);
    }

    /**
     * Validate a codeable concept using the provided value set using the provided validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param codeable concept
     *     the codeable concept
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, CodeableConcept codeableConcept, ValidationParameters parameters) {
        if (!ValidationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Validation parameters are not supported");
        }
        for (Coding coding : codeableConcept.getCoding()) {
            boolean result = ValueSetSupport.validateCode(valueSet, coding);
            if (result) {
                LookupOutcome outcome = lookup(coding);
                return validateCode(coding, result, outcome);
            }
        }
        return validateCode(null, false, null);
    }

    /**
     * Validate a coding using the provided value set using the provided validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param coding
     *     the coding
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, Coding coding) {
        return validateCode(valueSet, coding, ValidationParameters.EMPTY);
    }

    /**
     * Validate a coding using the provided value set using the provided validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param coding
     *     the coding
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, Coding coding, ValidationParameters parameters) {
        if (!ValidationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Validation parameters are not supported");
        }
        boolean result = ValueSetSupport.validateCode(valueSet, coding);
        LookupOutcome outcome = result ? lookup(coding) : null;
        return validateCode(coding, result, outcome);
    }

    /**
     * Validate a code and display using the provided value set
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param display
     *     the display
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, Uri system, String version, Code code, String display) {
        return validateCode(valueSet, system, version, code, display, ValidationParameters.EMPTY);
    }

    /**
     * Validate a code and display using the provided value set and validation parameters
     *
     * @apiNote
     *     the implementation will expand the provided value set if needed
     * @param valueSet
     *     the value set
     * @param system
     *     the system
     * @param version
     *     the version
     * @param code
     *     the code
     * @param display
     *     the display
     * @param parameters
     *     the validation parameters
     * @return
     *     the outcome of validation
     */
    public ValidationOutcome validateCode(ValueSet valueSet, Uri system, String version, Code code, String display, ValidationParameters parameters) {
        if (!ValidationParameters.EMPTY.equals(parameters)) {
            throw new UnsupportedOperationException("Validation parameters are not supported");
        }
        Coding coding = Coding.builder()
                .system(system)
                .version(version)
                .code(code)
                .build();
        return validateCode(valueSet, coding, parameters);
    }

    private FHIRTermServiceProvider findProvider(CodeSystem codeSystem) {
        for (FHIRTermServiceProvider provider : providers) {
            if (provider.isSupported(codeSystem)) {
                return provider;
            }
        }
        return NULL_TERM_SERVICE_PROVIDER;
    }

    private Uri getSource(ConceptMap conceptMap) {
        StringBuilder sb = new StringBuilder(conceptMap.getUrl().getValue());
        if (conceptMap.getVersion() != null) {
            sb.append("|").append(conceptMap.getVersion().getValue());
        }
        return Uri.of(sb.toString());
    }

    private List<FHIRTermServiceProvider> loadProviders() {
        List<FHIRTermServiceProvider> providers = new ArrayList<>();
        providers.add(new RegistryTermServiceProvider());
        Iterator<FHIRTermServiceProvider> iterator = ServiceLoader.load(FHIRTermServiceProvider.class).iterator();
        while (iterator.hasNext()) {
            providers.add(iterator.next());
        }
        return providers;
    }

    private ValidationOutcome validateCode(CodeSystem codeSystem, Coding coding, boolean result, LookupOutcome outcome) {
        java.lang.String message = null;
        if (!result && coding != null && coding.getCode() != null) {
            message = java.lang.String.format("Code '%s' is invalid", coding.getCode().getValue());
        }
        if (result && outcome != null && coding != null && outcome.getDisplay() != null && coding.getDisplay() != null) {
            java.lang.String system = null;
            if (coding.getSystem() != null) {
                system = coding.getSystem().getValue();
            } else if (codeSystem != null && codeSystem.getUrl() != null) {
                system = codeSystem.getUrl().getValue();
            }
            boolean caseSensitive = (codeSystem != null) ? CodeSystemSupport.isCaseSensitive(codeSystem) : false;
            if (codeSystem == null && system != null) {
                java.lang.String version = (coding.getVersion() != null) ? coding.getVersion().getValue() : null;
                java.lang.String url = (version != null) ? system + "|" + version : system;
                caseSensitive = CodeSystemSupport.isCaseSensitive(url);
            }
            result = caseSensitive ? outcome.getDisplay().equals(coding.getDisplay()) : normalize(outcome.getDisplay().getValue()).equals(normalize(coding.getDisplay().getValue()));
            message = !result ? java.lang.String.format("The display '%s' is incorrect for code '%s' from code system '%s'", coding.getDisplay().getValue(), coding.getCode().getValue(), system) : null;
        }
        return ValidationOutcome.builder()
                .result(result ? Boolean.TRUE : Boolean.FALSE)
                .message((message != null) ? string(message) : null)
                .display((outcome != null) ? outcome.getDisplay() : null)
                .build();
    }

    private ValidationOutcome validateCode(Coding coding, boolean result, LookupOutcome outcome) {
        return validateCode(null, coding, result, outcome);
    }

    public static FHIRTermService getInstance() {
        return INSTANCE;
    }
}
