package task.service;

import task.model.Task;
import task.repository.TaskRepository;
import task.enums.Priority;
import task.enums.TaskState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class TaskService {

    private TaskRepository repository;

    public TaskService() {
        this.repository = new TaskRepository();
    }

    public Task createTask(String title, String description, LocalDateTime expireDate, Priority priority) {

        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Title is required");
        }

        Task task = new Task(title);
        task.setDescription(description);
        task.setExpireDate(expireDate);

        if (priority != null) {
            task.setPriority(priority);
        }

        return repository.save(task);
    }

    public Optional<Task> getTask(int id) {
        return repository.findById(id);
    }

    public List<Task> getAllTasks() {
        return repository.findAll();
    }

    public Task updateTask(int id, String title, String description,
                           LocalDateTime expireDate, Priority priority, TaskState state) {
        Optional<Task> existing = repository.findById(id);
        if (existing.isEmpty()) {
            throw new IllegalArgumentException("Task not found");
        }

        Task task = existing.get();
        task.setTitle(title);
        task.setDescription(description);
        task.setExpireDate(expireDate);
        task.setPriority(priority);
        task.setTaskState(state);

        return repository.update(task);
    }

    public void deleteTask(int id) {
        repository.deleteById(id);
    }

    public void completeTask(int id) {
        Optional<Task> task = repository.findById(id);
        if (task.isPresent()) {
            task.get().setTaskState(TaskState.COMPLETED);
            repository.update(task.get());
        }
    }

    public List<Task> getTasksByPriority(Priority priority) {
        return repository.findByPriority(priority);
    }

    public List<Task> getTasksByState(TaskState state) {
        return repository.findByState(state);
    }
}