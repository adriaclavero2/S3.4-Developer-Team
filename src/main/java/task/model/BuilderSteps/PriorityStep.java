package task.model.BuilderSteps;

import task.enums.Priority;

public interface PriorityStep extends BuildStep {
    public BuildStep withPriority(Priority priority);
}
