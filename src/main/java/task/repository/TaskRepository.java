package task.repository;

import task.model.Task;
import task.enums.Priority;
import task.enums.TaskState;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskRepository implements CrudRepository<Task, Integer> {

    private List<Task> tasks;
    private int currentId;

    public TaskRepository() {
        this.tasks = new ArrayList<>();
        this.currentId = 1;
    }

    @Override
    public Task save(Task task) {
        task.setId(currentId++);
        tasks.add(task);
        return task;
    }

    @Override
    public Optional<Task> findById(Integer id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }

    @Override
    public List<Task> findAll() {
        return new ArrayList<>(tasks);
    }

    @Override
    public Task update(Task task) {
        deleteById(task.getId());
        tasks.add(task);
        return task;
    }

    @Override
    public void deleteById(Integer id) {
        tasks.removeIf(task -> task.getId() == id);
    }

    // Custom methods
    public List<Task> findByPriority(Priority priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getPriority() == priority) {
                result.add(task);
            }
        }
        return result;
    }

    public List<Task> findByState(TaskState state) {
        List<Task> result = new ArrayList<>();
        for (Task task : tasks) {
            if (task.getTaskState() == state) {
                result.add(task);
            }
        }
        return result;
    }
}