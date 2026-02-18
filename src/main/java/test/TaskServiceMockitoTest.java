package test;


import task.model.Task;
import task.repository.TaskRepository;
import task.service.TaskService;

import java.util.Optional;



    @ExtendWith
    class TaskServiceMockitoTest {

        @Mock
        private TaskRepository repositoryMock; // El "falso" repositorio

        @InjectMocks
        private TaskService service; // El servicio real que usa el falso repositorio

        @Test
        void testGetTaskById_NotFound_ShouldThrowException() {
            // 1. GIVEN (Preparar)
            // Configuramos el mock para que cuando pida el ID "999", devuelva un vacío
            when(repositoryMock.getById("999")).thenReturn(Optional.empty());

            // 2. WHEN & THEN (Ejecutar y Verificar)
            // Verificamos que al llamar al servicio, salta la excepción que tú programaste
            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                service.getTaskById("999");
            });

            // Comprobamos que el mensaje es el correcto
            assertTrue(exception.getMessage().contains("Error 404"));

            // Verificamos que se llamó al repositorio exactamente 1 vez
            verify(repositoryMock, times(1)).getById("999");

            System.out.println("✅ Test Negativo con Mockito: ¡Funcionó perfectamente!");
        }
    }
}
