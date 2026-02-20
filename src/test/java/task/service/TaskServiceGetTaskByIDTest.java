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
import task.dto.ErrorOutputDTO;
import task.dto.OutputDTO;
import task.dto.OutputTaskDTO;
import task.dto.TaskDTO;
import task.mapper.TaskToDTOMapper;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceGetTaskByIDTest {

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

        Task mockTask =  TaskBuilder.newTask()
                .withTitle("Testing Tittle")
                .withDescription("Description")
                .build();

        mockTask.setId(id);

        when(repository.getById(id)).thenReturn(Optional.of(mockTask));

        OutputDTO result = service.getTaskById(id);

        assertNotNull(result, "Expected a task but received null");
        assertEquals(id,((OutputTaskDTO) result).id(), "The Service should return the task with the requested ID");

        verify(repository, times(1)).getById(id); // esto verifica que se llamo al repository
    }

    @Test
    @DisplayName("You must throw an exception if the task does not exist in the database.")
    void testGetTaskById_Negative(){
        String notExistentId = "empty/null/wrong";

        when(repository.getById(notExistentId)).thenReturn(Optional.empty());

        OutputDTO result = service.getTaskById(notExistentId);
        assertEquals("Task not found", result.getOutputState());
    }

    /* =============================== remove test methods =====================================*/

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
