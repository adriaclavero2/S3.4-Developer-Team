package infrastructure.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import common.persistance.TaskDAO;
import org.bson.Document;
import org.bson.conversions.Bson;
import task.enums.TaskState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class MongoTaskDAOAdapter implements TaskDAO {

    private final MongoCollection<Document> collection;

    public MongoTaskDAOAdapter(MongoCollection<Document> collection) {
       // MongoDatabase database = MongoDBConnection.getDatabase();
        this.collection = collection;
    }

    @Override
    public void save(Document doc) {
        if (doc == null || doc.isEmpty())
            throw new IllegalArgumentException("The document can't be empty");

        if (!doc.containsKey("title"))
            throw new IllegalArgumentException("The task must have a title");

        try {
            collection.insertOne(doc);
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
        Object idValue = doc.get("_id");
        try {
            Document filter = new Document("_id", idValue);
            Document docToUpdate = new Document(doc);
            docToUpdate.remove("_id");

            UpdateResult result = collection.updateOne(filter, new Document("$set", docToUpdate)); /*Metodo de Mongo para update. En new Document "$set" lo va a interpretar como la funcion set de mongo y modifica el elemento en el server. las key que comienzan con $ se interpretan como operadores de accion*/

            if(result.getMatchedCount() == 0) {
                throw new DataAccessException("Task with _id " + idValue + " not found.");
            }
        } catch (DataAccessException e) {
            // Si es nuestra propia excepción de "no encontrado", la relanzamos tal cual
            throw e;
        } catch (Exception e) {
            throw new DataAccessException("MongoDB update error ", e);
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
    public List<Document> findTasksByStatus(TaskState state) {
        try {
            Bson filter = Filters.eq("status", state.name());
            return collection.find(filter).into(new ArrayList<>());
        } catch (Exception e) {
            throw new DataAccessException("Error retrieving tasks with state " + state + " from MongoDB", e);
        }
    }
}
