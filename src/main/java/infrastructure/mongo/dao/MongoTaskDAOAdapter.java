package infrastructure.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import infrastructure.mongo.connection.MongoDBConnection;
import org.bson.Document;
import org.bson.conversions.Bson;
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
    public void save(Document doc) {
        try {
           /* Document doc = new Document()
                    .append("title", entity.getTitle())
                    .append("description", entity.getDescription())
                    .append("expiredAt", entity.getExpireDate() != null ? entity.getExpireDate().toString() : null)
                    .append("priority", entity.getPriority().name())
                    .append("status", entity.getTaskState().name())
                    .append("createdAt", entity.getCreationDate().toString());*/

            collection.insertOne(doc);

            //String generatedId = doc.getObjectId("_id").toHexString();

           /*entity.setId(generatedId);

           Task taskWithId = new TaskCopyBuilder()
                        .copyOf(entity)
                        .setId(generatedId)
                        .build();*/

        } catch (MongoException e) {
            throw new DataAccessException("MongoDB", e);
        }

    }

    @Override
    public Optional<Document> findByID(String id) {
        return Optional.empty();
    }

    @Override
    public List<Document> findAll() {
        return List.of();
    }

    @Override
    public void update(Document doc) {
        try {
           /*Document query = new Document("_id", new org.bson.types.ObjectId(entity.getId()));/*Metodo de Mongo que toma el String hexadecimal y lo convierte en binario

            Document updates = new Document()
                    .append("title", entity.getTitle())
                    .append("description", entity.getDescription())
                    .append("expiredAt", entity.getExpireDate() != null ? entity.getExpireDate().toString() : null)
                    .append("priority", entity.getPriority().name())
                    .append("status", entity.getTaskState().name());*/

            collection.updateOne(doc.get("_id"), new Document("$set", doc)); /*Metodo de Mongo para update. En new Document "$set" lo va a interpretar como la funcion set de mongo y modifica el elemento en el server. las key que comienzan con $ se interpretan como operadores de accion*/
        } catch (Exception e) {
            throw new DataAccessException("MongoDB update", e);
        }

    }

    @Override
    public void delete(String id) {
        try {
            collection.deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
        } catch (Exception e) {
            throw new DataAccessException("MongoDB delete", e);
        }
    }

    @Override
    public List<Task> findCompletedTasks() {
        return List.of();
    }
}
