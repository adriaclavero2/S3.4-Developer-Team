package infrastructur.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.DeleteResult;
import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MongoTaskDAOAdapterTest {

    @Mock
    private MongoCollection<Document> collection;

    @Mock
    private FindIterable<Document> findIterable; // Necesario para el .find(). Se crea porque es el puntero que punta al inicio de un bloque de memoria con resultados

    @Mock
    private DeleteResult deleteResult;

    @InjectMocks
    private MongoTaskDAOAdapter dao;

    @Test
    @DisplayName("It must return a Document if the ID exists in Mongo")
    void findByID_Positive() {

        String validId = "69949f595f811f0d2276b457";
        Document mockDoc = new Document("_id", new ObjectId(validId)).append("title", "Task 1");

        // Mockeamos la cadena fluida: collection.find(filter).first()
        //Le decimos: "Cuando llamen a find, devuelve nuestro impostor findIterable"
        when(collection.find(any(Document.class))).thenReturn(findIterable);
        //Le decimos: "Cuando llamen a first sobre ese impostor, devuelve el documento"
        when(findIterable.first()).thenReturn(mockDoc);

        Optional<Document> result = dao.findByID(validId);

        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().get("title"));
    }

    @Test
    @DisplayName("It should return Optional.empty if the ID has an invalid format")
    void findByID_InvalidFormat() {

        String invalidId = "this-is-not-an-object-id";

        Optional<Document> result = dao.findByID(invalidId);

        assertTrue(result.isEmpty());
        // No hace falta mockear nada porque la excepción salta antes de llegar a la collection
    }

    @Test
    @DisplayName("Should complete without exception when task exists and is deleted")
    void delete_validId_taskExists() {
        String validId = "69949f595f811f0d2276b457";

        when(collection.deleteOne(any(Document.class))).thenReturn(deleteResult);
        when(deleteResult.getDeletedCount()).thenReturn(1L);

        assertDoesNotThrow(() -> dao.delete(validId));

        verify(collection).deleteOne(any(Document.class));
    }

    @Test
    @DisplayName("Should throw TaskNotFoundException when no document matches the ID")
    void delete_validId_taskNotFound() {
        String validId = "69949f595f811f0d2276b457";

        when(collection.deleteOne(any(Document.class))).thenReturn(deleteResult);

        when(deleteResult.getDeletedCount()).thenReturn(0L);

        assertThrows(TaskNotFoundException.class, () -> dao.delete(validId));
    }

    @Test
    @DisplayName("Should throw InvalidTaskIDException when ID format is invalid")
    void delete_invalidIdFormat() {
        String invalidId = "this-is-not-an-object-id";

        assertThrows(InvalidTaskIDException.class, () -> dao.delete(invalidId));

        verifyNoInteractions(collection);
    }

    @Test
    @DisplayName("Should throw DataAccessException when MongoDB raises an infrastructure error")
    void delete_mongoException() {
        String validId = "69949f595f811f0d2276b457";

        when(collection.deleteOne(any(Document.class)))
                .thenThrow(new MongoException("Simulated connection failure"));

        assertThrows(DataAccessException.class, () -> dao.delete(validId));
    }
}
