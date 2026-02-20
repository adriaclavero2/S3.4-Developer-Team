package task.mapper;

import task.dto.OutputTaskDTO;
import task.dto.TaskDTO;
import task.enums.Priority;
import task.model.Task;
import task.model.TaskBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskToDTOMapper {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private DateTimeFormatter onlyDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Task dtoToTask(TaskDTO dto) {
        LocalDateTime expireDate = !dto.expireDate().isEmpty() ? LocalDate.parse(dto.expireDate(), onlyDateFormatter).atTime(23, 59, 59) : null;
        Priority priority = !dto.priority().isEmpty() ? Priority.valueOf(dto.priority().toUpperCase()) : Priority.MEDIUM;

        Task task = TaskBuilder.newTask()
                .withTitle(dto.title())
                .withDescription(dto.description())
                .withExpireDate(expireDate)
                .withPriority(priority)
                .build();

        return task;
    }

    public OutputTaskDTO taskToDto(Task task, String outputState) {
        String creationDate = task.getCreationDate().format(formatter);
        String expireDate = task.getExpireDate() != null ? task.getCreationDate().format(formatter) : "undefined";


        OutputTaskDTO output = new OutputTaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                expireDate,
                creationDate,
                task.getPriority().toString(),
                task.getTaskState().toString(),
                outputState);

        return output;
    }
}
