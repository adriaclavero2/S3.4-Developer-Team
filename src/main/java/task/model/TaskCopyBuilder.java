package task.model;

import task.model.Task;
import task.enums.Priority;
import task.enums.TaskState;
import common.exception.InvalidTaskTitleException;
import common.exception.TaskExpireDateInPastException;
import java.time.LocalDateTime;

public class TaskCopyBuilder {

    public interface CopyBuilderSteps {
        CopyBuilderSteps title(String title);
        CopyBuilderSteps description(String description);
        CopyBuilderSteps expireDate(LocalDateTime expireDate);
        CopyBuilderSteps priority(Priority priority);
        CopyBuilderSteps taskState(TaskState taskState);
        CopyBuilderSteps setId(String id);
        Task build();
    }

    private static class CopyBuilder implements CopyBuilderSteps {

        private final Task original;

        private String title = null;
        private String description = null;
        private LocalDateTime expireDate = null;
        private Priority priority = null;
        private TaskState taskState = null;
        private String id = null;

        private boolean titleSet = false;
        private boolean descriptionSet = false;
        private boolean expireDateSet = false;
        private boolean prioritySet = false;
        private boolean taskStateSet = false;
        private boolean idSet = false;

        private CopyBuilder(Task original) {
            if (original == null) {
                throw new IllegalArgumentException("Cannot copy null task");
            }
          /*  if (original.getId().isEmpty() || original.getId() == null) {
                throw new IllegalStateException("Cannot copy task without ID (not persisted yet)");
            }*/
            this.original = original;
        }

        @Override
        public CopyBuilderSteps title(String title) {
            validateTitle(title);
            this.title = title;
            this.titleSet = true;
            return this;
        }

        @Override
        public CopyBuilderSteps description(String description) {
            this.description = description;
            this.descriptionSet = true;
            return this;
        }

        @Override
        public CopyBuilderSteps expireDate(LocalDateTime expireDate) {
            validateExpireDate(expireDate);
            this.expireDate = expireDate;
            this.expireDateSet = true;
            return this;
        }

        @Override
        public CopyBuilderSteps priority(Priority priority) {
            this.priority = priority;
            this.prioritySet = true;
            return this;
        }

        @Override
        public CopyBuilderSteps taskState(TaskState taskState) {
            this.taskState = taskState;
            this.taskStateSet = true;
            return this;
        }

        @Override
        public CopyBuilderSteps setId(String id) {
            this.id = id;
            this.idSet = true;
            return this;
        }

        @Override
        public Task build() {
            String finalTitle = titleSet ? title : original.getTitle();
            String finalDescription = descriptionSet ? description : original.getDescription();
            LocalDateTime finalExpireDate = expireDateSet ? expireDate : original.getExpireDate();
            Priority finalPriority = prioritySet ? priority : original.getPriority();
            TaskState finalTaskState = taskStateSet ? taskState : original.getTaskState();

            validateTitle(finalTitle);
            if (finalExpireDate != null) {
                validateExpireDate(finalExpireDate);
            }

            Task copy = new Task(finalTitle, finalDescription, finalExpireDate,
                    finalPriority, finalTaskState);

            copy.setId(original.getId());

            return copy;
        }


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
     * Creates a copy builder from an existing task
     * @param original
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalStateException
     */
    public static CopyBuilderSteps copyOf(Task original) {
        return new CopyBuilder(original);
    }
}
