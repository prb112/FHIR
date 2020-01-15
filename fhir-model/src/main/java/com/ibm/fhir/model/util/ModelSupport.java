/*
 * (C) Copyright IBM Corp. 2019, 2020
 * 
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.model.type.Address;
import com.ibm.fhir.model.type.Age;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Base64Binary;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.ContactDetail;
import com.ibm.fhir.model.type.ContactPoint;
import com.ibm.fhir.model.type.Contributor;
import com.ibm.fhir.model.type.Count;
import com.ibm.fhir.model.type.DataRequirement;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Distance;
import com.ibm.fhir.model.type.Dosage;
import com.ibm.fhir.model.type.Duration;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.ElementDefinition;
import com.ibm.fhir.model.type.Expression;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.HumanName;
import com.ibm.fhir.model.type.Id;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Markdown;
import com.ibm.fhir.model.type.MarketingStatus;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.MoneyQuantity;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Oid;
import com.ibm.fhir.model.type.ParameterDefinition;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Population;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.ProdCharacteristic;
import com.ibm.fhir.model.type.ProductShelfLife;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.RelatedArtifact;
import com.ibm.fhir.model.type.SampledData;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.SubstanceAmount;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.TriggerDefinition;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.Url;
import com.ibm.fhir.model.type.UsageContext;
import com.ibm.fhir.model.type.Uuid;
import com.ibm.fhir.model.type.Xhtml;

public final class ModelSupport {
    public static boolean DEBUG = false;
    
    private static final Map<Class<?>, Class<?>> CONCRETE_TYPE_MAP = buildConcreteTypeMap();
    private static final Map<Class<?>, Map<String, ElementInfo>> MODEL_CLASS_ELEMENT_INFO_MAP = buildModelClassElementInfoMap();
    private static final Map<String, Class<? extends Resource>> RESOURCE_TYPE_MAP = buildResourceTypeMap();
    private static final Map<Class<?>, Set<Constraint>> MODEL_CLASS_CONSTRAINT_MAP = buildModelClassConstraintMap();
    // LinkedHashSet is used just to preserve the order, for convenience only
    private static final Set<Class<? extends Element>> CHOICE_ELEMENT_TYPES = new LinkedHashSet<>(Arrays.asList(
        Base64Binary.class, 
        com.ibm.fhir.model.type.Boolean.class, 
        Canonical.class, 
        Code.class, 
        Date.class, 
        DateTime.class, 
        Decimal.class, 
        Id.class, 
        Instant.class, 
        com.ibm.fhir.model.type.Integer.class, 
        Markdown.class, 
        Oid.class, 
        PositiveInt.class, 
        com.ibm.fhir.model.type.String.class, 
        Time.class, 
        UnsignedInt.class, 
        Uri.class, 
        Url.class, 
        Uuid.class, 
        Address.class, 
        Age.class, 
        Annotation.class, 
        Attachment.class, 
        CodeableConcept.class, 
        Coding.class, 
        ContactPoint.class, 
        Count.class, 
        Distance.class, 
        Duration.class, 
        HumanName.class, 
        Identifier.class, 
        Money.class, 
        MoneyQuantity.class, // profiled type
        Period.class, 
        Quantity.class, 
        Range.class, 
        Ratio.class, 
        Reference.class, 
        SampledData.class, 
        SimpleQuantity.class, // profiled type
        Signature.class, 
        Timing.class, 
        ContactDetail.class, 
        Contributor.class, 
        DataRequirement.class, 
        Expression.class, 
        ParameterDefinition.class, 
        RelatedArtifact.class, 
        TriggerDefinition.class, 
        UsageContext.class, 
        Dosage.class, 
        Meta.class));
    private static final Set<Class<? extends Element>> DATA_TYPES;
    static {
        // LinkedHashSet is used just to preserve the order, for convenience only
        Set<Class<? extends Element>> dataTypes = new LinkedHashSet<>(CHOICE_ELEMENT_TYPES);
        dataTypes.add(Xhtml.class);
        dataTypes.add(Narrative.class);
        dataTypes.add(Extension.class);
        dataTypes.add(ElementDefinition.class);
        dataTypes.add(MarketingStatus.class);
        dataTypes.add(Population.class);
        dataTypes.add(ProductShelfLife.class);
        dataTypes.add(ProdCharacteristic.class);
        dataTypes.add(SubstanceAmount.class);
        DATA_TYPES = Collections.unmodifiableSet(dataTypes);
    }
    private static final Set<String> KEYWORDS = new HashSet<>(Arrays.asList(
        "$index", 
        "$this", 
        "$total", 
        "and", 
        "as", 
        "contains", 
        "day", 
        "days", 
        "div", 
        "false", 
        "hour", 
        "hours", 
        "implies", 
        "in", 
        "is", 
        "millisecond", 
        "milliseconds", 
        "minute", 
        "minutes", 
        "mod", 
        "month", 
        "months", 
        "or", 
        "seconds", 
        "true", 
        "week", 
        "weeks", 
        "xor", 
        "year", 
        "years", 
        "second"
    ));

    private ModelSupport() { }

    /**
     * Calling this method allows us to load/initialize this class during startup.
     */
    public static void init() { }

    public static final class ElementInfo {
        private final String name;
        private final Class<?> type;
        private final Class<?> declaringType;
        private final boolean required;
        private final boolean repeating;
        private final boolean choice;
        private final Set<Class<?>> choiceTypes;
        private final Binding binding;
        private final boolean summary;
        
        private final Set<String> choiceElementNames;
        
        ElementInfo(String name,
                Class<?> type, 
                Class<?> declaringType, 
                boolean required, 
                boolean repeating, 
                boolean choice, 
                Set<Class<?>> choiceTypes, 
                Binding binding,
                boolean isSummary) {
            this.name = name;
            this.declaringType = declaringType;
            this.type = type;
            this.required = required;
            this.repeating = repeating;
            this.choice = choice;
            this.choiceTypes = choiceTypes;
            this.binding = binding;
            this.summary = isSummary;
            Set<String> choiceElementNames = new LinkedHashSet<>();
            if (this.choice) {
                for (Class<?> choiceType : this.choiceTypes) {
                    choiceElementNames.add(getChoiceElementName(this.name, choiceType));
                }
            }
            this.choiceElementNames = Collections.unmodifiableSet(choiceElementNames);
        }
        
        public String getName() {
            return name;
        }
        
        public Class<?> getType() {
            return type;
        }
        
        public Class<?> getDeclaringType() {
            return declaringType;
        }
        
        public boolean isDeclaredBy(Class<?> type) {
            return declaringType.equals(type);
        }
        
        public boolean isRequired() {
            return required;
        }
        
        public boolean isSummary() {
            return summary;
        }
        
        public boolean isRepeating() {
            return repeating;
        }
        
        public boolean isChoice() {
            return choice;
        }
        
        public Set<Class<?>> getChoiceTypes() {
            return choiceTypes;
        }
        
        public Binding getBinding() {
            return binding;
        }
        
        public boolean hasBinding() {
            return (binding != null);
        }
        
        public Set<String> getChoiceElementNames() {
            return choiceElementNames;
        }
    }

    private static Map<Class<?>, Class<?>> buildConcreteTypeMap() {
        Map<Class<?>, Class<?>> concreteTypeMap = new LinkedHashMap<>();
        concreteTypeMap.put(SimpleQuantity.class, Quantity.class);
        concreteTypeMap.put(MoneyQuantity.class, Quantity.class);
        return Collections.unmodifiableMap(concreteTypeMap);
    }

    private static Map<Class<?>, Set<Constraint>> buildModelClassConstraintMap() {
        Map<Class<?>, Set<Constraint>> modelClassConstraintMap = new LinkedHashMap<>(1024);
        for (Class<?> modelClass : getModelClasses()) {
            Set<Constraint> constraints = new LinkedHashSet<>();
            for (Class<?> clazz : getClosure(modelClass)) {
                for (Constraint constraint : clazz.getDeclaredAnnotationsByType(Constraint.class)) {
                    constraints.add(constraint);
                }
            }
            modelClassConstraintMap.put(modelClass, Collections.unmodifiableSet(constraints));
        }
        return Collections.unmodifiableMap(modelClassConstraintMap);
    }

    private static Map<Class<?>, Map<String, ElementInfo>> buildModelClassElementInfoMap() {
        try (InputStream in = ModelSupport.class.getClassLoader().getResourceAsStream("modelClasses")) {
            Map<Class<?>, Map<String, ElementInfo>> modelClassElementInfoMap = new LinkedHashMap<>(1024);
            List<String> lines = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
            for (String className : lines) {
                Class<?> modelClass = Class.forName(className);
                Map<String, ElementInfo> elementInfoMap = new LinkedHashMap<>();
                for (Field field : getAllFields(modelClass)) {
                    String elementName = getElementName(field);
                    Class<?> type = getFieldType(field);
                    Class<?> declaringType = field.getDeclaringClass();
                    boolean required = isRequired(field);
                    boolean summary = isSummary(field);
                    boolean repeating = isRepeating(field);
                    boolean choice = isChoice(field);
                    Binding binding = field.getAnnotation(Binding.class);
                    Set<Class<?>> choiceTypes = choice ? Collections.unmodifiableSet(getChoiceTypes(field)) : Collections.emptySet();
                    elementInfoMap.put(elementName, new ElementInfo(
                            elementName, 
                            type, 
                            declaringType, 
                            required, 
                            repeating, 
                            choice, 
                            choiceTypes, 
                            binding,
                            summary
                        )
                    );
                }
                modelClassElementInfoMap.put(modelClass, Collections.unmodifiableMap(elementInfoMap));
            }
            return Collections.unmodifiableMap(modelClassElementInfoMap);
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Class<? extends Resource>> buildResourceTypeMap() {
        Map<String, Class<? extends Resource>> resourceTypeMap = new LinkedHashMap<>(256);
        for (Class<?> modelClass : getModelClasses()) {
            if (isResourceType(modelClass)) {
                resourceTypeMap.put(modelClass.getSimpleName(), (Class<? extends Resource>) modelClass);
            }
        }
        return Collections.unmodifiableMap(resourceTypeMap);
    }
    
    private static List<Field> getAllFields(Class<?> modelClass) {
        List<Field> fields = new ArrayList<>();
        for (Class<?> clazz : getClosure(modelClass)) {
            for (Field field : clazz.getDeclaredFields()) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) || Modifier.isVolatile(modifiers)) {
                    continue;
                }
                fields.add(field);
            }
        }
        return fields;
    }

    public static String getChoiceElementName(String name, Class<?> type) {
        return name + getConcreteType(type).getSimpleName();
    }
    
    public static Set<Class<?>> getChoiceElementTypes(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getChoiceTypes();
        }
        return Collections.emptySet();
    }
    
    private static Set<Class<?>> getChoiceTypes(Field field) {
        return new LinkedHashSet<>(Arrays.asList(field.getAnnotation(Choice.class).value()));
    }

    public static List<Class<?>> getClosure(Class<?> modelClass) {
        List<Class<?>> closure = new ArrayList<>();
        while (!Object.class.equals(modelClass)) {
            closure.add(modelClass);
            modelClass = modelClass.getSuperclass();
        }
        Collections.reverse(closure);
        return closure;
    }
    
    public static Class<?> getConcreteType(Class<?> type) {
        if (isProfiledType(type)) {
            return CONCRETE_TYPE_MAP.get(type);
        }
        return type;
    }

    public static Set<Constraint> getConstraints(Class<?> modelClass) {
        return MODEL_CLASS_CONSTRAINT_MAP.getOrDefault(modelClass, Collections.emptySet());
    }

    public static ElementInfo getElementInfo(Class<?> modelClass, String elementName) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).get(elementName);
    }
    
    public static Collection<ElementInfo> getElementInfo(Class<?> modelClass) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).values();
    }
    
    public static ElementInfo getChoiceElementInfo(Class<?> modelClass, String typeSpecificElementName) {
        for (ElementInfo elementInfo : getElementInfo(modelClass)) {
            if (elementInfo.isChoice() && elementInfo.getChoiceElementNames().contains(typeSpecificElementName)) {
                return elementInfo;
            }
        }
        return null;
    }
    
    /**
     * Get the actual element name from a Java field.
     */
    public static String getElementName(Field field) {
        return getElementName(field.getName());
    }
    
    /**
     * Get the actual element name from a Java field name.
     * This method reverses any encoding that was required to represent the FHIR element name in Java,
     * such as converting class to clazz.
     */
    public static String getElementName(String fieldName) {
        if ("clazz".equals(fieldName)) {
            return "class";
        }
        if (fieldName.startsWith("_")) {
            return fieldName.substring(1);
        }
        return fieldName;
    }
    
    public static Set<String> getElementNames(Class<?> modelClass) {
        return MODEL_CLASS_ELEMENT_INFO_MAP.getOrDefault(modelClass, Collections.emptyMap()).keySet();
    }
    
    public static Class<?> getElementType(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getType();
        }
        return null;
    }
    
    public static Class<?> getElementDeclaringType(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.getDeclaringType();
        }
        return null;
    }
    
    public static boolean isElementDeclaredBy(Class<?> modelClass, String elementName, Class<?> type) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isDeclaredBy(type);
        }
        return false;
    }
    
    private static Class<?> getFieldType(Field field) {
        Type genericType = field.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            return (Class<?>) parameterizedType.getActualTypeArguments()[0];
        }
        return field.getType();
    }
    
    public static Set<Class<?>> getModelClasses() {
        return MODEL_CLASS_ELEMENT_INFO_MAP.keySet();
    }
    
    public static Class<? extends Resource> getResourceType(String name) {
        return RESOURCE_TYPE_MAP.get(name);
    }
    
    public static Collection<Class<? extends Resource>> getResourceTypes() {
        return RESOURCE_TYPE_MAP.values();
    }
    
    /**
     * @return the set of classes for the FHIR data types
     */
    public static Set<Class<? extends Element>> getDataTypes() {
        return DATA_TYPES;
    }
    
    /**
     * @return the name of the FHIR data type which corresponds to the passed type
     */
    public static String getTypeName(Class<?> type) {
        String typeName = type.getSimpleName();
        if (Code.class.isAssignableFrom(type)) {
            typeName = "code";
        } else if (isPrimitiveType(type)) {
            typeName = typeName.substring(0, 1).toLowerCase() + typeName.substring(1);
        }
        return typeName;
    }

    /**
     * @return the set of FHIR data type names for the passed modelClass and its supertypes
     */
    public static Set<String> getTypeNames(Class<?> modelClass) {
        Set<String> typeNames = new HashSet<>();
        while (!Object.class.equals(modelClass)) {
            typeNames.add(getTypeName(modelClass));
            modelClass = modelClass.getSuperclass();
        }
        return typeNames;
    }

    public static boolean isBackboneElementType(Class<?> modelClass) {
        return BackboneElement.class.isAssignableFrom(modelClass);
    }
    
    private static boolean isChoice(Field field) {
        return field.isAnnotationPresent(Choice.class);
    }
    
    public static boolean isChoiceElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isChoice();
        }
        return false;
    }
    
    public static boolean isChoiceElementType(Class<?> type) {
        return CHOICE_ELEMENT_TYPES.contains(type);
    }
    
    public static boolean isCodeSubtype(Class<?> type) {
        return Code.class.isAssignableFrom(type) && !Code.class.equals(type);
    }

    public static boolean isElement(Object modelObject) {
        return (modelObject instanceof Element);
    }
    
    public static boolean isElementType(Class<?> modelClass) {
        return Element.class.isAssignableFrom(modelClass);
    }
    
    public static boolean isMetadataType(Class<?> type) {
        return ContactDetail.class.equals(type) ||
                Contributor.class.equals(type) ||
                DataRequirement.class.isAssignableFrom(type) || 
                RelatedArtifact.class.isAssignableFrom(type) ||
                UsageContext.class.equals(type) || 
                ParameterDefinition.class.equals(type) ||
                Expression.class.equals(type) || 
                TriggerDefinition.class.equals(type);
    }
    
    public static boolean isModelClass(Class<?> type) {
        return isResourceType(type) || isElementType(type);
    }

    /**
     * @implNote xhtml is considered a primitive type
     */
    public static boolean isPrimitiveType(Class<?> type) {
        return Base64Binary.class.equals(type) ||
            com.ibm.fhir.model.type.Boolean.class.equals(type) ||
            com.ibm.fhir.model.type.String.class.isAssignableFrom(type) || 
            Uri.class.isAssignableFrom(type) ||
            DateTime.class.equals(type) || 
            Date.class.equals(type) ||
            Time.class.equals(type) || 
            Instant.class.equals(type) || 
            com.ibm.fhir.model.type.Integer.class.isAssignableFrom(type) || 
            Decimal.class.equals(type) || 
            Xhtml.class.equals(type);
    }
    
    public static boolean isProfiledType(Class<?> type) {
        return CONCRETE_TYPE_MAP.containsKey(type);
    }
    
    private static boolean isRepeating(Field field) {
        return List.class.equals(field.getType());
    }
    
    public static boolean isRepeatingElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isRepeating();
        }
        return false;
    }
    
    private static boolean isRequired(Field field) {
        return field.isAnnotationPresent(Required.class);
    }
    
    private static boolean isSummary(Field field) {
        return field.isAnnotationPresent(Summary.class);
    }
    
    public static boolean isRequiredElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isRequired();
        }
        return false;
    }
    
    public static boolean isResource(Object modelObject) {
        return (modelObject instanceof Resource);
    }
    
    public static boolean isResourceType(Class<?> modelClass) {
        return Resource.class.isAssignableFrom(modelClass);
    }
    
    public static boolean isResourceType(String name) {
        return RESOURCE_TYPE_MAP.containsKey(name);
    }
    
    public static boolean isSummaryElement(Class<?> modelClass, String elementName) {
        ElementInfo elementInfo = getElementInfo(modelClass, elementName);
        if (elementInfo != null) {
            return elementInfo.isSummary();
        }
        return false;
    }
    
    public static ZonedDateTime truncateTime(ZonedDateTime dateTime, ChronoUnit unit) {
        return dateTime == null ? null : dateTime.truncatedTo(unit);
    }
    
    public static TemporalAccessor truncateTime(TemporalAccessor ta, ChronoUnit unit) {
        if (ta instanceof java.time.Instant) {
            ta = ((java.time.Instant) ta).truncatedTo(unit);
        } else if (ta instanceof ZonedDateTime) {
            ta = ((ZonedDateTime) ta).truncatedTo(unit);
        } else if (ta instanceof LocalDateTime) {
            ta = ((LocalDateTime) ta).truncatedTo(unit);
        } else if (ta instanceof LocalTime) {
            ta = ((LocalTime) ta).truncatedTo(unit);
        } else if (ta instanceof OffsetTime) {
            ta = ((OffsetTime) ta).truncatedTo(unit);
        } else if (ta instanceof OffsetDateTime) {
            ta = ((OffsetDateTime) ta).truncatedTo(unit);
        }
        
        return ta;
    }
    
    public static boolean isKeyword(String identifier) {
        return KEYWORDS.contains(identifier);
    }
    
    public static String delimit(String identifier) {
        return String.format("`%s`", identifier);
    }
}