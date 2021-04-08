/*
 * (C) Copyright IBM Corp. 2019, 2020
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.registry.spi;

import java.util.Collection;
import java.util.Collections;

import com.ibm.fhir.model.resource.Resource;
import com.ibm.fhir.registry.resource.FHIRRegistryResource;

/**
 * An SPI for {@link FHIRRegistryResource} instances
 */
public interface FHIRRegistryResourceProvider {
    /**
     * Get the registry resource from this provider for the given resource type, url and version
     *
     * <p>If the version is null, then the latest version of the registry resource is returned (if available)
     *
     * @param resourceType
     *     the resource type of the registry resource
     * @param url
     *     the url of the registry resource
     * @param version
     *     the version of the registry resource (optional)
     * @return
     *     the registry resource from this provider for the given resource type, url and version if exists, null otherwise
     */
    FHIRRegistryResource getRegistryResource(Class<? extends Resource> resourceType, String url, String version);

    /**
     * Get the registry resources from this provider for the given resource type
     *
     * @param resourceType
     *     the resource type of the registry resource
     * @return
     *     the registry resources from this provider for the given resource type
     */
    Collection<FHIRRegistryResource> getRegistryResources(Class<? extends Resource> resourceType);

    /**
     * Get all the registry resources from this provider
     *
     * @return
     *     all of the registry resources from this provider
     */
    Collection<FHIRRegistryResource> getRegistryResources();

    /**
     * Get the profile resources from this provider that constrain the given resource type
     *
     * @param type
     *     the constrained resource type
     * @return
     *     the profile resources from this provider that constrain the given resource type
     */
    Collection<FHIRRegistryResource> getProfileResources(String type);

    /**
     * Get the search parameter resources from this provider with the given search parameter type
     * (e.g. string, token, etc.)
     *
     * @param type
     *     the search parameter type
     * @return
     *     the search parameter resources from this provider with the given search parameter type
     */
    Collection<FHIRRegistryResource> getSearchParameterResources(String type);

    /**
     * Get the profiles for all of the resources.
     *
     * @return
     *     the profile resources from this provider that constrain the resource types
     */
    default Collection<FHIRRegistryResource> getProfileResources() {
        return Collections.emptyList();
    };
}