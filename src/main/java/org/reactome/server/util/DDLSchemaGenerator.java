package org.reactome.server.util;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.reactome.server.Application;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

import java.io.File;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.hibernate.tool.schema.TargetType.SCRIPT;
import static org.hibernate.tool.schema.TargetType.STDOUT;

/**
 * @author Guilherme S Viteri <gviteri@ebi.ac.uk>
 */

public class DDLSchemaGenerator {

    private static final String DB_NAME = "digester";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "root123123";
    private static String dbScript = "sql/ddl-report-##action##.sql";
    private static Map<String, String> settings = new HashMap<>();

    static {
        System.setProperty(Environment.STORAGE_ENGINE, "innodb");
        settings.put("connection.driver_class", "com.mysql.cj.jdbc.Driver");
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        // do not generate directly in the server,
        settings.put(Environment.URL, "jdbc:mysql://localhost:3306/" + DB_NAME+"?characterEncoding=utf-8&useUnicode=true&serverTimezone=America/Toronto");
        settings.put(Environment.USER, DB_USER);
        settings.put(Environment.PASS, DB_PASS);
    }

    /**
     * Schema generator provides the DDL statement for DB creation and updates.
     *
     * 1) Create report database first and then run the schema generator
     * $> mysql -u <user> -p[password] -e "CREATE DATABASE IF NOT EXISTS <dbName>";
     *
     * 2) Run the DDLSchemaGenerator
     * 3)
     */
    public static void main(String[] args) {
        if (args == null || args.length == 0) {
            System.err.println("Please provide an argument: CREATE OR UPDATE");
            System.exit(1);
        }

        if (args[0].equalsIgnoreCase("create")) {
            create();
        } else if (args[0].equalsIgnoreCase("update")) {
            update();
        } else {
            System.err.println("Please provide a valid argument: CREATE OR UPDATE");
        }

        System.exit(0);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "Duplicates"})
    private static void create() {
        settings.put(Environment.HBM2DDL_AUTO, "create");
        String dbFile = dbScript.replace("##action##", "create_"+ UUID.randomUUID());
        File script = new File(dbFile);
        if (script.exists()) script.delete();

        MetadataSources metadataSources = new MetadataSources(new StandardServiceRegistryBuilder().applySettings(settings).build());
        SchemaExport schemaExport = new SchemaExport();
        schemaExport.setHaltOnError(true);
        schemaExport.setFormat(true);
        schemaExport.setDelimiter(";");
        schemaExport.setOutputFile(dbFile);

        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        new LocalSessionFactoryBuilder(null, resourceLoader, metadataSources).scanPackages(Application.class.getPackage().getName());

        Metadata metadata = metadataSources.buildMetadata();
        schemaExport.createOnly(EnumSet.of(STDOUT, SCRIPT), metadata);
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "Duplicates"})
    private static void update() {
        settings.put(Environment.HBM2DDL_AUTO, "update");
        String dbFile = dbScript.replace("##action##", "update_"+ UUID.randomUUID());
        File script = new File(dbFile);
        if (script.exists()) script.delete();

        MetadataSources metadataSources = new MetadataSources(new StandardServiceRegistryBuilder().applySettings(settings).build());
        SchemaUpdate schemaUpdate = new SchemaUpdate();
        schemaUpdate.setHaltOnError(true);
        schemaUpdate.setFormat(true);
        schemaUpdate.setDelimiter(";");
        schemaUpdate.setOutputFile(dbFile);

        PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();
        new LocalSessionFactoryBuilder(null, resourceLoader, metadataSources).scanPackages(Application.class.getPackage().getName());

        Metadata metadata = metadataSources.buildMetadata();
        schemaUpdate.execute(EnumSet.of(STDOUT, SCRIPT), metadata);
    }
}
