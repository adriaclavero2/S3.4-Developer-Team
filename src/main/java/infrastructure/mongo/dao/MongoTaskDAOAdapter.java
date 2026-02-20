package infrastructure.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import common.persistance.TaskDAO;
import org.bson.Document;
import org.bson.types.ObjectId;
import task.model.Task;

import java.util.List;
import java.util.Optional;


public class MongoTaskDAOAdapter implements TaskDAO {

    private final MongoCollection<Document> collection;

    public MongoTaskDAOAdapter(MongoCollection<Document> collection) {
       // MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = collection;
    }

    @Override
    public Document save(Document doc) {
        if (doc == null || doc.isEmpty())
            throw new IllegalArgumentException("The document can't be empty");

        if (!doc.containsKey("title"))
            throw new IllegalArgumentException("The task must have a title");

        try {
            collection.insertOne(doc);
            return doc;

        } catch (MongoException e) {
            throw new DataAccessException("MongoDB", e);
        }

    }

    @Override
    public Optional<Document> findByID(String id) {
        try {
            Document filter = new Document("_id", new org.bson.types.ObjectId(id));
            Document result = collection.find(filter).first();
            return Optional.ofNullable(result);
        } catch(IllegalArgumentException e) {
            return Optional.empty();
        }
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
        DeleteResult deleteResult;
        try {
            Document filter = new Document("_id", new org.bson.types.ObjectId(id));
            deleteResult = collection.deleteOne(filter);

        } catch (IllegalArgumentException e) {
            throw new InvalidTaskIDException("MongoDB delete, new document construction raised an error" + e.getMessage(), e);

        } catch (MongoException e) {
            throw new DataAccessException("MongoDB delete", e);
        }

        if (deleteResult.getDeletedCount() == 0)
            throw new TaskNotFoundException(id);
    }

    @Override
    public List<Task> findCompletedTasks() {
        return List.of();
    }
}
