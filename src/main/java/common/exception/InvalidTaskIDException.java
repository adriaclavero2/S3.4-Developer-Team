package common.exception;

public class InvalidTaskIDException extends RuntimeException {
    public InvalidTaskIDException(String message) {
        super(message);
    }

    public InvalidTaskIDException(String message, Throwable exception) {
        super(message,exception);
    }
}
