/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.model.type.code;

import com.ibm.fhir.model.annotation.System;
import com.ibm.fhir.model.type.Code;
import com.ibm.fhir.model.type.Extension;
import com.ibm.fhir.model.type.String;

import java.util.Collection;
import java.util.Objects;

import javax.annotation.Generated;

@System("http://terminology.hl7.org/CodeSystem/standards-status")
@Generated("com.ibm.fhir.tools.CodeGenerator")
public class StandardsStatus extends Code {
    /**
     * Draft
     * 
     * <p>This portion of the specification is not considered to be complete enough or sufficiently reviewed to be safe for 
     * implementation. It may have known issues or still be in the "in development" stage. It is included in the publication 
     * as a place-holder, to solicit feedback from the implementation community and/or to give implementers some insight as 
     * to functionality likely to be included in future versions of the specification. Content at this level should only be 
     * implemented by the brave or desperate and is very much "use at your own risk". The content that is Draft that will 
     * usually be elevated to Trial Use once review and correction is complete after it has been subjected to ballot.
     */
    public static final StandardsStatus DRAFT = StandardsStatus.builder().value(ValueSet.DRAFT).build();

    /**
     * Normative
     * 
     * <p>This content has been subject to review and production implementation in a wide variety of environments. The 
     * content is considered to be stable and has been 'locked', subjecting it to FHIR Inter-version Compatibility Rules. 
     * While changes are possible, they are expected to be infrequent and are tightly constrained. Compatibility Rules.
     */
    public static final StandardsStatus NORMATIVE = StandardsStatus.builder().value(ValueSet.NORMATIVE).build();

    /**
     * Trial-Use
     * 
     * <p>This content has been well reviewed and is considered by the authors to be ready for use in production systems. It 
     * has been subjected to ballot and approved as an official standard. However, it has not yet seen widespread use in 
     * production across the full spectrum of environments it is intended to be used in. In some cases, there may be 
     * documented known issues that require implementation experience to determine appropriate resolutions for.
     * 
     * <p>Future versions of FHIR may make significant changes to Trial Use content that are not compatible with previously 
     * published content.
     */
    public static final StandardsStatus TRIAL_USE = StandardsStatus.builder().value(ValueSet.TRIAL_USE).build();

    /**
     * Informative
     * 
     * <p>This portion of the specification is provided for implementer assistance, and does not make rules that implementers 
     * are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, 
     * examples, and implementer advice.
     */
    public static final StandardsStatus INFORMATIVE = StandardsStatus.builder().value(ValueSet.INFORMATIVE).build();

    /**
     * Deprecated
     * 
     * <p>This portion of the specification is provided for implementer assistance, and does not make rules that implementers 
     * are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, 
     * examples, and implementer advice.
     */
    public static final StandardsStatus DEPRECATED = StandardsStatus.builder().value(ValueSet.DEPRECATED).build();

    /**
     * External
     * 
     * <p>This is content that is managed outside the FHIR Specification, but included for implementer convenience.
     */
    public static final StandardsStatus EXTERNAL = StandardsStatus.builder().value(ValueSet.EXTERNAL).build();

    private volatile int hashCode;

    private StandardsStatus(Builder builder) {
        super(builder);
    }

    public ValueSet getValueAsEnumConstant() {
        return (value != null) ? ValueSet.from(value) : null;
    }

    /**
     * Factory method for creating StandardsStatus objects from a passed enum value.
     */
    public static StandardsStatus of(ValueSet value) {
        switch (value) {
        case DRAFT:
            return DRAFT;
        case NORMATIVE:
            return NORMATIVE;
        case TRIAL_USE:
            return TRIAL_USE;
        case INFORMATIVE:
            return INFORMATIVE;
        case DEPRECATED:
            return DEPRECATED;
        case EXTERNAL:
            return EXTERNAL;
        default:
            throw new IllegalStateException(value.name());
        }
    }

    /**
     * Factory method for creating StandardsStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static StandardsStatus of(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating StandardsStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static String string(java.lang.String value) {
        return of(ValueSet.from(value));
    }

    /**
     * Inherited factory method for creating StandardsStatus objects from a passed string value.
     * 
     * @param value
     *     A string that matches one of the allowed code values
     * @throws IllegalArgumentException
     *     If the passed string cannot be parsed into an allowed code value
     */
    public static Code code(java.lang.String value) {
        return of(ValueSet.from(value));
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
        StandardsStatus other = (StandardsStatus) obj;
        return Objects.equals(id, other.id) && Objects.equals(extension, other.extension) && Objects.equals(value, other.value);
    }

    @Override
    public int hashCode() {
        int result = hashCode;
        if (result == 0) {
            result = Objects.hash(id, extension, value);
            hashCode = result;
        }
        return result;
    }

    public Builder toBuilder() {
        Builder builder = new Builder();
        builder.id(id);
        builder.extension(extension);
        builder.value(value);
        return builder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends Code.Builder {
        private Builder() {
            super();
        }

        @Override
        public Builder id(java.lang.String id) {
            return (Builder) super.id(id);
        }

        @Override
        public Builder extension(Extension... extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder extension(Collection<Extension> extension) {
            return (Builder) super.extension(extension);
        }

        @Override
        public Builder value(java.lang.String value) {
            return (value != null) ? (Builder) super.value(ValueSet.from(value).value()) : this;
        }

        public Builder value(ValueSet value) {
            return (value != null) ? (Builder) super.value(value.value()) : this;
        }

        @Override
        public StandardsStatus build() {
            return new StandardsStatus(this);
        }
    }

    public enum ValueSet {
        /**
         * Draft
         * 
         * <p>This portion of the specification is not considered to be complete enough or sufficiently reviewed to be safe for 
         * implementation. It may have known issues or still be in the "in development" stage. It is included in the publication 
         * as a place-holder, to solicit feedback from the implementation community and/or to give implementers some insight as 
         * to functionality likely to be included in future versions of the specification. Content at this level should only be 
         * implemented by the brave or desperate and is very much "use at your own risk". The content that is Draft that will 
         * usually be elevated to Trial Use once review and correction is complete after it has been subjected to ballot.
         */
        DRAFT("draft"),

        /**
         * Normative
         * 
         * <p>This content has been subject to review and production implementation in a wide variety of environments. The 
         * content is considered to be stable and has been 'locked', subjecting it to FHIR Inter-version Compatibility Rules. 
         * While changes are possible, they are expected to be infrequent and are tightly constrained. Compatibility Rules.
         */
        NORMATIVE("normative"),

        /**
         * Trial-Use
         * 
         * <p>This content has been well reviewed and is considered by the authors to be ready for use in production systems. It 
         * has been subjected to ballot and approved as an official standard. However, it has not yet seen widespread use in 
         * production across the full spectrum of environments it is intended to be used in. In some cases, there may be 
         * documented known issues that require implementation experience to determine appropriate resolutions for.
         * 
         * <p>Future versions of FHIR may make significant changes to Trial Use content that are not compatible with previously 
         * published content.
         */
        TRIAL_USE("trial-use"),

        /**
         * Informative
         * 
         * <p>This portion of the specification is provided for implementer assistance, and does not make rules that implementers 
         * are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, 
         * examples, and implementer advice.
         */
        INFORMATIVE("informative"),

        /**
         * Deprecated
         * 
         * <p>This portion of the specification is provided for implementer assistance, and does not make rules that implementers 
         * are required to follow. Typical examples of this content in the FHIR specification are tables of contents, registries, 
         * examples, and implementer advice.
         */
        DEPRECATED("deprecated"),

        /**
         * External
         * 
         * <p>This is content that is managed outside the FHIR Specification, but included for implementer convenience.
         */
        EXTERNAL("external");

        private final java.lang.String value;

        ValueSet(java.lang.String value) {
            this.value = value;
        }

        /**
         * @return
         *     The java.lang.String value of the code represented by this enum
         */
        public java.lang.String value() {
            return value;
        }

        /**
         * Factory method for creating StandardsStatus.ValueSet values from a passed string value.
         * 
         * @param value
         *     A string that matches one of the allowed code values
         * @throws IllegalArgumentException
         *     If the passed string cannot be parsed into an allowed code value
         */
        public static ValueSet from(java.lang.String value) {
            for (ValueSet c : ValueSet.values()) {
                if (c.value.equals(value)) {
                    return c;
                }
            }
            throw new IllegalArgumentException(value);
        }
    }
}
