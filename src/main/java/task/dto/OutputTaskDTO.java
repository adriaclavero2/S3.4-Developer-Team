package task.dto;

public record OutputTaskDTO (
        String id,
        String title,
        String description,
        String expireDate,
        String creationDate,
        String priority,
        String taskState,
        String outputState) implements OutputDTO
        {
            public String getOutputState() {
                return outputState();
            }
        }
