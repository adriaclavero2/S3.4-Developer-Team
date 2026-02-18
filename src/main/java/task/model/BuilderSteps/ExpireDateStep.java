package task.model.BuilderSteps;

import java.time.LocalDateTime;

public interface ExpireDateStep extends BuildStep {
    public BuildStep withExpireDate(LocalDateTime date);
}
