package common.config.builders;

import common.config.ConnectionStringBuilder;
import java.util.Properties;

public class MongoBuilder implements ConnectionStringBuilder {
    @Override
    public String stringBuild(Properties props) {
        return String.format("mongodb://%s:%s@%s:%s",
                props.getProperty("MONGO_USER"),
                props.getProperty("MONGO_PASSWORD"),
                props.getProperty("MONGO_HOST"),
                props.getProperty("MONGO_PORT"));
    }

    @Override
    public String getDbName(Properties props) {
        return props.getProperty("MONGO_DB");
    }
}
