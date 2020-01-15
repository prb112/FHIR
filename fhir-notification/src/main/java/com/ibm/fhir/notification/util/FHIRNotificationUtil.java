/*
 * (C) Copyright IBM Corp. 2016, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.notification.util;

import java.io.StringReader;

import javax.json.Json;
import javax.json.JsonBuilderFactory;
import javax.json.JsonException;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.json.JsonReader;
import javax.json.JsonReaderFactory;

import com.ibm.fhir.exception.FHIRException;
import com.ibm.fhir.model.util.JsonSupport;
import com.ibm.fhir.notification.FHIRNotificationEvent;

public class FHIRNotificationUtil {
    private static final JsonReaderFactory JSON_READER_FACTORY = Json.createReaderFactory(null);
    private static final JsonBuilderFactory JSON_BUILDER_FACTORY = Json.createBuilderFactory(null);
    public static FHIRNotificationEvent toNotificationEvent(String jsonString) {
        try (JsonReader reader = JSON_READER_FACTORY.createReader(new StringReader(jsonString))) {
            JsonObject jsonObject = reader.readObject();
            FHIRNotificationEvent event = new FHIRNotificationEvent();
            event.setOperationType(jsonObject.getString("operationType"));
            event.setLocation(jsonObject.getString("location"));
            event.setLastUpdated(jsonObject.getString("lastUpdated"));
            event.setResourceId(jsonObject.getString("resourceId"));
            return event;
        } catch (JsonException e) {
            System.out.println("Failed to parse json string: " + e.getLocalizedMessage());
            return null;
        }
    }
    
    /**
     * Serializes the notification event into a JSON string.
     * @param event the FHIRNotificationEvent structure to be serialized
     * @param includeResource a flag that controls whether or not the resource object within
     * the event structure should be included in the serialized message.
     * @return the serialized message as a String
     * @throws FHIRException 
     */
    public static String toJsonString(FHIRNotificationEvent event, boolean includeResource) throws FHIRException {
        JsonObjectBuilder builder = JSON_BUILDER_FACTORY.createObjectBuilder();
        builder.add("lastUpdated", event.getLastUpdated());
        builder.add("location", event.getLocation());
        builder.add("operationType", event.getOperationType());
        builder.add("resourceId", event.getResourceId());
        if (includeResource && event.getResource() != null) {
            builder.add("resource", JsonSupport.toJsonObject(event.getResource()));
        }
        JsonObject jsonObject = builder.build();
        String jsonString = jsonObject.toString();
        return jsonString;
    }
}
