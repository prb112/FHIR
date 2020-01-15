/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.search.parameters;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.ibm.fhir.search.SearchConstants;
import com.ibm.fhir.search.SearchConstants.Prefix;

/**
 * A search parameter value for a given search parameter that was passed in a search query
 */
public class QueryParameterValue {

    private Prefix prefix = null;

    private String valueString = null;

    private Instant valueDate = null;
    private Instant valueDateLowerBound = null;
    private Instant valueDateUpperBound = null;

    // Used for number and quantity search parameters
    private BigDecimal valueNumber = null;

    // Used for quantity and token search parameters
    private String valueSystem = null;
    private String valueCode = null;

    // Used for composite search parameters
    private List<QueryParameter> component = new ArrayList<>();
    
    // The delimiter starts off as EMPTY and goes to SearchConstants.PARAMETER_DELIMETER
    private String delim = "";

    public QueryParameterValue() {
        // No Operation
    }

    public void setPrefix(Prefix prefix) {
        this.prefix = prefix;
    }

    public QueryParameterValue withPrefix(Prefix prefix) {
        setPrefix(prefix);
        return this;
    }

    public Prefix getPrefix() {
        return prefix;
    }

    public String getValueString() {
        return valueString;
    }

    public void setValueString(String valueString) {
        this.valueString = valueString;
    }

    public Instant getValueDate() {
        return valueDate;
    }

    public void setValueDate(Instant valueDate) {
        this.valueDate = valueDate;
    }

    public BigDecimal getValueNumber() {
        return valueNumber;
    }

    public void setValueNumber(BigDecimal valueNumber) {
        this.valueNumber = valueNumber;
    }

    public String getValueSystem() {
        return valueSystem;
    }

    public void setValueSystem(String valueSystem) {
        this.valueSystem = valueSystem;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public Instant getValueDateLowerBound() {
        return valueDateLowerBound;
    }

    public void setValueDateLowerBound(Instant valueDateLowerBound) {
        this.valueDateLowerBound = valueDateLowerBound;
    }

    public Instant getValueDateUpperBound() {
        return valueDateUpperBound;
    }

    public void setValueDateUpperBound(Instant valueDateUpperBound) {
        this.valueDateUpperBound = valueDateUpperBound;
    }

    /**
     * @return the component
     */
    public List<QueryParameter> getComponent() {
        return component;
    }

    /**
     * @param component the components to add
     */
    public void addComponent(QueryParameter... component) {
        for (QueryParameter c : component) {
            this.component.add(c);
        }
    }

    /**
     * @param component the component to set
     */
    public void setComponent(Collection<QueryParameter> component) {
        this.component = new ArrayList<>(component);
    }
    /**
     * Serialize the ParameterValue to a query parameter string
     */
    @Override
    public String toString() {
        StringBuilder returnString = new StringBuilder();

        if (prefix != null) {
            returnString.append(prefix.value());
        }

        // Order is important; currently this ordering works for all search parameter types
        // quantity: [prefix]number|system|code
        // token: system|code
        // string/uri/reference: string
        // number: [prefix]number
        // date: [prefix]date

        // token search like "|code" is supported by setting the valueSystem to "" (NO_SYSTEM)
        outputBuilder(returnString, valueNumber);
        outputBuilder(returnString, valueSystem);
        outputBuilder(returnString, valueCode);
        outputBuilder(returnString, valueString);
        outputBuilder(returnString, valueDateLowerBound);

        if (component != null && !component.isEmpty()) {
            String componentDelim = "";
            for (QueryParameter componentParam : component) {
                List<QueryParameterValue> componentValues = componentParam.getValues();
                if (componentValues.size() != 1) {
                    throw new IllegalStateException("Components of a composite search parameter may only have a single value");
                }
                returnString.append(componentDelim).append(componentValues.get(0));
                componentDelim = "$";
            }
        }
        return returnString.toString();
    }

    /**
     * simple build method to apply consistent usage of StringBuilder.
     * 
     * @param outputBuilder
     * @param value
     */
    private void outputBuilder(StringBuilder outputBuilder, Object value) {
        if (value != null) {
            outputBuilder.append(delim);
            outputBuilder.append(value);
            delim = SearchConstants.PARAMETER_DELIMITER;
        }
    }
}
