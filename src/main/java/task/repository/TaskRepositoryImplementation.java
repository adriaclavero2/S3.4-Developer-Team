package task.repository;

import com.mongodb.client.MongoCollection;
import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import org.bson.Document;
import task.model.Task;

import java.util.List;
import java.util.Optional;

public class TaskRepositoryImplementation implements TaskRepository{
    private final TaskDAO taskDAO;

    public TaskRepositoryImplementation(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    public void create(Task entity) {
        try {
            taskDAO.save(entity);
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
