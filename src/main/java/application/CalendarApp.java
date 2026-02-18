package application;

import task.model.Task;
import task.model.TaskBuilder;
import task.service.TaskService;

public class CalendarApp {
    public static void main(String[] args) {
        System.out.println();
        com.mongodb.client.MongoDatabase db = null;
        try {
            // Esto dispara todo el sistema de builders y config que hiciste
            db = infrastructure.mongo.connection.MongoDBConnection.getDatabase();

            // Si llegamos aquí, la conexión es real
            System.out.println("✅ CONECTADO AL CONTENEDOR DOCKER");
            System.out.println("🔌 Trabajando en la DB: " + db.getName());

            db.listCollectionNames();

            // Una pequeña prueba extra: listar colecciones
            for (String name : db.listCollectionNames()) {
                System.out.println("📁 Colección encontrada: " + name);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        Task newTask = TaskBuilder.newTask()
                .withTitle("Comprar el pan")
                .withDescription("Comprar dos baguette y dos catalanas")
                .build();

        TaskService taskService = new TaskService();
        taskService.createTask(newTask);
    }
}
