package common.exception;

public class TaskExpireDateInPastException extends RuntimeException {
    public TaskExpireDateInPastException(String message) {
        super(message);
    }
}
