package task.service;


import common.exception.DataAccessException;
import common.exception.TaskNotFoundException;
import task.model.Task;
import task.repository.TaskRepository;

import java.util.List;

public class TaskService {
    private final TaskRepository repository;

    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    public void createTask(Task newTask) {
        try {
            repository.create(newTask);
            System.out.println("New task " + newTask.getTitle() + " created");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }

    public Task getTaskById(String id) {
        return repository.getById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

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

    public String listCompletedTasks() {
        try {
            List<Task> completedTasks = repository.getCompletedTasks();

            if (completedTasks.isEmpty()) {
                return "No tasks completed";
            }

            StringBuilder sb = new StringBuilder("--- COMPLETED TASKS ---\n");
            for (Task task : completedTasks) {
                sb.append(String.format("- %s: %s (Created: %s, Finished: %s)\n",
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
