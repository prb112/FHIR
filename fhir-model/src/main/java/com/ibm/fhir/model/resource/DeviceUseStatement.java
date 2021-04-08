/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Annotation;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.Timing;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.DeviceUseStatementStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A record of a device being used by a patient where the record is the result of a report from the patient or another 
 * clinician.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.ValueSet.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class DeviceUseStatement extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Summary
    @ReferenceTarget({ "ServiceRequest" })
    private final List<Reference> basedOn;
    @Summary
    @Binding(
        bindingName = "DeviceUseStatementStatus",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "A coded concept indicating the current status of the Device Usage.",
        valueSet = "http://hl7.org/fhir/ValueSet/device-statement-status|4.0.1"
    )
    @Required
    private final DeviceUseStatementStatus status;
    @Summary
    @ReferenceTarget({ "Patient", "Group" })
    @Required
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "ServiceRequest", "Procedure", "Claim", "Observation", "QuestionnaireResponse", "DocumentReference" })
    private final List<Reference> derivedFrom;
    @Summary
    @Choice({ Timing.class, Period.class, DateTime.class })
    private final Element timing;
    @Summary
    private final DateTime recordedOn;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
    private final Reference source;
    @Summary
    @ReferenceTarget({ "Device" })
    @Required
    private final Reference device;
    @Summary
    private final List<CodeableConcept> reasonCode;
    @Summary
    @ReferenceTarget({ "Condition", "Observation", "DiagnosticReport", "DocumentReference", "Media" })
    private final List<Reference> reasonReference;
    @Summary
    @Binding(
        bindingName = "BodySite",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Codes describing anatomical locations. May include laterality.",
        valueSet = "http://hl7.org/fhir/ValueSet/body-site"
    )
    private final CodeableConcept bodySite;
    private final List<Annotation> note;

    private volatile int hashCode;

    private DeviceUseStatement(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.checkList(builder.identifier, "identifier", Identifier.class));
        basedOn = Collections.unmodifiableList(ValidationSupport.checkList(builder.basedOn, "basedOn", Reference.class));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        subject = ValidationSupport.requireNonNull(builder.subject, "subject");
        derivedFrom = Collections.unmodifiableList(ValidationSupport.checkList(builder.derivedFrom, "derivedFrom", Reference.class));
        timing = ValidationSupport.choiceElement(builder.timing, "timing", Timing.class, Period.class, DateTime.class);
        recordedOn = builder.recordedOn;
        source = builder.source;
        device = ValidationSupport.requireNonNull(builder.device, "device");
        reasonCode = Collections.unmodifiableList(ValidationSupport.checkList(builder.reasonCode, "reasonCode", CodeableConcept.class));
        reasonReference = Collections.unmodifiableList(ValidationSupport.checkList(builder.reasonReference, "reasonReference", Reference.class));
        bodySite = builder.bodySite;
        note = Collections.unmodifiableList(ValidationSupport.checkList(builder.note, "note", Annotation.class));
        ValidationSupport.checkReferenceType(basedOn, "basedOn", "ServiceRequest");
        ValidationSupport.checkReferenceType(subject, "subject", "Patient", "Group");
        ValidationSupport.checkReferenceType(derivedFrom, "derivedFrom", "ServiceRequest", "Procedure", "Claim", "Observation", "QuestionnaireResponse", "DocumentReference");
        ValidationSupport.checkReferenceType(source, "source", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
        ValidationSupport.checkReferenceType(device, "device", "Device");
        ValidationSupport.checkReferenceType(reasonReference, "reasonReference", "Condition", "Observation", "DiagnosticReport", "DocumentReference", "Media");
        ValidationSupport.requireChildren(this);
    }

    /**
     * An external identifier for this statement such as an IRI.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * A code representing the patient or other source's judgment about the state of the device used that this statement is 
     * about. Generally this will be active or completed.
     * 
     * @return
     *     An immutable object of type {@link DeviceUseStatementStatus} that is non-null.
     */
    public DeviceUseStatementStatus getStatus() {
        return status;
    }

    /**
     * The patient who used the device.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
     * derive the DeviceUseStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getDerivedFrom() {
        return derivedFrom;
    }

    /**
     * How often the device was used.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getTiming() {
        return timing;
    }

    /**
     * The time at which the statement was made/recorded.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getRecordedOn() {
        return recordedOn;
    }

    /**
     * Who reported the device was being used by the patient.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * The details of the device used.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getDevice() {
        return device;
    }

    /**
     * Reason or justification for the use of the device.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getReasonCode() {
        return reasonCode;
    }

    /**
     * Indicates another resource whose existence justifies this DeviceUseStatement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getReasonReference() {
        return reasonReference;
    }

    /**
     * Indicates the anotomic location on the subject's body where the device was used ( i.e. the target).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getBodySite() {
        return bodySite;
    }

    /**
     * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
     * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Annotation} that may be empty.
     */
    public List<Annotation> getNote() {
        return note;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            !basedOn.isEmpty() || 
            (status != null) || 
            (subject != null) || 
            !derivedFrom.isEmpty() || 
            (timing != null) || 
            (recordedOn != null) || 
            (source != null) || 
            (device != null) || 
            !reasonCode.isEmpty() || 
            !reasonReference.isEmpty() || 
            (bodySite != null) || 
            !note.isEmpty();
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(meta, "meta", visitor);
                accept(implicitRules, "implicitRules", visitor);
                accept(language, "language", visitor);
                accept(text, "text", visitor);
                accept(contained, "contained", visitor, Resource.class);
                accept(extension, "extension", visitor, Extension.class);
                accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                accept(identifier, "identifier", visitor, Identifier.class);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(status, "status", visitor);
                accept(subject, "subject", visitor);
                accept(derivedFrom, "derivedFrom", visitor, Reference.class);
                accept(timing, "timing", visitor);
                accept(recordedOn, "recordedOn", visitor);
                accept(source, "source", visitor);
                accept(device, "device", visitor);
                accept(reasonCode, "reasonCode", visitor, CodeableConcept.class);
                accept(reasonReference, "reasonReference", visitor, Reference.class);
                accept(bodySite, "bodySite", visitor);
                accept(note, "note", visitor, Annotation.class);
            }
            visitor.visitEnd(elementName, elementIndex, this);
            visitor.postVisit(this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        DeviceUseStatement other = (DeviceUseStatement) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(basedOn, other.basedOn) && 
            Objects.equals(status, other.status) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(derivedFrom, other.derivedFrom) && 
            Objects.equals(timing, other.timing) && 
            Objects.equals(recordedOn, other.recordedOn) && 
            Objects.equals(source, other.source) && 
            Objects.equals(device, other.device) && 
            Objects.equals(reasonCode, other.reasonCode) && 
            Objects.equals(reasonReference, other.reasonReference) && 
            Objects.equals(bodySite, other.bodySite) && 
            Objects.equals(note, other.note);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                text, 
                contained, 
                extension, 
                modifierExtension, 
                identifier, 
                basedOn, 
                status, 
                subject, 
                derivedFrom, 
                timing, 
                recordedOn, 
                source, 
                device, 
                reasonCode, 
                reasonReference, 
                bodySite, 
                note);
            hashCode = result;
        }
        return result;
    }

    @Override
    public Builder toBuilder() {
        return new Builder().from(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends DomainResource.Builder {
        private List<Identifier> identifier = new ArrayList<>();
        private List<Reference> basedOn = new ArrayList<>();
        private DeviceUseStatementStatus status;
        private Reference subject;
        private List<Reference> derivedFrom = new ArrayList<>();
        private Element timing;
        private DateTime recordedOn;
        private Reference source;
        private Reference device;
        private List<CodeableConcept> reasonCode = new ArrayList<>();
        private List<Reference> reasonReference = new ArrayList<>();
        private CodeableConcept bodySite;
        private List<Annotation> note = new ArrayList<>();

        private Builder() {
            super();
        }

        /**
         * The logical id of the resource, as used in the URL for the resource. Once assigned, this value never changes.
         * 
         * @param id
         *     Logical id of this artifact
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        /**
         * The metadata about the resource. This is content that is maintained by the infrastructure. Changes to the content 
         * might not always be associated with version changes to the resource.
         * 
         * @param meta
         *     Metadata about the resource
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder meta(Meta meta) {
            return (Builder) super.meta(meta);
        }

        /**
         * A reference to a set of rules that were followed when the resource was constructed, and which must be understood when 
         * processing the content. Often, this is a reference to an implementation guide that defines the special rules along 
         * with other profiles etc.
         * 
         * @param implicitRules
         *     A set of rules under which this content was created
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder implicitRules(Uri implicitRules) {
            return (Builder) super.implicitRules(implicitRules);
        }

        /**
         * The base language in which the resource is written.
         * 
         * @param language
         *     Language of the resource content
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder language(Code language) {
            return (Builder) super.language(language);
        }

        /**
         * A human-readable narrative that contains a summary of the resource and can be used to represent the content of the 
         * resource to a human. The narrative need not encode all the structured data, but is required to contain sufficient 
         * detail to make it "clinically safe" for a human to just read the narrative. Resource definitions may define what 
         * content should be represented in the narrative to ensure clinical safety.
         * 
         * @param text
         *     Text summary of the resource, for human interpretation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder text(Narrative text) {
            return (Builder) super.text(text);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Resource... contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * These resources do not have an independent existence apart from the resource that contains them - they cannot be 
         * identified independently, and nor can they have their own independent transaction scope.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param contained
         *     Contained, inline Resources
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder contained(Collection<Resource> contained) {
            return (Builder) super.contained(contained);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource. To make the 
         * use of extensions safe and manageable, there is a strict set of governance applied to the definition and use of 
         * extensions. Though any implementer can define an extension, there is a set of requirements that SHALL be met as part 
         * of the definition of the extension.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param extension
         *     Additional content defined by implementations
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Extension... modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * May be used to represent additional information that is not part of the basic definition of the resource and that 
         * modifies the understanding of the element that contains it and/or the understanding of the containing element's 
         * descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe and 
         * manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
         * implementer is allowed to define an extension, there is a set of requirements that SHALL be met as part of the 
         * definition of the extension. Applications processing a resource are required to check for modifier extensions.
         * 
         * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
         * change the meaning of modifierExtension itself).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param modifierExtension
         *     Extensions that cannot be ignored
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder modifierExtension(Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * An external identifier for this statement such as an IRI.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param identifier
         *     External identifier for this record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier... identifier) {
            for (Identifier value : identifier) {
                this.identifier.add(value);
            }
            return this;
        }

        /**
         * An external identifier for this statement such as an IRI.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param identifier
         *     External identifier for this record
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Reference... basedOn) {
            for (Reference value : basedOn) {
                this.basedOn.add(value);
            }
            return this;
        }

        /**
         * A plan, proposal or order that is fulfilled in whole or in part by this DeviceUseStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Fulfills plan, proposal or order
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * A code representing the patient or other source's judgment about the state of the device used that this statement is 
         * about. Generally this will be active or completed.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | completed | entered-in-error +
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(DeviceUseStatementStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The patient who used the device.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Group}</li>
         * </ul>
         * 
         * @param subject
         *     Patient using device
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
         * derive the DeviceUseStatement.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Claim}</li>
         * <li>{@link Observation}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param derivedFrom
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Reference... derivedFrom) {
            for (Reference value : derivedFrom) {
                this.derivedFrom.add(value);
            }
            return this;
        }

        /**
         * Allows linking the DeviceUseStatement to the underlying Request, or to other information that supports or is used to 
         * derive the DeviceUseStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * <li>{@link Procedure}</li>
         * <li>{@link Claim}</li>
         * <li>{@link Observation}</li>
         * <li>{@link QuestionnaireResponse}</li>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param derivedFrom
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder derivedFrom(Collection<Reference> derivedFrom) {
            this.derivedFrom = new ArrayList<>(derivedFrom);
            return this;
        }

        /**
         * How often the device was used.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Timing}</li>
         * <li>{@link Period}</li>
         * <li>{@link DateTime}</li>
         * </ul>
         * 
         * @param timing
         *     How often the device was used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder timing(Element timing) {
            this.timing = timing;
            return this;
        }

        /**
         * The time at which the statement was made/recorded.
         * 
         * @param recordedOn
         *     When statement was recorded
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder recordedOn(DateTime recordedOn) {
            this.recordedOn = recordedOn;
            return this;
        }

        /**
         * Who reported the device was being used by the patient.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link RelatedPerson}</li>
         * </ul>
         * 
         * @param source
         *     Who made the statement
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Reference source) {
            this.source = source;
            return this;
        }

        /**
         * The details of the device used.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * </ul>
         * 
         * @param device
         *     Reference to device used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder device(Reference device) {
            this.device = device;
            return this;
        }

        /**
         * Reason or justification for the use of the device.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param reasonCode
         *     Why device was used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(CodeableConcept... reasonCode) {
            for (CodeableConcept value : reasonCode) {
                this.reasonCode.add(value);
            }
            return this;
        }

        /**
         * Reason or justification for the use of the device.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param reasonCode
         *     Why device was used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonCode(Collection<CodeableConcept> reasonCode) {
            this.reasonCode = new ArrayList<>(reasonCode);
            return this;
        }

        /**
         * Indicates another resource whose existence justifies this DeviceUseStatement.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * <li>{@link Media}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why was DeviceUseStatement performed?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Reference... reasonReference) {
            for (Reference value : reasonReference) {
                this.reasonReference.add(value);
            }
            return this;
        }

        /**
         * Indicates another resource whose existence justifies this DeviceUseStatement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Condition}</li>
         * <li>{@link Observation}</li>
         * <li>{@link DiagnosticReport}</li>
         * <li>{@link DocumentReference}</li>
         * <li>{@link Media}</li>
         * </ul>
         * 
         * @param reasonReference
         *     Why was DeviceUseStatement performed?
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder reasonReference(Collection<Reference> reasonReference) {
            this.reasonReference = new ArrayList<>(reasonReference);
            return this;
        }

        /**
         * Indicates the anotomic location on the subject's body where the device was used ( i.e. the target).
         * 
         * @param bodySite
         *     Target body site
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder bodySite(CodeableConcept bodySite) {
            this.bodySite = bodySite;
            return this;
        }

        /**
         * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
         * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param note
         *     Addition details (comments, instructions)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Annotation... note) {
            for (Annotation value : note) {
                this.note.add(value);
            }
            return this;
        }

        /**
         * Details about the device statement that were not represented at all or sufficiently in one of the attributes provided 
         * in a class. These may include for example a comment, an instruction, or a note associated with the statement.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param note
         *     Addition details (comments, instructions)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder note(Collection<Annotation> note) {
            this.note = new ArrayList<>(note);
            return this;
        }

        /**
         * Build the {@link DeviceUseStatement}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>subject</li>
         * <li>device</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link DeviceUseStatement}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid DeviceUseStatement per the base specification
         */
        @Override
        public DeviceUseStatement build() {
            return new DeviceUseStatement(this);
        }

        protected Builder from(DeviceUseStatement deviceUseStatement) {
            super.from(deviceUseStatement);
            identifier.addAll(deviceUseStatement.identifier);
            basedOn.addAll(deviceUseStatement.basedOn);
            status = deviceUseStatement.status;
            subject = deviceUseStatement.subject;
            derivedFrom.addAll(deviceUseStatement.derivedFrom);
            timing = deviceUseStatement.timing;
            recordedOn = deviceUseStatement.recordedOn;
            source = deviceUseStatement.source;
            device = deviceUseStatement.device;
            reasonCode.addAll(deviceUseStatement.reasonCode);
            reasonReference.addAll(deviceUseStatement.reasonReference);
            bodySite = deviceUseStatement.bodySite;
            note.addAll(deviceUseStatement.note);
            return this;
        }
    }
}
