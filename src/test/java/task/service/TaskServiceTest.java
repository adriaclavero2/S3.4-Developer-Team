package task.service;

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

        Task mockTask =  TaskBuilder.newTask()
                .withTitle("Testing Tittle")
                .withDescription("")
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
}
