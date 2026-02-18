package task.model.BuilderSteps;

import task.enums.Priority;
import task.enums.TaskState;

public interface PriorityTaskStateStep extends PriorityStep, TaskStateStep {
    @Override
    TaskStateStep withPriority(Priority priority);

    @Override
    PriorityStep withTaskState(TaskState taskState);
}
