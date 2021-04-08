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

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.Attachment;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Boolean;
import com.ibm.fhir.model.type.CodeableConcept;
import com.ibm.fhir.model.type.DateTime;
import com.ibm.fhir.model.type.Element;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Narrative;
import com.ibm.fhir.model.type.Quantity;
import com.ibm.fhir.model.type.Range;
import com.ibm.fhir.model.type.Ratio;
import com.ibm.fhir.model.type.Reference;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * The detailed description of a substance, typically at a level beyond what is used for prescribing.
 * 
 * <p>Maturity level: FMM0 (Trial Use)
 */
@Maturity(
    level = 0,
    status = StandardsStatus.ValueSet.TRIAL_USE
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class SubstanceSpecification extends DomainResource {
    @Summary
    private final Identifier identifier;
    @Summary
    private final CodeableConcept type;
    @Summary
    private final CodeableConcept status;
    @Summary
    private final CodeableConcept domain;
    @Summary
    private final String description;
    @Summary
    @ReferenceTarget({ "DocumentReference" })
    private final List<Reference> source;
    @Summary
    private final String comment;
    @Summary
    private final List<Moiety> moiety;
    @Summary
    private final List<Property> property;
    @Summary
    @ReferenceTarget({ "SubstanceReferenceInformation" })
    private final Reference referenceInformation;
    @Summary
    private final Structure structure;
    @Summary
    private final List<Code> code;
    @Summary
    private final List<Name> name;
    @Summary
    private final List<SubstanceSpecification.Structure.Isotope.MolecularWeight> molecularWeight;
    @Summary
    private final List<Relationship> relationship;
    @Summary
    @ReferenceTarget({ "SubstanceNucleicAcid" })
    private final Reference nucleicAcid;
    @Summary
    @ReferenceTarget({ "SubstancePolymer" })
    private final Reference polymer;
    @Summary
    @ReferenceTarget({ "SubstanceProtein" })
    private final Reference protein;
    @Summary
    @ReferenceTarget({ "SubstanceSourceMaterial" })
    private final Reference sourceMaterial;

    private volatile int hashCode;

    private SubstanceSpecification(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        type = builder.type;
        status = builder.status;
        domain = builder.domain;
        description = builder.description;
        source = Collections.unmodifiableList(ValidationSupport.checkList(builder.source, "source", Reference.class));
        comment = builder.comment;
        moiety = Collections.unmodifiableList(ValidationSupport.checkList(builder.moiety, "moiety", Moiety.class));
        property = Collections.unmodifiableList(ValidationSupport.checkList(builder.property, "property", Property.class));
        referenceInformation = builder.referenceInformation;
        structure = builder.structure;
        code = Collections.unmodifiableList(ValidationSupport.checkList(builder.code, "code", Code.class));
        name = Collections.unmodifiableList(ValidationSupport.checkList(builder.name, "name", Name.class));
        molecularWeight = Collections.unmodifiableList(ValidationSupport.checkList(builder.molecularWeight, "molecularWeight", SubstanceSpecification.Structure.Isotope.MolecularWeight.class));
        relationship = Collections.unmodifiableList(ValidationSupport.checkList(builder.relationship, "relationship", Relationship.class));
        nucleicAcid = builder.nucleicAcid;
        polymer = builder.polymer;
        protein = builder.protein;
        sourceMaterial = builder.sourceMaterial;
        ValidationSupport.checkReferenceType(source, "source", "DocumentReference");
        ValidationSupport.checkReferenceType(referenceInformation, "referenceInformation", "SubstanceReferenceInformation");
        ValidationSupport.checkReferenceType(nucleicAcid, "nucleicAcid", "SubstanceNucleicAcid");
        ValidationSupport.checkReferenceType(polymer, "polymer", "SubstancePolymer");
        ValidationSupport.checkReferenceType(protein, "protein", "SubstanceProtein");
        ValidationSupport.checkReferenceType(sourceMaterial, "sourceMaterial", "SubstanceSourceMaterial");
        ValidationSupport.requireChildren(this);
    }

    /**
     * Identifier by which this substance is known.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * High level categorization, e.g. polymer or nucleic acid.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getType() {
        return type;
    }

    /**
     * Status of substance within the catalogue e.g. approved.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getStatus() {
        return status;
    }

    /**
     * If the substance applies to only human or veterinary use.
     * 
     * @return
     *     An immutable object of type {@link CodeableConcept} that may be null.
     */
    public CodeableConcept getDomain() {
        return domain;
    }

    /**
     * Textual description of the substance.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Supporting literature.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
     */
    public List<Reference> getSource() {
        return source;
    }

    /**
     * Textual comment about this record of a substance.
     * 
     * @return
     *     An immutable object of type {@link String} that may be null.
     */
    public String getComment() {
        return comment;
    }

    /**
     * Moiety, for structural modifications.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Moiety} that may be empty.
     */
    public List<Moiety> getMoiety() {
        return moiety;
    }

    /**
     * General specifications for this substance, including how it is related to other substances.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Property} that may be empty.
     */
    public List<Property> getProperty() {
        return property;
    }

    /**
     * General information detailing this substance.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getReferenceInformation() {
        return referenceInformation;
    }

    /**
     * Structural information.
     * 
     * @return
     *     An immutable object of type {@link Structure} that may be null.
     */
    public Structure getStructure() {
        return structure;
    }

    /**
     * Codes associated with the substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Code} that may be empty.
     */
    public List<Code> getCode() {
        return code;
    }

    /**
     * Names applicable to this substance.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Name} that may be empty.
     */
    public List<Name> getName() {
        return name;
    }

    /**
     * The molecular weight or weight range (for proteins, polymers or nucleic acids).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link MolecularWeight} that may be empty.
     */
    public List<SubstanceSpecification.Structure.Isotope.MolecularWeight> getMolecularWeight() {
        return molecularWeight;
    }

    /**
     * A link between this substance and another, with details of the relationship.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Relationship} that may be empty.
     */
    public List<Relationship> getRelationship() {
        return relationship;
    }

    /**
     * Data items specific to nucleic acids.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getNucleicAcid() {
        return nucleicAcid;
    }

    /**
     * Data items specific to polymers.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getPolymer() {
        return polymer;
    }

    /**
     * Data items specific to proteins.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getProtein() {
        return protein;
    }

    /**
     * Material or taxonomic/anatomical source for the substance.
     * 
     * @return
     *     An immutable object of type {@link Reference} that may be null.
     */
    public Reference getSourceMaterial() {
        return sourceMaterial;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (type != null) || 
            (status != null) || 
            (domain != null) || 
            (description != null) || 
            !source.isEmpty() || 
            (comment != null) || 
            !moiety.isEmpty() || 
            !property.isEmpty() || 
            (referenceInformation != null) || 
            (structure != null) || 
            !code.isEmpty() || 
            !name.isEmpty() || 
            !molecularWeight.isEmpty() || 
            !relationship.isEmpty() || 
            (nucleicAcid != null) || 
            (polymer != null) || 
            (protein != null) || 
            (sourceMaterial != null);
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
                accept(type, "type", visitor);
                accept(status, "status", visitor);
                accept(domain, "domain", visitor);
                accept(description, "description", visitor);
                accept(source, "source", visitor, Reference.class);
                accept(comment, "comment", visitor);
                accept(moiety, "moiety", visitor, Moiety.class);
                accept(property, "property", visitor, Property.class);
                accept(referenceInformation, "referenceInformation", visitor);
                accept(structure, "structure", visitor);
                accept(code, "code", visitor, Code.class);
                accept(name, "name", visitor, Name.class);
                accept(molecularWeight, "molecularWeight", visitor, SubstanceSpecification.Structure.Isotope.MolecularWeight.class);
                accept(relationship, "relationship", visitor, Relationship.class);
                accept(nucleicAcid, "nucleicAcid", visitor);
                accept(polymer, "polymer", visitor);
                accept(protein, "protein", visitor);
                accept(sourceMaterial, "sourceMaterial", visitor);
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
        SubstanceSpecification other = (SubstanceSpecification) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(text, other.text) && 
            Objects.equals(contained, other.contained) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(modifierExtension, other.modifierExtension) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(status, other.status) && 
            Objects.equals(domain, other.domain) && 
            Objects.equals(description, other.description) && 
            Objects.equals(source, other.source) && 
            Objects.equals(comment, other.comment) && 
            Objects.equals(moiety, other.moiety) && 
            Objects.equals(property, other.property) && 
            Objects.equals(referenceInformation, other.referenceInformation) && 
            Objects.equals(structure, other.structure) && 
            Objects.equals(code, other.code) && 
            Objects.equals(name, other.name) && 
            Objects.equals(molecularWeight, other.molecularWeight) && 
            Objects.equals(relationship, other.relationship) && 
            Objects.equals(nucleicAcid, other.nucleicAcid) && 
            Objects.equals(polymer, other.polymer) && 
            Objects.equals(protein, other.protein) && 
            Objects.equals(sourceMaterial, other.sourceMaterial);
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
                type, 
                status, 
                domain, 
                description, 
                source, 
                comment, 
                moiety, 
                property, 
                referenceInformation, 
                structure, 
                code, 
                name, 
                molecularWeight, 
                relationship, 
                nucleicAcid, 
                polymer, 
                protein, 
                sourceMaterial);
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
        private CodeableConcept type;
        private CodeableConcept status;
        private CodeableConcept domain;
        private String description;
        private List<Reference> source = new ArrayList<>();
        private String comment;
        private List<Moiety> moiety = new ArrayList<>();
        private List<Property> property = new ArrayList<>();
        private Reference referenceInformation;
        private Structure structure;
        private List<Code> code = new ArrayList<>();
        private List<Name> name = new ArrayList<>();
        private List<SubstanceSpecification.Structure.Isotope.MolecularWeight> molecularWeight = new ArrayList<>();
        private List<Relationship> relationship = new ArrayList<>();
        private Reference nucleicAcid;
        private Reference polymer;
        private Reference protein;
        private Reference sourceMaterial;

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
        public Builder language(com.ibm.fhir.model.type.Code language) {
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
         * Identifier by which this substance is known.
         * 
         * @param identifier
         *     Identifier by which this substance is known
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * High level categorization, e.g. polymer or nucleic acid.
         * 
         * @param type
         *     High level categorization, e.g. polymer or nucleic acid
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(CodeableConcept type) {
            this.type = type;
            return this;
        }

        /**
         * Status of substance within the catalogue e.g. approved.
         * 
         * @param status
         *     Status of substance within the catalogue e.g. approved
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder status(CodeableConcept status) {
            this.status = status;
            return this;
        }

        /**
         * If the substance applies to only human or veterinary use.
         * 
         * @param domain
         *     If the substance applies to only human or veterinary use
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder domain(CodeableConcept domain) {
            this.domain = domain;
            return this;
        }

        /**
         * Textual description of the substance.
         * 
         * @param description
         *     Textual description of the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder description(String description) {
            this.description = description;
            return this;
        }

        /**
         * Supporting literature.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param source
         *     Supporting literature
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Reference... source) {
            for (Reference value : source) {
                this.source.add(value);
            }
            return this;
        }

        /**
         * Supporting literature.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * <p>Allowed resource types for the references:
         * <ul>
         * <li>{@link DocumentReference}</li>
         * </ul>
         * 
         * @param source
         *     Supporting literature
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder source(Collection<Reference> source) {
            this.source = new ArrayList<>(source);
            return this;
        }

        /**
         * Textual comment about this record of a substance.
         * 
         * @param comment
         *     Textual comment about this record of a substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder comment(String comment) {
            this.comment = comment;
            return this;
        }

        /**
         * Moiety, for structural modifications.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param moiety
         *     Moiety, for structural modifications
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder moiety(Moiety... moiety) {
            for (Moiety value : moiety) {
                this.moiety.add(value);
            }
            return this;
        }

        /**
         * Moiety, for structural modifications.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param moiety
         *     Moiety, for structural modifications
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder moiety(Collection<Moiety> moiety) {
            this.moiety = new ArrayList<>(moiety);
            return this;
        }

        /**
         * General specifications for this substance, including how it is related to other substances.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param property
         *     General specifications for this substance, including how it is related to other substances
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder property(Property... property) {
            for (Property value : property) {
                this.property.add(value);
            }
            return this;
        }

        /**
         * General specifications for this substance, including how it is related to other substances.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param property
         *     General specifications for this substance, including how it is related to other substances
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder property(Collection<Property> property) {
            this.property = new ArrayList<>(property);
            return this;
        }

        /**
         * General information detailing this substance.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link SubstanceReferenceInformation}</li>
         * </ul>
         * 
         * @param referenceInformation
         *     General information detailing this substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder referenceInformation(Reference referenceInformation) {
            this.referenceInformation = referenceInformation;
            return this;
        }

        /**
         * Structural information.
         * 
         * @param structure
         *     Structural information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder structure(Structure structure) {
            this.structure = structure;
            return this;
        }

        /**
         * Codes associated with the substance.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param code
         *     Codes associated with the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Code... code) {
            for (Code value : code) {
                this.code.add(value);
            }
            return this;
        }

        /**
         * Codes associated with the substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param code
         *     Codes associated with the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder code(Collection<Code> code) {
            this.code = new ArrayList<>(code);
            return this;
        }

        /**
         * Names applicable to this substance.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param name
         *     Names applicable to this substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(Name... name) {
            for (Name value : name) {
                this.name.add(value);
            }
            return this;
        }

        /**
         * Names applicable to this substance.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param name
         *     Names applicable to this substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder name(Collection<Name> name) {
            this.name = new ArrayList<>(name);
            return this;
        }

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param molecularWeight
         *     The molecular weight or weight range (for proteins, polymers or nucleic acids)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder molecularWeight(SubstanceSpecification.Structure.Isotope.MolecularWeight... molecularWeight) {
            for (SubstanceSpecification.Structure.Isotope.MolecularWeight value : molecularWeight) {
                this.molecularWeight.add(value);
            }
            return this;
        }

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param molecularWeight
         *     The molecular weight or weight range (for proteins, polymers or nucleic acids)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder molecularWeight(Collection<SubstanceSpecification.Structure.Isotope.MolecularWeight> molecularWeight) {
            this.molecularWeight = new ArrayList<>(molecularWeight);
            return this;
        }

        /**
         * A link between this substance and another, with details of the relationship.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param relationship
         *     A link between this substance and another, with details of the relationship
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relationship(Relationship... relationship) {
            for (Relationship value : relationship) {
                this.relationship.add(value);
            }
            return this;
        }

        /**
         * A link between this substance and another, with details of the relationship.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param relationship
         *     A link between this substance and another, with details of the relationship
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder relationship(Collection<Relationship> relationship) {
            this.relationship = new ArrayList<>(relationship);
            return this;
        }

        /**
         * Data items specific to nucleic acids.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link SubstanceNucleicAcid}</li>
         * </ul>
         * 
         * @param nucleicAcid
         *     Data items specific to nucleic acids
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder nucleicAcid(Reference nucleicAcid) {
            this.nucleicAcid = nucleicAcid;
            return this;
        }

        /**
         * Data items specific to polymers.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link SubstancePolymer}</li>
         * </ul>
         * 
         * @param polymer
         *     Data items specific to polymers
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder polymer(Reference polymer) {
            this.polymer = polymer;
            return this;
        }

        /**
         * Data items specific to proteins.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link SubstanceProtein}</li>
         * </ul>
         * 
         * @param protein
         *     Data items specific to proteins
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder protein(Reference protein) {
            this.protein = protein;
            return this;
        }

        /**
         * Material or taxonomic/anatomical source for the substance.
         * 
         * <p>Allowed resource types for this reference:
         * <ul>
         * <li>{@link SubstanceSourceMaterial}</li>
         * </ul>
         * 
         * @param sourceMaterial
         *     Material or taxonomic/anatomical source for the substance
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder sourceMaterial(Reference sourceMaterial) {
            this.sourceMaterial = sourceMaterial;
            return this;
        }

        /**
         * Build the {@link SubstanceSpecification}
         * 
         * @return
         *     An immutable object of type {@link SubstanceSpecification}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid SubstanceSpecification per the base specification
         */
        @Override
        public SubstanceSpecification build() {
            return new SubstanceSpecification(this);
        }

        protected Builder from(SubstanceSpecification substanceSpecification) {
            super.from(substanceSpecification);
            identifier = substanceSpecification.identifier;
            type = substanceSpecification.type;
            status = substanceSpecification.status;
            domain = substanceSpecification.domain;
            description = substanceSpecification.description;
            source.addAll(substanceSpecification.source);
            comment = substanceSpecification.comment;
            moiety.addAll(substanceSpecification.moiety);
            property.addAll(substanceSpecification.property);
            referenceInformation = substanceSpecification.referenceInformation;
            structure = substanceSpecification.structure;
            code.addAll(substanceSpecification.code);
            name.addAll(substanceSpecification.name);
            molecularWeight.addAll(substanceSpecification.molecularWeight);
            relationship.addAll(substanceSpecification.relationship);
            nucleicAcid = substanceSpecification.nucleicAcid;
            polymer = substanceSpecification.polymer;
            protein = substanceSpecification.protein;
            sourceMaterial = substanceSpecification.sourceMaterial;
            return this;
        }
    }

    /**
     * Moiety, for structural modifications.
     */
    public static class Moiety extends BackboneElement {
        @Summary
        private final CodeableConcept role;
        @Summary
        private final Identifier identifier;
        @Summary
        private final String name;
        @Summary
        private final CodeableConcept stereochemistry;
        @Summary
        private final CodeableConcept opticalActivity;
        @Summary
        private final String molecularFormula;
        @Summary
        @Choice({ Quantity.class, String.class })
        private final Element amount;

        private volatile int hashCode;

        private Moiety(Builder builder) {
            super(builder);
            role = builder.role;
            identifier = builder.identifier;
            name = builder.name;
            stereochemistry = builder.stereochemistry;
            opticalActivity = builder.opticalActivity;
            molecularFormula = builder.molecularFormula;
            amount = ValidationSupport.choiceElement(builder.amount, "amount", Quantity.class, String.class);
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Role that the moiety is playing.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRole() {
            return role;
        }

        /**
         * Identifier by which this moiety substance is known.
         * 
         * @return
         *     An immutable object of type {@link Identifier} that may be null.
         */
        public Identifier getIdentifier() {
            return identifier;
        }

        /**
         * Textual name for this moiety substance.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getName() {
            return name;
        }

        /**
         * Stereochemistry type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStereochemistry() {
            return stereochemistry;
        }

        /**
         * Optical activity type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOpticalActivity() {
            return opticalActivity;
        }

        /**
         * Molecular formula.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getMolecularFormula() {
            return molecularFormula;
        }

        /**
         * Quantitative value for this moiety.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (role != null) || 
                (identifier != null) || 
                (name != null) || 
                (stereochemistry != null) || 
                (opticalActivity != null) || 
                (molecularFormula != null) || 
                (amount != null);
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
                    accept(role, "role", visitor);
                    accept(identifier, "identifier", visitor);
                    accept(name, "name", visitor);
                    accept(stereochemistry, "stereochemistry", visitor);
                    accept(opticalActivity, "opticalActivity", visitor);
                    accept(molecularFormula, "molecularFormula", visitor);
                    accept(amount, "amount", visitor);
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
            Moiety other = (Moiety) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(role, other.role) && 
                Objects.equals(identifier, other.identifier) && 
                Objects.equals(name, other.name) && 
                Objects.equals(stereochemistry, other.stereochemistry) && 
                Objects.equals(opticalActivity, other.opticalActivity) && 
                Objects.equals(molecularFormula, other.molecularFormula) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    role, 
                    identifier, 
                    name, 
                    stereochemistry, 
                    opticalActivity, 
                    molecularFormula, 
                    amount);
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
            private CodeableConcept role;
            private Identifier identifier;
            private String name;
            private CodeableConcept stereochemistry;
            private CodeableConcept opticalActivity;
            private String molecularFormula;
            private Element amount;

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
             * Role that the moiety is playing.
             * 
             * @param role
             *     Role that the moiety is playing
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder role(CodeableConcept role) {
                this.role = role;
                return this;
            }

            /**
             * Identifier by which this moiety substance is known.
             * 
             * @param identifier
             *     Identifier by which this moiety substance is known
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder identifier(Identifier identifier) {
                this.identifier = identifier;
                return this;
            }

            /**
             * Textual name for this moiety substance.
             * 
             * @param name
             *     Textual name for this moiety substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Stereochemistry type.
             * 
             * @param stereochemistry
             *     Stereochemistry type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder stereochemistry(CodeableConcept stereochemistry) {
                this.stereochemistry = stereochemistry;
                return this;
            }

            /**
             * Optical activity type.
             * 
             * @param opticalActivity
             *     Optical activity type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder opticalActivity(CodeableConcept opticalActivity) {
                this.opticalActivity = opticalActivity;
                return this;
            }

            /**
             * Molecular formula.
             * 
             * @param molecularFormula
             *     Molecular formula
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularFormula(String molecularFormula) {
                this.molecularFormula = molecularFormula;
                return this;
            }

            /**
             * Quantitative value for this moiety.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param amount
             *     Quantitative value for this moiety
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Element amount) {
                this.amount = amount;
                return this;
            }

            /**
             * Build the {@link Moiety}
             * 
             * @return
             *     An immutable object of type {@link Moiety}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Moiety per the base specification
             */
            @Override
            public Moiety build() {
                return new Moiety(this);
            }

            protected Builder from(Moiety moiety) {
                super.from(moiety);
                role = moiety.role;
                identifier = moiety.identifier;
                name = moiety.name;
                stereochemistry = moiety.stereochemistry;
                opticalActivity = moiety.opticalActivity;
                molecularFormula = moiety.molecularFormula;
                amount = moiety.amount;
                return this;
            }
        }
    }

    /**
     * General specifications for this substance, including how it is related to other substances.
     */
    public static class Property extends BackboneElement {
        @Summary
        private final CodeableConcept category;
        @Summary
        private final CodeableConcept code;
        @Summary
        private final String parameters;
        @Summary
        @ReferenceTarget({ "SubstanceSpecification", "Substance" })
        @Choice({ Reference.class, CodeableConcept.class })
        private final Element definingSubstance;
        @Summary
        @Choice({ Quantity.class, String.class })
        private final Element amount;

        private volatile int hashCode;

        private Property(Builder builder) {
            super(builder);
            category = builder.category;
            code = builder.code;
            parameters = builder.parameters;
            definingSubstance = ValidationSupport.choiceElement(builder.definingSubstance, "definingSubstance", Reference.class, CodeableConcept.class);
            amount = ValidationSupport.choiceElement(builder.amount, "amount", Quantity.class, String.class);
            ValidationSupport.checkReferenceType(definingSubstance, "definingSubstance", "SubstanceSpecification", "Substance");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * A category for this property, e.g. Physical, Chemical, Enzymatic.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCategory() {
            return category;
        }

        /**
         * Property type e.g. viscosity, pH, isoelectric point.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getParameters() {
            return parameters;
        }

        /**
         * A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getDefiningSubstance() {
            return definingSubstance;
        }

        /**
         * Quantitative value for this property.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getAmount() {
            return amount;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (category != null) || 
                (code != null) || 
                (parameters != null) || 
                (definingSubstance != null) || 
                (amount != null);
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
                    accept(category, "category", visitor);
                    accept(code, "code", visitor);
                    accept(parameters, "parameters", visitor);
                    accept(definingSubstance, "definingSubstance", visitor);
                    accept(amount, "amount", visitor);
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
            Property other = (Property) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(category, other.category) && 
                Objects.equals(code, other.code) && 
                Objects.equals(parameters, other.parameters) && 
                Objects.equals(definingSubstance, other.definingSubstance) && 
                Objects.equals(amount, other.amount);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    category, 
                    code, 
                    parameters, 
                    definingSubstance, 
                    amount);
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
            private CodeableConcept category;
            private CodeableConcept code;
            private String parameters;
            private Element definingSubstance;
            private Element amount;

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
             * A category for this property, e.g. Physical, Chemical, Enzymatic.
             * 
             * @param category
             *     A category for this property, e.g. Physical, Chemical, Enzymatic
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder category(CodeableConcept category) {
                this.category = category;
                return this;
            }

            /**
             * Property type e.g. viscosity, pH, isoelectric point.
             * 
             * @param code
             *     Property type e.g. viscosity, pH, isoelectric point
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1).
             * 
             * @param parameters
             *     Parameters that were used in the measurement of a property (e.g. for viscosity: measured at 20C with a pH of 7.1)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder parameters(String parameters) {
                this.parameters = parameters;
                return this;
            }

            /**
             * A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol).
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Reference}</li>
             * <li>{@link CodeableConcept}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link SubstanceSpecification}</li>
             * <li>{@link Substance}</li>
             * </ul>
             * 
             * @param definingSubstance
             *     A substance upon which a defining property depends (e.g. for solubility: in water, in alcohol)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder definingSubstance(Element definingSubstance) {
                this.definingSubstance = definingSubstance;
                return this;
            }

            /**
             * Quantitative value for this property.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param amount
             *     Quantitative value for this property
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Element amount) {
                this.amount = amount;
                return this;
            }

            /**
             * Build the {@link Property}
             * 
             * @return
             *     An immutable object of type {@link Property}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Property per the base specification
             */
            @Override
            public Property build() {
                return new Property(this);
            }

            protected Builder from(Property property) {
                super.from(property);
                category = property.category;
                code = property.code;
                parameters = property.parameters;
                definingSubstance = property.definingSubstance;
                amount = property.amount;
                return this;
            }
        }
    }

    /**
     * Structural information.
     */
    public static class Structure extends BackboneElement {
        @Summary
        private final CodeableConcept stereochemistry;
        @Summary
        private final CodeableConcept opticalActivity;
        @Summary
        private final String molecularFormula;
        @Summary
        private final String molecularFormulaByMoiety;
        @Summary
        private final List<Isotope> isotope;
        @Summary
        private final SubstanceSpecification.Structure.Isotope.MolecularWeight molecularWeight;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;
        @Summary
        private final List<Representation> representation;

        private volatile int hashCode;

        private Structure(Builder builder) {
            super(builder);
            stereochemistry = builder.stereochemistry;
            opticalActivity = builder.opticalActivity;
            molecularFormula = builder.molecularFormula;
            molecularFormulaByMoiety = builder.molecularFormulaByMoiety;
            isotope = Collections.unmodifiableList(ValidationSupport.checkList(builder.isotope, "isotope", Isotope.class));
            molecularWeight = builder.molecularWeight;
            source = Collections.unmodifiableList(ValidationSupport.checkList(builder.source, "source", Reference.class));
            representation = Collections.unmodifiableList(ValidationSupport.checkList(builder.representation, "representation", Representation.class));
            ValidationSupport.checkReferenceType(source, "source", "DocumentReference");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * Stereochemistry type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStereochemistry() {
            return stereochemistry;
        }

        /**
         * Optical activity type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getOpticalActivity() {
            return opticalActivity;
        }

        /**
         * Molecular formula.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getMolecularFormula() {
            return molecularFormula;
        }

        /**
         * Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a 
         * dot.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getMolecularFormulaByMoiety() {
            return molecularFormulaByMoiety;
        }

        /**
         * Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Isotope} that may be empty.
         */
        public List<Isotope> getIsotope() {
            return isotope;
        }

        /**
         * The molecular weight or weight range (for proteins, polymers or nucleic acids).
         * 
         * @return
         *     An immutable object of type {@link SubstanceSpecification.Structure.Isotope.MolecularWeight} that may be null.
         */
        public SubstanceSpecification.Structure.Isotope.MolecularWeight getMolecularWeight() {
            return molecularWeight;
        }

        /**
         * Supporting literature.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSource() {
            return source;
        }

        /**
         * Molecular structural representation.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Representation} that may be empty.
         */
        public List<Representation> getRepresentation() {
            return representation;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (stereochemistry != null) || 
                (opticalActivity != null) || 
                (molecularFormula != null) || 
                (molecularFormulaByMoiety != null) || 
                !isotope.isEmpty() || 
                (molecularWeight != null) || 
                !source.isEmpty() || 
                !representation.isEmpty();
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
                    accept(stereochemistry, "stereochemistry", visitor);
                    accept(opticalActivity, "opticalActivity", visitor);
                    accept(molecularFormula, "molecularFormula", visitor);
                    accept(molecularFormulaByMoiety, "molecularFormulaByMoiety", visitor);
                    accept(isotope, "isotope", visitor, Isotope.class);
                    accept(molecularWeight, "molecularWeight", visitor);
                    accept(source, "source", visitor, Reference.class);
                    accept(representation, "representation", visitor, Representation.class);
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
            Structure other = (Structure) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(stereochemistry, other.stereochemistry) && 
                Objects.equals(opticalActivity, other.opticalActivity) && 
                Objects.equals(molecularFormula, other.molecularFormula) && 
                Objects.equals(molecularFormulaByMoiety, other.molecularFormulaByMoiety) && 
                Objects.equals(isotope, other.isotope) && 
                Objects.equals(molecularWeight, other.molecularWeight) && 
                Objects.equals(source, other.source) && 
                Objects.equals(representation, other.representation);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    stereochemistry, 
                    opticalActivity, 
                    molecularFormula, 
                    molecularFormulaByMoiety, 
                    isotope, 
                    molecularWeight, 
                    source, 
                    representation);
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
            private CodeableConcept stereochemistry;
            private CodeableConcept opticalActivity;
            private String molecularFormula;
            private String molecularFormulaByMoiety;
            private List<Isotope> isotope = new ArrayList<>();
            private SubstanceSpecification.Structure.Isotope.MolecularWeight molecularWeight;
            private List<Reference> source = new ArrayList<>();
            private List<Representation> representation = new ArrayList<>();

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
             * Stereochemistry type.
             * 
             * @param stereochemistry
             *     Stereochemistry type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder stereochemistry(CodeableConcept stereochemistry) {
                this.stereochemistry = stereochemistry;
                return this;
            }

            /**
             * Optical activity type.
             * 
             * @param opticalActivity
             *     Optical activity type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder opticalActivity(CodeableConcept opticalActivity) {
                this.opticalActivity = opticalActivity;
                return this;
            }

            /**
             * Molecular formula.
             * 
             * @param molecularFormula
             *     Molecular formula
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularFormula(String molecularFormula) {
                this.molecularFormula = molecularFormula;
                return this;
            }

            /**
             * Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a 
             * dot.
             * 
             * @param molecularFormulaByMoiety
             *     Specified per moiety according to the Hill system, i.e. first C, then H, then alphabetical, each moiety separated by a 
             *     dot
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularFormulaByMoiety(String molecularFormulaByMoiety) {
                this.molecularFormulaByMoiety = molecularFormulaByMoiety;
                return this;
            }

            /**
             * Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param isotope
             *     Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder isotope(Isotope... isotope) {
                for (Isotope value : isotope) {
                    this.isotope.add(value);
                }
                return this;
            }

            /**
             * Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param isotope
             *     Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder isotope(Collection<Isotope> isotope) {
                this.isotope = new ArrayList<>(isotope);
                return this;
            }

            /**
             * The molecular weight or weight range (for proteins, polymers or nucleic acids).
             * 
             * @param molecularWeight
             *     The molecular weight or weight range (for proteins, polymers or nucleic acids)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder molecularWeight(SubstanceSpecification.Structure.Isotope.MolecularWeight molecularWeight) {
                this.molecularWeight = molecularWeight;
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference... source) {
                for (Reference value : source) {
                    this.source.add(value);
                }
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            /**
             * Molecular structural representation.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param representation
             *     Molecular structural representation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder representation(Representation... representation) {
                for (Representation value : representation) {
                    this.representation.add(value);
                }
                return this;
            }

            /**
             * Molecular structural representation.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param representation
             *     Molecular structural representation
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder representation(Collection<Representation> representation) {
                this.representation = new ArrayList<>(representation);
                return this;
            }

            /**
             * Build the {@link Structure}
             * 
             * @return
             *     An immutable object of type {@link Structure}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Structure per the base specification
             */
            @Override
            public Structure build() {
                return new Structure(this);
            }

            protected Builder from(Structure structure) {
                super.from(structure);
                stereochemistry = structure.stereochemistry;
                opticalActivity = structure.opticalActivity;
                molecularFormula = structure.molecularFormula;
                molecularFormulaByMoiety = structure.molecularFormulaByMoiety;
                isotope.addAll(structure.isotope);
                molecularWeight = structure.molecularWeight;
                source.addAll(structure.source);
                representation.addAll(structure.representation);
                return this;
            }
        }

        /**
         * Applicable for single substances that contain a radionuclide or a non-natural isotopic ratio.
         */
        public static class Isotope extends BackboneElement {
            @Summary
            private final Identifier identifier;
            @Summary
            private final CodeableConcept name;
            @Summary
            private final CodeableConcept substitution;
            @Summary
            private final Quantity halfLife;
            @Summary
            private final MolecularWeight molecularWeight;

            private volatile int hashCode;

            private Isotope(Builder builder) {
                super(builder);
                identifier = builder.identifier;
                name = builder.name;
                substitution = builder.substitution;
                halfLife = builder.halfLife;
                molecularWeight = builder.molecularWeight;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * Substance identifier for each non-natural or radioisotope.
             * 
             * @return
             *     An immutable object of type {@link Identifier} that may be null.
             */
            public Identifier getIdentifier() {
                return identifier;
            }

            /**
             * Substance name for each non-natural or radioisotope.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getName() {
                return name;
            }

            /**
             * The type of isotopic substitution present in a single substance.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getSubstitution() {
                return substitution;
            }

            /**
             * Half life - for a non-natural nuclide.
             * 
             * @return
             *     An immutable object of type {@link Quantity} that may be null.
             */
            public Quantity getHalfLife() {
                return halfLife;
            }

            /**
             * The molecular weight or weight range (for proteins, polymers or nucleic acids).
             * 
             * @return
             *     An immutable object of type {@link MolecularWeight} that may be null.
             */
            public MolecularWeight getMolecularWeight() {
                return molecularWeight;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (identifier != null) || 
                    (name != null) || 
                    (substitution != null) || 
                    (halfLife != null) || 
                    (molecularWeight != null);
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
                        accept(name, "name", visitor);
                        accept(substitution, "substitution", visitor);
                        accept(halfLife, "halfLife", visitor);
                        accept(molecularWeight, "molecularWeight", visitor);
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
                Isotope other = (Isotope) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(identifier, other.identifier) && 
                    Objects.equals(name, other.name) && 
                    Objects.equals(substitution, other.substitution) && 
                    Objects.equals(halfLife, other.halfLife) && 
                    Objects.equals(molecularWeight, other.molecularWeight);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        identifier, 
                        name, 
                        substitution, 
                        halfLife, 
                        molecularWeight);
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
                private CodeableConcept name;
                private CodeableConcept substitution;
                private Quantity halfLife;
                private MolecularWeight molecularWeight;

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
                 * Substance identifier for each non-natural or radioisotope.
                 * 
                 * @param identifier
                 *     Substance identifier for each non-natural or radioisotope
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder identifier(Identifier identifier) {
                    this.identifier = identifier;
                    return this;
                }

                /**
                 * Substance name for each non-natural or radioisotope.
                 * 
                 * @param name
                 *     Substance name for each non-natural or radioisotope
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder name(CodeableConcept name) {
                    this.name = name;
                    return this;
                }

                /**
                 * The type of isotopic substitution present in a single substance.
                 * 
                 * @param substitution
                 *     The type of isotopic substitution present in a single substance
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder substitution(CodeableConcept substitution) {
                    this.substitution = substitution;
                    return this;
                }

                /**
                 * Half life - for a non-natural nuclide.
                 * 
                 * @param halfLife
                 *     Half life - for a non-natural nuclide
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder halfLife(Quantity halfLife) {
                    this.halfLife = halfLife;
                    return this;
                }

                /**
                 * The molecular weight or weight range (for proteins, polymers or nucleic acids).
                 * 
                 * @param molecularWeight
                 *     The molecular weight or weight range (for proteins, polymers or nucleic acids)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder molecularWeight(MolecularWeight molecularWeight) {
                    this.molecularWeight = molecularWeight;
                    return this;
                }

                /**
                 * Build the {@link Isotope}
                 * 
                 * @return
                 *     An immutable object of type {@link Isotope}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Isotope per the base specification
                 */
                @Override
                public Isotope build() {
                    return new Isotope(this);
                }

                protected Builder from(Isotope isotope) {
                    super.from(isotope);
                    identifier = isotope.identifier;
                    name = isotope.name;
                    substitution = isotope.substitution;
                    halfLife = isotope.halfLife;
                    molecularWeight = isotope.molecularWeight;
                    return this;
                }
            }

            /**
             * The molecular weight or weight range (for proteins, polymers or nucleic acids).
             */
            public static class MolecularWeight extends BackboneElement {
                @Summary
                private final CodeableConcept method;
                @Summary
                private final CodeableConcept type;
                @Summary
                private final Quantity amount;

                private volatile int hashCode;

                private MolecularWeight(Builder builder) {
                    super(builder);
                    method = builder.method;
                    type = builder.type;
                    amount = builder.amount;
                    ValidationSupport.requireValueOrChildren(this);
                }

                /**
                 * The method by which the molecular weight was determined.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
                 */
                public CodeableConcept getMethod() {
                    return method;
                }

                /**
                 * Type of molecular weight such as exact, average (also known as. number average), weight average.
                 * 
                 * @return
                 *     An immutable object of type {@link CodeableConcept} that may be null.
                 */
                public CodeableConcept getType() {
                    return type;
                }

                /**
                 * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
                 * the average. If only a single definite value for a given element is given, it would be captured in this field.
                 * 
                 * @return
                 *     An immutable object of type {@link Quantity} that may be null.
                 */
                public Quantity getAmount() {
                    return amount;
                }

                @Override
                public boolean hasChildren() {
                    return super.hasChildren() || 
                        (method != null) || 
                        (type != null) || 
                        (amount != null);
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
                            accept(method, "method", visitor);
                            accept(type, "type", visitor);
                            accept(amount, "amount", visitor);
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
                    MolecularWeight other = (MolecularWeight) obj;
                    return Objects.equals(id, other.id) && 
                        Objects.equals(extension, other.extension) && 
                        Objects.equals(modifierExtension, other.modifierExtension) && 
                        Objects.equals(method, other.method) && 
                        Objects.equals(type, other.type) && 
                        Objects.equals(amount, other.amount);
                }

                @Override
                public int hashCode() {
                    int result = hashCode;
                    if (result == 0) {
                        result = Objects.hash(id, 
                            extension, 
                            modifierExtension, 
                            method, 
                            type, 
                            amount);
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
                    private CodeableConcept method;
                    private CodeableConcept type;
                    private Quantity amount;

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
                     * The method by which the molecular weight was determined.
                     * 
                     * @param method
                     *     The method by which the molecular weight was determined
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder method(CodeableConcept method) {
                        this.method = method;
                        return this;
                    }

                    /**
                     * Type of molecular weight such as exact, average (also known as. number average), weight average.
                     * 
                     * @param type
                     *     Type of molecular weight such as exact, average (also known as. number average), weight average
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder type(CodeableConcept type) {
                        this.type = type;
                        return this;
                    }

                    /**
                     * Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
                     * the average. If only a single definite value for a given element is given, it would be captured in this field.
                     * 
                     * @param amount
                     *     Used to capture quantitative values for a variety of elements. If only limits are given, the arithmetic mean would be 
                     *     the average. If only a single definite value for a given element is given, it would be captured in this field
                     * 
                     * @return
                     *     A reference to this Builder instance
                     */
                    public Builder amount(Quantity amount) {
                        this.amount = amount;
                        return this;
                    }

                    /**
                     * Build the {@link MolecularWeight}
                     * 
                     * @return
                     *     An immutable object of type {@link MolecularWeight}
                     * @throws IllegalStateException
                     *     if the current state cannot be built into a valid MolecularWeight per the base specification
                     */
                    @Override
                    public MolecularWeight build() {
                        return new MolecularWeight(this);
                    }

                    protected Builder from(MolecularWeight molecularWeight) {
                        super.from(molecularWeight);
                        method = molecularWeight.method;
                        type = molecularWeight.type;
                        amount = molecularWeight.amount;
                        return this;
                    }
                }
            }
        }

        /**
         * Molecular structural representation.
         */
        public static class Representation extends BackboneElement {
            @Summary
            private final CodeableConcept type;
            @Summary
            private final String representation;
            @Summary
            private final Attachment attachment;

            private volatile int hashCode;

            private Representation(Builder builder) {
                super(builder);
                type = builder.type;
                representation = builder.representation;
                attachment = builder.attachment;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * The type of structure (e.g. Full, Partial, Representative).
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getType() {
                return type;
            }

            /**
             * The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getRepresentation() {
                return representation;
            }

            /**
             * An attached file with the structural representation.
             * 
             * @return
             *     An immutable object of type {@link Attachment} that may be null.
             */
            public Attachment getAttachment() {
                return attachment;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (type != null) || 
                    (representation != null) || 
                    (attachment != null);
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
                        accept(type, "type", visitor);
                        accept(representation, "representation", visitor);
                        accept(attachment, "attachment", visitor);
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
                Representation other = (Representation) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(type, other.type) && 
                    Objects.equals(representation, other.representation) && 
                    Objects.equals(attachment, other.attachment);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        type, 
                        representation, 
                        attachment);
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
                private CodeableConcept type;
                private String representation;
                private Attachment attachment;

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
                 * The type of structure (e.g. Full, Partial, Representative).
                 * 
                 * @param type
                 *     The type of structure (e.g. Full, Partial, Representative)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder type(CodeableConcept type) {
                    this.type = type;
                    return this;
                }

                /**
                 * The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX.
                 * 
                 * @param representation
                 *     The structural representation as text string in a format e.g. InChI, SMILES, MOLFILE, CDX
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder representation(String representation) {
                    this.representation = representation;
                    return this;
                }

                /**
                 * An attached file with the structural representation.
                 * 
                 * @param attachment
                 *     An attached file with the structural representation
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder attachment(Attachment attachment) {
                    this.attachment = attachment;
                    return this;
                }

                /**
                 * Build the {@link Representation}
                 * 
                 * @return
                 *     An immutable object of type {@link Representation}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Representation per the base specification
                 */
                @Override
                public Representation build() {
                    return new Representation(this);
                }

                protected Builder from(Representation representation) {
                    super.from(representation);
                    type = representation.type;
                    this.representation = representation.representation;
                    attachment = representation.attachment;
                    return this;
                }
            }
        }
    }

    /**
     * Codes associated with the substance.
     */
    public static class Code extends BackboneElement {
        @Summary
        private final CodeableConcept code;
        @Summary
        private final CodeableConcept status;
        @Summary
        private final DateTime statusDate;
        @Summary
        private final String comment;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private volatile int hashCode;

        private Code(Builder builder) {
            super(builder);
            code = builder.code;
            status = builder.status;
            statusDate = builder.statusDate;
            comment = builder.comment;
            source = Collections.unmodifiableList(ValidationSupport.checkList(builder.source, "source", Reference.class));
            ValidationSupport.checkReferenceType(source, "source", "DocumentReference");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * The specific code.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getCode() {
            return code;
        }

        /**
         * Status of the code assignment.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStatus() {
            return status;
        }

        /**
         * The date at which the code status is changed as part of the terminology maintenance.
         * 
         * @return
         *     An immutable object of type {@link DateTime} that may be null.
         */
        public DateTime getStatusDate() {
            return statusDate;
        }

        /**
         * Any comment can be provided in this field, if necessary.
         * 
         * @return
         *     An immutable object of type {@link String} that may be null.
         */
        public String getComment() {
            return comment;
        }

        /**
         * Supporting literature.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (code != null) || 
                (status != null) || 
                (statusDate != null) || 
                (comment != null) || 
                !source.isEmpty();
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
                    accept(code, "code", visitor);
                    accept(status, "status", visitor);
                    accept(statusDate, "statusDate", visitor);
                    accept(comment, "comment", visitor);
                    accept(source, "source", visitor, Reference.class);
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
            Code other = (Code) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(code, other.code) && 
                Objects.equals(status, other.status) && 
                Objects.equals(statusDate, other.statusDate) && 
                Objects.equals(comment, other.comment) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    code, 
                    status, 
                    statusDate, 
                    comment, 
                    source);
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
            private CodeableConcept code;
            private CodeableConcept status;
            private DateTime statusDate;
            private String comment;
            private List<Reference> source = new ArrayList<>();

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
             * The specific code.
             * 
             * @param code
             *     The specific code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder code(CodeableConcept code) {
                this.code = code;
                return this;
            }

            /**
             * Status of the code assignment.
             * 
             * @param status
             *     Status of the code assignment
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(CodeableConcept status) {
                this.status = status;
                return this;
            }

            /**
             * The date at which the code status is changed as part of the terminology maintenance.
             * 
             * @param statusDate
             *     The date at which the code status is changed as part of the terminology maintenance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder statusDate(DateTime statusDate) {
                this.statusDate = statusDate;
                return this;
            }

            /**
             * Any comment can be provided in this field, if necessary.
             * 
             * @param comment
             *     Any comment can be provided in this field, if necessary
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder comment(String comment) {
                this.comment = comment;
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference... source) {
                for (Reference value : source) {
                    this.source.add(value);
                }
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            /**
             * Build the {@link Code}
             * 
             * @return
             *     An immutable object of type {@link Code}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Code per the base specification
             */
            @Override
            public Code build() {
                return new Code(this);
            }

            protected Builder from(Code code) {
                super.from(code);
                this.code = code.code;
                status = code.status;
                statusDate = code.statusDate;
                comment = code.comment;
                source.addAll(code.source);
                return this;
            }
        }
    }

    /**
     * Names applicable to this substance.
     */
    public static class Name extends BackboneElement {
        @Summary
        @Required
        private final String name;
        @Summary
        private final CodeableConcept type;
        @Summary
        private final CodeableConcept status;
        @Summary
        private final Boolean preferred;
        @Summary
        private final List<CodeableConcept> language;
        @Summary
        private final List<CodeableConcept> domain;
        @Summary
        private final List<CodeableConcept> jurisdiction;
        @Summary
        private final List<SubstanceSpecification.Name> synonym;
        @Summary
        private final List<SubstanceSpecification.Name> translation;
        @Summary
        private final List<Official> official;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private volatile int hashCode;

        private Name(Builder builder) {
            super(builder);
            name = ValidationSupport.requireNonNull(builder.name, "name");
            type = builder.type;
            status = builder.status;
            preferred = builder.preferred;
            language = Collections.unmodifiableList(ValidationSupport.checkList(builder.language, "language", CodeableConcept.class));
            domain = Collections.unmodifiableList(ValidationSupport.checkList(builder.domain, "domain", CodeableConcept.class));
            jurisdiction = Collections.unmodifiableList(ValidationSupport.checkList(builder.jurisdiction, "jurisdiction", CodeableConcept.class));
            synonym = Collections.unmodifiableList(ValidationSupport.checkList(builder.synonym, "synonym", SubstanceSpecification.Name.class));
            translation = Collections.unmodifiableList(ValidationSupport.checkList(builder.translation, "translation", SubstanceSpecification.Name.class));
            official = Collections.unmodifiableList(ValidationSupport.checkList(builder.official, "official", Official.class));
            source = Collections.unmodifiableList(ValidationSupport.checkList(builder.source, "source", Reference.class));
            ValidationSupport.checkReferenceType(source, "source", "DocumentReference");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * The actual name.
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getName() {
            return name;
        }

        /**
         * Name type.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getType() {
            return type;
        }

        /**
         * The status of the name.
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getStatus() {
            return status;
        }

        /**
         * If this is the preferred name for this substance.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getPreferred() {
            return preferred;
        }

        /**
         * Language of the name.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getLanguage() {
            return language;
        }

        /**
         * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
         * colour additive.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getDomain() {
            return domain;
        }

        /**
         * The jurisdiction where this name applies.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link CodeableConcept} that may be empty.
         */
        public List<CodeableConcept> getJurisdiction() {
            return jurisdiction;
        }

        /**
         * A synonym of this name.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Name} that may be empty.
         */
        public List<SubstanceSpecification.Name> getSynonym() {
            return synonym;
        }

        /**
         * A translation for this name.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Name} that may be empty.
         */
        public List<SubstanceSpecification.Name> getTranslation() {
            return translation;
        }

        /**
         * Details of the official nature of this name.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Official} that may be empty.
         */
        public List<Official> getOfficial() {
            return official;
        }

        /**
         * Supporting literature.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (name != null) || 
                (type != null) || 
                (status != null) || 
                (preferred != null) || 
                !language.isEmpty() || 
                !domain.isEmpty() || 
                !jurisdiction.isEmpty() || 
                !synonym.isEmpty() || 
                !translation.isEmpty() || 
                !official.isEmpty() || 
                !source.isEmpty();
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
                    accept(name, "name", visitor);
                    accept(type, "type", visitor);
                    accept(status, "status", visitor);
                    accept(preferred, "preferred", visitor);
                    accept(language, "language", visitor, CodeableConcept.class);
                    accept(domain, "domain", visitor, CodeableConcept.class);
                    accept(jurisdiction, "jurisdiction", visitor, CodeableConcept.class);
                    accept(synonym, "synonym", visitor, SubstanceSpecification.Name.class);
                    accept(translation, "translation", visitor, SubstanceSpecification.Name.class);
                    accept(official, "official", visitor, Official.class);
                    accept(source, "source", visitor, Reference.class);
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
            Name other = (Name) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(name, other.name) && 
                Objects.equals(type, other.type) && 
                Objects.equals(status, other.status) && 
                Objects.equals(preferred, other.preferred) && 
                Objects.equals(language, other.language) && 
                Objects.equals(domain, other.domain) && 
                Objects.equals(jurisdiction, other.jurisdiction) && 
                Objects.equals(synonym, other.synonym) && 
                Objects.equals(translation, other.translation) && 
                Objects.equals(official, other.official) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    name, 
                    type, 
                    status, 
                    preferred, 
                    language, 
                    domain, 
                    jurisdiction, 
                    synonym, 
                    translation, 
                    official, 
                    source);
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
            private String name;
            private CodeableConcept type;
            private CodeableConcept status;
            private Boolean preferred;
            private List<CodeableConcept> language = new ArrayList<>();
            private List<CodeableConcept> domain = new ArrayList<>();
            private List<CodeableConcept> jurisdiction = new ArrayList<>();
            private List<SubstanceSpecification.Name> synonym = new ArrayList<>();
            private List<SubstanceSpecification.Name> translation = new ArrayList<>();
            private List<Official> official = new ArrayList<>();
            private List<Reference> source = new ArrayList<>();

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
             * The actual name.
             * 
             * <p>This element is required.
             * 
             * @param name
             *     The actual name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder name(String name) {
                this.name = name;
                return this;
            }

            /**
             * Name type.
             * 
             * @param type
             *     Name type
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder type(CodeableConcept type) {
                this.type = type;
                return this;
            }

            /**
             * The status of the name.
             * 
             * @param status
             *     The status of the name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder status(CodeableConcept status) {
                this.status = status;
                return this;
            }

            /**
             * If this is the preferred name for this substance.
             * 
             * @param preferred
             *     If this is the preferred name for this substance
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder preferred(Boolean preferred) {
                this.preferred = preferred;
                return this;
            }

            /**
             * Language of the name.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param language
             *     Language of the name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(CodeableConcept... language) {
                for (CodeableConcept value : language) {
                    this.language.add(value);
                }
                return this;
            }

            /**
             * Language of the name.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param language
             *     Language of the name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder language(Collection<CodeableConcept> language) {
                this.language = new ArrayList<>(language);
                return this;
            }

            /**
             * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             * colour additive.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param domain
             *     The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             *     colour additive
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder domain(CodeableConcept... domain) {
                for (CodeableConcept value : domain) {
                    this.domain.add(value);
                }
                return this;
            }

            /**
             * The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             * colour additive.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param domain
             *     The use context of this name for example if there is a different name a drug active ingredient as opposed to a food 
             *     colour additive
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder domain(Collection<CodeableConcept> domain) {
                this.domain = new ArrayList<>(domain);
                return this;
            }

            /**
             * The jurisdiction where this name applies.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param jurisdiction
             *     The jurisdiction where this name applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder jurisdiction(CodeableConcept... jurisdiction) {
                for (CodeableConcept value : jurisdiction) {
                    this.jurisdiction.add(value);
                }
                return this;
            }

            /**
             * The jurisdiction where this name applies.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param jurisdiction
             *     The jurisdiction where this name applies
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder jurisdiction(Collection<CodeableConcept> jurisdiction) {
                this.jurisdiction = new ArrayList<>(jurisdiction);
                return this;
            }

            /**
             * A synonym of this name.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param synonym
             *     A synonym of this name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder synonym(SubstanceSpecification.Name... synonym) {
                for (SubstanceSpecification.Name value : synonym) {
                    this.synonym.add(value);
                }
                return this;
            }

            /**
             * A synonym of this name.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param synonym
             *     A synonym of this name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder synonym(Collection<SubstanceSpecification.Name> synonym) {
                this.synonym = new ArrayList<>(synonym);
                return this;
            }

            /**
             * A translation for this name.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param translation
             *     A translation for this name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder translation(SubstanceSpecification.Name... translation) {
                for (SubstanceSpecification.Name value : translation) {
                    this.translation.add(value);
                }
                return this;
            }

            /**
             * A translation for this name.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param translation
             *     A translation for this name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder translation(Collection<SubstanceSpecification.Name> translation) {
                this.translation = new ArrayList<>(translation);
                return this;
            }

            /**
             * Details of the official nature of this name.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param official
             *     Details of the official nature of this name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder official(Official... official) {
                for (Official value : official) {
                    this.official.add(value);
                }
                return this;
            }

            /**
             * Details of the official nature of this name.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param official
             *     Details of the official nature of this name
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder official(Collection<Official> official) {
                this.official = new ArrayList<>(official);
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference... source) {
                for (Reference value : source) {
                    this.source.add(value);
                }
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            /**
             * Build the {@link Name}
             * 
             * <p>Required elements:
             * <ul>
             * <li>name</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Name}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Name per the base specification
             */
            @Override
            public Name build() {
                return new Name(this);
            }

            protected Builder from(Name name) {
                super.from(name);
                this.name = name.name;
                type = name.type;
                status = name.status;
                preferred = name.preferred;
                language.addAll(name.language);
                domain.addAll(name.domain);
                jurisdiction.addAll(name.jurisdiction);
                synonym.addAll(name.synonym);
                translation.addAll(name.translation);
                official.addAll(name.official);
                source.addAll(name.source);
                return this;
            }
        }

        /**
         * Details of the official nature of this name.
         */
        public static class Official extends BackboneElement {
            @Summary
            private final CodeableConcept authority;
            @Summary
            private final CodeableConcept status;
            @Summary
            private final DateTime date;

            private volatile int hashCode;

            private Official(Builder builder) {
                super(builder);
                authority = builder.authority;
                status = builder.status;
                date = builder.date;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * Which authority uses this official name.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getAuthority() {
                return authority;
            }

            /**
             * The status of the official name.
             * 
             * @return
             *     An immutable object of type {@link CodeableConcept} that may be null.
             */
            public CodeableConcept getStatus() {
                return status;
            }

            /**
             * Date of official name change.
             * 
             * @return
             *     An immutable object of type {@link DateTime} that may be null.
             */
            public DateTime getDate() {
                return date;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (authority != null) || 
                    (status != null) || 
                    (date != null);
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
                        accept(authority, "authority", visitor);
                        accept(status, "status", visitor);
                        accept(date, "date", visitor);
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
                Official other = (Official) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(authority, other.authority) && 
                    Objects.equals(status, other.status) && 
                    Objects.equals(date, other.date);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        authority, 
                        status, 
                        date);
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
                private CodeableConcept authority;
                private CodeableConcept status;
                private DateTime date;

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
                 * Which authority uses this official name.
                 * 
                 * @param authority
                 *     Which authority uses this official name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder authority(CodeableConcept authority) {
                    this.authority = authority;
                    return this;
                }

                /**
                 * The status of the official name.
                 * 
                 * @param status
                 *     The status of the official name
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder status(CodeableConcept status) {
                    this.status = status;
                    return this;
                }

                /**
                 * Date of official name change.
                 * 
                 * @param date
                 *     Date of official name change
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder date(DateTime date) {
                    this.date = date;
                    return this;
                }

                /**
                 * Build the {@link Official}
                 * 
                 * @return
                 *     An immutable object of type {@link Official}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Official per the base specification
                 */
                @Override
                public Official build() {
                    return new Official(this);
                }

                protected Builder from(Official official) {
                    super.from(official);
                    authority = official.authority;
                    status = official.status;
                    date = official.date;
                    return this;
                }
            }
        }
    }

    /**
     * A link between this substance and another, with details of the relationship.
     */
    public static class Relationship extends BackboneElement {
        @Summary
        @ReferenceTarget({ "SubstanceSpecification" })
        @Choice({ Reference.class, CodeableConcept.class })
        private final Element substance;
        @Summary
        private final CodeableConcept relationship;
        @Summary
        private final Boolean isDefining;
        @Summary
        @Choice({ Quantity.class, Range.class, Ratio.class, String.class })
        private final Element amount;
        @Summary
        private final Ratio amountRatioLowLimit;
        @Summary
        private final CodeableConcept amountType;
        @Summary
        @ReferenceTarget({ "DocumentReference" })
        private final List<Reference> source;

        private volatile int hashCode;

        private Relationship(Builder builder) {
            super(builder);
            substance = ValidationSupport.choiceElement(builder.substance, "substance", Reference.class, CodeableConcept.class);
            relationship = builder.relationship;
            isDefining = builder.isDefining;
            amount = ValidationSupport.choiceElement(builder.amount, "amount", Quantity.class, Range.class, Ratio.class, String.class);
            amountRatioLowLimit = builder.amountRatioLowLimit;
            amountType = builder.amountType;
            source = Collections.unmodifiableList(ValidationSupport.checkList(builder.source, "source", Reference.class));
            ValidationSupport.checkReferenceType(substance, "substance", "SubstanceSpecification");
            ValidationSupport.checkReferenceType(source, "source", "DocumentReference");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * A pointer to another substance, as a resource or just a representational code.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getSubstance() {
            return substance;
        }

        /**
         * For example "salt to parent", "active moiety", "starting material".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getRelationship() {
            return relationship;
        }

        /**
         * For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that 
         * enzyme, out of several possible substance relationships.
         * 
         * @return
         *     An immutable object of type {@link Boolean} that may be null.
         */
        public Boolean getIsDefining() {
            return isDefining;
        }

        /**
         * A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the 
         * active substance in relation to some other.
         * 
         * @return
         *     An immutable object of type {@link Element} that may be null.
         */
        public Element getAmount() {
            return amount;
        }

        /**
         * For use when the numeric.
         * 
         * @return
         *     An immutable object of type {@link Ratio} that may be null.
         */
        public Ratio getAmountRatioLowLimit() {
            return amountRatioLowLimit;
        }

        /**
         * An operator for the amount, for example "average", "approximately", "less than".
         * 
         * @return
         *     An immutable object of type {@link CodeableConcept} that may be null.
         */
        public CodeableConcept getAmountType() {
            return amountType;
        }

        /**
         * Supporting literature.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Reference} that may be empty.
         */
        public List<Reference> getSource() {
            return source;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (substance != null) || 
                (relationship != null) || 
                (isDefining != null) || 
                (amount != null) || 
                (amountRatioLowLimit != null) || 
                (amountType != null) || 
                !source.isEmpty();
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
                    accept(substance, "substance", visitor);
                    accept(relationship, "relationship", visitor);
                    accept(isDefining, "isDefining", visitor);
                    accept(amount, "amount", visitor);
                    accept(amountRatioLowLimit, "amountRatioLowLimit", visitor);
                    accept(amountType, "amountType", visitor);
                    accept(source, "source", visitor, Reference.class);
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
            Relationship other = (Relationship) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(substance, other.substance) && 
                Objects.equals(relationship, other.relationship) && 
                Objects.equals(isDefining, other.isDefining) && 
                Objects.equals(amount, other.amount) && 
                Objects.equals(amountRatioLowLimit, other.amountRatioLowLimit) && 
                Objects.equals(amountType, other.amountType) && 
                Objects.equals(source, other.source);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    substance, 
                    relationship, 
                    isDefining, 
                    amount, 
                    amountRatioLowLimit, 
                    amountType, 
                    source);
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
            private Element substance;
            private CodeableConcept relationship;
            private Boolean isDefining;
            private Element amount;
            private Ratio amountRatioLowLimit;
            private CodeableConcept amountType;
            private List<Reference> source = new ArrayList<>();

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
             * A pointer to another substance, as a resource or just a representational code.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Reference}</li>
             * <li>{@link CodeableConcept}</li>
             * </ul>
             * 
             * When of type {@link Reference}, the allowed resource types for this reference are:
             * <ul>
             * <li>{@link SubstanceSpecification}</li>
             * </ul>
             * 
             * @param substance
             *     A pointer to another substance, as a resource or just a representational code
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder substance(Element substance) {
                this.substance = substance;
                return this;
            }

            /**
             * For example "salt to parent", "active moiety", "starting material".
             * 
             * @param relationship
             *     For example "salt to parent", "active moiety", "starting material"
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relationship(CodeableConcept relationship) {
                this.relationship = relationship;
                return this;
            }

            /**
             * For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that 
             * enzyme, out of several possible substance relationships.
             * 
             * @param isDefining
             *     For example where an enzyme strongly bonds with a particular substance, this is a defining relationship for that 
             *     enzyme, out of several possible substance relationships
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder isDefining(Boolean isDefining) {
                this.isDefining = isDefining;
                return this;
            }

            /**
             * A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the 
             * active substance in relation to some other.
             * 
             * <p>This is a choice element with the following allowed types:
             * <ul>
             * <li>{@link Quantity}</li>
             * <li>{@link Range}</li>
             * <li>{@link Ratio}</li>
             * <li>{@link String}</li>
             * </ul>
             * 
             * @param amount
             *     A numeric factor for the relationship, for instance to express that the salt of a substance has some percentage of the 
             *     active substance in relation to some other
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amount(Element amount) {
                this.amount = amount;
                return this;
            }

            /**
             * For use when the numeric.
             * 
             * @param amountRatioLowLimit
             *     For use when the numeric
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amountRatioLowLimit(Ratio amountRatioLowLimit) {
                this.amountRatioLowLimit = amountRatioLowLimit;
                return this;
            }

            /**
             * An operator for the amount, for example "average", "approximately", "less than".
             * 
             * @param amountType
             *     An operator for the amount, for example "average", "approximately", "less than"
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder amountType(CodeableConcept amountType) {
                this.amountType = amountType;
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Reference... source) {
                for (Reference value : source) {
                    this.source.add(value);
                }
                return this;
            }

            /**
             * Supporting literature.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * <p>Allowed resource types for the references:
             * <ul>
             * <li>{@link DocumentReference}</li>
             * </ul>
             * 
             * @param source
             *     Supporting literature
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder source(Collection<Reference> source) {
                this.source = new ArrayList<>(source);
                return this;
            }

            /**
             * Build the {@link Relationship}
             * 
             * @return
             *     An immutable object of type {@link Relationship}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Relationship per the base specification
             */
            @Override
            public Relationship build() {
                return new Relationship(this);
            }

            protected Builder from(Relationship relationship) {
                super.from(relationship);
                substance = relationship.substance;
                this.relationship = relationship.relationship;
                isDefining = relationship.isDefining;
                amount = relationship.amount;
                amountRatioLowLimit = relationship.amountRatioLowLimit;
                amountType = relationship.amountType;
                source.addAll(relationship.source);
                return this;
            }
        }
    }
}
