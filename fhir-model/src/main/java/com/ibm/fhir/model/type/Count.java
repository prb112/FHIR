/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.type.code.QuantityComparator;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A measured amount (or an amount that can potentially be measured). Note that measured amounts include amounts that are 
 * not precisely quantified, including amounts involving arbitrary units and floating currencies.
 */
@Constraint(
    id = "cnt-3",
    level = "Rule",
    location = "(base)",
    description = "There SHALL be a code with a value of \"1\" if there is a value. If system is present, it SHALL be UCUM.  If present, the value SHALL be a whole number.",
    expression = "(code.exists() or value.empty()) and (system.empty() or system = %ucum) and (code.empty() or code = '1') and (value.empty() or value.hasValue().not() or value.toString().contains('.').not())"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Count extends Quantity {
    private volatile int hashCode;

    private Count(Builder builder) {
        super(builder);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(value, "value", visitor);
                accept(comparator, "comparator", visitor);
                accept(unit, "unit", visitor);
                accept(system, "system", visitor);
                accept(code, "code", visitor);
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
        Count other = (Count) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(value, other.value) && 
            Objects.equals(comparator, other.comparator) && 
            Objects.equals(unit, other.unit) && 
            Objects.equals(system, other.system) && 
            Objects.equals(code, other.code);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                value, 
                comparator, 
                unit, 
                system, 
                code);
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

    public static class Builder extends Quantity.Builder {
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
         * The value of the measured amount. The value includes an implicit precision in the presentation of the value.
         * 
         * @param value
         *     Numerical value (with implicit precision)
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder value(Decimal value) {
            return (Builder) super.value(value);
        }

        /**
         * How the value should be understood and represented - whether the actual value is greater or less than the stated value 
         * due to measurement issues; e.g. if the comparator is "&lt;" , then the real value is &lt; stated value.
         * 
         * @param comparator
         *     &lt; | &lt;= | &gt;= | &gt; - how to understand the value
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder comparator(QuantityComparator comparator) {
            return (Builder) super.comparator(comparator);
        }

        /**
         * A human-readable form of the unit.
         * 
         * @param unit
         *     Unit representation
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder unit(String unit) {
            return (Builder) super.unit(unit);
        }

        /**
         * The identification of the system that provides the coded form of the unit.
         * 
         * @param system
         *     System that defines coded unit form
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder system(Uri system) {
            return (Builder) super.system(system);
        }

        /**
         * A computer processable form of the unit in some unit representation system.
         * 
         * @param code
         *     Coded form of the unit
         * 
         * @return
         *     A reference to this Builder instance
         */
        @Override
        public Builder code(Code code) {
            return (Builder) super.code(code);
        }

        /**
         * Build the {@link Count}
         * 
         * @return
         *     An immutable object of type {@link Count}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Count per the base specification
         */
        @Override
        public Count build() {
            return new Count(this);
        }

        protected Builder from(Count count) {
            super.from(count);
            return this;
        }
    }
}
