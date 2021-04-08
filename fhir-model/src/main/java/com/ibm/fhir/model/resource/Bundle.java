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
import com.ibm.fhir.model.annotation.Constraint;
import com.ibm.fhir.model.annotation.Maturity;
import com.ibm.fhir.model.annotation.Required;
import com.ibm.fhir.model.annotation.Summary;
import com.ibm.fhir.model.type.BackboneElement;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Decimal;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.Identifier;
import com.ibm.fhir.model.type.Instant;
import com.ibm.fhir.model.type.Meta;
import com.ibm.fhir.model.type.Signature;
import com.ibm.fhir.model.type.String;
import com.ibm.fhir.model.type.UnsignedInt;
import com.ibm.fhir.model.type.Uri;
import com.ibm.fhir.model.type.code.BindingStrength;
import com.ibm.fhir.model.type.code.BundleType;
import com.ibm.fhir.model.type.code.HTTPVerb;
import com.ibm.fhir.model.type.code.SearchEntryMode;
import com.ibm.fhir.model.type.code.StandardsStatus;
import com.ibm.fhir.model.util.ValidationSupport;
import com.ibm.fhir.model.visitor.Visitor;

/**
 * A container for a collection of resources.
 * 
 * <p>Maturity level: FMM5 (Normative)
 */
@Maturity(
    level = 5,
    status = StandardsStatus.ValueSet.NORMATIVE
)
@Constraint(
    id = "bdl-1",
    level = "Rule",
    location = "(base)",
    description = "total only when a search or history",
    expression = "total.empty() or (type = 'searchset') or (type = 'history')"
)
@Constraint(
    id = "bdl-2",
    level = "Rule",
    location = "(base)",
    description = "entry.search only when a search",
    expression = "entry.search.empty() or (type = 'searchset')"
)
@Constraint(
    id = "bdl-3",
    level = "Rule",
    location = "(base)",
    description = "entry.request mandatory for batch/transaction/history, otherwise prohibited",
    expression = "entry.all(request.exists() = (%resource.type = 'batch' or %resource.type = 'transaction' or %resource.type = 'history'))"
)
@Constraint(
    id = "bdl-4",
    level = "Rule",
    location = "(base)",
    description = "entry.response mandatory for batch-response/transaction-response/history, otherwise prohibited",
    expression = "entry.all(response.exists() = (%resource.type = 'batch-response' or %resource.type = 'transaction-response' or %resource.type = 'history'))"
)
@Constraint(
    id = "bdl-5",
    level = "Rule",
    location = "Bundle.entry",
    description = "must be a resource unless there's a request or response",
    expression = "resource.exists() or request.exists() or response.exists()"
)
@Constraint(
    id = "bdl-7",
    level = "Rule",
    location = "(base)",
    description = "FullUrl must be unique in a bundle, or else entries with the same fullUrl must have different meta.versionId (except in history bundles)",
    expression = "(type = 'history') or entry.where(fullUrl.exists()).select(fullUrl&resource.meta.versionId).isDistinct()"
)
@Constraint(
    id = "bdl-8",
    level = "Rule",
    location = "Bundle.entry",
    description = "fullUrl cannot be a version specific reference",
    expression = "fullUrl.contains('/_history/').not()"
)
@Constraint(
    id = "bdl-9",
    level = "Rule",
    location = "(base)",
    description = "A document must have an identifier with a system and a value",
    expression = "type = 'document' implies (identifier.system.exists() and identifier.value.exists())"
)
@Constraint(
    id = "bdl-10",
    level = "Rule",
    location = "(base)",
    description = "A document must have a date",
    expression = "type = 'document' implies (timestamp.hasValue())"
)
@Constraint(
    id = "bdl-11",
    level = "Rule",
    location = "(base)",
    description = "A document must have a Composition as the first resource",
    expression = "type = 'document' implies entry.first().resource.is(Composition)"
)
@Constraint(
    id = "bdl-12",
    level = "Rule",
    location = "(base)",
    description = "A message must have a MessageHeader as the first resource",
    expression = "type = 'message' implies entry.first().resource.is(MessageHeader)"
)
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class Bundle extends Resource {
    @Summary
    private final Identifier identifier;
    @Summary
    @Binding(
        bindingName = "BundleType",
        strength = BindingStrength.ValueSet.REQUIRED,
        description = "Indicates the purpose of a bundle - how it is intended to be used.",
        valueSet = "http://hl7.org/fhir/ValueSet/bundle-type|4.0.1"
    )
    @Required
    private final BundleType type;
    @Summary
    private final Instant timestamp;
    @Summary
    private final UnsignedInt total;
    @Summary
    private final List<Link> link;
    @Summary
    private final List<Entry> entry;
    @Summary
    private final Signature signature;

    private volatile int hashCode;

    private Bundle(Builder builder) {
        super(builder);
        identifier = builder.identifier;
        type = ValidationSupport.requireNonNull(builder.type, "type");
        timestamp = builder.timestamp;
        total = builder.total;
        link = Collections.unmodifiableList(ValidationSupport.checkList(builder.link, "link", Link.class));
        entry = Collections.unmodifiableList(ValidationSupport.checkList(builder.entry, "entry", Entry.class));
        signature = builder.signature;
        ValidationSupport.requireChildren(this);
    }

    /**
     * A persistent identifier for the bundle that won't change as a bundle is copied from server to server.
     * 
     * @return
     *     An immutable object of type {@link Identifier} that may be null.
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Indicates the purpose of this bundle - how it is intended to be used.
     * 
     * @return
     *     An immutable object of type {@link BundleType} that is non-null.
     */
    public BundleType getType() {
        return type;
    }

    /**
     * The date/time that the bundle was assembled - i.e. when the resources were placed in the bundle.
     * 
     * @return
     *     An immutable object of type {@link Instant} that may be null.
     */
    public Instant getTimestamp() {
        return timestamp;
    }

    /**
     * If a set of search matches, this is the total number of entries of type 'match' across all pages in the search. It 
     * does not include search.mode = 'include' or 'outcome' entries and it does not provide a count of the number of entries 
     * in the Bundle.
     * 
     * @return
     *     An immutable object of type {@link UnsignedInt} that may be null.
     */
    public UnsignedInt getTotal() {
        return total;
    }

    /**
     * A series of links that provide context to this bundle.
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Link} that may be empty.
     */
    public List<Link> getLink() {
        return link;
    }

    /**
     * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
     * history only).
     * 
     * @return
     *     An unmodifiable list containing immutable objects of type {@link Entry} that may be empty.
     */
    public List<Entry> getEntry() {
        return entry;
    }

    /**
     * Digital Signature - base64 encoded. XML-DSig or a JWT.
     * 
     * @return
     *     An immutable object of type {@link Signature} that may be null.
     */
    public Signature getSignature() {
        return signature;
    }

    @Override
    public boolean hasChildren() {
        return super.hasChildren() || 
            (identifier != null) || 
            (type != null) || 
            (timestamp != null) || 
            (total != null) || 
            !link.isEmpty() || 
            !entry.isEmpty() || 
            (signature != null);
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
                accept(identifier, "identifier", visitor);
                accept(type, "type", visitor);
                accept(timestamp, "timestamp", visitor);
                accept(total, "total", visitor);
                accept(link, "link", visitor, Link.class);
                accept(entry, "entry", visitor, Entry.class);
                accept(signature, "signature", visitor);
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
        Bundle other = (Bundle) obj;
        return Objects.equals(id, other.id) && 
            Objects.equals(meta, other.meta) && 
            Objects.equals(implicitRules, other.implicitRules) && 
            Objects.equals(language, other.language) && 
            Objects.equals(identifier, other.identifier) && 
            Objects.equals(type, other.type) && 
            Objects.equals(timestamp, other.timestamp) && 
            Objects.equals(total, other.total) && 
            Objects.equals(link, other.link) && 
            Objects.equals(entry, other.entry) && 
            Objects.equals(signature, other.signature);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, 
                meta, 
                implicitRules, 
                language, 
                identifier, 
                type, 
                timestamp, 
                total, 
                link, 
                entry, 
                signature);
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

    public static class Builder extends Resource.Builder {
        private Identifier identifier;
        private BundleType type;
        private Instant timestamp;
        private UnsignedInt total;
        private List<Link> link = new ArrayList<>();
        private List<Entry> entry = new ArrayList<>();
        private Signature signature;

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
         * A persistent identifier for the bundle that won't change as a bundle is copied from server to server.
         * 
         * @param identifier
         *     Persistent identifier for the bundle
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder identifier(Identifier identifier) {
            this.identifier = identifier;
            return this;
        }

        /**
         * Indicates the purpose of this bundle - how it is intended to be used.
         * 
         * <p>This element is required.
         * 
         * @param type
         *     document | message | transaction | transaction-response | batch | batch-response | history | searchset | collection
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder type(BundleType type) {
            this.type = type;
            return this;
        }

        /**
         * The date/time that the bundle was assembled - i.e. when the resources were placed in the bundle.
         * 
         * @param timestamp
         *     When the bundle was assembled
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder timestamp(Instant timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        /**
         * If a set of search matches, this is the total number of entries of type 'match' across all pages in the search. It 
         * does not include search.mode = 'include' or 'outcome' entries and it does not provide a count of the number of entries 
         * in the Bundle.
         * 
         * @param total
         *     If search, the total number of matches
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder total(UnsignedInt total) {
            this.total = total;
            return this;
        }

        /**
         * A series of links that provide context to this bundle.
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param link
         *     Links related to this Bundle
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder link(Link... link) {
            for (Link value : link) {
                this.link.add(value);
            }
            return this;
        }

        /**
         * A series of links that provide context to this bundle.
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param link
         *     Links related to this Bundle
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder link(Collection<Link> link) {
            this.link = new ArrayList<>(link);
            return this;
        }

        /**
         * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
         * history only).
         * 
         * <p>Adds new element(s) to the existing list
         * 
         * @param entry
         *     Entry in the bundle - will have a resource or information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entry(Entry... entry) {
            for (Entry value : entry) {
                this.entry.add(value);
            }
            return this;
        }

        /**
         * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
         * history only).
         * 
         * <p>Replaces the existing list with a new one containing elements from the Collection
         * 
         * @param entry
         *     Entry in the bundle - will have a resource or information
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder entry(Collection<Entry> entry) {
            this.entry = new ArrayList<>(entry);
            return this;
        }

        /**
         * Digital Signature - base64 encoded. XML-DSig or a JWT.
         * 
         * @param signature
         *     Digital Signature
         * 
         * @return
         *     A reference to this Builder instance
         */
        public Builder signature(Signature signature) {
            this.signature = signature;
            return this;
        }

        /**
         * Build the {@link Bundle}
         * 
         * <p>Required elements:
         * <ul>
         * <li>type</li>
         * </ul>
         * 
         * @return
         *     An immutable object of type {@link Bundle}
         * @throws IllegalStateException
         *     if the current state cannot be built into a valid Bundle per the base specification
         */
        @Override
        public Bundle build() {
            return new Bundle(this);
        }

        protected Builder from(Bundle bundle) {
            super.from(bundle);
            identifier = bundle.identifier;
            type = bundle.type;
            timestamp = bundle.timestamp;
            total = bundle.total;
            link.addAll(bundle.link);
            entry.addAll(bundle.entry);
            signature = bundle.signature;
            return this;
        }
    }

    /**
     * A series of links that provide context to this bundle.
     */
    public static class Link extends BackboneElement {
        @Summary
        @Required
        private final String relation;
        @Summary
        @Required
        private final Uri url;

        private volatile int hashCode;

        private Link(Builder builder) {
            super(builder);
            relation = ValidationSupport.requireNonNull(builder.relation, "relation");
            url = ValidationSupport.requireNonNull(builder.url, "url");
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * A name which details the functional use for this link - see [http://www.iana.org/assignments/link-relations/link-
         * relations.xhtml#link-relations-1](http://www.iana.org/assignments/link-relations/link-relations.xhtml#link-relations-
         * 1).
         * 
         * @return
         *     An immutable object of type {@link String} that is non-null.
         */
        public String getRelation() {
            return relation;
        }

        /**
         * The reference details for the link.
         * 
         * @return
         *     An immutable object of type {@link Uri} that is non-null.
         */
        public Uri getUrl() {
            return url;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                (relation != null) || 
                (url != null);
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
                    accept(relation, "relation", visitor);
                    accept(url, "url", visitor);
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
            Link other = (Link) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(relation, other.relation) && 
                Objects.equals(url, other.url);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    relation, 
                    url);
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
            private String relation;
            private Uri url;

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
             * A name which details the functional use for this link - see [http://www.iana.org/assignments/link-relations/link-
             * relations.xhtml#link-relations-1](http://www.iana.org/assignments/link-relations/link-relations.xhtml#link-relations-
             * 1).
             * 
             * <p>This element is required.
             * 
             * @param relation
             *     See http://www.iana.org/assignments/link-relations/link-relations.xhtml#link-relations-1
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder relation(String relation) {
                this.relation = relation;
                return this;
            }

            /**
             * The reference details for the link.
             * 
             * <p>This element is required.
             * 
             * @param url
             *     Reference details for the link
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder url(Uri url) {
                this.url = url;
                return this;
            }

            /**
             * Build the {@link Link}
             * 
             * <p>Required elements:
             * <ul>
             * <li>relation</li>
             * <li>url</li>
             * </ul>
             * 
             * @return
             *     An immutable object of type {@link Link}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Link per the base specification
             */
            @Override
            public Link build() {
                return new Link(this);
            }

            protected Builder from(Link link) {
                super.from(link);
                relation = link.relation;
                url = link.url;
                return this;
            }
        }
    }

    /**
     * An entry in a bundle resource - will either contain a resource or information about a resource (transactions and 
     * history only).
     */
    public static class Entry extends BackboneElement {
        @Summary
        private final List<Bundle.Link> link;
        @Summary
        private final Uri fullUrl;
        @Summary
        private final Resource resource;
        @Summary
        private final Search search;
        @Summary
        private final Request request;
        @Summary
        private final Response response;

        private volatile int hashCode;

        private Entry(Builder builder) {
            super(builder);
            link = Collections.unmodifiableList(ValidationSupport.checkList(builder.link, "link", Bundle.Link.class));
            fullUrl = builder.fullUrl;
            resource = builder.resource;
            search = builder.search;
            request = builder.request;
            response = builder.response;
            ValidationSupport.requireValueOrChildren(this);
        }

        /**
         * A series of links that provide context to this entry.
         * 
         * @return
         *     An unmodifiable list containing immutable objects of type {@link Link} that may be empty.
         */
        public List<Bundle.Link> getLink() {
            return link;
        }

        /**
         * The Absolute URL for the resource. The fullUrl SHALL NOT disagree with the id in the resource - i.e. if the fullUrl is 
         * not a urn:uuid, the URL shall be version-independent URL consistent with the Resource.id. The fullUrl is a version 
         * independent reference to the resource. The fullUrl element SHALL have a value except that: 
         * * fullUrl can be empty on a POST (although it does not need to when specifying a temporary id for reference in the 
         * bundle)
         * * Results from operations might involve resources that are not identified.
         * 
         * @return
         *     An immutable object of type {@link Uri} that may be null.
         */
        public Uri getFullUrl() {
            return fullUrl;
        }

        /**
         * The Resource for the entry. The purpose/meaning of the resource is determined by the Bundle.type.
         * 
         * @return
         *     An immutable object of type {@link Resource} that may be null.
         */
        public Resource getResource() {
            return resource;
        }

        /**
         * Information about the search process that lead to the creation of this entry.
         * 
         * @return
         *     An immutable object of type {@link Search} that may be null.
         */
        public Search getSearch() {
            return search;
        }

        /**
         * Additional information about how this entry should be processed as part of a transaction or batch. For history, it 
         * shows how the entry was processed to create the version contained in the entry.
         * 
         * @return
         *     An immutable object of type {@link Request} that may be null.
         */
        public Request getRequest() {
            return request;
        }

        /**
         * Indicates the results of processing the corresponding 'request' entry in the batch or transaction being responded to 
         * or what the results of an operation where when returning history.
         * 
         * @return
         *     An immutable object of type {@link Response} that may be null.
         */
        public Response getResponse() {
            return response;
        }

        @Override
        public boolean hasChildren() {
            return super.hasChildren() || 
                !link.isEmpty() || 
                (fullUrl != null) || 
                (resource != null) || 
                (search != null) || 
                (request != null) || 
                (response != null);
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
                    accept(link, "link", visitor, Bundle.Link.class);
                    accept(fullUrl, "fullUrl", visitor);
                    accept(resource, "resource", visitor);
                    accept(search, "search", visitor);
                    accept(request, "request", visitor);
                    accept(response, "response", visitor);
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
            Entry other = (Entry) obj;
            return Objects.equals(id, other.id) && 
                Objects.equals(extension, other.extension) && 
                Objects.equals(modifierExtension, other.modifierExtension) && 
                Objects.equals(link, other.link) && 
                Objects.equals(fullUrl, other.fullUrl) && 
                Objects.equals(resource, other.resource) && 
                Objects.equals(search, other.search) && 
                Objects.equals(request, other.request) && 
                Objects.equals(response, other.response);
        }

        @Override
        public int hashCode() {
            int result = hashCode;
            if (result == 0) {
                result = Objects.hash(id, 
                    extension, 
                    modifierExtension, 
                    link, 
                    fullUrl, 
                    resource, 
                    search, 
                    request, 
                    response);
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
            private List<Bundle.Link> link = new ArrayList<>();
            private Uri fullUrl;
            private Resource resource;
            private Search search;
            private Request request;
            private Response response;

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
             * A series of links that provide context to this entry.
             * 
             * <p>Adds new element(s) to the existing list
             * 
             * @param link
             *     Links related to this entry
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder link(Bundle.Link... link) {
                for (Bundle.Link value : link) {
                    this.link.add(value);
                }
                return this;
            }

            /**
             * A series of links that provide context to this entry.
             * 
             * <p>Replaces the existing list with a new one containing elements from the Collection
             * 
             * @param link
             *     Links related to this entry
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder link(Collection<Bundle.Link> link) {
                this.link = new ArrayList<>(link);
                return this;
            }

            /**
             * The Absolute URL for the resource. The fullUrl SHALL NOT disagree with the id in the resource - i.e. if the fullUrl is 
             * not a urn:uuid, the URL shall be version-independent URL consistent with the Resource.id. The fullUrl is a version 
             * independent reference to the resource. The fullUrl element SHALL have a value except that: 
             * * fullUrl can be empty on a POST (although it does not need to when specifying a temporary id for reference in the 
             * bundle)
             * * Results from operations might involve resources that are not identified.
             * 
             * @param fullUrl
             *     URI for resource (Absolute URL server address or URI for UUID/OID)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder fullUrl(Uri fullUrl) {
                this.fullUrl = fullUrl;
                return this;
            }

            /**
             * The Resource for the entry. The purpose/meaning of the resource is determined by the Bundle.type.
             * 
             * @param resource
             *     A resource in the bundle
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder resource(Resource resource) {
                this.resource = resource;
                return this;
            }

            /**
             * Information about the search process that lead to the creation of this entry.
             * 
             * @param search
             *     Search related information
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder search(Search search) {
                this.search = search;
                return this;
            }

            /**
             * Additional information about how this entry should be processed as part of a transaction or batch. For history, it 
             * shows how the entry was processed to create the version contained in the entry.
             * 
             * @param request
             *     Additional execution information (transaction/batch/history)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder request(Request request) {
                this.request = request;
                return this;
            }

            /**
             * Indicates the results of processing the corresponding 'request' entry in the batch or transaction being responded to 
             * or what the results of an operation where when returning history.
             * 
             * @param response
             *     Results of execution (transaction/batch/history)
             * 
             * @return
             *     A reference to this Builder instance
             */
            public Builder response(Response response) {
                this.response = response;
                return this;
            }

            /**
             * Build the {@link Entry}
             * 
             * @return
             *     An immutable object of type {@link Entry}
             * @throws IllegalStateException
             *     if the current state cannot be built into a valid Entry per the base specification
             */
            @Override
            public Entry build() {
                return new Entry(this);
            }

            protected Builder from(Entry entry) {
                super.from(entry);
                link.addAll(entry.link);
                fullUrl = entry.fullUrl;
                resource = entry.resource;
                search = entry.search;
                request = entry.request;
                response = entry.response;
                return this;
            }
        }

        /**
         * Information about the search process that lead to the creation of this entry.
         */
        public static class Search extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "SearchEntryMode",
                strength = BindingStrength.ValueSet.REQUIRED,
                description = "Why an entry is in the result set - whether it's included as a match or because of an _include requirement, or to convey information or warning information about the search process.",
                valueSet = "http://hl7.org/fhir/ValueSet/search-entry-mode|4.0.1"
            )
            private final SearchEntryMode mode;
            @Summary
            private final Decimal score;

            private volatile int hashCode;

            private Search(Builder builder) {
                super(builder);
                mode = builder.mode;
                score = builder.score;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * Why this entry is in the result set - whether it's included as a match or because of an _include requirement, or to 
             * convey information or warning information about the search process.
             * 
             * @return
             *     An immutable object of type {@link SearchEntryMode} that may be null.
             */
            public SearchEntryMode getMode() {
                return mode;
            }

            /**
             * When searching, the server's search ranking score for the entry.
             * 
             * @return
             *     An immutable object of type {@link Decimal} that may be null.
             */
            public Decimal getScore() {
                return score;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (mode != null) || 
                    (score != null);
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
                        accept(mode, "mode", visitor);
                        accept(score, "score", visitor);
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
                Search other = (Search) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(mode, other.mode) && 
                    Objects.equals(score, other.score);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        mode, 
                        score);
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
                private SearchEntryMode mode;
                private Decimal score;

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
                 * Why this entry is in the result set - whether it's included as a match or because of an _include requirement, or to 
                 * convey information or warning information about the search process.
                 * 
                 * @param mode
                 *     match | include | outcome - why this is in the result set
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder mode(SearchEntryMode mode) {
                    this.mode = mode;
                    return this;
                }

                /**
                 * When searching, the server's search ranking score for the entry.
                 * 
                 * @param score
                 *     Search ranking (between 0 and 1)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder score(Decimal score) {
                    this.score = score;
                    return this;
                }

                /**
                 * Build the {@link Search}
                 * 
                 * @return
                 *     An immutable object of type {@link Search}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Search per the base specification
                 */
                @Override
                public Search build() {
                    return new Search(this);
                }

                protected Builder from(Search search) {
                    super.from(search);
                    mode = search.mode;
                    score = search.score;
                    return this;
                }
            }
        }

        /**
         * Additional information about how this entry should be processed as part of a transaction or batch. For history, it 
         * shows how the entry was processed to create the version contained in the entry.
         */
        public static class Request extends BackboneElement {
            @Summary
            @Binding(
                bindingName = "HTTPVerb",
                strength = BindingStrength.ValueSet.REQUIRED,
                description = "HTTP verbs (in the HTTP command line). See [HTTP rfc](https://tools.ietf.org/html/rfc7231) for details.",
                valueSet = "http://hl7.org/fhir/ValueSet/http-verb|4.0.1"
            )
            @Required
            private final HTTPVerb method;
            @Summary
            @Required
            private final Uri url;
            @Summary
            private final String ifNoneMatch;
            @Summary
            private final Instant ifModifiedSince;
            @Summary
            private final String ifMatch;
            @Summary
            private final String ifNoneExist;

            private volatile int hashCode;

            private Request(Builder builder) {
                super(builder);
                method = ValidationSupport.requireNonNull(builder.method, "method");
                url = ValidationSupport.requireNonNull(builder.url, "url");
                ifNoneMatch = builder.ifNoneMatch;
                ifModifiedSince = builder.ifModifiedSince;
                ifMatch = builder.ifMatch;
                ifNoneExist = builder.ifNoneExist;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * In a transaction or batch, this is the HTTP action to be executed for this entry. In a history bundle, this indicates 
             * the HTTP action that occurred.
             * 
             * @return
             *     An immutable object of type {@link HTTPVerb} that is non-null.
             */
            public HTTPVerb getMethod() {
                return method;
            }

            /**
             * The URL for this entry, relative to the root (the address to which the request is posted).
             * 
             * @return
             *     An immutable object of type {@link Uri} that is non-null.
             */
            public Uri getUrl() {
                return url;
            }

            /**
             * If the ETag values match, return a 304 Not Modified status. See the API documentation for ["Conditional Read"](http.
             * html#cread).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getIfNoneMatch() {
                return ifNoneMatch;
            }

            /**
             * Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.
             * html#cread).
             * 
             * @return
             *     An immutable object of type {@link Instant} that may be null.
             */
            public Instant getIfModifiedSince() {
                return ifModifiedSince;
            }

            /**
             * Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource 
             * Contention"](http.html#concurrency).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getIfMatch() {
                return ifMatch;
            }

            /**
             * Instruct the server not to perform the create if a specified resource already exists. For further information, see the 
             * API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what 
             * follows the "?" (not including the "?").
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getIfNoneExist() {
                return ifNoneExist;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (method != null) || 
                    (url != null) || 
                    (ifNoneMatch != null) || 
                    (ifModifiedSince != null) || 
                    (ifMatch != null) || 
                    (ifNoneExist != null);
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
                        accept(url, "url", visitor);
                        accept(ifNoneMatch, "ifNoneMatch", visitor);
                        accept(ifModifiedSince, "ifModifiedSince", visitor);
                        accept(ifMatch, "ifMatch", visitor);
                        accept(ifNoneExist, "ifNoneExist", visitor);
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
                Request other = (Request) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(method, other.method) && 
                    Objects.equals(url, other.url) && 
                    Objects.equals(ifNoneMatch, other.ifNoneMatch) && 
                    Objects.equals(ifModifiedSince, other.ifModifiedSince) && 
                    Objects.equals(ifMatch, other.ifMatch) && 
                    Objects.equals(ifNoneExist, other.ifNoneExist);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        method, 
                        url, 
                        ifNoneMatch, 
                        ifModifiedSince, 
                        ifMatch, 
                        ifNoneExist);
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
                private HTTPVerb method;
                private Uri url;
                private String ifNoneMatch;
                private Instant ifModifiedSince;
                private String ifMatch;
                private String ifNoneExist;

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
                 * In a transaction or batch, this is the HTTP action to be executed for this entry. In a history bundle, this indicates 
                 * the HTTP action that occurred.
                 * 
                 * <p>This element is required.
                 * 
                 * @param method
                 *     GET | HEAD | POST | PUT | DELETE | PATCH
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder method(HTTPVerb method) {
                    this.method = method;
                    return this;
                }

                /**
                 * The URL for this entry, relative to the root (the address to which the request is posted).
                 * 
                 * <p>This element is required.
                 * 
                 * @param url
                 *     URL for HTTP equivalent of this entry
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder url(Uri url) {
                    this.url = url;
                    return this;
                }

                /**
                 * If the ETag values match, return a 304 Not Modified status. See the API documentation for ["Conditional Read"](http.
                 * html#cread).
                 * 
                 * @param ifNoneMatch
                 *     For managing cache currency
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifNoneMatch(String ifNoneMatch) {
                    this.ifNoneMatch = ifNoneMatch;
                    return this;
                }

                /**
                 * Only perform the operation if the last updated date matches. See the API documentation for ["Conditional Read"](http.
                 * html#cread).
                 * 
                 * @param ifModifiedSince
                 *     For managing cache currency
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifModifiedSince(Instant ifModifiedSince) {
                    this.ifModifiedSince = ifModifiedSince;
                    return this;
                }

                /**
                 * Only perform the operation if the Etag value matches. For more information, see the API section ["Managing Resource 
                 * Contention"](http.html#concurrency).
                 * 
                 * @param ifMatch
                 *     For managing update contention
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifMatch(String ifMatch) {
                    this.ifMatch = ifMatch;
                    return this;
                }

                /**
                 * Instruct the server not to perform the create if a specified resource already exists. For further information, see the 
                 * API documentation for ["Conditional Create"](http.html#ccreate). This is just the query portion of the URL - what 
                 * follows the "?" (not including the "?").
                 * 
                 * @param ifNoneExist
                 *     For conditional creates
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder ifNoneExist(String ifNoneExist) {
                    this.ifNoneExist = ifNoneExist;
                    return this;
                }

                /**
                 * Build the {@link Request}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>method</li>
                 * <li>url</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Request}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Request per the base specification
                 */
                @Override
                public Request build() {
                    return new Request(this);
                }

                protected Builder from(Request request) {
                    super.from(request);
                    method = request.method;
                    url = request.url;
                    ifNoneMatch = request.ifNoneMatch;
                    ifModifiedSince = request.ifModifiedSince;
                    ifMatch = request.ifMatch;
                    ifNoneExist = request.ifNoneExist;
                    return this;
                }
            }
        }

        /**
         * Indicates the results of processing the corresponding 'request' entry in the batch or transaction being responded to 
         * or what the results of an operation where when returning history.
         */
        public static class Response extends BackboneElement {
            @Summary
            @Required
            private final String status;
            @Summary
            private final Uri location;
            @Summary
            private final String etag;
            @Summary
            private final Instant lastModified;
            @Summary
            private final Resource outcome;

            private volatile int hashCode;

            private Response(Builder builder) {
                super(builder);
                status = ValidationSupport.requireNonNull(builder.status, "status");
                location = builder.location;
                etag = builder.etag;
                lastModified = builder.lastModified;
                outcome = builder.outcome;
                ValidationSupport.requireValueOrChildren(this);
            }

            /**
             * The status code returned by processing this entry. The status SHALL start with a 3 digit HTTP code (e.g. 404) and may 
             * contain the standard HTTP description associated with the status code.
             * 
             * @return
             *     An immutable object of type {@link String} that is non-null.
             */
            public String getStatus() {
                return status;
            }

            /**
             * The location header created by processing this operation, populated if the operation returns a location.
             * 
             * @return
             *     An immutable object of type {@link Uri} that may be null.
             */
            public Uri getLocation() {
                return location;
            }

            /**
             * The Etag for the resource, if the operation for the entry produced a versioned resource (see [Resource Metadata and 
             * Versioning](http.html#versioning) and [Managing Resource Contention](http.html#concurrency)).
             * 
             * @return
             *     An immutable object of type {@link String} that may be null.
             */
            public String getEtag() {
                return etag;
            }

            /**
             * The date/time that the resource was modified on the server.
             * 
             * @return
             *     An immutable object of type {@link Instant} that may be null.
             */
            public Instant getLastModified() {
                return lastModified;
            }

            /**
             * An OperationOutcome containing hints and warnings produced as part of processing this entry in a batch or transaction.
             * 
             * @return
             *     An immutable object of type {@link Resource} that may be null.
             */
            public Resource getOutcome() {
                return outcome;
            }

            @Override
            public boolean hasChildren() {
                return super.hasChildren() || 
                    (status != null) || 
                    (location != null) || 
                    (etag != null) || 
                    (lastModified != null) || 
                    (outcome != null);
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
                        accept(status, "status", visitor);
                        accept(location, "location", visitor);
                        accept(etag, "etag", visitor);
                        accept(lastModified, "lastModified", visitor);
                        accept(outcome, "outcome", visitor);
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
                Response other = (Response) obj;
                return Objects.equals(id, other.id) && 
                    Objects.equals(extension, other.extension) && 
                    Objects.equals(modifierExtension, other.modifierExtension) && 
                    Objects.equals(status, other.status) && 
                    Objects.equals(location, other.location) && 
                    Objects.equals(etag, other.etag) && 
                    Objects.equals(lastModified, other.lastModified) && 
                    Objects.equals(outcome, other.outcome);
            }

            @Override
            public int hashCode() {
                int result = hashCode;
                if (result == 0) {
                    result = Objects.hash(id, 
                        extension, 
                        modifierExtension, 
                        status, 
                        location, 
                        etag, 
                        lastModified, 
                        outcome);
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
                private String status;
                private Uri location;
                private String etag;
                private Instant lastModified;
                private Resource outcome;

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
                 * The status code returned by processing this entry. The status SHALL start with a 3 digit HTTP code (e.g. 404) and may 
                 * contain the standard HTTP description associated with the status code.
                 * 
                 * <p>This element is required.
                 * 
                 * @param status
                 *     Status response code (text optional)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder status(String status) {
                    this.status = status;
                    return this;
                }

                /**
                 * The location header created by processing this operation, populated if the operation returns a location.
                 * 
                 * @param location
                 *     The location (if the operation returns a location)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder location(Uri location) {
                    this.location = location;
                    return this;
                }

                /**
                 * The Etag for the resource, if the operation for the entry produced a versioned resource (see [Resource Metadata and 
                 * Versioning](http.html#versioning) and [Managing Resource Contention](http.html#concurrency)).
                 * 
                 * @param etag
                 *     The Etag for the resource (if relevant)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder etag(String etag) {
                    this.etag = etag;
                    return this;
                }

                /**
                 * The date/time that the resource was modified on the server.
                 * 
                 * @param lastModified
                 *     Server's date time modified
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder lastModified(Instant lastModified) {
                    this.lastModified = lastModified;
                    return this;
                }

                /**
                 * An OperationOutcome containing hints and warnings produced as part of processing this entry in a batch or transaction.
                 * 
                 * @param outcome
                 *     OperationOutcome with hints and warnings (for batch/transaction)
                 * 
                 * @return
                 *     A reference to this Builder instance
                 */
                public Builder outcome(Resource outcome) {
                    this.outcome = outcome;
                    return this;
                }

                /**
                 * Build the {@link Response}
                 * 
                 * <p>Required elements:
                 * <ul>
                 * <li>status</li>
                 * </ul>
                 * 
                 * @return
                 *     An immutable object of type {@link Response}
                 * @throws IllegalStateException
                 *     if the current state cannot be built into a valid Response per the base specification
                 */
                @Override
                public Response build() {
                    return new Response(this);
                }

                protected Builder from(Response response) {
                    super.from(response);
                    status = response.status;
                    location = response.location;
                    etag = response.etag;
                    lastModified = response.lastModified;
                    outcome = response.outcome;
                    return this;
                }
            }
        }
    }
}
