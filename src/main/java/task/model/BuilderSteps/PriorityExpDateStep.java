package task.model.BuilderSteps;

import task.enums.Priority;
import java.time.LocalDateTime;

public interface PriorityExpDateStep extends PriorityStep, ExpireDateStep {
    @Override
    PriorityStep withExpireDate(LocalDateTime date);

    @Override
    ExpireDateStep withPriority(Priority priority);
}
