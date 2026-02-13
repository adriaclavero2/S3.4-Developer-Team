package common.exception;

public class DataAccessException extends RuntimeException {

    public static final String DEFAULT_MESSAGE = "DAO error: error inserting task into ";

    public DataAccessException(String message) {
        super(message);

    }

    public DataAccessException(String dataSource, Throwable cause) {

        super(DEFAULT_MESSAGE + dataSource, cause);
    }
}
