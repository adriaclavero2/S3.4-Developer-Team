package task.model;

import task.enums.Priority;

import java.time.LocalDateTime;

public record TaskDTO(
        String title,
        String description,
        LocalDateTime expireDate,
        Priority priority) {}
