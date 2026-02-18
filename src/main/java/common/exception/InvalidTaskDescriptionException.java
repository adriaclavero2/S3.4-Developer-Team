package common.exception;

public class InvalidTaskDescriptionException extends RuntimeException {
    public static final String NULL_DESCRIPTION_ERROR = "Task description cannot be null.";
    public static final String EMPTY_DESCRIPTION_ERROR = "Task description cannot be empty.";

    public InvalidTaskDescriptionException(String message) {
        super(message);
    }
}
