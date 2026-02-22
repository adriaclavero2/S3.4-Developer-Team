package infrastructur.mongo.dao;

import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.result.UpdateResult;
import common.exception.DataAccessException;
import com.mongodb.client.result.DeleteResult;
import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.enums.TaskState;

import java.util.List;
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

    @Mock
    private DeleteResult deleteResult;

    @InjectMocks
    private MongoTaskDAOAdapter dao;

    /* ============================== save method tests ==================================== */
    @Test
    @DisplayName("Should insert document successfully when valid document with title is provided")
    void save_validDocumentWithTitle_insertsSuccessfully() {
        Document validDoc = new Document("title", "New Task")
                .append("description", "Task description");

        assertDoesNotThrow(() -> dao.save(validDoc));

        verify(collection).insertOne(validDoc);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when document is null")
    void save_nullDocument_throwsIllegalArgumentException() {
        Document nullDoc = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(nullDoc)
        );

        assertEquals("The document can't be empty", exception.getMessage());
        verifyNoInteractions(collection);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when document is empty")
    void save_emptyDocument_throwsIllegalArgumentException() {
        Document emptyDoc = new Document();

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(emptyDoc)
        );

        assertEquals("The document can't be empty", exception.getMessage());
        verifyNoInteractions(collection);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when document lacks title field")
    void save_documentWithoutTitle_throwsIllegalArgumentException() {
        Document docWithoutTitle = new Document("description", "Some description")
                .append("status", "pending");

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> dao.save(docWithoutTitle)
        );

        assertEquals("The task must have a title", exception.getMessage());
        verifyNoInteractions(collection);
    }

    @Test
    @DisplayName("Should throw DataAccessException when MongoDB raises an infrastructure error")
    void save_mongoException_throwsDataAccessException() {
        Document validDoc = new Document("title", "New Task");

        doThrow(new MongoException("Simulated database error"))
                .when(collection).insertOne(validDoc);

        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> dao.save(validDoc)
        );

        assertTrue(exception.getMessage().contains("MongoDB"));
        verify(collection).insertOne(validDoc);
    }

    /* ============================== findByID method tests ==================================== */
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
    @DisplayName("It should return the updated document when the update is successful")
    void update_ExistingDocument_ReturnsUpdatedDocument() {
        // Given
        String id = "69949f595f811f0d2276b457";
        Document inputDoc = new Document("_id", id).append("title", "Updated Title");
        Document mockUpdatedDoc = new Document("_id", id).append("title", "Updated Title").append("status", "TODO");

        // IMPORTANTE: Ahora mockeamos findOneAndUpdate, no updateOne
        when(collection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
                .thenReturn(mockUpdatedDoc);

        // When
        Document result = dao.update(inputDoc);

        // Then
        assertNotNull(result);
        assertEquals("Updated Title", result.get("title"));
        assertEquals(id, result.get("_id"));

        // Verificamos que se llamó al método correcto con opciones
        verify(collection, times(1)).findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class));
    }

    @Test
    @DisplayName("It should throw DataAccessException when no document matches the provided ID")
    void update_IdNotFound_ThrowsDataAccessException() {
        // Given
        String id = "69949f595f811f0d2276b457";
        Document doc = new Document("_id", id);

        // findOneAndUpdate devuelve null si no encuentra el documento
        when(collection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
                .thenReturn(null);

        // When & Then
        DataAccessException ex = assertThrows(DataAccessException.class, () -> dao.update(doc));
        assertTrue(ex.getMessage().contains("Task with _id " + id + " not found."));
    }

    @Test
    @DisplayName("It should throw DataAccessException when a technical database error occurs")
    void update_MongoDbError_ThrowsDataAccessException() {
        // Given
        Document doc = new Document("_id", "123");
        when(collection.findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class)))
                .thenThrow(new RuntimeException("Connection lost"));

        // When & Then
        DataAccessException ex = assertThrows(DataAccessException.class, () -> dao.update(doc));

        assertTrue(ex.getMessage().contains("MongoDB update error"));
    }

    @Test
    @DisplayName("It should throw DataAccessException when the input document has no _id")
    void update_NoIdInDocument_ThrowsDataAccessException() {
        // Given: Un documento sin la clave "_id"
        Document doc = new Document("title", "No ID");

        // When & Then
        DataAccessException ex = assertThrows(DataAccessException.class, () -> dao.update(doc));
        assertEquals("Cannot update a document without _id", ex.getMessage());

        // Verificamos que ni siquiera intentó ir a la base de datos
        verify(collection, never()).findOneAndUpdate(any(Bson.class), any(Bson.class), any(FindOneAndUpdateOptions.class));
    }

    @Test
    @DisplayName("It should return a list of documents when MongoDB finds completed tasks")
    void findTasksByStatus_DocumentsExist_ReturnsDocumentList() {
        TaskState state = TaskState.COMPLETED;
        List<Document> mockDocs = List.of(new Document("title", "Done").append("status", state.name()));

        when(collection.find(any(Bson.class))).thenReturn(findIterable);
        when(findIterable.into(any())).thenReturn(mockDocs);

        List<Document> result = dao.findTasksByStatus(TaskState.COMPLETED);

        assertFalse(result.isEmpty());
        assertEquals("Done", result.get(0).get("title"));
        verify(collection).find(any(Bson.class));
    }

    @Test
    @DisplayName("It should throw DataAccessException when a technical database error occurs")
    void findTasksByStatus_MongoError_ThrowsDataAccessException() {
        TaskState state = TaskState.NOT_COMPLETED;
        when(collection.find(any(Bson.class))).thenThrow(new RuntimeException("Connection error"));

        DataAccessException exception = assertThrows(DataAccessException.class,
                () -> dao.findTasksByStatus(state));

        assertThrows(DataAccessException.class, () -> dao.findTasksByStatus(TaskState.NOT_COMPLETED));

        assertTrue(exception.getMessage().contains(state.name()));
        assertTrue(exception.getCause() instanceof RuntimeException);
    }


    /* ============================== delete method tests ==================================== */

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
