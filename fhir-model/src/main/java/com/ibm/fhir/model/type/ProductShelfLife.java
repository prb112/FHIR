/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The shelf-life and storage information for a medicinal product item or container can be described using this class.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class ProductShelfLife extends BackboneElement {
    @Summary
    private final Identifier identifier;
    @Summary
    @Required
    private final CodeableConcept type;
    @Summary
    @Required
    private final Quantity period;
    @Summary
    private final List<CodeableConcept> specialPrecautionsForStorage;

    private volatile int hashCode;

    private ProductShelfLife(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        type = ValidationSupport.requireNonNull(builder.type, "type");
        period = ValidationSupport.requireNonNull(builder.period, "period");
        specialPrecautionsForStorage = Collections.unmodifiableList(ValidationSupport.checkList(builder.specialPrecautionsForStorage, "specialPrecautionsForStorage", CodeableConcept.class));
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * Unique identifier for the packaged Medicinal Product.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal 
     * Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, 
     * etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the 
     * controlled term identifier shall be specified.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that is non-null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * The shelf life time period can be specified using a numerical value for the period of time and its unit of time 
     * measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The 
     * symbol and the symbol identifier shall be used.
     * 
     * @return
     *     An immutable object of type {@link Quantity} that is non-null.
     */
    public Quantity getPeriod() {
        return period;
    }

    /**
     * Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled 
     * term and the controlled term identifier shall be specified.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
     */
    public List<CodeableConcept> getSpecialPrecautionsForStorage() {
        return specialPrecautionsForStorage;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (type != null) || 
            (period != null) || 
            !specialPrecautionsForStorage.isEmpty();
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
                accept(identifier, "identifier", visitor);
                accept(type, "type", visitor);
                accept(period, "period", visitor);
                accept(specialPrecautionsForStorage, "specialPrecautionsForStorage", visitor, CodeableConcept.class);
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
        ProductShelfLife other = (ProductShelfLife) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(period, other.period) && 
            Objects.equals(specialPrecautionsForStorage, other.specialPrecautionsForStorage);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                modifierExtension, 
                identifier, 
                type, 
                period, 
                specialPrecautionsForStorage);
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
        private Identifier identifier;
        private CodeableConcept type;
        private Quantity period;
        private List<CodeableConcept> specialPrecautionsForStorage = new ArrayList<>();

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
         * Unique identifier for the packaged Medicinal Product.
         * 
         * @param identifier
         *     Unique identifier for the packaged Medicinal Product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal 
         * Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, 
         * etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the 
         * controlled term identifier shall be specified.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     This describes the shelf life, taking into account various scenarios such as shelf life of the packaged Medicinal 
         *     Product itself, shelf life after transformation where necessary and shelf life after the first opening of a bottle, 
         *     etc. The shelf life type shall be specified using an appropriate controlled vocabulary The controlled term and the 
         *     controlled term identifier shall be specified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * The shelf life time period can be specified using a numerical value for the period of time and its unit of time 
         * measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The 
         * symbol and the symbol identifier shall be used.
         * 
         * <p>This element is required.
         * 
         * @param period
         *     The shelf life time period can be specified using a numerical value for the period of time and its unit of time 
         *     measurement The unit of measurement shall be specified in accordance with ISO 11240 and the resulting terminology The 
         *     symbol and the symbol identifier shall be used
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder period(Quantity period) {
            this.period = period;
            return this;
        }

        /**
         * Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled 
         * term and the controlled term identifier shall be specified.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param specialPrecautionsForStorage
         *     Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled 
         *     term and the controlled term identifier shall be specified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specialPrecautionsForStorage(CodeableConcept... specialPrecautionsForStorage) {
            for (CodeableConcept value : specialPrecautionsForStorage) {
                this.specialPrecautionsForStorage.add(value);
            }
            return this;
        }

        /**
         * Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled 
         * term and the controlled term identifier shall be specified.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param specialPrecautionsForStorage
         *     Special precautions for storage, if any, can be specified using an appropriate controlled vocabulary The controlled 
         *     term and the controlled term identifier shall be specified
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder specialPrecautionsForStorage(Collection<CodeableConcept> specialPrecautionsForStorage) {
            this.specialPrecautionsForStorage = new ArrayList<>(specialPrecautionsForStorage);
            return this;
        }

        /**
         * Build the {@link ProductShelfLife}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * <li>period</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link ProductShelfLife}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid ProductShelfLife per the base specification
         */
        @Override
        public ProductShelfLife build() {
            return new ProductShelfLife(this);
        }

        protected Builder from(ProductShelfLife productShelfLife) {
            super.from(productShelfLife);
            identifier = productShelfLife.identifier;
            type = productShelfLife.type;
            period = productShelfLife.period;
            specialPrecautionsForStorage.addAll(productShelfLife.specialPrecautionsForStorage);
            return this;
        }
    }
}
