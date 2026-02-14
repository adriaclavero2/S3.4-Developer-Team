package common.config;

public enum DBType {
    MONGO("mongo"),
    MYSQL("mysql"),
    POSTGRES("postgres");

    private final String label;

    DBType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }
}
