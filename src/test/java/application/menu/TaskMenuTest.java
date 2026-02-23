package application.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.service.TaskService;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public class TaskMenuTest {
    private TaskService mockService;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        // Capturamos la salida de la consola para verificar los mensajes de error
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        // Inicializamos la consola (suponiendo que el constructor acepta el scanner o service)
        mockService = mock(TaskService.class);    }

    @Test
    void readString_ShouldRetry_WhenInputIsBlank() throws Exception {
        // 1. ARRANGE
        String inputSimulado = "\n   \nHola\n";
        System.setIn(new ByteArrayInputStream(inputSimulado.getBytes()));

        Scanner testScanner = new Scanner(System.in);
        TaskMenu console = new TaskMenu(testScanner, mockService);

        // Configuramos el acceso al método privado
        java.lang.reflect.Method method = TaskMenu.class.getDeclaredMethod("readString", String.class);
        method.setAccessible(true);

        // 2. ACT
        // IMPORTANTE: Para llamar a un método privado, NO usas console.readString()
        // Usas method.invoke(instancia, argumentos)
        String result = (String) method.invoke(console, "Ingrese Titulo:");

        // 3. ASSERT
        assertEquals("Hola", result);
        String output = outContent.toString();
        assertTrue(output.contains("This field cannot be blank"));
        assertTrue(output.contains("Ingrese Titulo:"));
    }
}
