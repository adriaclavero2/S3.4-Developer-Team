package task.repository;

import common.persistance.TaskDAO;
import common.utils.Mapper;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.model.Task;
import task.model.TaskBuilder;

import java.util.Optional;
import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskRepositoryMockitoTest {

    @Mock
    private TaskDAO taskDAO;

    @Mock
    private Mapper<Task, Document> mapper;

    @InjectMocks
    private TaskRepositoryImpl taskRepository;

    private Task sampleTask;
    private Document sampleDocument;


    @BeforeEach
    void setUp() {
        sampleTask = TaskBuilder.newTask()
                .withTitle("Tarea de Prueba")
                .withDescription("Descripción para Mockito")
                .build();
    }

    @Test
    void testSave() {

        taskRepository.create(sampleTask);

        verify(taskDAO, times(1)).save(sampleDocument);
    }

    @Test
    void testFindById() {
        sampleDocument = new Document("title", "Tarea de Prueba");

        when(taskDAO.findByID(any())).thenReturn(Optional.of(sampleDocument));

        when(mapper.toDomain(any())).thenReturn(sampleTask);

        Optional<Task> result = taskRepository.getById("123");

        assertTrue(result.isPresent(), "Si esto falla es que el DAO o el Mapper devolvieron null");
        assertEquals("Tarea de Prueba", result.get().getTitle());
    }

    @Test
    void testFindAll() {
        List<Document> documentos = new ArrayList<>();
        documentos.add(sampleDocument);

        when(taskDAO.findAll()).thenReturn(documentos);

        when(mapper.toDomain(sampleDocument)).thenReturn(sampleTask);

        List<Task> result = taskRepository.getAll();

        assertFalse(result.isEmpty());
        verify(taskDAO).findAll();
    }

    @Test
    void testDelete() {
        taskRepository.remove("123");

        verify(taskDAO).delete("123");
    }

    @Test
    void testUpdate() {
        taskRepository.modify(sampleTask);

        verify(taskDAO).update(sampleDocument);
    }
}