package common.config;

import java.util.Properties;

public interface ConnectionStringBuilder {
    String stringBuild(Properties props);
    String getDbName(Properties props);
}
