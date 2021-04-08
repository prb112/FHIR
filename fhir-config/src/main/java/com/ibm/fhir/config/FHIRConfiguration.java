/*
 * (C) Copyright IBM Corp. 2016, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * This class serves up a singleton instance of ConfigurationService containing the FHIR Server's configuration.
 */
public class FHIRConfiguration {
    private static final Logger log = Logger.getLogger(FHIRConfiguration.class.getName());

    public static final String CONFIG_LOCATION = "config";
    public static final String CONFIG_FILE_BASENAME = "fhir-server-config.json";
    public static final String DEFAULT_TENANT_ID = "default";
    public static final String DEFAULT_DATASTORE_ID = "default";

    // Core server properties
    public static final String PROPERTY_ORIGINAL_REQUEST_URI_HEADER_NAME = "fhirServer/core/originalRequestUriHeaderName";
    public static final String PROPERTY_TENANT_ID_HEADER_NAME = "fhirServer/core/tenantIdHeaderName";
    public static final String PROPERTY_DATASTORE_ID_HEADER_NAME = "fhirServer/core/datastoreIdHeaderName";
    public static final String PROPERTY_DEFAULT_TENANT_ID = "fhirServer/core/defaultTenantId";
    public static final String PROPERTY_DEFAULT_PRETTY_PRINT = "fhirServer/core/defaultPrettyPrint";
    public static final String PROPERTY_DEFAULT_HANDLING = "fhirServer/core/defaultHandling";
    public static final String PROPERTY_ALLOW_CLIENT_HANDLING_PREF = "fhirServer/core/allowClientHandlingPref";
    public static final String PROPERTY_CHECK_REFERENCE_TYPES = "fhirServer/core/checkReferenceTypes";
    public static final String PROPERTY_CONDITIONAL_DELETE_MAX_NUMBER = "fhirServer/core/conditionalDeleteMaxNumber";
    public static final String PROPERTY_SERVER_REGISTRY_RESOURCE_PROVIDER_ENABLED = "fhirServer/core/serverRegistryResourceProviderEnabled";
    public static final String PROPERTY_CAPABILITY_STATEMENT_CACHE = "fhirServer/core/capabilityStatementCacheTimeout";
    public static final String PROPERTY_EXTENDED_CODEABLE_CONCEPT_VALIDATION = "fhirServer/core/extendedCodeableConceptValidation";
    public static final String PROPERTY_DISABLED_OPERATIONS = "fhirServer/core/disabledOperations";

    // Terminology service properties
    public static final String PROPERTY_GRAPH_TERM_SERVICE_PROVIDER_ENABLED = "fhirServer/term/graphTermServiceProvider/enabled";
    public static final String PROPERTY_GRAPH_TERM_SERVICE_PROVIDER_TIME_LIMIT = "fhirServer/term/graphTermServiceProvider/timeLimit";
    public static final String PROPERTY_GRAPH_TERM_SERVICE_PROVIDER_CONFIGURATION = "fhirServer/term/graphTermServiceProvider/configuration";

    // Resources properties
    public static final String PROPERTY_RESOURCES = "fhirServer/resources";
    public static final String PROPERTY_FIELD_RESOURCES_OPEN = "open";
    public static final String PROPERTY_FIELD_RESOURCES_INTERACTIONS = "interactions";
    public static final String PROPERTY_FIELD_RESOURCES_SEARCH_PARAMETERS = "searchParameters";
    public static final String PROPERTY_FIELD_RESOURCES_SEARCH_INCLUDES = "searchIncludes";
    public static final String PROPERTY_FIELD_RESOURCES_SEARCH_REV_INCLUDES = "searchRevIncludes";
    public static final String PROPERTY_FIELD_RESOURCES_SEARCH_PARAMETER_COMBINATIONS = "searchParameterCombinations";
    public static final String PROPERTY_FIELD_RESOURCES_PROFILES = "profiles";
    public static final String PROPERTY_FIELD_RESOURCES_PROFILES_AT_LEAST_ONE = "atLeastOne";
    public static final String PROPERTY_USE_STORED_COMPARTMENT_PARAM = "fhirServer/search/useStoredCompartmentParam";

    // Auth and security properties
    public static final String PROPERTY_SECURITY_CORS = "fhirServer/security/cors";
    public static final String PROPERTY_SECURITY_BASIC_ENABLED = "fhirServer/security/basic/enabled";
    public static final String PROPERTY_SECURITY_CERT_ENABLED = "fhirServer/security/certificates/enabled";
    public static final String PROPERTY_SECURITY_OAUTH_ENABLED = "fhirServer/security/oauth/enabled";
    public static final String PROPERTY_SECURITY_OAUTH_REG_URL = "fhirServer/security/oauth/regUrl";
    public static final String PROPERTY_SECURITY_OAUTH_AUTH_URL = "fhirServer/security/oauth/authUrl";
    public static final String PROPERTY_SECURITY_OAUTH_TOKEN_URL = "fhirServer/security/oauth/tokenUrl";
    public static final String PROPERTY_SECURITY_OAUTH_MANAGE_URL = "fhirServer/security/oauth/manageUrl";
    public static final String PROPERTY_SECURITY_OAUTH_INTROSPECT_URL = "fhirServer/security/oauth/introspectUrl";
    public static final String PROPERTY_SECURITY_OAUTH_REVOKE_URL = "fhirServer/security/oauth/smart/revokeUrl";
    public static final String PROPERTY_SECURITY_OAUTH_SMART_ENABLED = "fhirServer/security/oauth/smart/enabled";
    public static final String PROPERTY_SECURITY_OAUTH_SMART_SCOPES = "fhirServer/security/oauth/smart/scopes";
    public static final String PROPERTY_SECURITY_OAUTH_SMART_CAPABILITIES = "fhirServer/security/oauth/smart/capabilities";

    // Audit config properties
    public static final String PROPERTY_AUDIT_SERVICE_CLASS_NAME = "fhirServer/audit/serviceClassName";
    public static final String PROPERTY_AUDIT_SERVICE_PROPERTIES = "fhirServer/audit/serviceProperties";
    public static final String PROPERTY_AUDIT_PATIENT_ID_EXTURL = "fhirServer/audit/patientIdExtensionUrl";
    public static final String PROPERTY_AUDIT_HOSTNAME = "fhirServer/audit/hostname";
    public static final String PROPERTY_AUDIT_IP = "fhirServer/audit/ip";

    // Notification config properties
    public static final String PROPERTY_NOTIFICATION_RESOURCE_TYPES = "fhirServer/notifications/common/includeResourceTypes";
    public static final String PROPERTY_WEBSOCKET_ENABLED = "fhirServer/notifications/websocket/enabled";
    public static final String PROPERTY_KAFKA_ENABLED = "fhirServer/notifications/kafka/enabled";
    public static final String PROPERTY_KAFKA_TOPICNAME = "fhirServer/notifications/kafka/topicName";
    public static final String PROPERTY_KAFKA_CONNECTIONPROPS = "fhirServer/notifications/kafka/connectionProperties";
    public static final String PROPERTY_NATS_ENABLED = "fhirServer/notifications/nats/enabled";
    public static final String PROPERTY_NATS_CLUSTER = "fhirServer/notifications/nats/cluster";
    public static final String PROPERTY_NATS_CHANNEL = "fhirServer/notifications/nats/channel";
    public static final String PROPERTY_NATS_CLIENT = "fhirServer/notifications/nats/client";
    public static final String PROPERTY_NATS_SERVERS = "fhirServer/notifications/nats/servers";
    public static final String PROPERTY_NATS_TLS_ENABLED = "fhirServer/notifications/nats/useTLS";
    public static final String PROPERTY_NATS_TRUSTSTORE = "fhirServer/notifications/nats/truststoreLocation";
    public static final String PROPERTY_NATS_TRUSTSTORE_PW = "fhirServer/notifications/nats/truststorePassword";
    public static final String PROPERTY_NATS_KEYSTORE = "fhirServer/notifications/nats/keystoreLocation";
    public static final String PROPERTY_NATS_KEYSTORE_PW = "fhirServer/notifications/nats/keystorePassword";

    // Persistence layer properties
    public static final String PROPERTY_UPDATE_CREATE_ENABLED = "fhirServer/persistence/common/updateCreateEnabled";
    public static final String PROPERTY_PERSISTENCE_FACTORY = "fhirServer/persistence/factoryClassname";
    public static final String PROPERTY_DATASOURCES = "fhirServer/persistence/datasources";
    @Deprecated
    public static final String PROPERTY_JDBC_DATASOURCE_JNDINAME = "fhirServer/persistence/jdbc/dataSourceJndiName";
    @Deprecated
    public static final String PROPERTY_JDBC_ENABLE_PROXY_DATASOURCE = "fhirServer/persistence/jdbc/enableProxyDatasource";
    public static final String PROPERTY_JDBC_ENABLE_READ_ONLY_REPLICAS = "fhirServer/persistence/jdbc/enableReadOnlyReplicas";
    public static final String PROPERTY_JDBC_ENABLE_CODE_SYSTEMS_CACHE = "fhirServer/persistence/jdbc/enableCodeSystemsCache";
    public static final String PROPERTY_JDBC_ENABLE_PARAMETER_NAMES_CACHE = "fhirServer/persistence/jdbc/enableParameterNamesCache";
    public static final String PROPERTY_JDBC_ENABLE_RESOURCE_TYPES_CACHE = "fhirServer/persistence/jdbc/enableResourceTypesCache";
    public static final String PROPERTY_JDBC_EXTERNAL_REF_SYSTEM_CACHE_SIZE = "fhirServer/persistence/jdbc/externalRefSystemCacheSize";
    public static final String PROPERTY_JDBC_EXTERNAL_REF_VALUE_CACHE_SIZE = "fhirServer/persistence/jdbc/externalRefValueCacheSize";

    // Optimizer options within a datasource definition
    public static final String PROPERTY_JDBC_SEARCH_OPTIMIZER_OPTIONS = "searchOptimizerOptions";

    // fhir-search - Bounding area
    public static final String PROPERTY_SEARCH_BOUNDING_AREA_RADIUS_TYPE = "fhirServer/search/useBoundingRadius";

    // bulkdata
    // JavaBatch Job id encryption key
    public static final String PROPERTY_BULKDATA_BATCHJOBID_ENCRYPTION_KEY = "fhirServer/bulkdata/bulkDataBatchJobIdEncryptionKey";
    // JavaBatch Job parameters
    public static final String PROPERTY_BULKDATA_BATCHJOB_PARAMETERS  = "fhirServer/bulkdata/jobParameters";
    public static final String PROPERTY_BULKDATA_BATCHJOB_APPLICATIONNAME = "fhirServer/bulkdata/applicationName";
    public static final String PROPERTY_BULKDATA_BATCHJOB_MODULENAME = "fhirServer/bulkdata/moduleName";
    public static final String PROPERTY_BULKDATA_BATCHJOB_JOBXMLNAME = "fhirServer/bulkdata/jobXMLName";
    public static final String PROPERTY_BULKDATA_BATCHJOB_IMPTYPE = "fhirServer/bulkdata/implementation_type";
    public static final String PROPERTY_BULKDATA_BATCHJOB_BATCHURI = "fhirServer/bulkdata/batch-uri";
    public static final String PROPERTY_BULKDATA_BATCHJOB_BATCHUSER = "fhirServer/bulkdata/batch-user";
    public static final String PROPERTY_BULKDATA_BATCHJOB_BATCHUSERPWD = "fhirServer/bulkdata/batch-user-password";
    public static final String PROPERTY_BULKDATA_BATCHJOB_BATCHTRUSTSTORE = "fhirServer/bulkdata/batch-truststore";
    public static final String PROPERTY_BULKDATA_BATCHJOB_BATCHTRUSTSTOREPWD = "fhirServer/bulkdata/batch-truststore-password";
    public static final String PROPERTY_BULKDATA_BATCHJOB_ISEXPORTPUBLIC = "fhirServer/bulkdata/isExportPublic";
    public static final String PROPERTY_BULKDATA_BATCHJOB_USEFHIRSERVERTRUSTSTORE = "fhirServer/bulkdata/useFhirServerTrustStore";
    public static final String PROPERTY_BULKDATA_BATCHJOB_ENABLEPARQUET = "fhirServer/bulkdata/enableParquet";
    public static final String PROPERTY_BULKDATA_BATCHJOB_SYSTEMEXPIMPL = "fhirServer/bulkdata/systemExportImpl";

    public static final String PROPERTY_BULKDATA_BATCHJOB_VALID_BASE_URLS = "fhirServer/bulkdata/validBaseUrls";
    public static final String PROPERTY_BULKDATA_BATCHJOB_VALID_URLS_DISABLED = "fhirServer/bulkdata/validBaseUrlsDisabled";
    public static final String PROPERTY_BULKDATA_BATCHJOB_MAX_INPUT_PER_TENANT =
            "fhirServer/bulkdata/maxInputPerRequest";
    public static final String PROPERTY_BULKDATA_BATCHJOB_COSFILEMAXSIZE = "fhirServer/bulkdata/cosFileMaxSize";
    public static final String PROPERTY_BULKDATA_BATCHJOB_COSFILEMAXRESOURCES = "fhirServer/bulkdata/cosFileMaxResources";
    public static final String PROPERTY_BULKDATA_PATIENTEXPORT_PAGESIZE = "fhirServer/bulkdata/patientExportPageSize";
    // Control if push OperationOutcomes to COS/S3.
    public static final String PROPERTY_BULKDATA_IGNORE_IMPORT_OPERATION_OUTCOMES = "fhirServer/bulkdata/ignoreImportOutcomes";

    // Custom header names
    public static final String DEFAULT_TENANT_ID_HEADER_NAME = "X-FHIR-TENANT-ID";
    public static final String DEFAULT_DATASTORE_ID_HEADER_NAME = "X-FHIR-DSID";
    public static final String DEFAULT_PRETTY_RESPONSE_HEADER_NAME = "X-FHIR-FORMATTED";

    public static final String FHIR_SERVER_DEFAULT_CONFIG = "config/default/fhir-server-config.json";

    // Optional "home directory" for config files.  Defaults to current directory.
    private static String configHome = "";

    private static FHIRConfiguration _instance = new FHIRConfiguration();

    public static FHIRConfiguration getInstance() {
        return _instance;
    }

    /**
     * This is our in-memory cache of PropertyGroup's keyed by tenant-id.
     */
    private TenantSpecificPropertyGroupCache configCache = new TenantSpecificPropertyGroupCache();

    /**
     * This method is used to configure an explicit top-level directory where FHIR Server configuration
     * information is expected to reside.
     * <p>
     * For example, by calling this method with value "/mydir", then we'd expect
     * to find config files whose names are of the form: {@code "/mydir/config/<tenant-id>/fhir-server-config.json"}
     * <p>
     * The default location for config files is the current working directory (i.e. "" - the empty string).
     * @param s the new config home directory name
     */
    public static void setConfigHome(String s) {
        if (s == null) {
            s = "";
        }
        if (!s.isEmpty() && !s.endsWith(File.separator)) {
            s += File.separator;
        }

        configHome = s;
    }

    /**
     * Returns the "home" directory for FHIR Server configuration information (this directory will contain
     * the "config" directory, etc.).
     * <p>
     * The default value of this property is "" which is interpreted to mean the current working directory
     * (which for a running FHIR Server will be $WLP_HOME/wlp/usr/servers/fhir-server).
     */
    public static String getConfigHome() {
        return configHome;
    }

    /**
     * Retrieves the FHIR Server default configuration and returns it as a PropertyGroup.
     *
     * @return the top-level property group of the default tenant or null if it doesn't exist
     * @throws Exception if the configuration file was found but couldn't be loaded
     */
    public PropertyGroup loadConfiguration() throws Exception {
        return loadConfigurationForTenant(DEFAULT_TENANT_ID);
    }

    /**
     * Loads the configuration for the specified tenant id.
     *
     * @param tenantId
     *            a shortname representing the tenant whose configuration will be loaded
     * @return the top-level property group representing this tenant's configuration or null if it doesn't exist
     * @throws Exception
     */
    public PropertyGroup loadConfigurationForTenant(String tenantId) throws Exception {
        return configCache.getCachedObjectForTenant(tenantId);
    }

    /**
     * Clears the entire cache of configuration objects. This can be used perhaps during testing when you need to clear
     * and re-load the configuration.
     */
    public void clearConfiguration() {
        synchronized (configCache) {
            configCache.clearCache();
        }
    }

    /**
     * This method returns the list of tenant id's for which a configuration exists.
     * @return
     */
    public List<String> getConfiguredTenants() {
        log.entering(this.getClass().getName(), "getConfiguredTenants");

        try {
            List<String> result = new ArrayList<>();

            // 'configDir' represents the directory that contains the tenant ids
            // Example: "/opt/ibm/fhir-server/wlp/usr/servers/fhir-server/config".
            File configDir = new File(getConfigHome() + CONFIG_LOCATION);
            log.fine("Listing tenant id's rooted at directory: " + configDir.getName());

            // List the directories within 'configDir' that contain a fhir-server-config.json file.
            for (File f : configDir.listFiles()) {
                // For a directory, let's verify that a config exists within it.
                // If yes, then add the name of the directory to the result list, as that
                // represents a tenant id.
                if (f.isDirectory()) {
                    File configFile = new File(f, CONFIG_FILE_BASENAME);
                    if (configFile.exists() && configFile.isFile()) {
                        result.add(f.getName());
                    }
                }
            }

            log.fine("Returning list of tenant ids: " + result.toString());

            return result;
        } finally {
            log.exiting(this.getClass().getName(), "getConfiguredTenants");
        }
    }
}
