package task.service;

import common.exception.DataAccessException;
import common.exception.InvalidTaskIDException;
import common.exception.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceGetTaskByIDTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    @DisplayName("GetTaskById must return the task with the requested ID")
    void testGetTaskById_Positive() {

        String id = "69949f595f811f0d2276b457";

        Task mockTask =  TaskBuilder.newTask()
                .withTitle("Testing Tittle")
                .withDescription("Description")
                .build();

        mockTask.setId(id);

        when(repository.getById(id)).thenReturn(Optional.of(mockTask));

        Task result = service.getTaskById(id);

        assertNotNull(result, "Expected a task but received null");
        assertEquals(id, result.getId(), "The Service should return the task with the requested ID");

        verify(repository, times(1)).getById(id); // esto verifica que se llamo al repository
    }

    @Test
    @DisplayName("You must throw an exception if the task does not exist in the database.")
    void testGetTaskById_Negative(){
        String notExistentId = "empty/null/wrong";

        when(repository.getById(notExistentId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            service.getTaskById(notExistentId);
        });
    }

    @Test
    @DisplayName("Should return a success message when the task is deleted correctly")
    void removeTask_taskExists() {
        String validId = "69949f595f811f0d2276b457";

        doNothing().when(repository).remove(validId);
        String result = service.removeTask(validId);

        assertEquals("Task (" + validId + ") successfully deleted.", result);
        verify(repository).remove(validId);
    }

    @Test
    @DisplayName("Should return a not found message when the task does not exist")
    void removeTask_taskNotFound() {
        String validId = "69949f595f811f0d2276b457";

        doThrow(new TaskNotFoundException(validId)).when(repository).remove(validId);
        String result = service.removeTask(validId);

        assertEquals("Task (" + validId + ") does not exist.", result);
        verify(repository).remove(validId);
    }

    @Test
    @DisplayName("Should return an invalid format message when the ID is malformed")
    void removeTask_invalidIdFormat() {
        String invalidId = "this-is-not-an-object-id";

        doThrow(new InvalidTaskIDException("Invalid format: " + invalidId))
                .when(repository).remove(invalidId);
        String result = service.removeTask(invalidId);

        assertEquals("Invalid id format.", result);
        verify(repository).remove(invalidId);
    }

    @Test
    @DisplayName("Should return an error message when a database infrastructure failure occurs")
    void removeTask_dataAccessException() {
        String validId = "69949f595f811f0d2276b457";

        doThrow(new DataAccessException("MongoDB failure")).when(repository).remove(validId);
        String result = service.removeTask(validId);

        assertEquals("Error raised during task deletion. Try again.", result);
        verify(repository).remove(validId);
    }
}
