package common.utils;

import common.exception.InvalidTaskDescriptionException;
import common.exception.InvalidTaskTitleException;
import common.exception.TaskExpireDateInPastException;

import java.time.LocalDateTime;

public class Validator {
    public static void validateTitle(String title) {
        if (title == null) {
            throw new InvalidTaskTitleException("Task title cannot be null");
        }
        if (title.trim().isEmpty()) {
            throw new InvalidTaskTitleException("Task title cannot be empty");
        }
        if (title.length() > 200) {
            throw new InvalidTaskTitleException("Task title cannot exceed 200 characters");
        }
    }

    public static void validateDescription(String description) {
        if (description == null) {
            throw new InvalidTaskDescriptionException(InvalidTaskDescriptionException.NULL_DESCRIPTION_ERROR);
        }
        if (description.trim().isEmpty()) {
            throw new InvalidTaskDescriptionException(InvalidTaskDescriptionException.EMPTY_DESCRIPTION_ERROR);
        }
    }

    public static void validateExpireDate(LocalDateTime expireDate) {
        if (expireDate != null && expireDate.isBefore(LocalDateTime.now())) {
            throw new TaskExpireDateInPastException(
                    "Task expire date cannot be in the past: " + expireDate
            );
        }
    }
}
