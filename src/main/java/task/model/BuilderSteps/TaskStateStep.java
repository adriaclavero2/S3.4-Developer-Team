package task.model.BuilderSteps;

import task.enums.TaskState;

public interface TaskStateStep extends BuildStep {
    public BuildStep withTaskState(TaskState taskState);
}
