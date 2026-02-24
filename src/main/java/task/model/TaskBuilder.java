package task.model;

import common.exception.InvalidTaskDescriptionException;
import common.utils.Validator;
import task.model.BuilderSteps.*;
import task.model.Task;
import task.enums.Priority;
import task.enums.TaskState;
import common.exception.InvalidTaskTitleException;
import common.exception.TaskExpireDateInPastException;
import java.time.LocalDateTime;

import static common.utils.Validator.*;

public class TaskBuilder {

    public static TitleStep newTask() {
        return new Builder();
    }

    public static TitleStep update(Task existingTask) {
        return new Builder(existingTask);
    }

    private static class Builder implements TitleStep {
        private Task newTask;

        private Builder() {
            this.newTask = new Task();
        }

        private Builder(Task existingTask) {
            this.newTask = new Task(existingTask);
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
    }
}