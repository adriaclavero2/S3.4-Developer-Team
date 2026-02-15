package infrastructure.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import infrastructure.mongo.connection.MongoDBConnection;
import org.bson.Document;
import task.model.Task;
import task.model.TaskCopyBuilder;

import java.util.List;
import java.util.Optional;


public class MongoTaskDAOAdapter implements TaskDAO {

    private final MongoCollection<Document> collection;

    public MongoTaskDAOAdapter() {
        MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = database.getCollection("tasks");
    }

    @Override
    public void save(Task entity) {
        try {
            Document doc = new Document()
                    .append("title", entity.getTitle())
                    .append("description", entity.getDescription())
                    .append("expiredAt", entity.getExpireDate() != null ? entity.getExpireDate().toString() : null)
                    .append("priority", entity.getPriority().name())
                    .append("status", entity.getTaskState().name())
                    .append("createdAt", entity.getCreationDate().toString());

            collection.insertOne(doc);

            String generatedId = doc.getObjectId("_id").toHexString();

            entity.setId(generatedId);

           Task taskWithId = new TaskCopyBuilder()
                        .copyOf(entity)
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
