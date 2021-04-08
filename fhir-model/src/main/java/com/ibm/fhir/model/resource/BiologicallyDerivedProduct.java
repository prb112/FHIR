/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Binding;
import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Integer;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Period;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.BiologicallyDerivedProductCategory;
import com.ibm.fhir.model.type.code.BiologicallyDerivedProductStatus;
import com.ibm.fhir.model.type.code.BiologicallyDerivedProductStorageScale;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A material substance originating from a biological entity intended to be transplanted or infused
 * into another (possibly the same) biological entity.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.ValueSet.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class BiologicallyDerivedProduct extends DomainResource {
    @Summary
    private final List<Identifier> identifier;
    @Binding(
        bindingName = "BiologicallyDerivedProductCategory",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Biologically Derived Product Category.",
        valueSet = "http://hl7.org/fhir/ValueSet/product-category|4.0.1"
    )
    private final BiologicallyDerivedProductCategory productCategory;
    @Binding(
        bindingName = "BiologicallyDerivedProductCode",
        strength = BindingStrength.ValueSet.EXAMPLE,
        description = "Biologically Derived Product Code."
    )
    private final CodeableConcept productCode;
    @Binding(
        bindingName = "BiologicallyDerivedProductStatus",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Biologically Derived Product Status.",
        valueSet = "http://hl7.org/fhir/ValueSet/product-status|4.0.1"
    )
    private final BiologicallyDerivedProductStatus status;
    @ReferenceTarget({ "ServiceRequest" })
    private final List<Reference> request;
    private final Integer quantity;
    @ReferenceTarget({ "BiologicallyDerivedProduct" })
    private final List<Reference> parent;
    private final Collection collection;
    private final List<Processing> processing;
    private final Manipulation manipulation;
    private final List<Storage> storage;

    private volatile int hashCode;

    private BiologicallyDerivedProduct(Builder builder) {
        super(builder);
        identifier = Collections.unmodifiableList(ValidationSupport.checkList(builder.identifier, "identifier", Identifier.class));
        productCategory = builder.productCategory;
        productCode = builder.productCode;
        status = builder.status;
        request = Collections.unmodifiableList(ValidationSupport.checkList(builder.request, "request", Reference.class));
        quantity = builder.quantity;
        parent = Collections.unmodifiableList(ValidationSupport.checkList(builder.parent, "parent", Reference.class));
        collection = builder.collection;
        processing = Collections.unmodifiableList(ValidationSupport.checkList(builder.processing, "processing", Processing.class));
        manipulation = builder.manipulation;
        storage = Collections.unmodifiableList(ValidationSupport.checkList(builder.storage, "storage", Storage.class));
        ValidationSupport.checkReferenceType(request, "request", "ServiceRequest");
        ValidationSupport.checkReferenceType(parent, "parent", "BiologicallyDerivedProduct");
        ValidationSupport.requireChildren(this);
    }

    /**
     * This records identifiers associated with this biologically derived product instance that are defined by business 
     * processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in 
     * CDA documents, or in written / printed documentation).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Identifier} that may be empty.
     */
    public List<Identifier> getIdentifier() {
        return identifier;
    }

    /**
     * Broad category of this product.
     * 
     * @return
     *     An immutable object of type {@link BiologicallyDerivedProductCategory} that may be null.
     */
    public BiologicallyDerivedProductCategory getProductCategory() {
        return productCategory;
    }

    /**
     * A code that identifies the kind of this biologically derived product (SNOMED Ctcode).
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getProductCode() {
        return productCode;
    }

    /**
     * Whether the product is currently available.
     * 
     * @return
     *     An immutable object of type {@link BiologicallyDerivedProductStatus} that may be null.
     */
    public BiologicallyDerivedProductStatus getStatus() {
        return status;
    }

    /**
     * Procedure request to obtain this biologically derived product.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getRequest() {
        return request;
    }

    /**
     * Number of discrete units within this product.
     * 
     * @return
     *     An immutable object of type {@link Integer} that may be null.
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Parent product (if any).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getParent() {
        return parent;
    }

    /**
     * How this product was collected.
     * 
     * @return
     *     An immutable object of type {@link Collection} that may be null.
     */
    public Collection getCollection() {
        return collection;
    }

    /**
     * Any processing of the product during collection that does not change the fundamental nature of the product. For 
     * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Processing} that may be empty.
     */
    public List<Processing> getProcessing() {
        return processing;
    }

    /**
     * Any manipulation of product post-collection that is intended to alter the product. For example a buffy-coat enrichment 
     * or CD8 reduction of Peripheral Blood Stem Cells to make it more suitable for infusion.
     * 
     * @return
     *     An immutable object of type {@link Manipulation} that may be null.
     */
    public Manipulation getManipulation() {
        return manipulation;
    }

    /**
     * Product storage.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Storage} that may be empty.
     */
    public List<Storage> getStorage() {
        return storage;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            !identifier.isEmpty() || 
            (productCategory != null) || 
            (productCode != null) || 
            (status != null) || 
            !request.isEmpty() || 
            (quantity != null) || 
            !parent.isEmpty() || 
            (collection != null) || 
            !processing.isEmpty() || 
            (manipulation != null) || 
            !storage.isEmpty();
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
                accept(productCategory, "productCategory", visitor);
                accept(productCode, "productCode", visitor);
                accept(status, "status", visitor);
                accept(request, "request", visitor, Reference.class);
                accept(quantity, "quantity", visitor);
                accept(parent, "parent", visitor, Reference.class);
                accept(collection, "collection", visitor);
                accept(processing, "processing", visitor, Processing.class);
                accept(manipulation, "manipulation", visitor);
                accept(storage, "storage", visitor, Storage.class);
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
        BiologicallyDerivedProduct other = (BiologicallyDerivedProduct) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(productCategory, other.productCategory) && 
            Objects.equals(productCode, other.productCode) && 
            Objects.equals(status, other.status) && 
            Objects.equals(request, other.request) && 
            Objects.equals(quantity, other.quantity) && 
            Objects.equals(parent, other.parent) && 
            Objects.equals(collection, other.collection) && 
            Objects.equals(processing, other.processing) && 
            Objects.equals(manipulation, other.manipulation) && 
            Objects.equals(storage, other.storage);
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
                productCategory, 
                productCode, 
                status, 
                request, 
                quantity, 
                parent, 
                collection, 
                processing, 
                manipulation, 
                storage);
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
        private BiologicallyDerivedProductCategory productCategory;
        private CodeableConcept productCode;
        private BiologicallyDerivedProductStatus status;
        private List<Reference> request = new ArrayList<>();
        private Integer quantity;
        private List<Reference> parent = new ArrayList<>();
        private Collection collection;
        private List<Processing> processing = new ArrayList<>();
        private Manipulation manipulation;
        private List<Storage> storage = new ArrayList<>();

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
        public Builder contained(java.util.Collection<Resource> contained) {
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
        public Builder extension(java.util.Collection<Extension> extension) {
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
        public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
            return (Builder) super.modifierExtension(modifierExtension);
        }

        /**
         * This records identifiers associated with this biologically derived product instance that are defined by business 
         * processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in 
         * CDA documents, or in written / printed documentation).
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param identifier
         *     External ids for this item
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
         * This records identifiers associated with this biologically derived product instance that are defined by business 
         * processes and/or used to refer to it when a direct URL reference to the resource itself is not appropriate (e.g. in 
         * CDA documents, or in written / printed documentation).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param identifier
         *     External ids for this item
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(java.util.Collection<Identifier> identifier) {
            this.identifier = new ArrayList<>(identifier);
            return this;
        }

        /**
         * Broad category of this product.
         * 
         * @param productCategory
         *     organ | tissue | fluid | cells | biologicalAgent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productCategory(BiologicallyDerivedProductCategory productCategory) {
            this.productCategory = productCategory;
            return this;
        }

        /**
         * A code that identifies the kind of this biologically derived product (SNOMED Ctcode).
         * 
         * @param productCode
         *     What this biologically derived product is
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder productCode(CodeableConcept productCode) {
            this.productCode = productCode;
            return this;
        }

        /**
         * Whether the product is currently available.
         * 
         * @param status
         *     available | unavailable
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(BiologicallyDerivedProductStatus status) {
            this.status = status;
            return this;
        }

        /**
         * Procedure request to obtain this biologically derived product.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param request
         *     Procedure request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(Reference... request) {
            for (Reference value : request) {
                this.request.add(value);
            }
            return this;
        }

        /**
         * Procedure request to obtain this biologically derived product.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link ServiceRequest}</li>
         * </ul>
         * 
         * @param request
         *     Procedure request
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder request(java.util.Collection<Reference> request) {
            this.request = new ArrayList<>(request);
            return this;
        }

        /**
         * Number of discrete units within this product.
         * 
         * @param quantity
         *     The amount of this biologically derived product
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder quantity(Integer quantity) {
            this.quantity = quantity;
            return this;
        }

        /**
         * Parent product (if any).
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link BiologicallyDerivedProduct}</li>
         * </ul>
         * 
         * @param parent
         *     BiologicallyDerivedProduct parent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parent(Reference... parent) {
            for (Reference value : parent) {
                this.parent.add(value);
            }
            return this;
        }

        /**
         * Parent product (if any).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link BiologicallyDerivedProduct}</li>
         * </ul>
         * 
         * @param parent
         *     BiologicallyDerivedProduct parent
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder parent(java.util.Collection<Reference> parent) {
            this.parent = new ArrayList<>(parent);
            return this;
        }

        /**
         * How this product was collected.
         * 
         * @param collection
         *     How this product was collected
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder collection(Collection collection) {
            this.collection = collection;
            return this;
        }

        /**
         * Any processing of the product during collection that does not change the fundamental nature of the product. For 
         * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param processing
         *     Any processing of the product during collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processing(Processing... processing) {
            for (Processing value : processing) {
                this.processing.add(value);
            }
            return this;
        }

        /**
         * Any processing of the product during collection that does not change the fundamental nature of the product. For 
         * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param processing
         *     Any processing of the product during collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder processing(java.util.Collection<Processing> processing) {
            this.processing = new ArrayList<>(processing);
            return this;
        }

        /**
         * Any manipulation of product post-collection that is intended to alter the product. For example a buffy-coat enrichment 
         * or CD8 reduction of Peripheral Blood Stem Cells to make it more suitable for infusion.
         * 
         * @param manipulation
         *     Any manipulation of product post-collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder manipulation(Manipulation manipulation) {
            this.manipulation = manipulation;
            return this;
        }

        /**
         * Product storage.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param storage
         *     Product storage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder storage(Storage... storage) {
            for (Storage value : storage) {
                this.storage.add(value);
            }
            return this;
        }

        /**
         * Product storage.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param storage
         *     Product storage
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder storage(java.util.Collection<Storage> storage) {
            this.storage = new ArrayList<>(storage);
            return this;
        }

        /**
         * Build the {@link BiologicallyDerivedProduct}
         * 
         * @return
         *     An immutable object of type {@link BiologicallyDerivedProduct}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid BiologicallyDerivedProduct per the base specification
         */
        @Override
        public BiologicallyDerivedProduct build() {
            return new BiologicallyDerivedProduct(this);
        }

        protected Builder from(BiologicallyDerivedProduct biologicallyDerivedProduct) {
            super.from(biologicallyDerivedProduct);
            identifier.addAll(biologicallyDerivedProduct.identifier);
            productCategory = biologicallyDerivedProduct.productCategory;
            productCode = biologicallyDerivedProduct.productCode;
            status = biologicallyDerivedProduct.status;
            request.addAll(biologicallyDerivedProduct.request);
            quantity = biologicallyDerivedProduct.quantity;
            parent.addAll(biologicallyDerivedProduct.parent);
            collection = biologicallyDerivedProduct.collection;
            processing.addAll(biologicallyDerivedProduct.processing);
            manipulation = biologicallyDerivedProduct.manipulation;
            storage.addAll(biologicallyDerivedProduct.storage);
            return this;
        }
    }

    /**
     * How this product was collected.
     */
    public static class Collection extends BackboneElement {
        @ReferenceTarget({ "Practitioner", "PractitionerRole" })
        private final Reference collector;
        @ReferenceTarget({ "Patient", "Organization" })
        private final Reference source;
        @Choice({ DateTime.class, Period.class })
        private final Element collected;

        private volatile int hashCode;

        private Collection(Builder builder) {
            super(builder);
            collector = builder.collector;
            source = builder.source;
            collected = ValidationSupport.choiceElement(builder.collected, "collected", DateTime.class, Period.class);
            ValidationSupport.checkReferenceType(collector, "collector", "Practitioner", "PractitionerRole");
            ValidationSupport.checkReferenceType(source, "source", "Patient", "Organization");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Healthcare professional who is performing the collection.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getCollector() {
            return collector;
        }

        /**
         * The patient or entity, such as a hospital or vendor in the case of a processed/manipulated/manufactured product, 
         * providing the product.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getSource() {
            return source;
        }

        /**
         * Time of product collection.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getCollected() {
            return collected;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (collector != null) || 
                (source != null) || 
                (collected != null);
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
                    accept(collector, "collector", visitor);
                    accept(source, "source", visitor);
                    accept(collected, "collected", visitor);
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
            Collection other = (Collection) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(collector, other.collector) && 
                Objects.equals(source, other.source) && 
                Objects.equals(collected, other.collected);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    collector, 
                    source, 
                    collected);
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
            private Reference collector;
            private Reference source;
            private Element collected;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Healthcare professional who is performing the collection.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Practitioner}</li>
             * <li>{@link PractitionerRole}</li>
             * </ul>
             * 
             * @param collector
             *     Individual performing collection
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder collector(Reference collector) {
                this.collector = collector;
                return this;
            }

            /**
             * The patient or entity, such as a hospital or vendor in the case of a processed/manipulated/manufactured product, 
             * providing the product.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Patient}</li>
             * <li>{@link Organization}</li>
             * </ul>
             * 
             * @param source
             *     Who is product from
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference source) {
                this.source = source;
                return this;
            }

            /**
             * Time of product collection.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param collected
             *     Time of product collection
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder collected(Element collected) {
                this.collected = collected;
                return this;
            }

            /**
             * Build the {@link Collection}
             * 
             * @return
             *     An immutable object of type {@link Collection}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Collection per the base specification
             */
            @Override
            public Collection build() {
                return new Collection(this);
            }

            protected Builder from(Collection collection) {
                super.from(collection);
                collector = collection.collector;
                source = collection.source;
                collected = collection.collected;
                return this;
            }
        }
    }

    /**
     * Any processing of the product during collection that does not change the fundamental nature of the product. For 
     * example adding anti-coagulants during the collection of Peripheral Blood Stem Cells.
     */
    public static class Processing extends BackboneElement {
        private final String description;
        @Binding(
            bindingName = "BiologicallyDerivedProductProcedure",
            strength = BindingStrength.ValueSet.EXAMPLE,
            description = "Biologically Derived Product Procedure.",
            valueSet = "http://hl7.org/fhir/ValueSet/procedure-code"
        )
        private final CodeableConcept procedure;
        @ReferenceTarget({ "Substance" })
        private final Reference additive;
        @Choice({ DateTime.class, Period.class })
        private final Element time;

        private volatile int hashCode;

        private Processing(Builder builder) {
            super(builder);
            description = builder.description;
            procedure = builder.procedure;
            additive = builder.additive;
            time = ValidationSupport.choiceElement(builder.time, "time", DateTime.class, Period.class);
            ValidationSupport.checkReferenceType(additive, "additive", "Substance");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Description of of processing.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Procesing code.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getProcedure() {
            return procedure;
        }

        /**
         * Substance added during processing.
         * 
         * @return
         *     An immutable object of type {@link Reference} that may be null.
         */
        public Reference getAdditive() {
            return additive;
        }

        /**
         * Time of processing.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getTime() {
            return time;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (procedure != null) || 
                (additive != null) || 
                (time != null);
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
                    accept(description, "description", visitor);
                    accept(procedure, "procedure", visitor);
                    accept(additive, "additive", visitor);
                    accept(time, "time", visitor);
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
            Processing other = (Processing) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(procedure, other.procedure) && 
                Objects.equals(additive, other.additive) && 
                Objects.equals(time, other.time);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    procedure, 
                    additive, 
                    time);
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
            private String description;
            private CodeableConcept procedure;
            private Reference additive;
            private Element time;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Description of of processing.
             * 
             * @param description
             *     Description of of processing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Procesing code.
             * 
             * @param procedure
             *     Procesing code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder procedure(CodeableConcept procedure) {
                this.procedure = procedure;
                return this;
            }

            /**
             * Substance added during processing.
             * 
             * <p>Allowed resource types for this reference:
             * <ul>
             * <li>{@link Substance}</li>
             * </ul>
             * 
             * @param additive
             *     Substance added during processing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder additive(Reference additive) {
                this.additive = additive;
                return this;
            }

            /**
             * Time of processing.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param time
             *     Time of processing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder time(Element time) {
                this.time = time;
                return this;
            }

            /**
             * Build the {@link Processing}
             * 
             * @return
             *     An immutable object of type {@link Processing}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Processing per the base specification
             */
            @Override
            public Processing build() {
                return new Processing(this);
            }

            protected Builder from(Processing processing) {
                super.from(processing);
                description = processing.description;
                procedure = processing.procedure;
                additive = processing.additive;
                time = processing.time;
                return this;
            }
        }
    }

    /**
     * Any manipulation of product post-collection that is intended to alter the product. For example a buffy-coat enrichment 
     * or CD8 reduction of Peripheral Blood Stem Cells to make it more suitable for infusion.
     */
    public static class Manipulation extends BackboneElement {
        private final String description;
        @Choice({ DateTime.class, Period.class })
        private final Element time;

        private volatile int hashCode;

        private Manipulation(Builder builder) {
            super(builder);
            description = builder.description;
            time = ValidationSupport.choiceElement(builder.time, "time", DateTime.class, Period.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Description of manipulation.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Time of manipulation.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getTime() {
            return time;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (time != null);
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
                    accept(description, "description", visitor);
                    accept(time, "time", visitor);
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
            Manipulation other = (Manipulation) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(time, other.time);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    time);
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
            private String description;
            private Element time;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Description of manipulation.
             * 
             * @param description
             *     Description of manipulation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Time of manipulation.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link DateTime}</li>
             * <li>{@link Period}</li>
             * </ul>
             * 
             * @param time
             *     Time of manipulation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder time(Element time) {
                this.time = time;
                return this;
            }

            /**
             * Build the {@link Manipulation}
             * 
             * @return
             *     An immutable object of type {@link Manipulation}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Manipulation per the base specification
             */
            @Override
            public Manipulation build() {
                return new Manipulation(this);
            }

            protected Builder from(Manipulation manipulation) {
                super.from(manipulation);
                description = manipulation.description;
                time = manipulation.time;
                return this;
            }
        }
    }

    /**
     * Product storage.
     */
    public static class Storage extends BackboneElement {
        private final String description;
        private final Decimal temperature;
        @Binding(
            bindingName = "BiologicallyDerivedProductStorageScale",
            strength = BindingStrength.ValueSet.REQUIRED,
            description = "BiologicallyDerived Product Storage Scale.",
            valueSet = "http://hl7.org/fhir/ValueSet/product-storage-scale|4.0.1"
        )
        private final BiologicallyDerivedProductStorageScale scale;
        private final Period duration;

        private volatile int hashCode;

        private Storage(Builder builder) {
            super(builder);
            description = builder.description;
            temperature = builder.temperature;
            scale = builder.scale;
            duration = builder.duration;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Description of storage.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getDescription() {
            return description;
        }

        /**
         * Storage temperature.
         * 
         * @return
         *     An immutable object of type {@link Decimal} that may be null.
         */
        public Decimal getTemperature() {
            return temperature;
        }

        /**
         * Temperature scale used.
         * 
         * @return
         *     An immutable object of type {@link BiologicallyDerivedProductStorageScale} that may be null.
         */
        public BiologicallyDerivedProductStorageScale getScale() {
            return scale;
        }

        /**
         * Storage timeperiod.
         * 
         * @return
         *     An immutable object of type {@link Period} that may be null.
         */
        public Period getDuration() {
            return duration;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (description != null) || 
                (temperature != null) || 
                (scale != null) || 
                (duration != null);
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
                    accept(description, "description", visitor);
                    accept(temperature, "temperature", visitor);
                    accept(scale, "scale", visitor);
                    accept(duration, "duration", visitor);
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
            Storage other = (Storage) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(description, other.description) && 
                Objects.equals(temperature, other.temperature) && 
                Objects.equals(scale, other.scale) && 
                Objects.equals(duration, other.duration);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    description, 
                    temperature, 
                    scale, 
                    duration);
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
            private String description;
            private Decimal temperature;
            private BiologicallyDerivedProductStorageScale scale;
            private Period duration;

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
            public Builder extension(java.util.Collection<Extension> extension) {
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
            public Builder modifierExtension(java.util.Collection<Extension> modifierExtension) {
                return (Builder) super.modifierExtension(modifierExtension);
            }

            /**
             * Description of storage.
             * 
             * @param description
             *     Description of storage
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder description(String description) {
                this.description = description;
                return this;
            }

            /**
             * Storage temperature.
             * 
             * @param temperature
             *     Storage temperature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder temperature(Decimal temperature) {
                this.temperature = temperature;
                return this;
            }

            /**
             * Temperature scale used.
             * 
             * @param scale
             *     farenheit | celsius | kelvin
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder scale(BiologicallyDerivedProductStorageScale scale) {
                this.scale = scale;
                return this;
            }

            /**
             * Storage timeperiod.
             * 
             * @param duration
             *     Storage timeperiod
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder duration(Period duration) {
                this.duration = duration;
                return this;
            }

            /**
             * Build the {@link Storage}
             * 
             * @return
             *     An immutable object of type {@link Storage}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Storage per the base specification
             */
            @Override
            public Storage build() {
                return new Storage(this);
            }

            protected Builder from(Storage storage) {
                super.from(storage);
                description = storage.description;
                temperature = storage.temperature;
                scale = storage.scale;
                duration = storage.duration;
                return this;
            }
        }
    }
}
