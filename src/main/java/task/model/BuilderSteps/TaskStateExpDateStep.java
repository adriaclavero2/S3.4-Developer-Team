package task.model.BuilderSteps;

import task.enums.TaskState;
import java.time.LocalDateTime;

public interface TaskStateExpDateStep extends TaskStateStep, ExpireDateStep {
    @Override
    TaskStateStep withExpireDate(LocalDateTime date);

    @Override
    ExpireDateStep withTaskState(TaskState taskState);
}
