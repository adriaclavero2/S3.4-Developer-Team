package task.repository;

import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import common.utils.Mapper;
import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import org.bson.Document;
import task.mapper.TaskMapper;
import task.model.Task;

import java.util.List;
import java.util.Optional;

public class TaskRepositoryImpl implements TaskRepository{
    private final TaskDAO taskDAO;
    private final Mapper<Task,Document> mapper;

    public TaskRepositoryImpl() {
        this.taskDAO = new MongoTaskDAOAdapter();
        this.mapper = new TaskMapper();
    }

    public TaskRepositoryImpl(TaskDAO taskDAO, Mapper<Task,Document> mapper) {
        this.taskDAO = taskDAO;
        this.mapper = mapper;
    }

    @Override
    public void create(Task entity) {
        try {
            Document newDocument = mapper.toDocument(entity);
            taskDAO.save(newDocument);
            System.out.println("Log: task created successfully");
        } catch (DataAccessException e) {
            throw new DataAccessException("MongoDB" + e);
        }
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
