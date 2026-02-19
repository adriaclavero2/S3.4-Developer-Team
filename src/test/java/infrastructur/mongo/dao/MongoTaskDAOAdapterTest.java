package infrastructur.mongo.dao;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import common.exception.DataAccessException;
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
    private UpdateResult updateResult;

    @InjectMocks
    private MongoTaskDAOAdapter dao;

    @Test
    @DisplayName("It must return a Document if the ID exists in Mongo")
    void findByID_Positive() {

        String validId = "69949f595f811f0d2276b457";
        Document mockDoc = new Document("_id", new ObjectId(validId)).append("title", "Task 1").append("description", "Test description");

        // Mockeamos la cadena fluida: collection.find(filter).first()
        //Le decimos: "Cuando llamen a find, devuelve nuestro impostor findIterable"
        when(collection.find(any(Document.class))).thenReturn(findIterable);
        //Le decimos: "Cuando llamen a first sobre ese impostor, devuelve el documento"
        when(findIterable.first()).thenReturn(mockDoc);

        Optional<Document> result = dao.findByID(validId);

        assertTrue(result.isPresent());
        assertEquals("Task 1", result.get().get("title"));
        assertEquals("Test description", result.get().get("description"));
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
    @DisplayName("It should complete successfully when the document is updated in MongoDB")
    void update_ExistingDocument_CompletesSuccessfully() {
        Document doc = new Document("_id", "69949f595f811f0d2276b457").append("title", "Test");
        when(collection.updateOne(any(Document.class), any(Document.class))).thenReturn(updateResult);
        when(updateResult.getMatchedCount()).thenReturn(1L);

        assertDoesNotThrow(() -> dao.update(doc));
        verify(collection, times(1)).updateOne(any(Document.class), any(Document.class));
    }

    @Test
    @DisplayName("It should throw DataAccessException when no document matches the provided ID")
    void update_IdNotFound_ThrowsDataAccessException() {
        // Given
        String id = "69949f595f811f0d2276b457";
        Document doc = new Document("_id", id);
        when(collection.updateOne(any(Document.class), any(Document.class))).thenReturn(updateResult);
        when(updateResult.getMatchedCount()).thenReturn(0L); // Simulamos que MongoDB no encontró el ID

        // When & Then
        DataAccessException ex = assertThrows(DataAccessException.class, () -> dao.update(doc));

        // Verificamos que el mensaje de error sea el esperado
        assertTrue(ex.getMessage().contains("Task with _id " + id + " not found."));
    }

    @Test
    @DisplayName("It should throw DataAccessException when a technical database error occurs")
    void update_MongoDbError_ThrowsDataAccessException() {
        // Given
        Document doc = new Document("_id", "123");
        // Simulamos un error interno de MongoDB (ej. timeout o pérdida de conexión)
        when(collection.updateOne(any(Document.class), any(Document.class)))
                .thenThrow(new RuntimeException("Connection lost"));

        // When & Then
        DataAccessException ex = assertThrows(DataAccessException.class, () -> dao.update(doc));

        assertTrue(ex.getMessage().contains("MongoDB update error"));
        assertTrue(ex.getCause() instanceof RuntimeException);
    }

}
