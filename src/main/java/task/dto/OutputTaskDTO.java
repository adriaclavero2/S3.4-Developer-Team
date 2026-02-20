package task.dto;

public record OutputTaskDTO(
        String id,
        String title,
        String description,
        String expireDate,
        String creationDate,
        String priority,
        String taskState
) {}
