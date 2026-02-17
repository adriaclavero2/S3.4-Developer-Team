package task.model.BuilderSteps;

import task.model.TaskBuilder;

public interface DescriptionStep {
    TaskBuilder.OptionalSteps withDescription(String content);
}
