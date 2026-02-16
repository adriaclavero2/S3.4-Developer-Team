package infrastructure.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import common.exception.DataAccessException;
import common.persistance.TaskDAO;
import infrastructure.mongo.connection.MongoDBConnection;
import org.bson.Document;
import task.model.Task;

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
            if (doc == null || doc.isEmpty()) {
                throw new IllegalArgumentException("The document can't be empty");
            }
            if (!doc.containsKey("title")) {
                throw new IllegalArgumentException("The task must have a title");
            }
            collection.insertOne(doc);
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
            Object idValue = doc.get("_id");
            if (idValue == null) {
                throw new IllegalArgumentException("The document must have a -id to be update");
            }

            Document filter = new Document("_id", idValue);

            Document docToUpdate = new Document(doc);
            docToUpdate.remove("_id");

            collection.updateOne(filter, new Document("$set", docToUpdate)); /*Metodo de Mongo para update. En new Document "$set" lo va a interpretar como la funcion set de mongo y modifica el elemento en el server. las key que comienzan con $ se interpretan como operadores de accion*/
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
