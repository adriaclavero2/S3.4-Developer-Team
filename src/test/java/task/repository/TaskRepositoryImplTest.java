package task.repository;

import common.persistance.TaskDAO;
import org.bson.Document;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.mapper.TaskMapper;
import task.model.Task;
import task.model.TaskBuilder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryImplTest {

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private TaskMapper mapper;

    @InjectMocks
    private TaskRepositoryImpl repository;

    @Test
    @DisplayName("The mapped Task should return when the DAO finds the document")
    void getById_Positive() {

        String id = "69949f595f811f0d2276b457";
        Document mockDoc = new Document("_id", id).append("title", "Test Task");
        Task mockTask = TaskBuilder.newTask().title("Test Task").build();

        when(taskDAO.findByID(id)).thenReturn(Optional.of(mockDoc));
        when(mapper.toDomain(mockDoc)).thenReturn(mockTask);

        Optional<Task> result = repository.getById(id);

        assertTrue(result.isPresent(), "The result should contain a Task");
        assertEquals("Test Task", result.get().getTitle());

        verify(taskDAO).findByID(id);
        verify(mapper).toDomain(mockDoc);
    }
}
