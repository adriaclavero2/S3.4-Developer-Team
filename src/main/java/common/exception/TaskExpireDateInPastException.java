package task.exceptions;

public class TaskExpireDateInPastException extends RuntimeException {
    public TaskExpireDateInPastException(String message) {
        super(message);
    }
}
