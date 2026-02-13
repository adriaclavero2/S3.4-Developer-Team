package infrastructure.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import org.bson.Document;
import task.model.Task;

import java.util.List;
import java.util.Optional;


public class MongoTaskDAOAdapter implements TaskDAO {

    private final MongoCollection<Document> collection;

    public MongoTaskDAOAdapter(MongoCollection<Document> collection) {
        this.collection = collection;
    }

    @Override
    public void save(Task entity) {
        try {
            Document doc = new Document()
                    .append("title", entity.getTitle())
                    .append("description", entity.getDescription())
                    .append("expiredAt", entity.getExpirationDate().toString() != null ? entity.getExpirationDate().toString() : null)
                    .append("priority", entity.getPriority().name())
                    .append("status", entity.getTaskState().name())
                    .append("createdAt", entity.getCreationDate().toString());

            collection.insertOne(doc);

            String generatedId = doc.getObjectId("_id").toHexString();

            Task taskWithId = new Task.Builder()
                    .copyFrom(entity)
                    .setId(generatedId)
                    .build();
        } catch (MongoException e) {
            throw new DataAccessException("MongoDB", e);
        }

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

    @Override
    public List<Task> findCompletedTasks() {
        return List.of();
    }
}
