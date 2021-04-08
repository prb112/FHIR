/*
 * (C) Copyright IBM Corp. 2019, 2021
 *
 * SPDX-License-Identifier: Apache-2.0
 */

package com.ibm.fhir.schema.app;

import static com.ibm.fhir.schema.app.util.CommonUtil.configureLogger;
import static com.ibm.fhir.schema.app.util.CommonUtil.getDbAdapter;
import static com.ibm.fhir.schema.app.util.CommonUtil.getPropertyAdapter;
import static com.ibm.fhir.schema.app.util.CommonUtil.getRandomKey;
import static com.ibm.fhir.schema.app.util.CommonUtil.loadDriver;
import static com.ibm.fhir.schema.app.util.CommonUtil.logClasspath;
import static com.ibm.fhir.schema.app.util.CommonUtil.printUsage;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.ibm.fhir.database.utils.api.DataAccessException;
import com.ibm.fhir.database.utils.api.DatabaseNotReadyException;
import com.ibm.fhir.database.utils.api.IDatabaseAdapter;
import com.ibm.fhir.database.utils.api.IDatabaseTranslator;
import com.ibm.fhir.database.utils.api.ITransaction;
import com.ibm.fhir.database.utils.api.ITransactionProvider;
import com.ibm.fhir.database.utils.api.TenantStatus;
import com.ibm.fhir.database.utils.common.DataDefinitionUtil;
import com.ibm.fhir.database.utils.common.JdbcConnectionProvider;
import com.ibm.fhir.database.utils.common.JdbcPropertyAdapter;
import com.ibm.fhir.database.utils.common.JdbcTarget;
import com.ibm.fhir.database.utils.db2.Db2Adapter;
import com.ibm.fhir.database.utils.db2.Db2GetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2SetTenantVariable;
import com.ibm.fhir.database.utils.db2.Db2Translator;
import com.ibm.fhir.database.utils.derby.DerbyTranslator;
import com.ibm.fhir.database.utils.model.DatabaseObjectType;
import com.ibm.fhir.database.utils.model.DbType;
import com.ibm.fhir.database.utils.model.PhysicalDataModel;
import com.ibm.fhir.database.utils.model.Table;
import com.ibm.fhir.database.utils.model.Tenant;
import com.ibm.fhir.database.utils.pool.PoolConnectionProvider;
import com.ibm.fhir.database.utils.postgres.PostgresTranslator;
import com.ibm.fhir.database.utils.tenant.AddTenantKeyDAO;
import com.ibm.fhir.database.utils.tenant.GetTenantDAO;
import com.ibm.fhir.database.utils.transaction.SimpleTransactionProvider;
import com.ibm.fhir.database.utils.transaction.TransactionFactory;
import com.ibm.fhir.database.utils.version.CreateVersionHistory;
import com.ibm.fhir.database.utils.version.VersionHistoryService;
import com.ibm.fhir.model.util.ModelSupport;
import com.ibm.fhir.schema.app.util.TenantKeyFileUtil;
import com.ibm.fhir.schema.control.BackfillResourceChangeLog;
import com.ibm.fhir.schema.control.BackfillResourceChangeLogDb2;
import com.ibm.fhir.schema.control.DisableForeignKey;
import com.ibm.fhir.schema.control.EnableForeignKey;
import com.ibm.fhir.schema.control.FhirSchemaConstants;
import com.ibm.fhir.schema.control.FhirSchemaGenerator;
import com.ibm.fhir.schema.control.GetResourceChangeLogEmpty;
import com.ibm.fhir.schema.control.GetResourceTypeList;
import com.ibm.fhir.schema.control.GetTenantInfo;
import com.ibm.fhir.schema.control.GetTenantList;
import com.ibm.fhir.schema.control.GetXXXLogicalResourceNeedsMigration;
import com.ibm.fhir.schema.control.InitializeLogicalResourceDenorms;
import com.ibm.fhir.schema.control.JavaBatchSchemaGenerator;
import com.ibm.fhir.schema.control.OAuthSchemaGenerator;
import com.ibm.fhir.schema.control.PopulateParameterNames;
import com.ibm.fhir.schema.control.PopulateResourceTypes;
import com.ibm.fhir.schema.control.SetTenantIdDb2;
import com.ibm.fhir.schema.control.TenantInfo;
import com.ibm.fhir.schema.model.ResourceType;
import com.ibm.fhir.schema.model.Schema;
import com.ibm.fhir.task.api.ITaskCollector;
import com.ibm.fhir.task.api.ITaskGroup;
import com.ibm.fhir.task.core.service.TaskService;

/**
 * Utility app to connect to a database and create/update the IBM FHIR Server schema.
 * The DDL processing is idempotent, with only the necessary changes applied.
 * <br>
 * This utility also includes an option to exercise the tenant partitioning code.
 */
public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    private static final int EXIT_OK = 0; // validation was successful
    private static final int EXIT_BAD_ARGS = 1; // invalid CLI arguments
    private static final int EXIT_RUNTIME_ERROR = 2; // programming error
    private static final int EXIT_VALIDATION_FAILED = 3; // validation test failed
    private static final int EXIT_NOT_READY = 4; // DATABASE NOT READY
    private static final double NANOS = 1e9;

    // Indicates if the feature is enabled for the DbType
    public List<DbType> MULTITENANT_FEATURE_ENABLED = Arrays.asList(DbType.DB2);
    public List<DbType> STORED_PROCEDURE_ENABLED = Arrays.asList(DbType.DB2, DbType.POSTGRESQL);
    public List<DbType> PRIVILEGES_FEATURE_ENABLED = Arrays.asList(DbType.DB2, DbType.POSTGRESQL);

    // Properties accumulated as we parse args and read configuration files
    private final Properties properties = new Properties();

    // Default Values for schema names
    public static final String ADMIN_SCHEMANAME = "FHIR_ADMIN";
    public static final String OAUTH_SCHEMANAME = "FHIR_OAUTH";
    public static final String BATCH_SCHEMANAME = "FHIR_JBATCH";
    public static final String DATA_SCHEMANAME = "FHIRDATA";

    // Force upper-case to avoid tricky-to-catch errors related to quoting names
    private Schema schema = new Schema();

    // Arguments requesting we drop the objects from the schema
    private boolean dropAdmin = false;
    private boolean confirmDrop = false;
    private boolean updateProc = false;
    private boolean checkCompatibility = false;

    // Action flags related to FHIR Schema
    private boolean createFhirSchema = false;
    private boolean updateFhirSchema = false;
    private boolean dropFhirSchema = false;
    private boolean grantFhirSchema = false;

    // Action flags related to OAuth Schema
    private boolean createOauthSchema = false;
    private boolean updateOauthSchema = false;
    private boolean dropOauthSchema = false;
    private boolean grantOauthSchema = false;

    // Action flags related to Java Batch Schema
    private boolean createJavaBatchSchema = false;
    private boolean updateJavaBatchSchema = false;
    private boolean dropJavaBatchSchema = false;
    private boolean grantJavaBatchSchema = false;

    // The database user we will grant tenant data access privileges to
    private String grantTo;

    // The database type being populated (default: Db2)
    private DbType dbType = DbType.DB2;
    private IDatabaseTranslator translator = new Db2Translator();

    // Optional subset of resource types (for faster schema builds when testing)
    private Set<String> resourceTypeSubset;

    // Tenant management
    private boolean allocateTenant;
    private boolean refreshTenants;
    private boolean dropTenant;
    private boolean freezeTenant;
    private String tenantName;
    private boolean testTenant;
    private String tenantKey;
    private boolean listTenants;
    private boolean dropDetached;
    private boolean deleteTenantMeta;

    // Tenant Key Output or Input File
    private String tenantKeyFileName;
    private TenantKeyFileUtil tenantKeyFileUtil = new TenantKeyFileUtil();

    // The tenant name for when we want to add a new tenant key
    private String addKeyForTenant;

    // What status to leave with
    private int exitStatus = EXIT_OK;

    // The connection pool and transaction provider to support concurrent operations
    private int maxConnectionPoolSize = FhirSchemaConstants.DEFAULT_POOL_SIZE;
    private PoolConnectionProvider connectionPool;
    private ITransactionProvider transactionProvider;

    //-----------------------------------------------------------------------------------------------------------------
    // The following method is related to the common methods and functions
    /**
     * @return a created connection to the selected database
     */
    protected Connection createConnection() {
        Properties connectionProperties = new Properties();
        JdbcPropertyAdapter adapter = getPropertyAdapter(dbType, properties);
        adapter.getExtraProperties(connectionProperties);

        String url = translator.getUrl(properties);
        logger.info("Opening connection to: " + url);
        Connection connection;
        try {
            connection = DriverManager.getConnection(url, connectionProperties);
            connection.setAutoCommit(false);
        } catch (SQLException x) {
            throw translator.translate(x);
        }
        return connection;
    }

    /**
     * Create a simple connection pool associated with our data source so that we
     * can perform the DDL deployment in parallel
     */
    protected void configureConnectionPool() {
        if (dbType == DbType.DERBY && maxConnectionPoolSize > 1) {
            logger.warning("Embedded Derby does not support concurrent schema updates;" +
                    " ignoring '--pool-size' and using a single thread.");
            this.maxConnectionPoolSize = 1;
        }
        JdbcPropertyAdapter adapter = getPropertyAdapter(dbType, properties);
        JdbcConnectionProvider cp = new JdbcConnectionProvider(this.translator, adapter);
        this.connectionPool = new PoolConnectionProvider(cp, this.maxConnectionPoolSize);
        this.transactionProvider = new SimpleTransactionProvider(this.connectionPool);
    }

    /**
     * builds the common model based on the flags passed in
     * @param pdm
     * @param fhirSchema - true indicates if the fhir model is added to the Physical Data Model
     * @param oauthSchema - true indicates if the oauth model is added to the Physical Data Model
     * @param javaBatchSchema - true indicates if the model is added to the Physical Data Model
     */
    protected void buildCommonModel(PhysicalDataModel pdm, boolean fhirSchema, boolean oauthSchema, boolean javaBatchSchema) {
        if (fhirSchema) {

            FhirSchemaGenerator gen;
            if (resourceTypeSubset == null || resourceTypeSubset.isEmpty()) {
                gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), isMultitenant());
            } else {
                gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), isMultitenant(), resourceTypeSubset);
            }

            gen.buildSchema(pdm);
            switch (dbType) {
            case DB2:
                gen.buildDatabaseSpecificArtifactsDb2(pdm);
                break;
            case DERBY:
                logger.info("No database specific artifacts");
                break;
            case POSTGRESQL:
                gen.buildDatabaseSpecificArtifactsPostgres(pdm);
                break;
            default:
                throw new IllegalStateException("Unsupported db type: " + dbType);
            }
        }

        // Build/update the Liberty OAuth-related tables
        if (oauthSchema) {
            OAuthSchemaGenerator oauthSchemaGenerator = new OAuthSchemaGenerator(schema.getOauthSchemaName());
            oauthSchemaGenerator.buildOAuthSchema(pdm);
        }

        // Build/update the Liberty JBatch related tables
        if (javaBatchSchema) {
            JavaBatchSchemaGenerator javaBatchSchemaGenerator = new JavaBatchSchemaGenerator(schema.getJavaBatchSchemaName());
            javaBatchSchemaGenerator.buildJavaBatchSchema(pdm);
        }
    }

    /**
     * Start the schema object creation tasks and wait for everything to complete
     *
     * @param pdm
     * @param adapter
     * @param collector
     * @param vhs
     */
    protected void applyModel(PhysicalDataModel pdm, IDatabaseAdapter adapter, ITaskCollector collector,
            VersionHistoryService vhs) {
        logger.info("Collecting model update tasks");
        pdm.collect(collector, adapter, this.transactionProvider, vhs);

        // FHIR in the hole!
        logger.info("Starting model updates");
        collector.startAndWait();

        Collection<ITaskGroup> failedTaskGroups = collector.getFailedTaskGroups();
        if (failedTaskGroups.size() > 0) {
            this.exitStatus = EXIT_RUNTIME_ERROR;

            final String failedStr =
                    failedTaskGroups.stream().map((tg) -> tg.getTaskId()).collect(Collectors.joining(","));
            logger.severe("List of failed task groups: " + failedStr);
        }
    }

    /**
     * specific feature to check if it is compatible.
     * @return
     */
    protected boolean checkCompatibility() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            return adapter.checkCompatibility(schema.getAdminSchemaName());
        }
    }

    /**
     * Create the schemas
     */
    protected void createSchemas() {
        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(dbType, target);

                    // We always create the 'admin' schema to track to the changes to any of the other schemas.
                    adapter.createSchema(schema.getAdminSchemaName());

                    // FHIR Data Schema
                    if (createFhirSchema) {
                        adapter.createSchema(schema.getSchemaName());
                        c.commit();
                    }

                    // OAuth Schema
                    if (createOauthSchema) {
                        adapter.createSchema(schema.getOauthSchemaName());
                    }

                    // Java Batch Schema
                    if (createJavaBatchSchema) {
                        adapter.createSchema(schema.getJavaBatchSchemaName());
                    }
                } catch (Exception x) {
                    c.rollback();
                    throw x;
                }
                c.commit();
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    /**
     * Update the schema
     */
    protected void updateSchema() {
        // Build/update the FHIR-related tables as well as the stored procedures
        PhysicalDataModel pdm = new PhysicalDataModel();
        buildCommonModel(pdm, updateFhirSchema, updateOauthSchema,updateJavaBatchSchema);

        // The objects are applied in parallel, which relies on each object
        // expressing its dependencies correctly. Changes are only applied
        // if their version is greater than the current version.
        TaskService taskService = new TaskService();
        ExecutorService pool = Executors.newFixedThreadPool(this.maxConnectionPoolSize);
        ITaskCollector collector = taskService.makeTaskCollector(pool);
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);

        // Before we start anything, we need to make sure our schema history
        // tables are in place. There's only a single history table, which
        // resides in the admin schema and handles the history of all objects
        // in any schema being managed.
        CreateVersionHistory.createTableIfNeeded(schema.getAdminSchemaName(), adapter);

        // Current version history for the data schema
        VersionHistoryService vhs = new VersionHistoryService(schema.getAdminSchemaName(), schema.getSchemaName(), schema.getOauthSchemaName(), schema.getJavaBatchSchemaName());
        vhs.setTransactionProvider(transactionProvider);
        vhs.setTarget(adapter);
        vhs.init();

        // Use the version history service to determine if this table existed before we run `applyWithHistory`
        boolean newDb = vhs.getVersion(schema.getSchemaName(), DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == null ||
                vhs.getVersion(schema.getSchemaName(), DatabaseObjectType.TABLE.name(), "PARAMETER_NAMES") == 0;

        applyModel(pdm, adapter, collector, vhs);
        // There is a working data model at this point.

        // If the db is multi-tenant, we populate the resource types and parameter names in allocate-tenant.
        // Otherwise, if its a new schema, populate the resource types and parameters names (codes) now
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType) && newDb ) {
            populateResourceTypeAndParameterNameTableEntries(null);
        }

        // Let's refresh the procedures and functions.
        logger.info("Refreshing procedures and functions");
        updateProcedures();

        if (MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            logger.info("Refreshing tenant partitions");
            refreshTenants();
        }

        // backfill the resource_change_log table if needed
        backfillResourceChangeLog();

        // perform any updates we need related to the V0010 schema change (IS_DELETED flag)
        applyDataMigrationForV0010();
    }

    /**
     * populates for the given tenantId the RESOURCE_TYPE table.
     *
     * @implNote if you update this method, be sure to update
     *           DerbyBootstrapper.populateResourceTypeAndParameterNameTableEntries
     *           and DerbyFhirDatabase.populateResourceTypeAndParameterNameTableEntries
     *           The reason is there are three different ways of managing the transaction.
     * @param tenantId the mt_id that is used to setup the partition.
     *                 passing in null signals not multi-tenant.
     */
    protected void populateResourceTypeAndParameterNameTableEntries(Integer tenantId) {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection();) {
                String logTenantId = tenantId != null ? Integer.toString(tenantId) : "default";
                logger.info("tenantId [" + logTenantId + "] is being pre-populated with lookup table data.");
                PopulateResourceTypes populateResourceTypes =
                        new PopulateResourceTypes(schema.getAdminSchemaName(), schema.getSchemaName(), tenantId);
                populateResourceTypes.run(translator, c);

                PopulateParameterNames populateParameterNames =
                        new PopulateParameterNames(schema.getAdminSchemaName(), schema.getSchemaName(), tenantId);
                populateParameterNames.run(translator, c);
                logger.info("Finished prepopulating the resource type and search parameter code/name tables tables");
            } catch (SQLException ex) {
                tx.setRollbackOnly();
                throw new DataAccessException(ex);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Drop all the objects in the admin and data schemas.
     * Typically used during development.
     */
    protected void dropSchema() {
        PhysicalDataModel pdm = new PhysicalDataModel();
        buildCommonModel(pdm, dropFhirSchema, dropOauthSchema, dropJavaBatchSchema);

        try {
            try (Connection c = createConnection()) {
                try {
                    JdbcTarget target = new JdbcTarget(c);
                    IDatabaseAdapter adapter = getDbAdapter(dbType, target);

                    if (dropFhirSchema || dropOauthSchema || dropJavaBatchSchema) {
                        // Just drop the objects associated with the FHIRDATA schema group
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.FHIRDATA_GROUP);
                    }

                    if (dropAdmin) {
                        // Just drop the objects associated with the ADMIN schema group
                        pdm.drop(adapter, FhirSchemaGenerator.SCHEMA_GROUP_TAG, FhirSchemaGenerator.ADMIN_GROUP);
                    }
                } catch (Exception x) {
                    c.rollback();
                    throw x;
                }
                c.commit();
            }
        } catch (SQLException x) {
            throw translator.translate(x);
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    // The following method is related to the Stored Procedures and Functions feature
    /**
     * Update the stored procedures used by FHIR to insert records
     * into the FHIR resource tables
     */
    protected void updateProcedures() {
        if (!STORED_PROCEDURE_ENABLED.contains(dbType)) {
            return;
        }

        // Build/update the tables as well as the stored procedures
        PhysicalDataModel pdm = new PhysicalDataModel();
        // Since this is a stored procedure, we need the model.
        // We must pass in true to flag to the underlying layer that the
        // Procedures need to be generated.
        buildCommonModel(pdm, true, updateOauthSchema, updateJavaBatchSchema);

        // Now only apply the procedures in the model. Much faster than
        // going through the whole schema
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try (Connection c = connectionPool.getConnection();) {
                try {
                    IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
                    pdm.applyProcedures(adapter);
                    pdm.applyFunctions(adapter);
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
                // Reminder: Don't fall into the trap, let the connectionPool and Transaction Management handle the transaction commit.
            } catch (SQLException x) {
                tx.setRollbackOnly();
                throw translator.translate(x);
            }
        }
    }

    //-----------------------------------------------------------------------------------------------------------------
    // The following method is related to the Privilege feature
    /**
     * Grant the minimum required set of privileges on the FHIR schema objects
     * to the grantTo user. All tenant data access is via this user, and is the
     * only user the FHIR server itself is configured with.
     */
    protected void grantPrivileges() {
        if (!PRIVILEGES_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // The case where all are to be granted on the default schemas.
        if (!(updateFhirSchema || grantFhirSchema || updateOauthSchema
                || grantOauthSchema || updateJavaBatchSchema || grantJavaBatchSchema)) {
            grantOauthSchema = true;
            grantFhirSchema = true;
            grantJavaBatchSchema = true;
        }

        // Build/update the tables as well as the stored procedures
        PhysicalDataModel pdm = new PhysicalDataModel();
        buildCommonModel(pdm, updateFhirSchema || grantFhirSchema, updateOauthSchema || grantOauthSchema,
            updateJavaBatchSchema || grantJavaBatchSchema);

        final IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                pdm.applyGrants(adapter, FhirSchemaConstants.FHIR_USER_GRANT_GROUP, grantTo);
                pdm.applyGrants(adapter, FhirSchemaConstants.FHIR_OAUTH_GRANT_GROUP, grantTo);
                pdm.applyGrants(adapter, FhirSchemaConstants.FHIR_BATCH_GRANT_GROUP, grantTo);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Do we want to build the multitenant variant of the schema (currently only supported
     * by DB2)
     * @return
     */
    protected boolean isMultitenant() {
        return MULTITENANT_FEATURE_ENABLED.contains(this.dbType);
    }

    //-----------------------------------------------------------------------------------------------------------------
    // The following methods are related to Multi-Tenant only.
    /**
     * Add a new tenant key so that we can rotate the values (add a
     * new key, update config files, then remove the old key). This
     * avoids any service interruption.
     */
    protected void addTenantKey() {
        if (!isMultitenant()) {
            return;
        }

        // Only if the Tenant Key file is provided as a parameter is it not null.
        // in  this case we want special behavior.
        if (tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
            tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
        } else {
            tenantKey = getRandomKey();
        }

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = getRandomKey();

        Db2Adapter adapter = new Db2Adapter(connectionPool);
        checkIfTenantNameAndTenantKeyExists(adapter, tenantName, tenantKey);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetTenantDAO tid = new GetTenantDAO(schema.getAdminSchemaName(), addKeyForTenant);
                Tenant tenant = adapter.runStatement(tid);

                if (tenant != null) {
                    // Attach the new tenant key to the tenant:
                    AddTenantKeyDAO adder =
                            new AddTenantKeyDAO(schema.getAdminSchemaName(), tenant.getTenantId(), tenantKey, tenantSalt,
                                    FhirSchemaConstants.TENANT_SEQUENCE);
                    adapter.runStatement(adder);
                } else {
                    throw new IllegalArgumentException("Tenant does not exist: " + addKeyForTenant);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        if (tenantKeyFileName == null) {
            // Generated
            logger.info("New tenant key: " + addKeyForTenant + " [key=" + tenantKey + "]");
        } else {
            // Loaded from File
            logger.info(
                    "New tenant key from file: " + addKeyForTenant + " [tenantKeyFileName=" + tenantKeyFileName + "]");
            if (!tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
                tenantKeyFileUtil.writeTenantFile(tenantKeyFileName, tenantKey);
            }
        }

    }

    /**
     * checks if tenant name and tenant key exists.
     *
     * @param adapter    the db2 adapter as this is a db2 feature only now
     * @param tenantName the tenant's name
     * @param tenantKey  tenant key
     */
    protected void checkIfTenantNameAndTenantKeyExists(Db2Adapter adapter, String tenantName, String tenantKey) {
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                final String sql =
                        "SELECT t.tenant_status FROM fhir_admin.tenants t WHERE t.tenant_name = ? "
                                + "AND EXISTS (SELECT 1 FROM fhir_admin.tenant_keys tk WHERE tk.mt_id = t.mt_id "
                                + "AND tk.tenant_hash = sysibm.hash(tk.tenant_salt || ?, 2))";
                try (PreparedStatement stmt = connectionPool.getConnection().prepareStatement(sql)) {
                    stmt.setString(1, tenantName);
                    stmt.setString(2, tenantKey);
                    if (stmt.execute()) {
                        try (ResultSet resultSet = stmt.getResultSet();) {
                            if (resultSet.next()) {
                                throw new IllegalArgumentException("tenantName and tenantKey already exists");
                            }
                        }
                    } else {
                        throw new IllegalArgumentException("Problem checking the results");
                    }
                } catch (SQLException e) {
                    throw new IllegalArgumentException(
                            "Exception when querying backend to verify tenant key and tenant name", e);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Allocate this tenant, creating new partitions if required.
     */
    protected void allocateTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // IMPORTANT! Check the schema name aligns with the actual schema for this tenant
        checkSchemaForTenant();

        // The key we'll use for this tenant. This key should be used in subsequent
        // activities related to this tenant, such as setting the tenant context.
        if (tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
            // Only if the Tenant Key file is provided as a parameter is it not null.
            // in  this case we want special behavior.
            tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
        } else {
            tenantKey = getRandomKey();
        }

        // The salt is used when we hash the tenantKey. We're just using SHA-256 for
        // the hash here, not multiple rounds of a password hashing algorithm. It's
        // sufficient in our case because we are using a 32-byte random value as the
        // key, giving 256 bits of entropy.
        final String tenantSalt = getRandomKey();

        Db2Adapter adapter = new Db2Adapter(connectionPool);
        checkIfTenantNameAndTenantKeyExists(adapter, tenantName, tenantKey);

        if (tenantKeyFileName == null) {
            logger.info("Allocating new tenant: " + tenantName + " [key=" + tenantKey + "]");
        } else {
            logger.info("Allocating new tenant: " + tenantName + " [tenantKeyFileName=" + tenantKeyFileName + "]");
        }

        // Open a new transaction and associate it with our connection pool. Remember
        // that we don't support distributed transactions, so all connections within
        // this transaction must come from the same pool
        int tenantId;
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                tenantId =
                        adapter.allocateTenant(schema.getAdminSchemaName(), schema.getSchemaName(), tenantName, tenantKey, tenantSalt,
                                FhirSchemaConstants.TENANT_SEQUENCE);

                // The tenant-id is important because this is also used to identify the partition number
                logger.info("Tenant Id[" + tenantName + "] = [" + tenantId + "]");
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        // Build/update the tables as well as the stored procedures
        FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), schema.getSchemaName(), isMultitenant());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Get the data model to create the table partitions. This is threaded, so transactions are
        // handled within each thread by the adapter. This means we should probably pull some of
        // that logic out of the adapter and handle it at a higher level. Note...the extent size used
        // for the partitions needs to match the extent size of the original table tablespace (FHIR_TS)
        // so this must be constant.
        pdm.addTenantPartitions(adapter, schema.getSchemaName(), tenantId, FhirSchemaConstants.FHIR_TS_EXTENT_KB);

        // Fill any static data tables (which are also partitioned by tenant)
        // Prepopulate the Resource Type Tables and Parameters Name/Code Table
        populateResourceTypeAndParameterNameTableEntries(tenantId);

        // Now all the table partitions have been allocated, we can mark the tenant as ready
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                adapter.updateTenantStatus(schema.getAdminSchemaName(), tenantId, TenantStatus.ALLOCATED);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        if (tenantKeyFileName == null) {
            logger.info("Allocated tenant: " + tenantName + " [key=" + tenantKey + "] with Id = " + tenantId);
            logger.info("The tenantKey JSON follows: \t\n{\"tenantKey\": \"" + tenantKey + "\"}");
        } else {
            logger.info("Allocated tenant: " + tenantName + " [tenantKeyFileName=" + tenantKeyFileName + "] with Id = "
                    + tenantId);
            if (!tenantKeyFileUtil.keyFileExists(tenantKeyFileName)) {
                tenantKeyFileUtil.writeTenantFile(tenantKeyFileName, tenantKey);
            }
        }
    }

    /**
     * Run a check to make sure that if the tenant already exists its schema
     * matches the specified schema. This prevents users from accidentally
     * creating a second instance of a tenant in a different schema
     */
    protected void checkSchemaForTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            // only relevant for databases which support multiple tenants within one schema (like Db2)
            return;
        }
        List<TenantInfo> tenants = getTenantList();

        // Scan over the list to see if the tenant we are working with
        // already exists
        for (TenantInfo ti: tenants) {
            if (ti.getTenantName().equals(this.tenantName)) {
                if (ti.getTenantSchema() == null) {
                    // The schema is empty so no chance of adding tenants
                    throw new IllegalArgumentException("Schema '" + schema.getSchemaName() + "'"
                        + " for tenant '" + ti.getTenantName() + "'"
                        + " does not contain a valid IBM FHIR Server schema");
                } else if (!ti.getTenantSchema().equalsIgnoreCase(schema.getSchemaName())) {
                    // The given schema name doesn't match where we think this tenant
                    // should be located, so throw an error before any damage is done
                    throw new IllegalArgumentException("--schema-name argument '" + schema.getSchemaName()
                    + "' does not match schema '" + ti.getTenantSchema() + "' for tenant '"
                    + ti.getTenantName() + "'");
                }
            }
        }
    }

    /**
     * Get the list of tenants and the schemas each is currently defined in. Because
     * this tenant-schema mapping isn't stored directly, it has to be inferred by
     * looking up the schema from one of the tables we know should exist. The schema
     * name may therefore be null if the tenant record (in FHIR_ADMIN.TENANTS) exists,
     * but all the tables from that schema have been dropped.
     * @return
     */
    protected List<TenantInfo> getTenantList() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return Collections.emptyList();
        }

        // Similar to the allocate tenant processing, except in this case we want to
        // make sure that each table has all the tenant partitions required. This is
        // to handle the case where a schema update has added a new table.
        List<TenantInfo> tenants;
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetTenantList rtListGetter = new GetTenantList(schema.getAdminSchemaName());
                tenants = adapter.runStatement(rtListGetter);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        return tenants;
    }

    /**
     * Make sure all the tables has a partition created for the configured tenant
     */
    protected void refreshTenants() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // Similar to the allocate tenant processing, except in this case we want to
        // make sure that each table has all the tenant partitions required. This is
        // to handle the case where a schema update has added a new table.
        List<TenantInfo> tenants = getTenantList();

        // make sure the list is sorted by tenantId. Lambdas really do clean up this sort of code
        tenants.sort((TenantInfo left, TenantInfo right) -> left.getTenantId() < right.getTenantId() ? -1 : left.getTenantId() > right.getTenantId() ? 1 : 0);
        for (TenantInfo ti: tenants) {
            // For issue 1847 we only want to process for the named data schema if one is provided
            // If no -schema-name arg is given, we process all tenants.
            if (ti.getTenantSchema() != null && (!schema.isOverrideDataSchema() || schema.matchesDataSchema(ti.getTenantSchema()))) {
                // It's crucial we use the correct schema for each particular tenant, which
                // is why we have to build the PhysicalDataModel separately for each tenant
                FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), ti.getTenantSchema(), isMultitenant());
                PhysicalDataModel pdm = new PhysicalDataModel();
                gen.buildSchema(pdm);

                Db2Adapter adapter = new Db2Adapter(connectionPool);
                pdm.addNewTenantPartitions(adapter, ti.getTenantSchema(), ti.getTenantId());
            }
        }
    }

    /**
     * List the tenants currently configured
     */
    protected void listTenants() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                GetTenantList rtListGetter = new GetTenantList(schema.getAdminSchemaName());
                List<TenantInfo> tenants = adapter.runStatement(rtListGetter);

                System.out.println(TenantInfo.getHeader());
                tenants.forEach(t -> System.out.println(t.toString()));
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Check that we can call the set_tenant procedure successfully (which means
     * that the
     * tenant record exists in the tenants table)
     */
    protected void testTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        if (this.tenantName == null || this.tenantName.isEmpty()) {
            throw new IllegalStateException("Missing tenant name");
        }

        // Part of Bring your own Tenant Key
        if (tenantKeyFileName != null) {
            // Only if the Tenant Key file is provided as a parameter is it not null.
            // in  this case we want special behavior.
            tenantKey = this.tenantKeyFileUtil.readTenantFile(tenantKeyFileName);
        }

        if (this.tenantKey == null || this.tenantKey.isEmpty()) {
            throw new IllegalArgumentException("No tenant-key value provided");
        }

        logger.info("Testing tenant: [" + tenantName + "]");

        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // The tenants table, variable and set_tenant procedure are all located in
                // the admin schema. The data access user only has execute privileges on the
                // set_tenant procedure and read access to the variable. The variable can
                // only be set by calling the stored procedure
                Db2SetTenantVariable cmd = new Db2SetTenantVariable(schema.getAdminSchemaName(), tenantName, tenantKey);
                adapter.runStatement(cmd);

                Db2GetTenantVariable getter = new Db2GetTenantVariable(schema.getAdminSchemaName());
                Integer tid = adapter.runStatement(getter);
                if (tid == null) {
                    throw new IllegalStateException("SV_TENANT_ID not set!");
                }

                // Print the id from the session variable (used for access control)
                logger.info("tenantName='" + tenantName + "', tenantId=" + tid);

                // Now let's check we can run a select against one our tenant-based
                // tables
                GetResourceTypeList rtListGetter = new GetResourceTypeList(schema.getSchemaName());
                List<ResourceType> rtList = adapter.runStatement(rtListGetter);
                rtList.forEach(rt -> logger.info("ResourceType: " + rt.toString()));
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    protected TenantInfo getTenantInfo() {
        TenantInfo result;
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            throw new IllegalStateException("Not a multi-tenant database");
        }

        Db2Adapter adapter = new Db2Adapter(connectionPool);

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {

            try {
                GetTenantInfo command = new GetTenantInfo(schema.getAdminSchemaName(), tenantName);
                result = adapter.runStatement(command);

                if (result == null) {
                    logger.info("Use --list-tenants to display the current tenants");
                    throw new IllegalArgumentException("Tenant '" + tenantName + "' not found in admin schema " + schema.getAdminSchemaName());
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        // make sure we set the schema name correctly if it couldn't be found in the database
        // (which happens after all the partitions for a particular tenant are detached)
        String tenantSchema = result.getTenantSchema();
        if (tenantSchema == null || tenantSchema.isEmpty()) {
            // the schema can no longer be derived from the database, so we
            // need it to be provided on the command line.
            if (schema.getSchemaName() == null || schema.getSchemaName().isEmpty()) {
                throw new IllegalArgumentException("Must provide the tenant schema with --schema-name");
            }
            result.setTenantSchema(schema.getSchemaName());
        } else {
            // if a schema name was provided on the command line, let's double-check it matches
            // the schema used for this tenant in the database
            if (!tenantSchema.equalsIgnoreCase(schema.getSchemaName())) {
                throw new IllegalArgumentException("--schema-name '" + schema.getSchemaName() + "' argument does not match tenant schema: '"
                    + tenantSchema + "'");
            }
        }

        return result;
    }

    /**
     * Mark the tenant so that it can no longer be accessed (this prevents
     * the SET_TENANT method from authenticating the tenantName/tenantKey
     * pair, so the SV_TENANT_ID variable never gets set).
     * @return the TenantInfo associated with the tenant
     */
    protected TenantInfo freezeTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            throw new IllegalStateException("Not a multi-tenant database");
        }

        TenantInfo result = getTenantInfo();
        Db2Adapter adapter = new Db2Adapter(connectionPool);


        logger.info("Marking tenant for drop: " + tenantName);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {

            try {
                // Mark the tenant as frozen before we proceed with dropping anything
                if (result.getTenantStatus() == TenantStatus.ALLOCATED) {
                    adapter.updateTenantStatus(schema.getAdminSchemaName(), result.getTenantId(), TenantStatus.FROZEN);
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
        return result;
    }


    /**
     * Deallocate this tenant, dropping all the related partitions. This needs to be
     * idempotent because there are steps which must be run in separate transactions
     * (due to how Db2 handles detaching partitions). This is further complicated by
     * referential integrity constraints in the schema. See:
     *  - https://www.ibm.com/support/knowledgecenter/SSEPGG_11.5.0/com.ibm.db2.luw.admin.partition.doc/doc/t0021576.html
     *
     * The workaround described in the above link is:
     *   // Change the RI constraint to informational:
     *   1. ALTER TABLE child ALTER FOREIGN KEY fk NOT ENFORCED;
     *
     *   2. ALTER TABLE parent DETACH PARTITION p0 INTO TABLE pdet;
     *
     *   3. SET INTEGRITY FOR child OFF;
     *
     *   // Change the RI constraint back to enforced:
     *   4. ALTER TABLE child ALTER FOREIGN KEY fk ENFORCED;
     *
     *   5. SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
     *   6.   Assuming that the CHILD table does not have any dependencies on partition P0,
     *   7.   and that no updates on the CHILD table are permitted until this UOW is complete,
     *        no RI violation is possible during this UOW.
     *
     *   COMMIT WORK;
     *
     *   Unfortunately, #7 above essentially requires that all writes cease until the
     *   UOW is completed and the integrity is enabled again on all the child (leaf)
     *   tables. Of course, this could be relaxed if the application is trusted not
     *   to mess up the referential integrity...which we know to be true for the
     *   FHIR server persistence layer.
     *
     *   If the risk is deemed too high, tenant removal should be performed in a
     *   maintenance window.
     */
    protected void dropTenant() {
        if (!MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            return;
        }

        // Mark the tenant as being dropped. This should prevent it from
        // being used in any way because the SET_TENANT stored procedure
        // will reject any request for the tenant being dropped.
        TenantInfo tenantInfo = freezeTenant();

        // Build the model of the data (FHIRDATA) schema which is then used to drive the drop
        FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), tenantInfo.getTenantSchema(), isMultitenant());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        // Detach the tenant partition from each of the data tables
        detachTenantPartitions(pdm, tenantInfo);

        // this may not complete successfully because Db2 runs the detach as an async
        // process. Just need to run --drop-detached to clean up.
        dropDetachedPartitionTables(pdm, tenantInfo);

    }

    /**
     * Drop any tables which have previously been detached. The detach process is asynchronous,
     * so this is a sort of garbage collection, sweeping up cruft left in the database.
     */
    protected void dropDetachedPartitionTables() {

        TenantInfo tenantInfo = getTenantInfo();
        FhirSchemaGenerator gen = new FhirSchemaGenerator(schema.getAdminSchemaName(), tenantInfo.getTenantSchema(), isMultitenant());
        PhysicalDataModel pdm = new PhysicalDataModel();
        gen.buildSchema(pdm);

        dropDetachedPartitionTables(pdm, tenantInfo);
    }

    /**
     * Drop any tables which have previously been detached. Once all tables have been
     * dropped, we go on to drop the tablespace. If the tablespace drop is successful,
     * we know the cleanup is complete so we can update the tenant status accordingly
     * @param pdm
     * @param tenantInfo
     */
    protected void dropDetachedPartitionTables(PhysicalDataModel pdm, TenantInfo tenantInfo) {
        // In a new transaction, drop any of the tables that were created by
        // the partition detach operation.
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {

            try {
                pdm.dropDetachedPartitions(adapter, tenantInfo.getTenantSchema(), tenantInfo.getTenantId());
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

        // We can drop the tenant's tablespace only after all the table drops have been committed
        logger.info("Dropping tablespace for tenant " + tenantInfo.getTenantId() + "/" + tenantName);
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {

            try {
                // With all the objects removed, it should be safe to remove the tablespace
                pdm.dropTenantTablespace(adapter, tenantInfo.getTenantId());
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }


        // Now all the table partitions have been allocated, we can mark the tenant as dropped
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                adapter.updateTenantStatus(schema.getAdminSchemaName(), tenantInfo.getTenantId(), TenantStatus.DROPPED);
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Temporarily suspend RI so that tables which are the subject of foreign
     * key relationships can have their partitions dropped. A bit frustrating
     * to have to go through this, because the child table partition will be
     * detached before the parent anyway, so theoretically this workaround
     * shouldn't be necessary.
     *   ALTER TABLE child ALTER FOREIGN KEY fk NOT ENFORCED;
     *   ALTER TABLE parent DETACH PARTITION p0 INTO TABLE pdet;
     *   SET INTEGRITY FOR child OFF;
     *   ALTER TABLE child ALTER FOREIGN KEY fk ENFORCED;
     *   SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
     */
    protected void detachTenantPartitions(PhysicalDataModel pdm, TenantInfo tenantInfo) {
        Db2Adapter adapter = new Db2Adapter(connectionPool);

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                // collect the set of all child tables with FK relationships to
                // partitioned tables
                Set<Table> childTables = new HashSet<>();

                // ALTER TABLE child ALTER FOREIGN KEY fk NOT ENFORCED;
                pdm.visit(new DisableForeignKey(adapter, childTables));

                // ALTER TABLE parent DETACH PARTITION p0 INTO TABLE pdet;
                pdm.detachTenantPartitions(adapter, tenantInfo.getTenantSchema(), tenantInfo.getTenantId());

                // SET INTEGRITY FOR child OFF;
                childTables.forEach(t -> adapter.setIntegrityOff(t.getSchemaName(), t.getObjectName()));

                // ALTER TABLE child ALTER FOREIGN KEY fk ENFORCED;
                pdm.visit(new EnableForeignKey(adapter));

                // SET INTEGRITY FOR child ALL IMMEDIATE UNCHECKED;
                childTables.forEach(t -> adapter.setIntegrityUnchecked(t.getSchemaName(), t.getObjectName()));
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

    }

    /**
     * Delete all the metadata associated with the named tenant.
     */
    protected void deleteTenantMeta() {
        Db2Adapter adapter = new Db2Adapter(connectionPool);
        TenantInfo tenantInfo = getTenantInfo();
        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                if (tenantInfo.getTenantStatus() == TenantStatus.DROPPED) {
                    adapter.deleteTenantMeta(schema.getAdminSchemaName(), tenantInfo.getTenantId());
                } else {
                    throw new IllegalStateException("Cannot delete tenant meta data until status is " + TenantStatus.DROPPED.name());
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }

    }

    //-----------------------------------------------------------------------------------------------------------------
    // The following methods are related to parsing arguments and action selection
    /**
     * Parse the command-line arguments, building up the environment and
     * establishing
     * the run-list
     *
     * @param args
     */
    protected void parseArgs(String[] args) {
        // Arguments are pretty simple, so we go with a basic switch instead of having
        // yet another dependency (e.g. commons-cli).
        for (int i = 0; i < args.length; i++) {
            int nextIdx = (i + 1);
            String arg = args[i];
            switch (arg) {
            case "--prop-file":
                if (++i < args.length) {
                    loadPropertyFile(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--schema-name":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);
                    schema.setSchemaName(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--grant-to":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);

                    // Force upper-case because user names are case-insensitive
                    this.grantTo = args[i].toUpperCase();
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--target":
                if (++i < args.length) {
                    DataDefinitionUtil.assertValidName(args[i]);
                    List<String> targets = Arrays.asList(args[i].split(","));
                    for (String target : targets) {
                        String tmp = target.toUpperCase();
                        nextIdx++;
                        if (tmp.startsWith("BATCH")) {
                            this.grantJavaBatchSchema = true;
                            if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                                schema.setJavaBatchSchemaName(args[nextIdx]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                            }
                        } else if (tmp.startsWith("OAUTH")){
                            this.grantOauthSchema = true;
                            if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                                schema.setOauthSchemaName(args[nextIdx]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                            }
                        } else if (tmp.startsWith("DATA")){
                            this.grantFhirSchema = true;
                            if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                                schema.setSchemaName(args[nextIdx]);
                                i++;
                            } else {
                                throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                            }
                        } else {
                            throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                        }
                    }
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--add-tenant-key":
                if (++i < args.length) {
                    this.addKeyForTenant = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--update-proc":
                this.updateProc = true;
                break;
            case "--check-compatibility":
                this.checkCompatibility = true;
                break;
            case "--drop-admin":
                this.dropAdmin = true;
                break;
            case "--test-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.testTenant = true;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--tenant-key":
                if (++i < args.length) {
                    this.tenantKey = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--tenant-key-file":
                if (++i < args.length) {
                    tenantKeyFileName = args[i];
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--list-tenants":
                this.listTenants = true;
                break;
            case "--update-schema":
                this.updateFhirSchema = true;
                this.updateOauthSchema = true;
                this.updateJavaBatchSchema = true;
                break;
            case "--update-schema-fhir":
                this.updateFhirSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setSchemaName(args[nextIdx]);
                    i++;
                } else {
                    schema.setSchemaName(DATA_SCHEMANAME);
                }
                break;
            case "--update-schema-batch":
                this.updateJavaBatchSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setJavaBatchSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case "--update-schema-oauth":
                this.updateOauthSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setOauthSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case "--create-schemas":
                this.createFhirSchema = true;
                this.createOauthSchema = true;
                this.createJavaBatchSchema = true;
                break;
            case "--create-schema-fhir":
                this.createFhirSchema =  true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case "--create-schema-batch":
                this.createJavaBatchSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setJavaBatchSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case "--create-schema-oauth":
                this.createOauthSchema = true;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setOauthSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case "--drop-schema":
                System.err.print("Option '--drop-schema' has been retired.  Please use '--drop-schema-fhir', "
                        + "'--drop-schema-batch', and/or '--drop-schema-oauth'.");
                break;
            case "--drop-schema-fhir":
                this.dropFhirSchema = Boolean.TRUE;
                break;
            case "--drop-schema-batch":
                this.dropJavaBatchSchema = Boolean.TRUE;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setJavaBatchSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case "--drop-schema-oauth":
                this.dropOauthSchema = Boolean.TRUE;
                if (nextIdx < args.length && !args[nextIdx].startsWith("--")) {
                    schema.setOauthSchemaName(args[nextIdx]);
                    i++;
                }
                break;
            case "--pool-size":
                if (++i < args.length) {
                    this.maxConnectionPoolSize = Integer.parseInt(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--prop":
                if (++i < args.length) {
                    // properties are given as name=value
                    addProperty(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--confirm-drop":
                this.confirmDrop = true;
                break;
            case "--allocate-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.allocateTenant = true;
                    this.dropTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--refresh-tenants":
                this.refreshTenants = true;
                this.allocateTenant = false;
                this.dropTenant = false;
                break;
            case "--drop-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.dropTenant = true;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--freeze-tenant":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.freezeTenant = true;
                    this.dropTenant = false;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--drop-detached":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.dropDetached = true;
                    this.dropTenant = false;
                    this.allocateTenant = false;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--delete-tenant-meta":
                if (++i < args.length) {
                    this.tenantName = args[i];
                    this.deleteTenantMeta = true;
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                break;
            case "--db-type":
                if (++i < args.length) {
                    this.dbType = DbType.from(args[i]);
                } else {
                    throw new IllegalArgumentException("Missing value for argument at posn: " + i);
                }
                switch (dbType) {
                case DERBY:
                    translator = new DerbyTranslator();
                    break;
                case POSTGRESQL:
                    translator = new PostgresTranslator();
                    break;
                case DB2:
                default:
                    break;
                }
                break;
            default:
                throw new IllegalArgumentException("Invalid argument: '" + arg + "'");
            }
        }
    }

    /**
     * Read the properties from the given file
     *
     * @param filename
     */
    public void loadPropertyFile(String filename) {
        try (InputStream is = new FileInputStream(filename)) {
            properties.load(is);
        } catch (IOException x) {
            throw new IllegalArgumentException(x);
        }
    }

    /**
     * Parse the given key=value string and add to the properties being collected
     *
     * @param pair
     */
    public void addProperty(String pair) {
        String[] kv = pair.split("=");
        if (kv.length == 2) {
            properties.put(kv[0], kv[1]);
        } else {
            throw new IllegalArgumentException("Property must be defined as key=value, not: " + pair);
        }
    }

    /**
     * Perform the special data migration steps required for the V0010 version of the schema
     */
    protected void applyDataMigrationForV0010() {
        if (MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            // Process each tenant one-by-one
            List<TenantInfo> tenants = getTenantList();
            for (TenantInfo ti: tenants) {

                // If no --schema-name override was specified, we process all tenants, otherwise we
                // process only tenants which belong to the override schema name
                if (!schema.isOverrideDataSchema() || schema.matchesDataSchema(ti.getTenantSchema())) {
                    dataMigrationForV0010(ti);
                }
            }
        } else {
            doMigrationForV0010();
        }

    }

    /**
     * Get the list of resource types to drive resource-by-resource operations
     * @return the full list of FHIR R4 resource types, or a subset of names if so configured
     */
    private Set<String> getResourceTypes() {
        Set<String> result;
        if (this.resourceTypeSubset == null || this.resourceTypeSubset.isEmpty()) {
            // pass 'false' to getResourceTypes to avoid building tables for abstract resource types
            // Should simplify FhirSchemaGenerator and always pass in this list. When switching
            // over to false, migration is required to drop the tables no longer required.
            final boolean includeAbstractResourceTypes = true;
            result = ModelSupport.getResourceTypes(includeAbstractResourceTypes).stream()
                    .map(t -> ModelSupport.getTypeName(t))
                    .collect(Collectors.toSet());
        } else {
            result = this.resourceTypeSubset;
        }

        return result;
    }

    /**
     * Migrate the IS_DELETED data for the given tenant
     * @param ti
     */
    private void dataMigrationForV0010(TenantInfo ti) {
        // Multi-tenant schema so we know this is Db2:
        Db2Adapter adapter = new Db2Adapter(connectionPool);

        Set<String> resourceTypes = getResourceTypes();

        // Process each update in its own transaction so we don't stress the tx log space
        for (String resourceTypeName: resourceTypes) {
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    SetTenantIdDb2 setTenantId = new SetTenantIdDb2(schema.getAdminSchemaName(), ti.getTenantId());
                    adapter.runStatement(setTenantId);

                    GetXXXLogicalResourceNeedsMigration needsMigrating = new GetXXXLogicalResourceNeedsMigration(schema.getSchemaName(), resourceTypeName);
                    if (adapter.runStatement(needsMigrating)) {
                        logger.info("V0010 Migration: Updating " + resourceTypeName + "_LOGICAL_RESOURCES.IS_DELETED "
                                + "for tenant '" + ti.getTenantName() + "', schema '" + ti.getTenantSchema() + "'");
                        InitializeLogicalResourceDenorms cmd = new InitializeLogicalResourceDenorms(schema.getSchemaName(), resourceTypeName);
                        adapter.runStatement(cmd);
                    }
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
            }
        }
    }

    /**
     * Perform the data migration for V0010 (non-multi-tenant schema)
     */
    private void doMigrationForV0010() {
        IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
        Set<String> resourceTypes = getResourceTypes();

        // Process each resource type in its own transaction to avoid pressure on the tx log
        for (String resourceTypeName: resourceTypes) {
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    // only process tables which have been converted to the V0010 schema but
                    // which have not yet had their data migrated. The migration can't be
                    // done as part of the schema change because some tables need a REORG which
                    // has to be done after the transaction in which the alter table was performed.
                    GetXXXLogicalResourceNeedsMigration needsMigrating = new GetXXXLogicalResourceNeedsMigration(schema.getSchemaName(), resourceTypeName);
                    if (adapter.runStatement(needsMigrating)) {
                        logger.info("V0010-V0012 Migration: Updating " + resourceTypeName + "_LOGICAL_RESOURCES denormalized columns in schema " + schema.getSchemaName());
                        InitializeLogicalResourceDenorms cmd = new InitializeLogicalResourceDenorms(schema.getSchemaName(), resourceTypeName);
                        adapter.runStatement(cmd);
                    }
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
            }
        }
    }

    /**
     * Backfill the RESOURCE_CHANGE_LOG table if it is empty
     */
    protected void backfillResourceChangeLog() {

        if (MULTITENANT_FEATURE_ENABLED.contains(dbType)) {
            // Process each tenant one-by-one
            List<TenantInfo> tenants = getTenantList();
            for (TenantInfo ti: tenants) {

                // If no --schema-name override was specified, we process all tenants, otherwise we
                // process only tenants which belong to the override schema name
                if (!schema.isOverrideDataSchema() || schema.matchesDataSchema(ti.getTenantSchema())) {
                    backfillResourceChangeLogDb2(ti);
                }
            }
        } else {
            // Not a multi-tenant database, so we only have to do this once
            IDatabaseAdapter adapter = getDbAdapter(dbType, connectionPool);
            try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
                try {
                    GetResourceChangeLogEmpty isEmpty = new GetResourceChangeLogEmpty(schema.getSchemaName());
                    if (adapter.runStatement(isEmpty)) {
                        // change log is empty, so we need to backfill it with data
                        doBackfill(adapter);
                    } else {
                        logger.info("RESOURCE_CHANGE_LOG has data so skipping backfill");
                    }
                } catch (DataAccessException x) {
                    // Something went wrong, so mark the transaction as failed
                    tx.setRollbackOnly();
                    throw x;
                }
            }
        }
    }

    /**
     * backfill the resource_change_log table if it is empty
     */
    protected void backfillResourceChangeLogDb2(TenantInfo ti) {

        Db2Adapter adapter = new Db2Adapter(connectionPool);

        Set<String> resourceTypes = getResourceTypes();

        try (ITransaction tx = TransactionFactory.openTransaction(connectionPool)) {
            try {
                SetTenantIdDb2 setTenantId = new SetTenantIdDb2(schema.getAdminSchemaName(), ti.getTenantId());
                adapter.runStatement(setTenantId);

                GetResourceChangeLogEmpty isEmpty = new GetResourceChangeLogEmpty(schema.getSchemaName());
                if (adapter.runStatement(isEmpty)) {
                    // change log is empty, so we need to backfill it with data
                    for (String resourceTypeName: resourceTypes) {
                        logger.info("Backfilling RESOURCE_CHANGE_LOG with " + resourceTypeName
                            + " resources for tenant '" + ti.getTenantName() + "', schema '" + ti.getTenantSchema() + "'");
                        BackfillResourceChangeLogDb2 backfill = new BackfillResourceChangeLogDb2(schema.getSchemaName(), resourceTypeName);
                        adapter.runStatement(backfill);
                    }
                } else {
                    logger.info("RESOURCE_CHANGE_LOG has data for tenant '" + ti.getTenantName() + "' so skipping backfill");
                }
            } catch (DataAccessException x) {
                // Something went wrong, so mark the transaction as failed
                tx.setRollbackOnly();
                throw x;
            }
        }
    }

    /**
     * Backfill the RESOURCE_CHANGE_LOG table for all the resource types. Non-multi-tenant
     * implementation
     * @param adapter
     */
    private void doBackfill(IDatabaseAdapter adapter) {
        Set<String> resourceTypes = getResourceTypes();

        for (String resourceTypeName: resourceTypes) {
            logger.info("Backfilling RESOURCE_CHANGE_LOG with " + resourceTypeName
                + " resources for schema '" + schema.getSchemaName() + "'");
            BackfillResourceChangeLog backfill = new BackfillResourceChangeLog(schema.getSchemaName(), resourceTypeName);
            adapter.runStatement(backfill);
        }
    }

    /**
     * Process the requested operation
     */
    protected void process() {
        long start = System.nanoTime();
        loadDriver(translator);
        configureConnectionPool();

        if (this.checkCompatibility) {
            checkCompatibility();
        }

        if (translator.isDerby() && !"APP".equals(schema.getSchemaName())) {
            if (schema.isOverrideDataSchema()) {
                logger.warning("Only the APP schema is supported for Apache Derby; ignoring the passed"
                        + " schema name '" + schema.getSchemaName() + "' and using APP.");
            }
            schema.setSchemaName("APP");
        }

        // [optional] use a subset of resource types to make testing quicker
        String resourceTypesString = properties.getProperty("resourceTypes");
        if (resourceTypesString != null && resourceTypesString.length() > 0) {
            resourceTypeSubset = new HashSet<>(Arrays.asList(resourceTypesString.split(",")));
        }

        if (addKeyForTenant != null) {
            addTenantKey();
        } else if (this.dropAdmin || this.dropFhirSchema || this.dropJavaBatchSchema || this.dropOauthSchema) {
            // only proceed with the drop if the user has provided additional confirmation
            if (this.confirmDrop) {
                dropSchema();
            } else {
                throw new IllegalArgumentException("[ERROR] Drop not confirmed with --confirm-drop");
            }
        } else if (updateFhirSchema || updateOauthSchema || updateJavaBatchSchema) {
            updateSchema();
        } else if (createFhirSchema || createOauthSchema || createJavaBatchSchema) {
            createSchemas();
        } else if (updateProc) {
            updateProcedures();
        } else if (this.listTenants) {
            listTenants();
        } else if (this.allocateTenant) {
            allocateTenant();
        } else if (this.refreshTenants) {
            refreshTenants();
        } else if (this.testTenant) {
            testTenant();
        } else if (this.freezeTenant) {
            freezeTenant();
        } else if (this.dropDetached) {
            dropDetachedPartitionTables();
        } else if (this.deleteTenantMeta) {
            deleteTenantMeta();
        } else if (this.dropTenant) {
            dropTenant();
        }

        if (this.grantTo != null) {
            grantPrivileges();
        }

        long elapsed = System.nanoTime() - start;
        logger.info(String.format("Processing took: %7.3f s", elapsed / NANOS));
    }

    /**
     * Get the program exit status from the environment
     *
     * @return
     */
    protected int getExitStatus() {
        return this.exitStatus;
    }

    /**
     * Write a final status message - useful for QA to review when checking the
     * output
     */
    protected void logStatusMessage(int status) {
        switch (status) {
        case EXIT_OK:
            logger.info("SCHEMA CHANGE: OK");
            break;
        case EXIT_BAD_ARGS:
            logger.severe("SCHEMA CHANGE: BAD ARGS");
            break;
        case EXIT_RUNTIME_ERROR:
            logger.severe("SCHEMA CHANGE: RUNTIME ERROR");
            break;
        case EXIT_VALIDATION_FAILED:
            logger.warning("SCHEMA CHANGE: FAILED");
            break;
        default:
            logger.severe("SCHEMA CHANGE: RUNTIME ERROR");
            break;
        }
    }

    /**
     * Main entry point
     *
     * @param args
     */
    public static void main(String[] args) {
        logClasspath(logger);

        int exitStatus;
        Main m = new Main();
        try {
            configureLogger();
            m.parseArgs(args);
            m.process();
            exitStatus = m.getExitStatus();
        } catch(DatabaseNotReadyException x) {
            logger.log(Level.SEVERE, "The database is not yet available. Please re-try.", x);
            exitStatus = EXIT_NOT_READY;
        } catch (IllegalArgumentException x) {
            logger.log(Level.SEVERE, "bad argument", x);
            printUsage();
            exitStatus = EXIT_BAD_ARGS;
        } catch (Exception x) {
            logger.log(Level.SEVERE, "schema tool failed", x);
            exitStatus = EXIT_RUNTIME_ERROR;
        }

        // Write out a final status message to make it easy to see validation success/failure
        m.logStatusMessage(exitStatus);

        // almost certainly will get flagged during code-scan, but this is intentional,
        // as we genuinely want to exit with the correct status here. The code-scan tool
        // really ought to be able to see that this is a main function in a J2SE environment
        System.exit(exitStatus);
    }
}
