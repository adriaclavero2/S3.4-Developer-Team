package task.service;


import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import task.dto.ErrorOutputDTO;
import task.dto.OutputDTO;
import task.dto.OutputTaskDTO;
import task.mapper.TaskToDTOMapper;
import task.model.Task;
import task.repository.TaskRepository;

import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;
    private final TaskToDTOMapper mapper = new TaskToDTOMapper();

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public OutputDTO createTask(Task newTask) {
        if(newTask == null){
            throw new IllegalArgumentException("CreateTask: Task cannot be null");
        }

        try {
            Task createdTask = repository.create(newTask);
            return mapper.taskToDto(createdTask, "New Task created");
        } catch (IllegalArgumentException e) {
            return new ErrorOutputDTO("Something go wrong with data format");
        } catch (DataAccessException e) {
            return new ErrorOutputDTO("Error raised during task creation. Try again.");
        }
    }

    public OutputDTO getTaskById(String id) {
        Optional<Task> taskOptional = repository.getById(id);

        if(taskOptional.isEmpty())
            return new ErrorOutputDTO("Task not found");

        Task output = taskOptional.get();
        return mapper.taskToDto(output, "Task retrieved successfully");
    }

    public String removeTask(String id) {
        try {
            repository.remove(id);
            return "Task (" + id + ") successfully deleted.";

        } catch (TaskNotFoundException e) {
            return "Task (" + id + ") does not exist.";
        } catch (InvalidTaskIDException e) {
            return "Invalid id format.";
        } catch (DataAccessException e) {
            return "Error raised during task deletion. Try again.";
        }
    }
}
