package task.dto;

import java.util.List;

public record TaskListOutputDTO(
        List<OutputTaskDTO> tasks,
        String outputState
) implements OutputDTO {

    @Override
    public String getOutputState() {
        return outputState;
    }
}
