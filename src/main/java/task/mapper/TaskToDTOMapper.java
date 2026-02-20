package task.mapper;

import task.dto.OutputTaskDTO;
import task.dto.TaskDTO;
import task.enums.Priority;
import task.model.Task;
import task.model.TaskBuilder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskToDTOMapper {
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private DateTimeFormatter onlyDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public Task dtoToTask(TaskDTO dto) {
        LocalDateTime expireDate = !dto.expireDate().isEmpty() ? LocalDateTime.parse(dto.expireDate(), onlyDateFormatter) : null;

        Task task = TaskBuilder.newTask()
                .withTitle(dto.title())
                .withDescription(dto.description())
                .withExpireDate(expireDate)
                .withPriority(Priority.valueOf(dto.priority().toUpperCase()))
                .build();

        return task;
    }

    public OutputTaskDTO taskToDto(Task task) {
        String creationDate = task.getCreationDate().format(formatter);
        String expireDate = task.getExpireDate() != null ? task.getCreationDate().format(formatter) : "undefined";


        OutputTaskDTO output = new OutputTaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                expireDate,
                creationDate,
                task.getPriority().toString(),
                task.getTaskState().toString());

        return output;
    }
}
