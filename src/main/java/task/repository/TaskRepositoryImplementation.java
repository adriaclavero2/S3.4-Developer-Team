package task.repository;

import common.persistance.TaskDAO;
import task.model.Task;

import java.util.List;
import java.util.Optional;

public class TaskRepositoryImplementation implements TaskRepository{
    private final TaskDAO dao;

    public TaskRepositoryImplementation(TaskDAO dao) {
        this.dao = dao;
    }

    @Override
    public Task create(Task entity) {
        return null;
    }

    @Override
    public Optional<Task> getById(String s) {
        return Optional.empty();
    }

    @Override
    public List<Task> getAll() {
        return List.of();
    }

    @Override
    public void modify(Task entity) {

    }

    @Override
    public void remove(String s) {

    }

    @Override
    public List<Task> getCompletedTasks() {
        return List.of();
    }
}
