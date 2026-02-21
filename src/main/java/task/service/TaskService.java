package task.service;

import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import task.dto.*;
import task.mapper.TaskToDTOMapper;
import task.model.Task;
import task.repository.TaskRepository;

import java.util.Optional;

public class TaskService {
    private final TaskRepository repository;
    private final TaskToDTOMapper mapper;

    public TaskService(TaskRepository repository, TaskToDTOMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public OutputDTO createTask(TaskDTO input) {
        if(input == null){
            throw new IllegalArgumentException("CreateTask: Task cannot be null");
        }

        try {
            Task newTask = mapper.dtoToTask(input);
            Task createdTask = repository.create(newTask);
            return mapper.taskToDto(createdTask, "New Task created");
        } catch (IllegalArgumentException e) {
            return new ErrorOutputDTO("Something go wrong with data format");
        } catch (DataAccessException e) {
            return new ErrorOutputDTO("Error raised during task creation. Try again.");
        }
    }

    public OutputDTO getTaskById(TaskIdDTO idDTO) {
        String id = idDTO.id();
        Optional<Task> taskOptional = repository.getById(id);

        if(taskOptional.isEmpty())
            return new ErrorOutputDTO("Task not found");

        Task output = taskOptional.get();
        return mapper.taskToDto(output, "Task retrieved successfully");
    }

    public OutputDTO removeTask(TaskIdDTO idDTo) {
        try {
            String id = idDTo.id();
            repository.remove(id);
            return new HappyOutputDTO("Task (" + id + ") successfully deleted.");

        } catch (TaskNotFoundException e) {
            return new ErrorOutputDTO("Task (" + idDTo.id() + ") does not exist.");
        } catch (InvalidTaskIDException e) {
            return new ErrorOutputDTO("Invalid id format.");
        } catch (DataAccessException e) {
            return new ErrorOutputDTO("Error raised during task deletion. Try again.");
        }
    }

    public OutputDTO updateTask(TaskUpdateDTO input) {
        if (input == null) {
            throw new IllegalArgumentException("Error: Task cannot be null");
        }
        if (input.id() == null) {
            throw new IllegalArgumentException("Error: Cannot update a task without an ID");
        }
        try {
            Task existingTask = repository.getById(input.id())
                    .orElseThrow(() -> new IllegalArgumentException("Task with ID " + input.id() + " not found"));

            Task taskToUpdate = mapper.dtoUpdateToTask(input, existingTask);

            Task updatedTask = repository.modify(taskToUpdate);

            return mapper.taskToDto(updatedTask, "Task with _id " + updatedTask.getId() + " updated successfully");
        } catch (DataAccessException e) {
            return new ErrorOutputDTO("Persistence error: " + e.getMessage());
        } catch (Exception e) {
            return new ErrorOutputDTO("Unexpected error: " + e.getMessage());
        }
    }

    public String listTasksByStatus(TaskState state) {
        try {
            List<Task> tasks = repository.getTasksByStatus(state);

            if (tasks.isEmpty()) {
                return state == TaskState.COMPLETED ? "No tasks completed" : "No pending tasks";
            }

            String header = state == TaskState.COMPLETED ? "--- COMPLETED TASKS ---" : "--- PENDING TASKS ---";
            StringBuilder sb = new StringBuilder(header).append("\n");
            for (Task task : tasks) {
                sb.append(String.format("- [%s] %s: %s (Created: %s, Finished: %s)\n",
                        task.getPriority(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getCreationDate(),
                        task.getExpireDate()));
            }
            return sb.toString();

        } catch (DataAccessException e) {
            return "Persistence error: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
        }
    }


}
