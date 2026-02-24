package task.repository;

import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import common.utils.Mapper;
import org.bson.Document;
import task.enums.TaskState;
import task.mapper.TaskMapper;
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
        List<Document> documents = taskDAO.findAll();
        return documents.stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Task modify(Task entity) {

            Document doc = mapper.toDocument(entity);
            Document updatedDoc = taskDAO.update(doc);

            return mapper.toDomain(updatedDoc);
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
