package task.model;

import common.exception.InvalidTaskDescriptionException;
import task.model.BuilderSteps.*;
import task.model.Task;
import task.enums.Priority;
import task.enums.TaskState;
import common.exception.InvalidTaskTitleException;
import task.exceptions.TaskExpireDateInPastException;
import java.time.LocalDateTime;

public class TaskBuilder {

    public static TitleStep newTask() {
        return new Builder();
    }

    private static class Builder implements TitleStep {
        private Task newTask;

        private Builder() {
            this.newTask = new Task();
        }

        public DescriptionStep withTitle(String title) {
            validateTitle(title);
            this.newTask.setTitle(title);
            return new DescriptionStepImpl();
        }

        private class DescriptionStepImpl implements DescriptionStep {
            @Override
            public OptionalSteps withDescription(String description) {
                validateDescription(description);
                newTask.setDescription(description);
                return new OptionalStepsImpl();
            }
        }

        private class OptionalStepsImpl implements OptionalSteps {
            @Override
            public TaskStateExpDateStep withPriority(Priority priority) {
                newTask.setPriority(priority);
                return new TaskStateExpDateStepImpl();
            }

            @Override
            public PriorityTaskStateStep withExpireDate(LocalDateTime date) {
                validateExpireDate(date);
                newTask.setExpireDate(date);
                return new PriorityTaskStateImpl();
            }

            @Override
            public PriorityExpDateStep withTaskState(TaskState taskState) {
                newTask.setTaskState(taskState);
                return new PriorityExpDateImpl();
            }

            @Override
            public Task build() {
                return newTask;
            }
        }

        private class TaskStateExpDateStepImpl implements TaskStateExpDateStep {
            @Override
            public TaskStateStep withExpireDate(LocalDateTime date) {
                validateExpireDate(date);
                newTask.setExpireDate(date);
                return new TaskStateStepImpl();
            }

            @Override
            public ExpireDateStep withTaskState(TaskState taskState) {
                newTask.setTaskState(taskState);
                return new ExpireDateImpl();
            }

            @Override
            public Task build() {
                return newTask;
            }
        }

        private class PriorityTaskStateImpl implements PriorityTaskStateStep {
            @Override
            public TaskStateStep withPriority(Priority priority) {
                newTask.setPriority(priority);
                return new TaskStateStepImpl();
            }

            @Override
            public PriorityStep withTaskState(TaskState taskState) {
                newTask.setTaskState(taskState);
                return new PriorityStepImpl();
            }

            @Override
            public Task build() {
                return newTask;
            }
        }

        private class PriorityExpDateImpl implements PriorityExpDateStep {
            @Override
            public PriorityStep withExpireDate(LocalDateTime date) {
                validateExpireDate(date);
                newTask.setExpireDate(date);
                return new PriorityStepImpl();
            }

            @Override
            public ExpireDateStep withPriority(Priority priority) {
                newTask.setPriority(priority);
                return new ExpireDateImpl();
            }

            @Override
            public Task build() {
                return newTask;
            }
        }

        private class TaskStateStepImpl implements TaskStateStep {
            @Override
            public BuildStep withTaskState(TaskState taskState) {
                newTask.setTaskState(taskState);
                return new BuildStepImpl();
            }

            @Override
            public Task build() {
                return newTask;
            }
        }

        private class PriorityStepImpl implements PriorityStep {
            @Override
            public BuildStep withPriority(Priority priority) {
                newTask.setPriority(priority);
                return new BuildStepImpl();
            }

            @Override
            public Task build() {
                return newTask;
            }
        }

        private class ExpireDateImpl implements ExpireDateStep {
            @Override
            public BuildStep withExpireDate(LocalDateTime date) {
                validateExpireDate(date);
                newTask.setExpireDate(date);
                return new BuildStepImpl();
            }

            @Override
            public Task build() {
                return newTask;
            }
        }

        private class BuildStepImpl implements BuildStep {
            @Override
            public Task build() {
                return newTask;
            }
        }

        // ================ VALIDATION PRIVATE METHODS ====================

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

        private void validateDescription(String description) {
            if (description == null) {
                throw new InvalidTaskDescriptionException(InvalidTaskDescriptionException.NULL_DESCRIPTION_ERROR);
            }
            if (description.trim().isEmpty()) {
                throw new InvalidTaskDescriptionException(InvalidTaskDescriptionException.EMPTY_DESCRIPTION_ERROR);
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
}