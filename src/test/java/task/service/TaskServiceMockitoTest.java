package task.service;

import common.exception.TaskNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.enums.TaskState;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceMockitoTest {

    @Mock
    private TaskRepository repositoryMock;

    @InjectMocks
    private TaskService service;


    @Test
    void testGetTaskById_NotFound_ShouldThrowException() {
        when(repositoryMock.getById("999")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            service.getTaskById("999");
        });

        assertTrue(exception.getMessage().contains("Error 404"));
        verify(repositoryMock, times(1)).getById("999");

        System.out.println("✅ Test Negativo con Mockito: ¡Funcionó perfectamente!");
    }


    @Test
    void testCompleteTask_ShouldChangeStatusAndSave() {
        Task task = TaskBuilder.newTask()
                .withTitle("Completar Sprint")
                .withDescription("Lógica de negocio")
                .build();
        task.setTaskState(TaskState.NOT_COMPLETED);

        when(repositoryMock.getById("123")).thenReturn(Optional.of(task));

        service.completeTask("123");

        assertEquals(TaskState.COMPLETED, task.getTaskState(), "El estado debería ser COMPLETED");
        verify(repositoryMock).modify(task); // Verificamos que llamó al repositorio para guardar
        System.out.println("✅ Test CompleteTask: El estado cambió y se persistió.");
    }

    @Test
    void testCompleteTask_AlreadyCompleted_ShouldNotModify() {
        Task task = TaskBuilder.newTask()
                .withTitle("Tarea Hecha")
                .withDescription("Ya estaba lista")
                .build();
        task.setTaskState(TaskState.COMPLETED);

        when(repositoryMock.getById("123")).thenReturn(Optional.of(task));

        service.completeTask("123");

        verify(repositoryMock, never()).modify(any());
        System.out.println("✅ Test CompleteTask: No se llamó a modify porque ya estaba completada.");
    }
}