package task.repository;

import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import common.utils.Mapper;
import org.bson.Document;
import task.mapper.TaskMapper;
import task.model.Task;

import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository{
    private final TaskDAO taskDAO;
    private final Mapper<Task,Document> mapper;

    public TaskRepositoryImpl(TaskDAO taskDAO, Mapper<Task,Document> mapper) {
        this.taskDAO = taskDAO;
        this.mapper = mapper;
    }

    @Override
    public Task create(Task entity) {
        Document doc = mapper.toDocument(entity);
        doc = taskDAO.save(doc);
        return mapper.toDomain(doc);
    }

    @Override
    public Optional<Task> getById(String id) {
        return taskDAO.findByID(id)
                .map(doc -> (Task) mapper.toDomain(doc));
    }

    @Override
    public List<Task> getAll() {
        try {
            taskDAO.findAll();
            System.out.println("Log: task list all successfully");
        } catch (DataAccessException e) {
            throw new DataAccessException("MongoDB", e);
        }
        return List.of();
    }

    @Override
    public void modify(Task entity) {
        try {
            Document doc = mapper.toDocument(entity);
            taskDAO.update(doc);
            System.out.println("Log: task updated successfully");
        } catch (DataAccessException e) {
            throw new DataAccessException("Error modifying task: " + e.getMessage());
        }

    }

    @Override
    public void remove(String id) {
        taskDAO.delete(id);
    }

    @Override
    public List<Task> getCompletedTasks() {
        return List.of();
    }
}
