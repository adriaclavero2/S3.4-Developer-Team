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

    public String createTask(Task newTask) {

        if(newTask == null){
            throw new IllegalArgumentException("CreateTask: Task cannot be null");
            // or return "Provide a task to create";
        }

        try {
            repository.create(newTask);
            return "New task " + newTask.getTitle() + " created";

        } catch (IllegalArgumentException e) {
            return "Something go wrong with data format";
        } catch (DataAccessException e) {
            return "Error raised during task creation. Try again.";
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
