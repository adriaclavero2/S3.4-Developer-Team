package task.service;

import common.exception.DataAccessException;
import common.exception.TaskNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.enums.Priority;
import task.enums.TaskState;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    @Test
    @DisplayName("GetTaskById must return the task with the requested ID")
    void testGetTaskById_Positive() {

        String id = "69949f595f811f0d2276b457";

        Task mockTask = TaskBuilder.newTask()
                .withTitle("Testing Title")
                .withDescription("Testing Description")
                .build();

        mockTask.setId(id);

        when(repository.getById(id)).thenReturn(Optional.of(mockTask));

        Task result = service.getTaskById(id);

        assertNotNull(result, "Expected a task but received null");
        assertEquals(id, result.getId(), "The Service should return the task with the requested ID");

        verify(repository, times(1)).getById(id); // esto verifica que se llamo al repository
    }

    @Test
    @DisplayName("It must throw an exception if the task does not exist in the database.")
    void testGetTaskById_Negative() {
        String notExistentId = "empty/null/wrong";

        when(repository.getById(notExistentId)).thenReturn(Optional.empty());

        assertThrows(TaskNotFoundException.class, () -> {
            service.getTaskById(notExistentId);
        });
    }

    @Test
    @DisplayName("It should call repository.modify when the task is valid")
    void testUpdateTask_Positive() {
        Task mockTask = TaskBuilder.newTask()
                .withTitle("Testing Title")
                .withDescription("Testing Description")
                .build();
        mockTask.setId("69949f595f811f0d2276b457");
        // When
        String result = service.updateTask(mockTask);

        // Then
        assertEquals("Task with _id 69949f595f811f0d2276b457 updated successfully", result);
        verify(repository, times(1)).modify(mockTask);

    }

    @Test
    @DisplayName("It should throw IllegalArgumentException when the input task is null")
    void testUpdateTask_Negative_NullTask() {

        String result = service.updateTask(null);

        assertEquals("Error: Task cannot be null", result);
    }

    @Test
    @DisplayName("It should throw TaskNotFoundException and preserve cause when repository fails")
    void updateTask_Negative_NotFound() {
        // Given
        Task mockTask = TaskBuilder.newTask()
                .withTitle("Ghost Task")
                .withDescription("Non-existent task description")
                .build();
        mockTask.setId("999999999999999999999999");

        // Simulamos que el DAO/Repository lanza la excepción técnica
        doThrow(new DataAccessException("ID not found"))
                .when(repository).modify(any(Task.class));

        // When & Then
        String result = service.updateTask(mockTask);

        // Verificamos congruencia de mensajes y encadenamiento (Stack Trace)
        assertTrue(result.contains("Persistence error: ID not found"));
    }

    @Test
    @DisplayName("It should return error message when the task has no ID")
    void testUpdateTask_Negative_NoId() {
        // Given: Una tarea construida pero sin setearle el ID
        Task taskWithoutId = TaskBuilder.newTask()
                .withTitle("No ID Task")
                .withDescription("Non-existent task description")
                .build();

        // When
        String result = service.updateTask(taskWithoutId);

        // Then
        assertEquals("Error: Cannot update a task without an ID", result);
        // Verificamos que NI SIQUIERA se intentó llamar al repositorio
        verify(repository, never()).modify(any());
    }
}
