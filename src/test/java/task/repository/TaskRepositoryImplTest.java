package task.repository;

import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import common.persistance.TaskDAO;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.enums.TaskState;
import task.mapper.TaskMapper;
import task.model.Task;
import task.model.TaskBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryImplTest {

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskRepositoryImpl repository;

    /* =============================== create test methods =====================================*/
    @Test
    @DisplayName("Should create task successfully when valid Task is provided")
    void create_validTask_savesSuccessfully() {
        Task mockTask = TaskBuilder.newTask()
                .withTitle("New Task")
                .withDescription("Task description")
                .build();

        Document mockDoc = new Document("title", "New Task")
                .append("description", "Task description");

        when(mapper.toDocument(mockTask)).thenReturn(mockDoc);
        when(taskDAO.save(mockDoc)).thenReturn(mockDoc);

        assertDoesNotThrow(() -> repository.create(mockTask));

        verify(mapper).toDocument(mockTask);
        verify(taskDAO).save(mockDoc);
    }

    @Test
    @DisplayName("Should throw NullPointerException when task is null")
    void create_nullTask_throwsNullPointerException() {
        Task nullTask = null;

        when(mapper.toDocument(null)).thenThrow(new NullPointerException("Task cannot be null"));

        assertThrows(NullPointerException.class, () -> repository.create(nullTask));

        verify(mapper).toDocument(null);
        verifyNoInteractions(taskDAO);
    }

    @Test
    @DisplayName("Should propagate IllegalArgumentException when DAO rejects the document")
    void create_invalidDocument_propagatesIllegalArgumentException() {
        Task mockTask = TaskBuilder.newTask()
                .withTitle("Task")
                .withDescription("new description")
                .build();

        Document invalidDoc = new Document(); // Empty document

        when(mapper.toDocument(mockTask)).thenReturn(invalidDoc);
        doThrow(new IllegalArgumentException("The document can't be empty"))
                .when(taskDAO).save(invalidDoc);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> repository.create(mockTask)
        );

        assertEquals("The document can't be empty", exception.getMessage());
        verify(mapper).toDocument(mockTask);
        verify(taskDAO).save(invalidDoc);
    }

    @Test
    @DisplayName("Should propagate DataAccessException when database infrastructure error occurs")
    void create_dataAccessException_propagatesException() {
        Task mockTask = TaskBuilder.newTask()
                .withTitle("New Task")
                .withDescription("New description")
                .build();

        Document mockDoc = new Document("title", "New Task");

        when(mapper.toDocument(mockTask)).thenReturn(mockDoc);
        doThrow(new DataAccessException("MongoDB connection failed"))
                .when(taskDAO).save(mockDoc);

        DataAccessException exception = assertThrows(
                DataAccessException.class,
                () -> repository.create(mockTask)
        );

        assertTrue(exception.getMessage().contains("MongoDB"));
        verify(mapper).toDocument(mockTask);
        verify(taskDAO).save(mockDoc);
    }

    /* =============================== getByID test methods =====================================*/

    @Test
    @DisplayName("The mapped Task should return when the DAO finds the document")
    void getById_Positive() {

        String id = "69949f595f811f0d2276b457";
        Document mockDoc = new Document("_id", id).append("title", "Test Task").append("description", "Test description");
        Task mockTask = TaskBuilder.newTask()
                .withTitle("Test Task")
                .withDescription("Test description")
                .build();

        when(taskDAO.findByID(id)).thenReturn(Optional.of(mockDoc));
        when(mapper.toDomain(mockDoc)).thenReturn(mockTask);

        Optional<Task> result = repository.getById(id);

        assertTrue(result.isPresent(), "The result should contain a Task");
        assertEquals("Test Task", result.get().getTitle());

        verify(taskDAO).findByID(id);
        verify(mapper).toDomain(mockDoc);
    }

    @Test
    @DisplayName("It should return an empty Optional if the DAO finds nothing")
    void getById_Negative() {

        String id = "id_nonexistent";
        when(taskDAO.findByID(id)).thenReturn(Optional.empty());

        Optional<Task> result = repository.getById(id);

        assertTrue(result.isEmpty(), "The result should be an empty Optional");

        // VERIFY: ¡Muy importante!
        // Verificamos que el DAO se llamó, pero el Mapper NO (eficiencia)
        verify(taskDAO).findByID(id);
        verifyNoInteractions(mapper);// Si no existe el doc el mapper no se llama en el metodo, entonces con este metodo se verifica que no ha sido llamado
    }

    @Test
    @DisplayName("It should map the entity to a document and call DAO update")
    void modify_Positive() {
        // Given - Usando tu Builder
        Task mockTask = TaskBuilder.newTask()
                .withTitle("Testing Title")
                .withDescription("Testing Description")
                .build();
        mockTask.setId("69949f595f811f0d2276b457");

        Document mockDoc = new Document("_id", "69949f595f811f0d2276b457")
                .append("title", "Testing Title");

        // Configuramos el comportamiento de los colaboradores
        when(mapper.toDocument(mockTask)).thenReturn(mockDoc);

        // When
        assertDoesNotThrow(() -> repository.modify(mockTask));

        // Then
        verify(mapper, times(1)).toDocument(mockTask);
        verify(taskDAO, times(1)).update(mockDoc);
    }

    @Test
    @DisplayName("It should propagate DataAccessException when the DAO fails")
    void modify_Negative_DaoFails() {
        // Given
        Task mockTask = TaskBuilder.newTask()
                .withTitle("Failing Task")
                .withDescription("Testing error_id Description")
                .build();
        mockTask.setId("error-id");

        Document mockDoc = new Document("_id", "error-id");

        when(mapper.toDocument(mockTask)).thenReturn(mockDoc);

        // Como el DAO.update es void, usamos doThrow para simular el fallo de red o ID no encontrado
        doThrow(new DataAccessException("MongoDB connection error"))
                .when(taskDAO).update(mockDoc);

        // When & Then
        assertThrows(DataAccessException.class, () -> repository.modify(mockTask));

        // Verificamos que aunque falle el DAO, el mapper sí llegó a trabajar
        verify(mapper).toDocument(mockTask);
    }

    @Test
    @DisplayName("It should return a list of domain tasks when DAO returns documents for a given status")
    void getTasksByStatus_DocumentsExist_ReturnsMappedTaskList() {

        TaskState state = TaskState.COMPLETED;
        Document doc = new Document("title", "Task Test").append("state", state.name());
        List<Document> mockDocs = List.of(doc);

        Task task = TaskBuilder.newTask()
                .withTitle("Task Test")
                .withDescription("Testing ReturnsMappedTaskList Description")
                .withTaskState(state)
                .build();

        when(taskDAO.findTasksByStatus(state)).thenReturn(mockDocs);
        when(mapper.toDomain(doc)).thenReturn(task);

        List<Task> result = repository.getTasksByStatus(state);

        assertEquals(1, result.size());
        assertEquals(state, result.get(0).getTaskState());
        verify(taskDAO, times(1)).findTasksByStatus(state);
        verify(mapper, times(1)).toDomain(any(Document.class));
    }

    @Test
    @DisplayName("It should return an empty list when DAO finds no no tasks for the given status")
    void getTasksByStatus_NoDocuments_ReturnsEmptyList() {
        TaskState state = TaskState.NOT_COMPLETED;
        when(taskDAO.findTasksByStatus(state)).thenReturn(Collections.emptyList());

        List<Task> result = repository.getTasksByStatus(state);

        assertTrue(result.isEmpty());
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("It should propagate DataAccessException when DAO fails during status lookup")
    void getCompletedTasks_DaoFails_ThrowsDataAccessException() {
        TaskState state = TaskState.NOT_COMPLETED;
        when(taskDAO.findTasksByStatus(state)).thenThrow(new DataAccessException("DB Error"));

        assertThrows(DataAccessException.class, () -> repository.getTasksByStatus(state));
        verify(taskDAO).findTasksByStatus(state);
    }

    /* =============================== remove test methods =====================================*/

    @Test
    @DisplayName("Should complete without exception when the DAO deletes successfully")
    void remove_taskExists() {
        String validId = "69949f595f811f0d2276b457";

        doNothing().when(taskDAO).delete(validId);
        assertDoesNotThrow(() -> repository.remove(validId));

        verify(taskDAO).delete(validId);
    }

    @Test
    @DisplayName("Should propagate TaskNotFoundException when the DAO cannot find the task")
    void remove_taskNotFound() {
        String validId = "69949f595f811f0d2276b457";

        doThrow(new TaskNotFoundException(validId)).when(taskDAO).delete(validId);

        assertThrows(TaskNotFoundException.class, () -> repository.remove(validId));

        verify(taskDAO).delete(validId);
    }

    @Test
    @DisplayName("Should propagate InvalidTaskIDException when the ID format is invalid")
    void remove_invalidIdFormat() {
        String invalidId = "this-is-not-an-object-id";

        doThrow(new InvalidTaskIDException("Invalid format: " + invalidId))
                .when(taskDAO).delete(invalidId);

        assertThrows(InvalidTaskIDException.class, () -> repository.remove(invalidId));

        verify(taskDAO).delete(invalidId);
    }

    @Test
    @DisplayName("Should propagate DataAccessException when a database infrastructure error occurs")
    void remove_dataAccessException() {
        String validId = "69949f595f811f0d2276b457";

        doThrow(new DataAccessException("MongoDB failure")).when(taskDAO).delete(validId);

        assertThrows(DataAccessException.class, () -> repository.remove(validId));

        verify(taskDAO).delete(validId);
    }
}
