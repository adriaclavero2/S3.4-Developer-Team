package infrastructur.mongo.dao;

import common.persistance.TaskDAO;
import common.utils.Mapper;
import org.bson.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepositoryImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryMockitoTest {

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
        // Creamos la tarea completando los pasos del Builder para que no de error
        sampleTask = TaskBuilder.newTask()
                .withTitle("Tarea de Prueba")
                .withDescription("Descripción de prueba")
                .build();

        sampleDocument = new Document("title", "Tarea de Prueba")
                .append("description", "Descripción de prueba");
    }

    @Test
    @DisplayName("Debería encontrar una tarea por ID")
    void testFindById() {
        String id = "65d5ef1234567890abcdef12"; // ID formato Mongo

        // Entrenamos los mocks
        when(taskDAO.findByID(id)).thenReturn(Optional.of(sampleDocument));
        when(mapper.toDomain(any(Document.class))).thenReturn(sampleTask);

        // Ejecución
        Optional<Task> result = taskRepository.getById(id);

        // Verificación
        assertTrue(result.isPresent());
        assertEquals("Tarea de Prueba", result.get().getTitle());
        verify(taskDAO).findByID(id);
    }

    @Test
    @DisplayName("Debería llamar al DAO para eliminar una tarea")
    void testRemoveTask() {
        String id = "65d5ef1234567890abcdef12";

        // Ejecución
        taskRepository.remove(id);

        // Verificación: En el remove no hay 'when' porque el método es void
        verify(taskDAO).delete(id);
    }

    @Test
    @DisplayName("Debería crear una tarea correctamente")
    void testCreateTask() {
        // Entrenamos al mapper para que convierta nuestra tarea en documento
        when(mapper.toDocument(any(Task.class))).thenReturn(sampleDocument);

        // Ejecución
        taskRepository.create(sampleTask);

        // Verificación
        verify(taskDAO).save(sampleDocument);
    }
}