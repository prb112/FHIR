/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

import com.ibm.fhir.model.annotation.Choice;
import com.ibm.fhir.model.annotation.ReferenceTarget;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A text note which also contains information about who made the statement and when.
 */
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Annotation extends Element {
    @Summary
    @ReferenceTarget({ "Practitioner", "Patient", "RelatedPerson", "Organization" })
    @Choice({ Reference.class, String.class })
    private final Element author;
    @Summary
    private final DateTime time;
    @Summary
    @Required
    private final Markdown text;

    private volatile int hashCode;

    private Annotation(Builder builder) {
        super(builder);
        author = ValidationSupport.choiceElement(builder.author, "author", Reference.class, String.class);
        time = builder.time;
        text = ValidationSupport.requireNonNull(builder.text, "text");
        ValidationSupport.checkReferenceType(author, "author", "Practitioner", "Patient", "RelatedPerson", "Organization");
        ValidationSupport.requireValueOrChildren(this);
    }

    /**
     * The individual responsible for making the annotation.
     * 
     * @return
     *     An immutable object of type {@link Element} that may be null.
     */
    public Element getAuthor() {
        return author;
    }

    /**
     * Indicates when this particular annotation was made.
     * 
     * @return
     *     An immutable object of type {@link DateTime} that may be null.
     */
    public DateTime getTime() {
        return time;
    }

    /**
     * The text of the annotation in markdown format.
     * 
     * @return
     *     An immutable object of type {@link Markdown} that is non-null.
     */
    public Markdown getText() {
        return text;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (author != null) || 
            (time != null) || 
            (text != null);
    }

    @Override
    public void accept(java.lang.String elementName, int elementIndex, Visitor visitor) {
        if (visitor.preVisit(this)) {
            visitor.visitStart(elementName, elementIndex, this);
            if (visitor.visit(elementName, elementIndex, this)) {
                // visit children
                accept(id, "id", visitor);
                accept(extension, "extension", visitor, Extension.class);
                accept(author, "author", visitor);
                accept(time, "time", visitor);
                accept(text, "text", visitor);
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
        Annotation other = (Annotation) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(extension, other.extension) && 
            Objects.equals(author, other.author) && 
            Objects.equals(time, other.time) && 
            Objects.equals(text, other.text);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                extension, 
                author, 
                time, 
                text);
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
        private Element author;
        private DateTime time;
        private Markdown text;

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
         * The individual responsible for making the annotation.
         * 
         * <p>This is a choice element with the following allowed types:
         * <ul>
         * <li>{@link Reference}</li>
         * <li>{@link String}</li>
         * </ul>
         * 
         * When of type {@link Reference}, the allowed resource types for this reference are:
         * <ul>
         * <li>{@link Practitioner}</li>
         * <li>{@link Patient}</li>
         * <li>{@link RelatedPerson}</li>
         * <li>{@link Organization}</li>
         * </ul>
         * 
         * @param author
         *     Individual responsible for the annotation
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder author(Element author) {
            this.author = author;
            return this;
        }

        /**
         * Indicates when this particular annotation was made.
         * 
         * @param time
         *     When the annotation was made
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder time(DateTime time) {
            this.time = time;
            return this;
        }

        /**
         * The text of the annotation in markdown format.
         * 
         * <p>This element is required.
         * 
         * @param text
         *     The annotation - text content (as markdown)
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder text(Markdown text) {
            this.text = text;
            return this;
        }

        /**
         * Build the {@link Annotation}
         * 
         * <p>Required elements:
         * <ul>
         * <li>text</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Annotation}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Annotation per the base specification
         */
        @Override
        public Annotation build() {
            return new Annotation(this);
        }

        protected Builder from(Annotation annotation) {
            super.from(annotation);
            author = annotation.author;
            time = annotation.time;
            text = annotation.text;
            return this;
        }
    }
}
