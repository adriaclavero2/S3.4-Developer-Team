package task.dto;

public record TaskDTO(
        String title,
        String description,
        String expireDate,
        String priority
) {}
