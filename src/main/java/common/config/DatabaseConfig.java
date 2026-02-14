package common.config;

import common.config.builders.MongoBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class DatabaseConfig {
    private static final Properties props = new Properties();
    private static final Map<String, ConnectionStringBuilder> builders = new HashMap<>();
    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) props.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Configuration could not be loaded", e);
        }
        builders.put("mongo", new MongoBuilder());
    }

    public static String getConnectionString(DBType type) {
        ConnectionStringBuilder builder = builders.get(type.getLabel());
        if (builder == null) {
            throw new RuntimeException("Database " + type + " is not supported");
        }
        return builder.stringBuild(props);
    }

    public static String getDbName(DBType type) {
        ConnectionStringBuilder builder = builders.get(type.getLabel());
        if (builder == null) {
            throw new RuntimeException(type + " is not a configuration name for database");
        }
        return builder.getDbName(props);
    }
}
