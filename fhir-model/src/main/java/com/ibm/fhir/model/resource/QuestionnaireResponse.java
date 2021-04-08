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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.Canonical;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Coding;
import com.ibm.fhir.model.type.Date;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Time;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.QuestionnaireResponseStatus;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A structured set of questions and their answers. The questions are ordered and grouped into coherent subsets, 
 * corresponding to the structure of the grouping of the questionnaire being responded to.
 * 
 * <p>Maturity level: FMM3 (Trial Use)
 */
@Maturity(
    level = 3,
    status = StandardsStatus.ValueSet.TRIAL_USE
)
@Constraint(
    id = "qrs-1",
    level = "Rule",
    location = "QuestionnaireResponse.item",
    description = "Nested item can't be beneath both item and answer",
    expression = "(answer.exists() and item.exists()).not()"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class QuestionnaireResponse extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    @ReferenceTarget({ "CarePlan", "ServiceRequest" })
    private final List<Reference> basedOn;
    @Summary
    @ReferenceTarget({ "Observation", "Procedure" })
    private final List<Reference> partOf;
    @Summary
    private final Canonical questionnaire;
    @Summary
    @Binding(
        bindingName = "QuestionnaireResponseStatus",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Lifecycle status of the questionnaire response.",
        valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-answers-status|4.0.1"
    )
    @Required
    private final QuestionnaireResponseStatus status;
    @Summary
    private final Reference subject;
    @Summary
    @ReferenceTarget({ "Encounter" })
    private final Reference encounter;
    @Summary
    private final DateTime authored;
    @Summary
    @ReferenceTarget({ "Device", "Practitioner", "PractitionerRole", "Patient", "RelatedPerson", "Organization" })
    private final Reference author;
    @Summary
    @ReferenceTarget({ "Patient", "Practitioner", "PractitionerRole", "RelatedPerson" })
    private final Reference source;
    private final List<Item> item;

    private volatile int hashCode;

    private QuestionnaireResponse(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        basedOn = Collections.unmodifiableList(ValidationSupport.checkList(builder.basedOn, "basedOn", Reference.class));
        partOf = Collections.unmodifiableList(ValidationSupport.checkList(builder.partOf, "partOf", Reference.class));
        questionnaire = builder.questionnaire;
        status = ValidationSupport.requireNonNull(builder.status, "status");
        subject = builder.subject;
        encounter = builder.encounter;
        authored = builder.authored;
        author = builder.author;
        source = builder.source;
        item = Collections.unmodifiableList(ValidationSupport.checkList(builder.item, "item", Item.class));
        ValidationSupport.checkReferenceType(basedOn, "basedOn", "CarePlan", "ServiceRequest");
        ValidationSupport.checkReferenceType(partOf, "partOf", "Observation", "Procedure");
        ValidationSupport.checkReferenceType(encounter, "encounter", "Encounter");
        ValidationSupport.checkReferenceType(author, "author", "Device", "Practitioner", "PractitionerRole", "Patient", "RelatedPerson", "Organization");
        ValidationSupport.checkReferenceType(source, "source", "Patient", "Practitioner", "PractitionerRole", "RelatedPerson");
        ValidationSupport.requireChildren(this);
    }

    /**
     * A business identifier assigned to a particular completed (or partially completed) questionnaire.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse. For example, a 
     * ServiceRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getBasedOn() {
        return basedOn;
    }

    /**
     * A procedure or observation that this questionnaire was performed as part of the execution of. For example, the surgery 
     * a checklist was executed as part of.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getPartOf() {
        return partOf;
    }

    /**
     * The Questionnaire that defines and organizes the questions for which answers are being provided.
     * 
     * @return
     *     An immutable object of type {@link Canonical} that may be null.
     */
    public Canonical getQuestionnaire() {
        return questionnaire;
    }

    /**
     * The position of the questionnaire response within its overall lifecycle.
     * 
     * @return
     *     An immutable object of type {@link QuestionnaireResponseStatus} that is non-null.
     */
    public QuestionnaireResponseStatus getStatus() {
        return status;
    }

    /**
     * The subject of the questionnaire response. This could be a patient, organization, practitioner, device, etc. This is 
     * who/what the answers apply to, but is not necessarily the source of information.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSubject() {
        return subject;
    }

    /**
     * The Encounter during which this questionnaire response was created or to which the creation of this record is tightly 
     * associated.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getEncounter() {
        return encounter;
    }

    /**
     * The date and/or time that this set of answers were last changed.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getAuthored() {
        return authored;
    }

    /**
     * Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getAuthor() {
        return author;
    }

    /**
     * The person who answered the questions about the subject.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSource() {
        return source;
    }

    /**
     * A group or question item from the original questionnaire for which answers are provided.
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
            (identifier != null) || 
            !basedOn.isEmpty() || 
            !partOf.isEmpty() || 
            (questionnaire != null) || 
            (status != null) || 
            (subject != null) || 
            (encounter != null) || 
            (authored != null) || 
            (author != null) || 
            (source != null) || 
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
                accept(identifier, "identifier", visitor);
                accept(basedOn, "basedOn", visitor, Reference.class);
                accept(partOf, "partOf", visitor, Reference.class);
                accept(questionnaire, "questionnaire", visitor);
                accept(status, "status", visitor);
                accept(subject, "subject", visitor);
                accept(encounter, "encounter", visitor);
                accept(authored, "authored", visitor);
                accept(author, "author", visitor);
                accept(source, "source", visitor);
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
        QuestionnaireResponse other = (QuestionnaireResponse) obj;
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
            Objects.equals(partOf, other.partOf) && 
            Objects.equals(questionnaire, other.questionnaire) && 
            Objects.equals(status, other.status) && 
            Objects.equals(subject, other.subject) && 
            Objects.equals(encounter, other.encounter) && 
            Objects.equals(authored, other.authored) && 
            Objects.equals(author, other.author) && 
            Objects.equals(source, other.source) && 
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
                basedOn, 
                partOf, 
                questionnaire, 
                status, 
                subject, 
                encounter, 
                authored, 
                author, 
                source, 
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
        private Identifier identifier;
        private List<Reference> basedOn = new ArrayList<>();
        private List<Reference> partOf = new ArrayList<>();
        private Canonical questionnaire;
        private QuestionnaireResponseStatus status;
        private Reference subject;
        private Reference encounter;
        private DateTime authored;
        private Reference author;
        private Reference source;
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
         * A business identifier assigned to a particular completed (or partially completed) questionnaire.
         * 
         * @param identifier
         *     Unique id for this set of answers
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse. For example, a 
         * ServiceRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link CarePlan}</li>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Request fulfilled by this QuestionnaireResponse
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
         * The order, proposal or plan that is fulfilled in whole or in part by this QuestionnaireResponse. For example, a 
         * ServiceRequest seeking an intake assessment or a decision support recommendation to assess for post-partum depression.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link CarePlan}</li>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param basedOn
         *     Request fulfilled by this QuestionnaireResponse
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder basedOn(Collection<Reference> basedOn) {
            this.basedOn = new ArrayList<>(basedOn);
            return this;
        }

        /**
         * A procedure or observation that this questionnaire was performed as part of the execution of. For example, the surgery 
         * a checklist was executed as part of.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Observation}</li>
         * <li>{@link Procedure}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of this action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Reference... partOf) {
            for (Reference value : partOf) {
                this.partOf.add(value);
            }
            return this;
        }

        /**
         * A procedure or observation that this questionnaire was performed as part of the execution of. For example, the surgery 
         * a checklist was executed as part of.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link Observation}</li>
         * <li>{@link Procedure}</li>
         * </ul>
         * 
         * @param partOf
         *     Part of this action
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder partOf(Collection<Reference> partOf) {
            this.partOf = new ArrayList<>(partOf);
            return this;
        }

        /**
         * The Questionnaire that defines and organizes the questions for which answers are being provided.
         * 
         * @param questionnaire
         *     Form being answered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder questionnaire(Canonical questionnaire) {
            this.questionnaire = questionnaire;
            return this;
        }

        /**
         * The position of the questionnaire response within its overall lifecycle.
         * 
         * <p>This element is required.
         * 
         * @param status
         *     in-progress | completed | amended | entered-in-error | stopped
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(QuestionnaireResponseStatus status) {
            this.status = status;
            return this;
        }

        /**
         * The subject of the questionnaire response. This could be a patient, organization, practitioner, device, etc. This is 
         * who/what the answers apply to, but is not necessarily the source of information.
         * 
         * @param subject
         *     The subject of the questions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder subject(Reference subject) {
            this.subject = subject;
            return this;
        }

        /**
         * The Encounter during which this questionnaire response was created or to which the creation of this record is tightly 
         * associated.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Encounter}</li>
         * </ul>
         * 
         * @param encounter
         *     Encounter created as part of
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder encounter(Reference encounter) {
            this.encounter = encounter;
            return this;
        }

        /**
         * The date and/or time that this set of answers were last changed.
         * 
         * @param authored
         *     Date the answers were gathered
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder authored(DateTime authored) {
            this.authored = authored;
            return this;
        }

        /**
         * Person who received the answers to the questions in the QuestionnaireResponse and recorded them in the system.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link Device}</li>
         * <li>{@link Practitioner}</li>
         * <li>{@link PractitionerRole}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param author
         *     Person who received and recorded the answers
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Reference author) {
            this.author = author;
            return this;
        }

        /**
         * The person who answered the questions about the subject.
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
         *     The person who answered the questions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Reference source) {
            this.source = source;
            return this;
        }

        /**
         * A group or question item from the original questionnaire for which answers are provided.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param item
         *     Groups and questions
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
         * A group or question item from the original questionnaire for which answers are provided.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param item
         *     Groups and questions
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder item(Collection<Item> item) {
            this.item = new ArrayList<>(item);
            return this;
        }

        /**
         * Build the {@link QuestionnaireResponse}
         * 
         * <p>Required elements:
         * <ul>
         * <li>status</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link QuestionnaireResponse}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid QuestionnaireResponse per the base specification
         */
        @Override
        public QuestionnaireResponse build() {
            return new QuestionnaireResponse(this);
        }

        protected Builder from(QuestionnaireResponse questionnaireResponse) {
            super.from(questionnaireResponse);
            identifier = questionnaireResponse.identifier;
            basedOn.addAll(questionnaireResponse.basedOn);
            partOf.addAll(questionnaireResponse.partOf);
            questionnaire = questionnaireResponse.questionnaire;
            status = questionnaireResponse.status;
            subject = questionnaireResponse.subject;
            encounter = questionnaireResponse.encounter;
            authored = questionnaireResponse.authored;
            author = questionnaireResponse.author;
            source = questionnaireResponse.source;
            item.addAll(questionnaireResponse.item);
            return this;
        }
    }

    /**
     * A group or question item from the original questionnaire for which answers are provided.
     */
    public static class Item extends BackboneElement {
        @Required
        private final String linkId;
        private final Uri definition;
        private final String text;
        private final List<Answer> answer;
        private final List<QuestionnaireResponse.Item> item;

        private volatile int hashCode;

        private Item(Builder builder) {
            super(builder);
            linkId = ValidationSupport.requireNonNull(builder.linkId, "linkId");
            definition = builder.definition;
            text = builder.text;
            answer = Collections.unmodifiableList(ValidationSupport.checkList(builder.answer, "answer", Answer.class));
            item = Collections.unmodifiableList(ValidationSupport.checkList(builder.item, "item", QuestionnaireResponse.Item.class));
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getLinkId() {
            return linkId;
        }

        /**
         * A reference to an [ElementDefinition](elementdefinition.html) that provides the details for the item.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getDefinition() {
            return definition;
        }

        /**
         * Text that is displayed above the contents of the group or as the text of the question being answered.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getText() {
            return text;
        }

        /**
         * The respondent's answer(s) to the question.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Answer} that may be empty.
         */
        public List<Answer> getAnswer() {
            return answer;
        }

        /**
         * Questions or sub-groups nested beneath a question or group.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Item} that may be empty.
         */
        public List<QuestionnaireResponse.Item> getItem() {
            return item;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (linkId != null) || 
                (definition != null) || 
                (text != null) || 
                !answer.isEmpty() || 
                !item.isEmpty();
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
                    accept(linkId, "linkId", visitor);
                    accept(definition, "definition", visitor);
                    accept(text, "text", visitor);
                    accept(answer, "answer", visitor, Answer.class);
                    accept(item, "item", visitor, QuestionnaireResponse.Item.class);
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
                Objects.equals(linkId, other.linkId) && 
                Objects.equals(definition, other.definition) && 
                Objects.equals(text, other.text) && 
                Objects.equals(answer, other.answer) && 
                Objects.equals(item, other.item);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    linkId, 
                    definition, 
                    text, 
                    answer, 
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

        public static class Builder extends BackboneElement.Builder {
            private String linkId;
            private Uri definition;
            private String text;
            private List<Answer> answer = new ArrayList<>();
            private List<QuestionnaireResponse.Item> item = new ArrayList<>();

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
             * The item from the Questionnaire that corresponds to this item in the QuestionnaireResponse resource.
             * 
             * <p>This element is required.
             * 
             * @param linkId
             *     Pointer to specific item from Questionnaire
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder linkId(String linkId) {
                this.linkId = linkId;
                return this;
            }

            /**
             * A reference to an [ElementDefinition](elementdefinition.html) that provides the details for the item.
             * 
             * @param definition
             *     ElementDefinition - details for the item
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder definition(Uri definition) {
                this.definition = definition;
                return this;
            }

            /**
             * Text that is displayed above the contents of the group or as the text of the question being answered.
             * 
             * @param text
             *     Name for group or question text
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder text(String text) {
                this.text = text;
                return this;
            }

            /**
             * The respondent's answer(s) to the question.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param answer
             *     The response(s) to the question
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder answer(Answer... answer) {
                for (Answer value : answer) {
                    this.answer.add(value);
                }
                return this;
            }

            /**
             * The respondent's answer(s) to the question.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param answer
             *     The response(s) to the question
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder answer(Collection<Answer> answer) {
                this.answer = new ArrayList<>(answer);
                return this;
            }

            /**
             * Questions or sub-groups nested beneath a question or group.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param item
             *     Nested questionnaire response items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(QuestionnaireResponse.Item... item) {
                for (QuestionnaireResponse.Item value : item) {
                    this.item.add(value);
                }
                return this;
            }

            /**
             * Questions or sub-groups nested beneath a question or group.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param item
             *     Nested questionnaire response items
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder item(Collection<QuestionnaireResponse.Item> item) {
                this.item = new ArrayList<>(item);
                return this;
            }

            /**
             * Build the {@link Item}
             * 
             * <p>Required elements:
             * <ul>
             * <li>linkId</li>
             * </ul>
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
                linkId = item.linkId;
                definition = item.definition;
                text = item.text;
                answer.addAll(item.answer);
                this.item.addAll(item.item);
                return this;
            }
        }

        /**
         * The respondent's answer(s) to the question.
         */
        public static class Answer extends BackboneElement {
            @Choice({ Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class })
            @Binding(
                bindingName = "QuestionnaireAnswer",
                strength = BindingStrength.ValueSet.EXAMPLE,
                description = "Code indicating the response provided for a question.",
                valueSet = "http://hl7.org/fhir/ValueSet/questionnaire-answers"
            )
            private final Element value;
            private final List<QuestionnaireResponse.Item> item;

            private volatile int hashCode;

            private Answer(Builder builder) {
                super(builder);
                value = ValidationSupport.choiceElement(builder.value, "value", Boolean.class, Decimal.class, Integer.class, Date.class, DateTime.class, Time.class, String.class, Uri.class, Attachment.class, Coding.class, Quantity.class, Reference.class);
                item = Collections.unmodifiableList(ValidationSupport.checkList(builder.item, "item", QuestionnaireResponse.Item.class));
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * The answer (or one of the answers) provided by the respondent to the question.
             * 
             * @return
             *     An immutable object of type {@link Element} that may be null.
             */
            public Element getValue() {
                return value;
            }

            /**
             * Nested groups and/or questions found within this particular answer.
             * 
             * @return
             *     An unmodifiable list containing immutable objects of type {@link Item} that may be empty.
             */
            public List<QuestionnaireResponse.Item> getItem() {
                return item;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (value != null) || 
                    !item.isEmpty();
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
                        accept(value, "value", visitor);
                        accept(item, "item", visitor, QuestionnaireResponse.Item.class);
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
                Answer other = (Answer) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(value, other.value) && 
                    Objects.equals(item, other.item);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        value, 
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

            public static class Builder extends BackboneElement.Builder {
                private Element value;
                private List<QuestionnaireResponse.Item> item = new ArrayList<>();

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
                 * The answer (or one of the answers) provided by the respondent to the question.
                 * 
                 * <p>This is a choice element with the following allowed types:
                 * <ul>
                 * <li>{@link Boolean}</li>
                 * <li>{@link Decimal}</li>
                 * <li>{@link Integer}</li>
                 * <li>{@link Date}</li>
                 * <li>{@link DateTime}</li>
                 * <li>{@link Time}</li>
                 * <li>{@link String}</li>
                 * <li>{@link Uri}</li>
                 * <li>{@link Attachment}</li>
                 * <li>{@link Coding}</li>
                 * <li>{@link Quantity}</li>
                 * <li>{@link Reference}</li>
                 * </ul>
                 * 
                 * @param value
                 *     Single-valued answer to the question
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder value(Element value) {
                    this.value = value;
                    return this;
                }

                /**
                 * Nested groups and/or questions found within this particular answer.
                 * 
                 * <p>Adds new element(s) to the existing list
                 * 
                 * @param item
                 *     Nested groups and questions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder item(QuestionnaireResponse.Item... item) {
                    for (QuestionnaireResponse.Item value : item) {
                        this.item.add(value);
                    }
                    return this;
                }

                /**
                 * Nested groups and/or questions found within this particular answer.
                 * 
                 * <p>Replaces the existing list with a new one containing elements from the Collection
                 * 
                 * @param item
                 *     Nested groups and questions
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder item(Collection<QuestionnaireResponse.Item> item) {
                    this.item = new ArrayList<>(item);
                    return this;
                }

                /**
                 * Build the {@link Answer}
                 * 
                 * @return
                 *     An immutable object of type {@link Answer}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Answer per the base specification
                 */
                @Override
                public Answer build() {
                    return new Answer(this);
                }

                protected Builder from(Answer answer) {
                    super.from(answer);
                    value = answer.value;
                    item.addAll(answer.item);
                    return this;
                }
            }
        }
    }
}
