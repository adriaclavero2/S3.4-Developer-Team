package test;

import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import org.bson.Document;
import org.bson.types.ObjectId;
import task.model.Task;
import task.repository.TaskRepositoryImpl;
import task.service.TaskService;

import java.util.List;

public class TestCrudRead {

    public static void main(String[] args) {
        System.out.println("INICIANDO TEST MANUAL (POSITIVE & NEGATIVE)");

        MongoTaskDAOAdapter dao = new MongoTaskDAOAdapter();

        // Creamos una tarea "cebo" para pescarla después
        Document docPrueba = new Document("title", "Tarea de Prueba")
                .append("description", "Esto es un test positivo")
                .append("status", "PENDING");
        dao.save(docPrueba);

        // Recuperamos el ID real que Mongo le asignó (lo necesitamos para el test positivo)
        String realId = dao.findAll().stream()
                .filter(d -> "Tarea de Prueba".equals(d.getString("title")))
                .findFirst()
                .map(d -> d.getObjectId("_id").toString())
                .orElse(null);

        String fakeId = "507f1f77bcf86cd799439011"; // Un ID con formato válido pero que no existe

        // Instanciamos el Servicio (La capa que vamos a probar)
        TaskService service = new TaskService(new TaskRepositoryImpl());

        System.out.println("-ID Real generado: " + realId);
        System.out.println("-ID Falso usado:   " + fakeId + "\n");


        // TEST 1: (POSITIVE) - Encontrar algo que existe
        System.out.println("Test 1: Buscar por ID existente (Positive)...");
        try {
            Task encontrada = service.getTaskById(realId);
            if (encontrada != null && "Tarea de Prueba".equals(encontrada.getTitle())) {
                System.out.println("✅ APROBADO: Se encontró la tarea y los datos coinciden.");
            } else {
                System.err.println("❌ FALLIDO: Se encontró algo, pero los datos no coinciden.");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLIDO: Saltó una excepción inesperada: " + e.getMessage());
        }

        System.out.println("--------------------------------------------------");


        // TEST 2: READ ONE (NEGATIVE) - Buscar algo que NO existe
        System.out.println("Test 2: Buscar por ID inexistente (Negative)...");
        try {
            service.getTaskById(fakeId);
            // Si llegamos a esta línea, MALO. Debería haber explotado antes.
            System.err.println("❌ FALLIDO: El servicio NO lanzó error, pero debería haberlo hecho.");
        } catch (RuntimeException e) {
            // Esperamos que el mensaje sea el que pusimos en el Service
            if (e.getMessage().contains("Error 404") || e.getMessage().contains("No existe")) {
                System.out.println("✅ APROBADO: El servicio lanzó la excepción correcta: " + e.getMessage());
            } else {
                System.err.println("⚠️ ALERTA: Lanzó error, pero el mensaje es diferente al esperado: " + e.getMessage());
            }
        }

        System.out.println("--------------------------------------------------");


        // TEST 3: READ ALL (POSITIVE) - Listar todo
        System.out.println("Test 3: Listar todas las tareas (Positive)...");
        try {
            List<Task> lista = service.getAllTasks();
            if (!lista.isEmpty()) {
                System.out.println("✅ APROBADO: La lista no está vacía (Tamaño: " + lista.size() + ")");
                // Opcional: imprimir títulos para verificar orden
                // lista.forEach(t -> System.out.println("   -> " + t.getTitle()));
            } else {
                System.err.println("❌ FALLIDO: La lista está vacía (y acabamos de insertar una).");
            }
        } catch (Exception e) {
            System.err.println("❌ FALLIDO: Error al listar: " + e.getMessage());
        }

        // 4. LIMPIEZA (TEARDOWN)
        // Borramos la tarea de prueba para no ensuciar la BD
        if (realId != null) {
            dao.delete(realId);
            System.out.println("\n(Limpieza: Tarea de prueba eliminada de la BD)");
        }
    }
}