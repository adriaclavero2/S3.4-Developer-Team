package common.config.builders;

import common.config.ConnectionStringBuilder;
import java.util.Properties;

public class MongoBuilder implements ConnectionStringBuilder {
    @Override
    public String stringBuild(Properties props) {
        return String.format("mongodb://%s:%s@%s:%s",
                props.getProperty("mongodb.username"),
                props.getProperty("mongodb.password"),
                props.getProperty("mongodb.host"),
                props.getProperty("mongodb.port"));
    }

    @Override
    public String getDbName(Properties props) {
        return props.getProperty("mongodb.database");
    }
}
