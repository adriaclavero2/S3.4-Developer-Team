package task.repository;

import task.model.Task;

import java.util.List;
import java.util.Optional;

public class MongoTaskDAOAdapter implements TaskDAO {
    @Override
    public List<Task> findCompletedTasks() {
        return List.of();
    }

    @Override
    public void save(Task entity) {

    }

    @Override
    public Optional<Task> findByID(String id) {
        return Optional.empty();
    }

    @Override
    public List<Task> findAll() {
        return List.of();
    }

    @Override
    public void update(Task entity) {

    }

    @Override
    public void delete(String id) {

    }
}
