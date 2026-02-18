package common.exception;

public class TaskNotFoundException extends RuntimeException {
    public static final String DEFAULT_MESSAGE = "No task was found with the ID: ";

    public TaskNotFoundException(String id) {
        super(DEFAULT_MESSAGE + id);
    }

    public TaskNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
