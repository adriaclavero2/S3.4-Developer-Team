package common.exception;

public class InvalidTaskDescriptionException extends RuntimeException {
  public InvalidTaskDescriptionException(String message) {
    super(message);
  }
}
