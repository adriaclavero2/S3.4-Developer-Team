package task.repository;

import common.exception.DataAccessException;
import common.exception.TaskNotFoundException;
import common.persistance.TaskDAO;
import common.utils.Mapper;
import org.bson.Document;
import task.enums.TaskState;
import task.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskRepositoryImpl implements TaskRepository{
    private final TaskDAO taskDAO;
    private final Mapper<Task,Document> mapper;

    public TaskRepositoryImpl(TaskDAO taskDAO, Mapper<Task,Document> mapper) {
        this.taskDAO = taskDAO;
        this.mapper = mapper;
    }

    @Override
    public void create(Task entity) {
        Document doc = mapper.toDocument(entity);
        taskDAO.save(doc);
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

            Document doc = mapper.toDocument(entity);
            taskDAO.update(doc);
            System.out.println("Log: task updated successfully");

    }

    @Override
    public void remove(String id) {
        taskDAO.delete(id);
    }

    @Override
    public List<Task> getTasksByStatus(TaskState state) {
        List<Document> docs = taskDAO.findTasksByStatus(state);
        return docs.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
