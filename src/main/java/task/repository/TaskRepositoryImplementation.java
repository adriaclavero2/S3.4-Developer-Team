package task.repository;

import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import common.utils.GenericMapper;
import org.bson.Document;
import task.model.Task;
import java.util.List;
import java.util.Optional;

public class TaskRepositoryImplementation implements TaskRepository{

    private final TaskDAO taskDAO;
    private final GenericMapper mapper = new GenericMapper() {
        @Override
        public Object toDocument(Object o) {
            return null;
        }

        @Override
        public Object toDomain(Object o) {
            return null;
        }
    };

    public TaskRepositoryImplementation(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    @Override
    public void create(Task entity) {
        try {
            Document doc = mapper.toDocument(entity);
            taskDAO.save(doc);
            System.out.println("Log: task created successfully");
        } catch (DataAccessException e) {
            throw new DataAccessException("MongoDB" + e);
        }
    }

    @Override
    public Optional<Task> getById(String id) {
        return Optional.empty();
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
        try {
            taskDAO.delete(id);
            System.out.println("Log: task deleted successfully");
        } catch (DataAccessException e) {
            throw new DataAccessException("Error al eliminar la tarea: " + e.getMessage());
        }

    }

    @Override
    public List<Task> getCompletedTasks() {
        return List.of();
    }
}
