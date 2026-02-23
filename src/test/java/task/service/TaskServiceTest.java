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
import task.dto.*;
import task.enums.Priority;
import task.enums.TaskState;
import task.mapper.TaskToDTOMapper;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    @Mock
    private TaskToDTOMapper mapper;

    @Mock
    private TaskRepository repository;

    @InjectMocks
    private TaskService service;

    /* =============================== create test methods =====================================*/
    @Test
    @DisplayName("Should return success message when task is created successfully")
    void createTask_validTask_returnsSuccessMessage() {
        TaskDTO inputDTO = new TaskDTO("New Task", "Task Description", "", "medium");

        Task taskToCreate = TaskBuilder.newTask()
                .withTitle("New Task")
                .withDescription("Task Description")
                .build();

        Task createdTask = TaskBuilder.newTask()
                .withTitle("New Task")
                .withDescription("Task Description")
                .build();
        createdTask.setId("123456");

        OutputTaskDTO expectedOutput = new OutputTaskDTO(
                "123456",
                "New Task",
                "Task Description",
                null,
                "20-02-2026 10:30:00",
                "MEDIUM",
                "NOT_COMPLETED",
                "New Task created"
        );

        when(mapper.dtoToTask(inputDTO)).thenReturn(taskToCreate);
        when(repository.create(any(Task.class))).thenReturn(createdTask);
        when(mapper.taskToDto(createdTask, "New Task created")).thenReturn(expectedOutput);

        OutputDTO result = service.createTask(inputDTO);

        assertEquals("New Task created", result.getOutputState());
        assertInstanceOf(OutputTaskDTO.class, result);

        verify(mapper).dtoToTask(inputDTO);
        verify(repository).create(any(Task.class));
        verify(mapper).taskToDto(createdTask, "New Task created");
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when TaskDTO is null")
    void createTask_nullTask_throwsIllegalArgumentException() {
        TaskDTO nullTaskDTO = null;

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createTask(nullTaskDTO)
        );

        assertEquals("CreateTask: Task cannot be null", exception.getMessage());
        verifyNoInteractions(repository);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should return error message when repository throws IllegalArgumentException")
    void createTask_invalidDataFormat_returnsErrorMessage() {
        TaskDTO inputDTO = new TaskDTO("Task", "new description", "", "medium");

        Task taskToCreate = TaskBuilder.newTask()
                .withTitle("Task")
                .withDescription("new description")
                .build();

        when(mapper.dtoToTask(inputDTO)).thenReturn(taskToCreate);
        doThrow(new IllegalArgumentException("The document can't be empty"))
                .when(repository).create(any(Task.class));

        OutputDTO result = service.createTask(inputDTO);

        assertInstanceOf(ErrorOutputDTO.class, result);
        assertEquals("Something go wrong with data format", result.getOutputState());

        verify(mapper).dtoToTask(inputDTO);
        verify(repository).create(any(Task.class));
        verify(mapper, never()).taskToDto(any(), any());
    }

    @Test
    @DisplayName("Should return error message when database infrastructure failure occurs")
    void createTask_dataAccessException_returnsErrorMessage() {
        TaskDTO inputDTO = new TaskDTO("New Task", "new description", "", "high");

        Task taskToCreate = TaskBuilder.newTask()
                .withTitle("New Task")
                .withDescription("new description")
                .build();

        when(mapper.dtoToTask(inputDTO)).thenReturn(taskToCreate);
        doThrow(new DataAccessException("MongoDB connection failed"))
                .when(repository).create(any(Task.class));

        OutputDTO result = service.createTask(inputDTO);

        assertInstanceOf(ErrorOutputDTO.class, result);
        assertEquals("Error raised during task creation. Try again.", result.getOutputState());

        verify(mapper).dtoToTask(inputDTO);
        verify(repository).create(any(Task.class));
        verify(mapper, never()).taskToDto(any(), any());
    }
    /* =============================== getTaskByID test methods =====================================*/
    @Test
    @DisplayName("GetTaskById must return the task with the requested ID")
    void testGetTaskById_Positive() {
        String id = "69949f595f811f0d2276b457";


        Task mockTask = TaskBuilder.newTask()
                .withTitle("Testing Title")
                .withDescription("Description")
                .build();

        mockTask.setId(id);

        OutputTaskDTO expectedOutput = new OutputTaskDTO(
                id,
                "Testing Title",
                "Description",
                null,
                "20-02-2026 10:30:00",
                "MEDIUM",
                "NOT_COMPLETED",
                "Task retrieved successfully"
        );

        TaskIdDTO idDTO = new TaskIdDTO(id);

        when(repository.getById(id)).thenReturn(Optional.of(mockTask));
        when(mapper.taskToDto(mockTask, "Task retrieved successfully")).thenReturn(expectedOutput);

        OutputDTO result = service.getTaskById(idDTO);

        assertNotNull(result, "Expected a task but received null");
        assertInstanceOf(OutputTaskDTO.class, result);
        assertEquals(id, ((OutputTaskDTO) result).id(), "The Service should return the task with the requested ID");
        assertEquals("Task retrieved successfully", result.getOutputState());

        verify(repository, times(1)).getById(id);
        verify(mapper, times(1)).taskToDto(mockTask, "Task retrieved successfully");
    }

    @Test
    @DisplayName("Must return ErrorOutputDTO if the task does not exist in the database")
    void testGetTaskById_Negative() {
        String notExistentId = "empty/null/wrong";
        TaskIdDTO idDTO = new TaskIdDTO(notExistentId);

        when(repository.getById(notExistentId)).thenReturn(Optional.empty());

        OutputDTO result = service.getTaskById(idDTO);

        assertNotNull(result);
        assertInstanceOf(ErrorOutputDTO.class, result);
        assertEquals("Task not found", result.getOutputState());

        verify(repository, times(1)).getById(notExistentId);
        verify(mapper, never()).taskToDto(any(), any());
    }
    /* =============================== remove test methods =====================================*/

    @Test
    @DisplayName("Should return a success message when the task is deleted correctly")
    void removeTask_taskExists() {
        String validId = "69949f595f811f0d2276b457";

        doNothing().when(repository).remove(validId);
        OutputDTO result = service.removeTask(new TaskIdDTO(validId));

        assertEquals("Task (" + validId + ") successfully deleted.", result.getOutputState());
        verify(repository).remove(validId);
    }

    @Test
    @DisplayName("Should return a not found message when the task does not exist")
    void removeTask_taskNotFound() {
        String validId = "69949f595f811f0d2276b457";

        doThrow(new TaskNotFoundException(validId)).when(repository).remove(validId);
        OutputDTO result = service.removeTask(new TaskIdDTO(validId));

        assertEquals("Task (" + validId + ") does not exist.", result.getOutputState());
        verify(repository).remove(validId);
    }

    @Test
    @DisplayName("Should return an invalid format message when the ID is malformed")
    void removeTask_invalidIdFormat() {
        String invalidId = "this-is-not-an-object-id";

        doThrow(new InvalidTaskIDException("Invalid format: " + invalidId))
                .when(repository).remove(invalidId);
        OutputDTO result = service.removeTask(new TaskIdDTO(invalidId));

        assertEquals("Invalid id format.", result.getOutputState());
        verify(repository).remove(invalidId);
    }

    @Test
    @DisplayName("Should return an error message when a database infrastructure failure occurs")
    void removeTask_dataAccessException() {
        String validId = "69949f595f811f0d2276b457";

        doThrow(new DataAccessException("MongoDB failure")).when(repository).remove(validId);
        OutputDTO result = service.removeTask(new TaskIdDTO(validId));

        assertEquals("Error raised during task deletion. Try again.", result.getOutputState());
        verify(repository).remove(validId);
    }

    /* =============================== update test methods =====================================*/

    @Test
    @DisplayName("updateTask_ValidRequest_ReturnsOutputTaskDTO")
    void updateTask_ValidRequest_ReturnsOutputTaskDTO() {
        String id = "69949f595f811f0d2276b457";
        TaskUpdateDTO inputDTO = new TaskUpdateDTO(id, "Title", "Desc", "2026-12-31", "HIGH");

        // 1. Usamos la clase exacta que devuelve tu mapper
        OutputTaskDTO expectedResponse = new OutputTaskDTO(
                id, "Title", "Desc", "2026-12-31", "2026-02-21", "HIGH", "TODO",
                "Task with _id " + id + " updated successfully"
        );

        // 2. Configuramos el mock para que devuelva el objeto correcto
        when(repository.getById(id)).thenReturn(Optional.of(mock(Task.class)));
        when(mapper.dtoUpdateToTask(any(), any())).thenReturn(mock(Task.class));
        when(repository.modify(any())).thenReturn(mock(Task.class));

        // IMPORTANTE: El retorno del mapper debe coincidir con expectedResponse
        when(mapper.taskToDto(any(Task.class), anyString())).thenReturn(expectedResponse);

        // When
        OutputDTO result = service.updateTask(inputDTO);

        // Then
        assertNotNull(result);
        // Si OutputTaskDTO tiene los datos correctos, el test pasará
        assertEquals(expectedResponse, result);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when the input DTO is null")
    void updateTask_NullInput_ThrowsIllegalArgumentException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> service.updateTask(null));
    }

    @Test
    @DisplayName("updateTask_IdNotFound_ReturnsErrorOutputDTO")
    void updateTask_IdNotFound_ReturnsErrorOutputDTO() {
        // Given
        String id = "non-existent";
        TaskUpdateDTO inputDTO = new TaskUpdateDTO(id, "Title", "Desc", null, null);

        when(repository.getById(id)).thenReturn(Optional.empty());

        // When
        OutputDTO result = service.updateTask(inputDTO);

        // Then
        assertTrue(result instanceof ErrorOutputDTO);
        // Usamos getOutputState() que es el método que implementa tu Record
        assertTrue(result.getOutputState().contains("not found"));
    }

    @Test
    @DisplayName("updateTask_RepositoryFails_ReturnsErrorOutputDTO")
    void updateTask_RepositoryFails_ReturnsErrorOutputDTO() {
        // Given
        String id = "69949f595f811f0d2276b457";
        TaskUpdateDTO inputDTO = new TaskUpdateDTO(id, "Title", "Desc", null, null);
        Task existingTask = mock(Task.class);

        when(repository.getById(id)).thenReturn(Optional.of(existingTask));
        when(mapper.dtoUpdateToTask(any(), any())).thenReturn(mock(Task.class));
        when(repository.modify(any())).thenThrow(new DataAccessException("DB Connection Lost"));

        // When
        OutputDTO result = service.updateTask(inputDTO);

        // Then
        assertTrue(result instanceof ErrorOutputDTO);
        assertTrue(result.getOutputState().contains("Persistence error"));
    }

    @Test
    @DisplayName("updateTask_GeneralException_ReturnsErrorOutputDTO")
    void updateTask_GeneralException_ReturnsErrorOutputDTO() {
        // Given
        TaskUpdateDTO inputDTO = new TaskUpdateDTO("123", "Title", "Desc", null, null);
        when(repository.getById(any())).thenThrow(new RuntimeException("Fatal Crash"));

        // When
        OutputDTO result = service.updateTask(inputDTO);

        // Then
        assertTrue(result instanceof ErrorOutputDTO);
        assertTrue(result.getOutputState().contains("Unexpected error"));
    }
    /* =============================== listTasksByStatus test methods =====================================*/

    @Test
    @DisplayName("listTasksByStatus_CompletedTasksExist_ReturnsTaskListOutputDTO")
    void listTasksByStatus_CompletedTasksExist_ReturnsTaskListOutputDTO() {
        // Given
        TaskState state = TaskState.COMPLETED;
        Task mockTask = TaskBuilder.newTask()
                .withTitle("Testing Title")
                .withDescription("Description")
                .withPriority(Priority.HIGH)
                .build();

        OutputTaskDTO mockDto = mock(OutputTaskDTO.class);
        when(repository.getTasksByStatus(state)).thenReturn(List.of(mockTask));
        when(mapper.taskToDto(eq(mockTask), anyString())).thenReturn(mockDto);

        // When
        OutputDTO result = service.listTasksByStatus(state);

        // Then
        assertTrue(result instanceof TaskListOutputDTO);
        TaskListOutputDTO listResult = (TaskListOutputDTO) result;

        assertEquals(1, listResult.tasks().size());
        assertEquals("--- COMPLETED TASKS ---", listResult.getOutputState());
        verify(repository).getTasksByStatus(state);
    }

    @Test
    @DisplayName("listTasksByStatus_NoPendingTasks_ReturnsErrorOutputDTO")
    void listTasksByStatus_NoPendingTasks_ReturnsErrorOutputDTO() {
        // Given
        TaskState state = TaskState.NOT_COMPLETED;
        when(repository.getTasksByStatus(state)).thenReturn(Collections.emptyList());

        // When
        OutputDTO result = service.listTasksByStatus(state);

        // Then
        assertTrue(result instanceof ErrorOutputDTO);
        assertEquals("No pending tasks", result.getOutputState());
    }

    @Test
    @DisplayName("listTasksByStatus_NoCompletedTasks_ReturnsErrorOutputDTO")
    void listTasksByStatus_NoCompletedTasks_ReturnsErrorOutputDTO() {
        // Given
        TaskState state = TaskState.COMPLETED;
        when(repository.getTasksByStatus(state)).thenReturn(Collections.emptyList());

        // When
        OutputDTO result = service.listTasksByStatus(state);

        // Then
        assertTrue(result instanceof ErrorOutputDTO);
        assertEquals("No tasks completed", result.getOutputState());
    }

    @Test
    @DisplayName("listTasksByStatus_RepositoryFails_ReturnsErrorOutputDTO")
    void listTasksByStatus_RepositoryFails_ReturnsErrorOutputDTO() {
        // Given
        TaskState state = TaskState.NOT_COMPLETED;
        when(repository.getTasksByStatus(any())).thenThrow(new DataAccessException("Connection lost"));

        // When
        OutputDTO result = service.listTasksByStatus(state);

        // Then
        assertTrue(result instanceof ErrorOutputDTO);
        assertTrue(result.getOutputState().contains("Persistence error"));
        assertTrue(result.getOutputState().contains("Connection lost"));
    }
}
