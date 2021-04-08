/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.ContactPointSystem;
import com.ibm.fhir.model.type.code.ContactPointUse;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * Details for all kinds of technology mediated contact points for a person or organization, including telephone, email, 
 * etc.
 */
@Constraint(
    id = "cpt-2",
    level = "Rule",
    location = "(base)",
    description = "A system is required if a value is provided.",
    expression = "value.empty() or system.exists()"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ContactPoint extends Element {
    @Summary
    @Binding(
        bindingName = "ContactPointSystem",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Telecommunications form for contact point.",
        valueSet = "http://hl7.org/fhir/ValueSet/contact-point-system|4.0.1"
    )
    private final ContactPointSystem system;
    @Summary
    private final String value;
    @Summary
    @Binding(
        bindingName = "ContactPointUse",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Use of contact point.",
        valueSet = "http://hl7.org/fhir/ValueSet/contact-point-use|4.0.1"
    )
    private final ContactPointUse use;
    @Summary
    private final PositiveInt rank;
    @Summary
    private final Period period;

    private volatile int hashCode;

    private ContactPoint(Builder builder) {
        super(builder);
        system = builder.system;
        value = builder.value;
        use = builder.use;
        rank = builder.rank;
        period = builder.period;
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * Telecommunications form for contact point - what communications system is required to make use of the contact.
     * 
     * @return
     *     An immutable object of type {@link ContactPointSystem} that may be null.
     */
    public ContactPointSystem getSystem() {
        return system;
    }

    /**
     * The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone 
     * number or email address).
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getValue() {
        return value;
    }

    /**
     * Identifies the purpose for the contact point.
     * 
     * @return
     *     An immutable object of type {@link ContactPointUse} that may be null.
     */
    public ContactPointUse getUse() {
        return use;
    }

    /**
     * Specifies a preferred order in which to use a set of contacts. ContactPoints with lower rank values are more preferred 
     * than those with higher rank values.
     * 
     * @return
     *     An immutable object of type {@link PositiveInt} that may be null.
     */
    public PositiveInt getRank() {
        return rank;
    }

    /**
     * Time period when the contact point was/is in use.
     * 
     * @return
     *     An immutable object of type {@link Period} that may be null.
     */
    public Period getPeriod() {
        return period;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (system != null) || 
            (value != null) || 
            (use != null) || 
            (rank != null) || 
            (period != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(system, "system", visitor);
                accept(value, "value", visitor);
                accept(use, "use", visitor);
                accept(rank, "rank", visitor);
                accept(period, "period", visitor);
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
        ContactPoint other = (ContactPoint) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(system, other.system) && 
            Objects.equals(value, other.value) && 
            Objects.equals(use, other.use) && 
            Objects.equals(rank, other.rank) && 
            Objects.equals(period, other.period);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                system, 
                value, 
                use, 
                rank, 
                period);
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

    public static class Builder extends Element.Builder {
        private ContactPointSystem system;
        private String value;
        private ContactPointUse use;
        private PositiveInt rank;
        private Period period;

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
         * Telecommunications form for contact point - what communications system is required to make use of the contact.
         * 
         * @param system
         *     phone | fax | email | pager | url | sms | other
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder system(ContactPointSystem system) {
            this.system = system;
            return this;
        }

        /**
         * The actual contact point details, in a form that is meaningful to the designated communication system (i.e. phone 
         * number or email address).
         * 
         * @param value
         *     The actual contact point details
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder value(String value) {
            this.value = value;
            return this;
        }

        /**
         * Identifies the purpose for the contact point.
         * 
         * @param use
         *     home | work | temp | old | mobile - purpose of this contact point
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder use(ContactPointUse use) {
            this.use = use;
            return this;
        }

        /**
         * Specifies a preferred order in which to use a set of contacts. ContactPoints with lower rank values are more preferred 
         * than those with higher rank values.
         * 
         * @param rank
         *     Specify preferred order of use (1 = highest)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder rank(PositiveInt rank) {
            this.rank = rank;
            return this;
        }

        /**
         * Time period when the contact point was/is in use.
         * 
         * @param period
         *     Time period when the contact point was/is in use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Period period) {
            this.period = period;
            return this;
        }

        /**
         * Build the {@link ContactPoint}
         * 
         * @return
         *     An immutable object of type {@link ContactPoint}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ContactPoint per the base specification
         */
        @Override
        public ContactPoint build() {
            return new ContactPoint(this);
        }

        protected Builder from(ContactPoint contactPoint) {
            super.from(contactPoint);
            system = contactPoint.system;
            value = contactPoint.value;
            use = contactPoint.use;
            rank = contactPoint.rank;
            period = contactPoint.period;
            return this;
        }
    }
}
