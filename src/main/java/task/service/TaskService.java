package task.service;


import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import task.enums.TaskState;
import task.model.Task;
import task.repository.TaskRepository;

import java.util.List;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public String createTask(Task newTask) {

        if(newTask == null){
            throw new IllegalArgumentException("CreateTask: Task cannot be null");
            // or return "Provide a task to create";
        }

        try {
            repository.create(newTask);
            return "New task " + newTask.getTitle() + " created";
        } catch (RuntimeException e) {
            return "Unexpected error: " + e.getMessage();
        }
    }

    public Task getTaskById(String id) {
        return repository.getById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

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

    public String updateTask(Task newTask) {
        if (newTask == null) {
            return "Error: Task cannot be null";
        }
        if (newTask.getId() == null) {
            return "Error: Cannot update a task without an ID";
        }
        try {
            repository.modify(newTask);
            return "Task with _id " + newTask.getId() + " updated successfully";
        } catch (DataAccessException e) {
            return "Persistence error: " + e.getMessage();
        } catch (Exception e) {
            return "Unexpected error: " + e.getMessage();
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
