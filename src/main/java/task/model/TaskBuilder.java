package task.creation;

import task.model.Task;
import task.enums.Priority;
import task.enums.TaskState;
import task.exceptions.InvalidTaskTitleException;
import task.exceptions.TaskExpireDateInPastException;
import java.time.LocalDateTime;

/**
 * TaskBuilder - External Builder for Task entity
 *
 * Features:
 * - Step Builder pattern with interfaces
 * - Validates all business rules
 * - Applies default values
 * - Throws semantic exceptions
 * - Package-private constructor for Task
 */
public class TaskBuilder {

    /**
     * Step 1: Title is mandatory
     */
    public interface TitleStep {
        /**
         * Sets the task title (required field)
         * @param title Task title (cannot be null or empty)
         * @return Next step with optional fields
         * @throws InvalidTaskTitleException if title is invalid
         */
        OptionalSteps title(String title);
    }

    /**
     * Step 2+: All optional fields
     */
    public interface OptionalSteps {
        OptionalSteps description(String description);
        OptionalSteps expireDate(LocalDateTime expireDate);
        OptionalSteps priority(Priority priority);
        OptionalSteps taskState(TaskState taskState);
        Task build();
    }

    /**
     * Builder implementation
     */
    private static class Builder implements TitleStep, OptionalSteps {

        // Required field
        private String title;

        // Optional fields with defaults
        private String description = null;
        private LocalDateTime expireDate = null;
        private Priority priority = Priority.MEDIUM;
        private TaskState taskState = TaskState.NOT_COMPLETED;

        @Override
        public OptionalSteps title(String title) {
            validateTitle(title);
            this.title = title;
            return this;
        }

        @Override
        public OptionalSteps description(String description) {
            this.description = description;
            return this;
        }

        @Override
        public OptionalSteps expireDate(LocalDateTime expireDate) {
            validateExpireDate(expireDate);
            this.expireDate = expireDate;
            return this;
        }

        @Override
        public OptionalSteps priority(Priority priority) {
            this.priority = priority != null ? priority : Priority.MEDIUM;
            return this;
        }

        @Override
        public OptionalSteps taskState(TaskState taskState) {
            this.taskState = taskState != null ? taskState : TaskState.NOT_COMPLETED;
            return this;
        }

        @Override
        public Task build() {
            // Final validation before creating
            validateTitle(title);
            if (expireDate != null) {
                validateExpireDate(expireDate);
            }

            return new Task(title, description, expireDate, priority, taskState);
        }

        // ========== VALIDATIONS ==========

        private void validateTitle(String title) {
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

        private void validateExpireDate(LocalDateTime expireDate) {
            if (expireDate != null && expireDate.isBefore(LocalDateTime.now())) {
                throw new TaskExpireDateInPastException(
                        "Task expire date cannot be in the past: " + expireDate
                );
            }
        }
    }

    /**
     * Entry point to create a new Task
     * @return TitleStep forcing user to provide title first
     */
    public static TitleStep newTask() {
        return new Builder();
    }
}