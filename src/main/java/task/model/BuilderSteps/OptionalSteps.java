package task.model.BuilderSteps;

import task.enums.Priority;
import task.enums.TaskState;
import task.model.Task;

import java.time.LocalDateTime;

public interface OptionalSteps extends TaskStateExpDateStep, PriorityExpDateStep, PriorityTaskStateStep{
    @Override
    TaskStateExpDateStep withPriority(Priority priority);

    @Override
    PriorityTaskStateStep withExpireDate(LocalDateTime date);

    @Override
    PriorityExpDateStep withTaskState(TaskState taskState);
}
