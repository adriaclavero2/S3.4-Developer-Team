package task.model;

import task.enums.Priority;
import task.enums.TaskState;

import java.time.LocalDateTime;

public record TaskDTO(
        String id,
        String title,
        String decription,
        LocalDateTime expireDate,
        LocalDateTime creationDate,
        Priority priority,
        TaskState taskState) {}
