package task.service;


import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import task.model.Task;
import task.repository.TaskRepository;

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
