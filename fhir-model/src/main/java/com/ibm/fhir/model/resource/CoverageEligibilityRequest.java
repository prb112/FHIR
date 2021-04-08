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
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Money;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.PositiveInt;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.SimpleQuantity;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.EligibilityRequestPurpose;
import com.ibm.fhir.model.type.code.EligibilityRequestStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The CoverageEligibilityRequest provides patient and insurance coverage information to an insurer for them to respond, 
 * in the form of an CoverageEligibilityResponse, with information regarding whether the stated coverage is valid and in-
 * force and optionally to provide the insurance details of the policy.
 * 
 * <p>Maturity level: FMM2 (Trial Use)
 */
@Maturity(
    level = 2,
    status = StandardsStatus.ValueSet.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class CoverageEligibilityRequest extends DomainResource {
    private final List<Identifier> identifier;
    @Summary
    @Binding(
        bindingName = "EligibilityRequestStatus",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "A code specifying the state of the resource instance.",
        valueSet = "http://hl7.org/fhir/ValueSet/fm-status|4.0.1"
    )
    @Required
    private final EligibilityRequestStatus status;
    @Binding(
        bindingName = "ProcessPriority",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "The timeliness with which processing is required: STAT, normal, Deferred.",
        valueSet = "http://hl7.org/fhir/ValueSet/process-priority"
    )
    private final CodeableConcept priority;
    @Summary
    @Binding(
        bindingName = "EligibilityRequestPurpose",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "A code specifying the types of information being requested.",
        valueSet = "http://hl7.org/fhir/ValueSet/eligibilityrequest-purpose|4.0.1"
    )
    @Required
    private final List<EligibilityRequestPurpose> purpose;
    @Summary
    @ReferenceTarget({ "Patient" })
    @Required
    private final Reference patient;
    @Choice({ Date.class, Period.class })
    private final Element serviced;
    @Summary
    @Required
    private final DateTime created;
    @ReferenceTarget({ "Practitioner", "PractitionerRole" })
    private final Reference enterer;
    @ReferenceTarget({ "Practitioner", "PractitionerRole", "Organization" })
    private final Reference provider;
    @Summary
    @ReferenceTarget({ "Organization" })
    @Required
    private final Reference insurer;
    @ReferenceTarget({ "Location" })
    private final Reference facility;
    private final List<SupportingInfo> supportingInfo;
    private final List<Insurance> insurance;
    private final List<Item> item;

    private volatile int hashCode;

    private CoverageEligibilityRequest(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.checkList(builder.identifier, "identifier", Identifier.class));
        status = ValidationSupport.requireNonNull(builder.status, "status");
        priority = builder.priority;
        purpose = Collections.unmodifiableList(ValidationSupport.checkNonEmptyList(builder.purpose, "purpose", EligibilityRequestPurpose.class));
        patient = ValidationSupport.requireNonNull(builder.patient, "patient");
        serviced = ValidationSupport.choiceElement(builder.serviced, "serviced", Date.class, Period.class);
        created = ValidationSupport.requireNonNull(builder.created, "created");
        enterer = builder.enterer;
        provider = builder.provider;
        insurer = ValidationSupport.requireNonNull(builder.insurer, "insurer");
        facility = builder.facility;
        supportingInfo = Collections.unmodifiableList(ValidationSupport.checkList(builder.supportingInfo, "supportingInfo", SupportingInfo.class));
        insurance = Collections.unmodifiableList(ValidationSupport.checkList(builder.insurance, "insurance", Insurance.class));
        item = Collections.unmodifiableList(ValidationSupport.checkList(builder.item, "item", Item.class));
        ValidationSupport.checkReferenceType(patient, "patient", "Patient");
        ValidationSupport.checkReferenceType(enterer, "enterer", "Practitioner", "PractitionerRole");
        ValidationSupport.checkReferenceType(provider, "provider", "Practitioner", "PractitionerRole", "Organization");
        ValidationSupport.checkReferenceType(insurer, "insurer", "Organization");
        ValidationSupport.checkReferenceType(facility, "facility", "Location");
        ValidationSupport.requireChildren(this);
    }

    /**
     * A unique identifier assigned to this coverage eligiblity request.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * The status of the resource instance.
     * 
     * @return
     *     An immutable object of type {@link EligibilityRequestStatus} that is non-null.
     */
    public EligibilityRequestStatus getStatus() {
        return status;
    }

    /**
     * When the requestor expects the processor to complete processing.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getPriority() {
        return priority;
    }

    /**
     * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; 
     * benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation 
     * that the specified coverage is in-force at the date/period specified or 'now' if not specified.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link EligibilityRequestPurpose} that is non-empty.
     */
    public List<EligibilityRequestPurpose> getPurpose() {
        return purpose;
    }

    /**
     * The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getPatient() {
        return patient;
    }

    /**
     * The date or dates when the enclosed suite of services were performed or completed.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getServiced() {
        return serviced;
    }

    /**
     * The date when this resource was created.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that is non-null.
     */
    public DateTime getCreated() {
        return created;
    }

    /**
     * Person who created the request.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEnterer() {
        return enterer;
    }

    /**
     * The provider which is responsible for the request.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getProvider() {
        return provider;
    }

    /**
     * The Insurer who issued the coverage in question and is the recipient of the request.
     * 
     * @return
     *     An immutable object of type {@link Reference} that is non-null.
     */
    public Reference getInsurer() {
        return insurer;
    }

    /**
     * Facility where the services are intended to be provided.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getFacility() {
        return facility;
    }

    /**
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link SupportingInfo} that may be empty.
     */
    public List<SupportingInfo> getSupportingInfo() {
        return supportingInfo;
    }

    /**
     * Financial instruments for reimbursement for the health care products and services.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Insurance} that may be empty.
     */
    public List<Insurance> getInsurance() {
        return insurance;
    }

    /**
     * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
     * may be required by the payor.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Item} that may be empty.
     */
    public List<Item> getItem() {
        return item;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (status != null) || 
            (priority != null) || 
            !purpose.isEmpty() || 
            (patient != null) || 
            (serviced != null) || 
            (created != null) || 
            (enterer != null) || 
            (provider != null) || 
            (insurer != null) || 
            (facility != null) || 
            !supportingInfo.isEmpty() || 
            !insurance.isEmpty() || 
            !item.isEmpty();
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
                accept(status, "status", visitor);
                accept(priority, "priority", visitor);
                accept(purpose, "purpose", visitor, EligibilityRequestPurpose.class);
                accept(patient, "patient", visitor);
                accept(serviced, "serviced", visitor);
                accept(created, "created", visitor);
                accept(enterer, "enterer", visitor);
                accept(provider, "provider", visitor);
                accept(insurer, "insurer", visitor);
                accept(facility, "facility", visitor);
                accept(supportingInfo, "supportingInfo", visitor, SupportingInfo.class);
                accept(insurance, "insurance", visitor, Insurance.class);
                accept(item, "item", visitor, Item.class);
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
        CoverageEligibilityRequest other = (CoverageEligibilityRequest) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(status, other.status) && 
            Objects.equals(priority, other.priority) && 
            Objects.equals(purpose, other.purpose) && 
            Objects.equals(patient, other.patient) && 
            Objects.equals(serviced, other.serviced) && 
            Objects.equals(created, other.created) && 
            Objects.equals(enterer, other.enterer) && 
            Objects.equals(provider, other.provider) && 
            Objects.equals(insurer, other.insurer) && 
            Objects.equals(facility, other.facility) && 
            Objects.equals(supportingInfo, other.supportingInfo) && 
            Objects.equals(insurance, other.insurance) && 
            Objects.equals(item, other.item);
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
                status, 
                priority, 
                purpose, 
                patient, 
                serviced, 
                created, 
                enterer, 
                provider, 
                insurer, 
                facility, 
                supportingInfo, 
                insurance, 
                item);
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
        private EligibilityRequestStatus status;
        private CodeableConcept priority;
        private List<EligibilityRequestPurpose> purpose = new ArrayList<>();
        private Reference patient;
        private Element serviced;
        private DateTime created;
        private Reference enterer;
        private Reference provider;
        private Reference insurer;
        private Reference facility;
        private List<SupportingInfo> supportingInfo = new ArrayList<>();
        private List<Insurance> insurance = new ArrayList<>();
        private List<Item> item = new ArrayList<>();

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
         * A unique identifier assigned to this coverage eligiblity request.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param identifier
         *     Business Identifier for coverage eligiblity request
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
         * A unique identifier assigned to this coverage eligiblity request.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param identifier
         *     Business Identifier for coverage eligiblity request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * The status of the resource instance.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     active | cancelled | draft | entered-in-error
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(EligibilityRequestStatus status) {
            this.status = status;
            return this;
        }

        /**
         * When the requestor expects the processor to complete processing.
         * 
         * @param priority
         *     Desired processing priority
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder priority(CodeableConcept priority) {
            this.priority = priority;
            return this;
        }

        /**
         * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; 
         * benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation 
         * that the specified coverage is in-force at the date/period specified or 'now' if not specified.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>This element is required.
         * 
         * @param purpose
         *     auth-requirements | benefits | discovery | validation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(EligibilityRequestPurpose... purpose) {
            for (EligibilityRequestPurpose value : purpose) {
                this.purpose.add(value);
            }
            return this;
        }

        /**
         * Code to specify whether requesting: prior authorization requirements for some service categories or billing codes; 
         * benefits for coverages specified or discovered; discovery and return of coverages for the patient; and/or validation 
         * that the specified coverage is in-force at the date/period specified or 'now' if not specified.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>This element is required.
         * 
         * @param purpose
         *     auth-requirements | benefits | discovery | validation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder purpose(Collection<EligibilityRequestPurpose> purpose) {
            this.purpose = new ArrayList<>(purpose);
            return this;
        }

        /**
         * The party who is the beneficiary of the supplied coverage and for whom eligibility is sought.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Patient}</li>
         * </ul>
         * 
         * @param patient
         *     Intended recipient of products and services
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder patient(Reference patient) {
            this.patient = patient;
            return this;
        }

        /**
         * The date or dates when the enclosed suite of services were performed or completed.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Date}</li>
         * <li>{@link Period}</li>
         * </ul>
         * 
         * @param serviced
         *     Estimated date or dates of service
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder serviced(Element serviced) {
            this.serviced = serviced;
            return this;
        }

        /**
         * The date when this resource was created.
         * 
         * <p>This element is required.
         * 
         * @param created
         *     Creation date
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder created(DateTime created) {
            this.created = created;
            return this;
        }

        /**
         * Person who created the request.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * </ul>
         * 
         * @param enterer
         *     Author
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder enterer(Reference enterer) {
            this.enterer = enterer;
            return this;
        }

        /**
         * The provider which is responsible for the request.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param provider
         *     Party responsible for the request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder provider(Reference provider) {
            this.provider = provider;
            return this;
        }

        /**
         * The Insurer who issued the coverage in question and is the recipient of the request.
         * 
         * <p>This element is required.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param insurer
         *     Coverage issuer
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurer(Reference insurer) {
            this.insurer = insurer;
            return this;
        }

        /**
         * Facility where the services are intended to be provided.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Location}</li>
         * </ul>
         * 
         * @param facility
         *     Servicing facility
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder facility(Reference facility) {
            this.facility = facility;
            return this;
        }

        /**
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(SupportingInfo... supportingInfo) {
            for (SupportingInfo value : supportingInfo) {
                this.supportingInfo.add(value);
            }
            return this;
        }

        /**
         * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
         * concurrent issues.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param supportingInfo
         *     Supporting information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder supportingInfo(Collection<SupportingInfo> supportingInfo) {
            this.supportingInfo = new ArrayList<>(supportingInfo);
            return this;
        }

        /**
         * Financial instruments for reimbursement for the health care products and services.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Insurance... insurance) {
            for (Insurance value : insurance) {
                this.insurance.add(value);
            }
            return this;
        }

        /**
         * Financial instruments for reimbursement for the health care products and services.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param insurance
         *     Patient insurance information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder insurance(Collection<Insurance> insurance) {
            this.insurance = new ArrayList<>(insurance);
            return this;
        }

        /**
         * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
         * may be required by the payor.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param item
         *     Item to be evaluated for eligibiity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Item... item) {
            for (Item value : item) {
                this.item.add(value);
            }
            return this;
        }

        /**
         * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
         * may be required by the payor.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param item
         *     Item to be evaluated for eligibiity
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Collection<Item> item) {
            this.item = new ArrayList<>(item);
            return this;
        }

        /**
         * Build the {@link CoverageEligibilityRequest}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * <li>purpose</li>
         * <li>patient</li>
         * <li>created</li>
         * <li>insurer</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link CoverageEligibilityRequest}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid CoverageEligibilityRequest per the base specification
         */
        @Override
        public CoverageEligibilityRequest build() {
            return new CoverageEligibilityRequest(this);
        }

        protected Builder from(CoverageEligibilityRequest coverageEligibilityRequest) {
            super.from(coverageEligibilityRequest);
            identifier.addAll(coverageEligibilityRequest.identifier);
            status = coverageEligibilityRequest.status;
            priority = coverageEligibilityRequest.priority;
            purpose.addAll(coverageEligibilityRequest.purpose);
            patient = coverageEligibilityRequest.patient;
            serviced = coverageEligibilityRequest.serviced;
            created = coverageEligibilityRequest.created;
            enterer = coverageEligibilityRequest.enterer;
            provider = coverageEligibilityRequest.provider;
            insurer = coverageEligibilityRequest.insurer;
            facility = coverageEligibilityRequest.facility;
            supportingInfo.addAll(coverageEligibilityRequest.supportingInfo);
            insurance.addAll(coverageEligibilityRequest.insurance);
            item.addAll(coverageEligibilityRequest.item);
            return this;
        }
    }

    /**
     * Additional information codes regarding exceptions, special considerations, the condition, situation, prior or 
     * concurrent issues.
     */
    public static class SupportingInfo extends BackboneElement {
        @Required
        private final PositiveInt sequence;
        @Required
        private final Reference information;
        private final Boolean appliesToAll;

        private volatile int hashCode;

        private SupportingInfo(Builder builder) {
            super(builder);
            sequence = ValidationSupport.requireNonNull(builder.sequence, "sequence");
            information = ValidationSupport.requireNonNull(builder.information, "information");
            appliesToAll = builder.appliesToAll;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * A number to uniquely identify supporting information entries.
         * 
         * @return
         *     An immutable object of type {@link PositiveInt} that is non-null.
         */
        public PositiveInt getSequence() {
            return sequence;
        }

        /**
         * Additional data or information such as resources, documents, images etc. including references to the data or the 
         * actual inclusion of the data.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getInformation() {
            return information;
        }

        /**
         * The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getAppliesToAll() {
            return appliesToAll;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (sequence != null) || 
                (information != null) || 
                (appliesToAll != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(sequence, "sequence", visitor);
                    accept(information, "information", visitor);
                    accept(appliesToAll, "appliesToAll", visitor);
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
            SupportingInfo other = (SupportingInfo) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(sequence, other.sequence) && 
                Objects.equals(information, other.information) && 
                Objects.equals(appliesToAll, other.appliesToAll);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    sequence, 
                    information, 
                    appliesToAll);
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

        public static class Builder extends BackboneElement.Builder {
            private PositiveInt sequence;
            private Reference information;
            private Boolean appliesToAll;

            private Builder() {
                super();
            }

            /**
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * A number to uniquely identify supporting information entries.
             * 
             * <p>This element is required.
             * 
             * @param sequence
             *     Information instance identifier
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder sequence(PositiveInt sequence) {
                this.sequence = sequence;
                return this;
            }

            /**
             * Additional data or information such as resources, documents, images etc. including references to the data or the 
             * actual inclusion of the data.
             * 
             * <p>This element is required.
             * 
             * @param information
             *     Data to be provided
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder information(Reference information) {
                this.information = information;
                return this;
            }

            /**
             * The supporting materials are applicable for all detail items, product/servce categories and specific billing codes.
             * 
             * @param appliesToAll
             *     Applies to all items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder appliesToAll(Boolean appliesToAll) {
                this.appliesToAll = appliesToAll;
                return this;
            }

            /**
             * Build the {@link SupportingInfo}
             * 
             * <p>Required elements:
             * <ul>
             * <li>sequence</li>
             * <li>information</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link SupportingInfo}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid SupportingInfo per the base specification
             */
            @Override
            public SupportingInfo build() {
                return new SupportingInfo(this);
            }

            protected Builder from(SupportingInfo supportingInfo) {
                super.from(supportingInfo);
                sequence = supportingInfo.sequence;
                information = supportingInfo.information;
                appliesToAll = supportingInfo.appliesToAll;
                return this;
            }
        }
    }

    /**
     * Financial instruments for reimbursement for the health care products and services.
     */
    public static class Insurance extends BackboneElement {
        private final Boolean focal;
        @ReferenceTarget({ "Coverage" })
        @Required
        private final Reference coverage;
        private final String businessArrangement;

        private volatile int hashCode;

        private Insurance(Builder builder) {
            super(builder);
            focal = builder.focal;
            coverage = ValidationSupport.requireNonNull(builder.coverage, "coverage");
            businessArrangement = builder.businessArrangement;
            ValidationSupport.checkReferenceType(coverage, "coverage", "Coverage");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getFocal() {
            return focal;
        }

        /**
         * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
         * will use these details to locate the patient's actual coverage within the insurer's information system.
         * 
         * @return
         *     An immutable object of type {@link Reference} that is non-null.
         */
        public Reference getCoverage() {
            return coverage;
        }

        /**
         * A business agreement number established between the provider and the insurer for special business processing purposes.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getBusinessArrangement() {
            return businessArrangement;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (focal != null) || 
                (coverage != null) || 
                (businessArrangement != null);
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(focal, "focal", visitor);
                    accept(coverage, "coverage", visitor);
                    accept(businessArrangement, "businessArrangement", visitor);
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
            Insurance other = (Insurance) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(focal, other.focal) && 
                Objects.equals(coverage, other.coverage) && 
                Objects.equals(businessArrangement, other.businessArrangement);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    focal, 
                    coverage, 
                    businessArrangement);
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

        public static class Builder extends BackboneElement.Builder {
            private Boolean focal;
            private Reference coverage;
            private String businessArrangement;

            private Builder() {
                super();
            }

            /**
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * A flag to indicate that this Coverage is to be used for evaluation of this request when set to true.
             * 
             * @param focal
             *     Applicable coverage
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder focal(Boolean focal) {
                this.focal = focal;
                return this;
            }

            /**
             * Reference to the insurance card level information contained in the Coverage resource. The coverage issuing insurer 
             * will use these details to locate the patient's actual coverage within the insurer's information system.
             * 
             * <p>This element is required.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Coverage}</li>
             * </ul>
             * 
             * @param coverage
             *     Insurance information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder coverage(Reference coverage) {
                this.coverage = coverage;
                return this;
            }

            /**
             * A business agreement number established between the provider and the insurer for special business processing purposes.
             * 
             * @param businessArrangement
             *     Additional provider contract number
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder businessArrangement(String businessArrangement) {
                this.businessArrangement = businessArrangement;
                return this;
            }

            /**
             * Build the {@link Insurance}
             * 
             * <p>Required elements:
             * <ul>
             * <li>coverage</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Insurance}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Insurance per the base specification
             */
            @Override
            public Insurance build() {
                return new Insurance(this);
            }

            protected Builder from(Insurance insurance) {
                super.from(insurance);
                focal = insurance.focal;
                coverage = insurance.coverage;
                businessArrangement = insurance.businessArrangement;
                return this;
            }
        }
    }

    /**
     * Service categories or billable services for which benefit details and/or an authorization prior to service delivery 
     * may be required by the payor.
     */
    public static class Item extends BackboneElement {
        private final List<PositiveInt> supportingInfoSequence;
        @Binding(
            bindingName = "BenefitCategory",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Benefit categories such as: oral, medical, vision etc.",
            valueSet = "http://hl7.org/fhir/ValueSet/ex-benefitcategory"
        )
        private final CodeableConcept category;
        @Binding(
            bindingName = "ServiceProduct",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Allowable service and product codes.",
            valueSet = "http://hl7.org/fhir/ValueSet/service-uscls"
        )
        private final CodeableConcept productOrService;
        @Binding(
            bindingName = "Modifiers",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Item type or modifiers codes, eg for Oral whether the treatment is cosmetic or associated with TMJ, or an appliance was lost or stolen.",
            valueSet = "http://hl7.org/fhir/ValueSet/claim-modifiers"
        )
        private final List<CodeableConcept> modifier;
        @ReferenceTarget({ "Practitioner", "PractitionerRole" })
        private final Reference provider;
        private final SimpleQuantity quantity;
        private final Money unitPrice;
        @ReferenceTarget({ "Location", "Organization" })
        private final Reference facility;
        private final List<Diagnosis> diagnosis;
        private final List<Reference> detail;

        private volatile int hashCode;

        private Item(Builder builder) {
            super(builder);
            supportingInfoSequence = Collections.unmodifiableList(ValidationSupport.checkList(builder.supportingInfoSequence, "supportingInfoSequence", PositiveInt.class));
            category = builder.category;
            productOrService = builder.productOrService;
            modifier = Collections.unmodifiableList(ValidationSupport.checkList(builder.modifier, "modifier", CodeableConcept.class));
            provider = builder.provider;
            quantity = builder.quantity;
            unitPrice = builder.unitPrice;
            facility = builder.facility;
            diagnosis = Collections.unmodifiableList(ValidationSupport.checkList(builder.diagnosis, "diagnosis", Diagnosis.class));
            detail = Collections.unmodifiableList(ValidationSupport.checkList(builder.detail, "detail", Reference.class));
            ValidationSupport.checkReferenceType(provider, "provider", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(facility, "facility", "Location", "Organization");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Exceptions, special conditions and supporting information applicable for this service or product line.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link PositiveInt} that may be empty.
         */
        public List<PositiveInt> getSupportingInfoSequence() {
            return supportingInfoSequence;
        }

        /**
         * Code to identify the general type of benefits under which products and services are provided.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * This contains the product, service, drug or other billing code for the item.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getProductOrService() {
            return productOrService;
        }

        /**
         * Item typification or modifiers codes to convey additional context for the product or service.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getModifier() {
            return modifier;
        }

        /**
         * The practitioner who is responsible for the product or service to be rendered to the patient.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getProvider() {
            return provider;
        }

        /**
         * The number of repetitions of a service or product.
         * 
         * @return
         *     An immutable object of type {@link SimpleQuantity} that may be null.
         */
        public SimpleQuantity getQuantity() {
            return quantity;
        }

        /**
         * The amount charged to the patient by the provider for a single unit.
         * 
         * @return
         *     An immutable object of type {@link Money} that may be null.
         */
        public Money getUnitPrice() {
            return unitPrice;
        }

        /**
         * Facility where the services will be provided.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getFacility() {
            return facility;
        }

        /**
         * Patient diagnosis for which care is sought.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Diagnosis} that may be empty.
         */
        public List<Diagnosis> getDiagnosis() {
            return diagnosis;
        }

        /**
         * The plan/proposal/order describing the proposed service in detail.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getDetail() {
            return detail;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !supportingInfoSequence.isEmpty() || 
                (category != null) || 
                (productOrService != null) || 
                !modifier.isEmpty() || 
                (provider != null) || 
                (quantity != null) || 
                (unitPrice != null) || 
                (facility != null) || 
                !diagnosis.isEmpty() || 
                !detail.isEmpty();
        }

        @Override
        public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
            if (visitor.preVisit(this)) {
                visitor.visitStart(elementName, elementIndex, this);
                if (visitor.visit(elementName, elementIndex, this)) {
                    // visit children
                    accept(id, "id", visitor);
                    accept(extension, "extension", visitor, Extension.class);
                    accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                    accept(supportingInfoSequence, "supportingInfoSequence", visitor, PositiveInt.class);
                    accept(category, "category", visitor);
                    accept(productOrService, "productOrService", visitor);
                    accept(modifier, "modifier", visitor, CodeableConcept.class);
                    accept(provider, "provider", visitor);
                    accept(quantity, "quantity", visitor);
                    accept(unitPrice, "unitPrice", visitor);
                    accept(facility, "facility", visitor);
                    accept(diagnosis, "diagnosis", visitor, Diagnosis.class);
                    accept(detail, "detail", visitor, Reference.class);
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
            Item other = (Item) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(supportingInfoSequence, other.supportingInfoSequence) && 
                Objects.equals(category, other.category) && 
                Objects.equals(productOrService, other.productOrService) && 
                Objects.equals(modifier, other.modifier) && 
                Objects.equals(provider, other.provider) && 
                Objects.equals(quantity, other.quantity) && 
                Objects.equals(unitPrice, other.unitPrice) && 
                Objects.equals(facility, other.facility) && 
                Objects.equals(diagnosis, other.diagnosis) && 
                Objects.equals(detail, other.detail);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    supportingInfoSequence, 
                    category, 
                    productOrService, 
                    modifier, 
                    provider, 
                    quantity, 
                    unitPrice, 
                    facility, 
                    diagnosis, 
                    detail);
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

        public static class Builder extends BackboneElement.Builder {
            private List<PositiveInt> supportingInfoSequence = new ArrayList<>();
            private CodeableConcept category;
            private CodeableConcept productOrService;
            private List<CodeableConcept> modifier = new ArrayList<>();
            private Reference provider;
            private SimpleQuantity quantity;
            private Money unitPrice;
            private Reference facility;
            private List<Diagnosis> diagnosis = new ArrayList<>();
            private List<Reference> detail = new ArrayList<>();

            private Builder() {
                super();
            }

            /**
             * Unique id for the element within a resource (for internal references). This may be any string value that does not 
             * contain spaces.
             * 
             * @param id
             *     Unique id for inter-element referencing
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder id(java.lang.String id) {
                return (Builder) super.id(id);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Extension... modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * May be used to represent additional information that is not part of the basic definition of the element and that 
             * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
             * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
             * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
             * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
             * extension. Applications processing a resource are required to check for modifier extensions.
             * 
             * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
             * change the meaning of modifierExtension itself).
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifierExtension
             *     Extensions that cannot be ignored even if unrecognized
             * 
             * @return
             *     A reference to this Builder instance
             */
            @Override
            public Builder modifierExtension(Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Exceptions, special conditions and supporting information applicable for this service or product line.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param supportingInfoSequence
             *     Applicable exception or supporting information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingInfoSequence(PositiveInt... supportingInfoSequence) {
                for (PositiveInt value : supportingInfoSequence) {
                    this.supportingInfoSequence.add(value);
                }
                return this;
            }

            /**
             * Exceptions, special conditions and supporting information applicable for this service or product line.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param supportingInfoSequence
             *     Applicable exception or supporting information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder supportingInfoSequence(Collection<PositiveInt> supportingInfoSequence) {
                this.supportingInfoSequence = new ArrayList<>(supportingInfoSequence);
                return this;
            }

            /**
             * Code to identify the general type of benefits under which products and services are provided.
             * 
             * @param category
             *     Benefit classification
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * This contains the product, service, drug or other billing code for the item.
             * 
             * @param productOrService
             *     Billing, service, product, or drug code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder productOrService(CodeableConcept productOrService) {
                this.productOrService = productOrService;
                return this;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(CodeableConcept... modifier) {
                for (CodeableConcept value : modifier) {
                    this.modifier.add(value);
                }
                return this;
            }

            /**
             * Item typification or modifiers codes to convey additional context for the product or service.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param modifier
             *     Product or service billing modifiers
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder modifier(Collection<CodeableConcept> modifier) {
                this.modifier = new ArrayList<>(modifier);
                return this;
            }

            /**
             * The practitioner who is responsible for the product or service to be rendered to the patient.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param provider
             *     Perfoming practitioner
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder provider(Reference provider) {
                this.provider = provider;
                return this;
            }

            /**
             * The number of repetitions of a service or product.
             * 
             * @param quantity
             *     Count of products or services
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder quantity(SimpleQuantity quantity) {
                this.quantity = quantity;
                return this;
            }

            /**
             * The amount charged to the patient by the provider for a single unit.
             * 
             * @param unitPrice
             *     Fee, charge or cost per item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder unitPrice(Money unitPrice) {
                this.unitPrice = unitPrice;
                return this;
            }

            /**
             * Facility where the services will be provided.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Location}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param facility
             *     Servicing facility
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder facility(Reference facility) {
                this.facility = facility;
                return this;
            }

            /**
             * Patient diagnosis for which care is sought.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param diagnosis
             *     Applicable diagnosis
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diagnosis(Diagnosis... diagnosis) {
                for (Diagnosis value : diagnosis) {
                    this.diagnosis.add(value);
                }
                return this;
            }

            /**
             * Patient diagnosis for which care is sought.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param diagnosis
             *     Applicable diagnosis
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder diagnosis(Collection<Diagnosis> diagnosis) {
                this.diagnosis = new ArrayList<>(diagnosis);
                return this;
            }

            /**
             * The plan/proposal/order describing the proposed service in detail.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param detail
             *     Product or service details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Reference... detail) {
                for (Reference value : detail) {
                    this.detail.add(value);
                }
                return this;
            }

            /**
             * The plan/proposal/order describing the proposed service in detail.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param detail
             *     Product or service details
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder detail(Collection<Reference> detail) {
                this.detail = new ArrayList<>(detail);
                return this;
            }

            /**
             * Build the {@link Item}
             * 
             * @return
             *     An immutable object of type {@link Item}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Item per the base specification
             */
            @Override
            public Item build() {
                return new Item(this);
            }

            protected Builder from(Item item) {
                super.from(item);
                supportingInfoSequence.addAll(item.supportingInfoSequence);
                category = item.category;
                productOrService = item.productOrService;
                modifier.addAll(item.modifier);
                provider = item.provider;
                quantity = item.quantity;
                unitPrice = item.unitPrice;
                facility = item.facility;
                diagnosis.addAll(item.diagnosis);
                detail.addAll(item.detail);
                return this;
            }
        }

        /**
         * Patient diagnosis for which care is sought.
         */
        public static class Diagnosis extends BackboneElement {
            @ReferenceTarget({ "Condition" })
            @Choice({ CodeableConcept.class, Reference.class })
            @Binding(
                bindingName = "ICD10",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "ICD10 Diagnostic codes.",
                valueSet = "http://hl7.org/fhir/ValueSet/icd-10"
            )
            private final Element diagnosis;

            private volatile int hashCode;

            private Diagnosis(Builder builder) {
                super(builder);
                diagnosis = ValidationSupport.choiceElement(builder.diagnosis, "diagnosis", CodeableConcept.class, Reference.class);
                ValidationSupport.checkReferenceType(diagnosis, "diagnosis", "Condition");
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
             * 
             * @return
             *     An immutable object of type {@link Element} that may be null.
             */
            public Element getDiagnosis() {
                return diagnosis;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (diagnosis != null);
            }

            @Override
            public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
                if (visitor.preVisit(this)) {
                    visitor.visitStart(elementName, elementIndex, this);
                    if (visitor.visit(elementName, elementIndex, this)) {
                        // visit children
                        accept(id, "id", visitor);
                        accept(extension, "extension", visitor, Extension.class);
                        accept(modifierExtension, "modifierExtension", visitor, Extension.class);
                        accept(diagnosis, "diagnosis", visitor);
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
                Diagnosis other = (Diagnosis) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(diagnosis, other.diagnosis);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        diagnosis);
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

            public static class Builder extends BackboneElement.Builder {
                private Element diagnosis;

                private Builder() {
                    super();
                }

                /**
                 * Unique id for the element within a resource (for internal references). This may be any string value that does not 
                 * contain spaces.
                 * 
                 * @param id
                 *     Unique id for inter-element referencing
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder id(java.lang.String id) {
                    return (Builder) super.id(id);
                }

                /**
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
                 * May be used to represent additional information that is not part of the basic definition of the element. To make the 
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
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Extension... modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * May be used to represent additional information that is not part of the basic definition of the element and that 
                 * modifies the understanding of the element in which it is contained and/or the understanding of the containing 
                 * element's descendants. Usually modifier elements provide negation or qualification. To make the use of extensions safe 
                 * and manageable, there is a strict set of governance applied to the definition and use of extensions. Though any 
                 * implementer can define an extension, there is a set of requirements that SHALL be met as part of the definition of the 
                 * extension. Applications processing a resource are required to check for modifier extensions.
                 * 
                 * <p>Modifier extensions SHALL NOT change the meaning of any elements on Resource or DomainResource (including cannot 
                 * change the meaning of modifierExtension itself).
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param modifierExtension
                 *     Extensions that cannot be ignored even if unrecognized
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                @Override
                public Builder modifierExtension(Collection<Extension> modifierExtension) {
                    return (Builder) super.modifierExtension(modifierExtension);
                }

                /**
                 * The nature of illness or problem in a coded form or as a reference to an external defined Condition.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link CodeableConcept}</li>
                 * <li>{@link Reference}</li>
                 * </ul>
                 * 
                 * When of type {@link Reference}, the allowed resource types for this reference are:
                 * <ul>
                 * <li>{@link Condition}</li>
                 * </ul>
                 * 
                 * @param diagnosis
                 *     Nature of illness or problem
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder diagnosis(Element diagnosis) {
                    this.diagnosis = diagnosis;
                    return this;
                }

                /**
                 * Build the {@link Diagnosis}
                 * 
                 * @return
                 *     An immutable object of type {@link Diagnosis}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Diagnosis per the base specification
                 */
                @Override
                public Diagnosis build() {
                    return new Diagnosis(this);
                }

                protected Builder from(Diagnosis diagnosis) {
                    super.from(diagnosis);
                    this.diagnosis = diagnosis.diagnosis;
                    return this;
                }
            }
        }
    }
}
