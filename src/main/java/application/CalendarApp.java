package application;

import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import task.enums.Priority;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepositoryImplementation;

public class CalendarApp {
    public static void main(String[] args) {

        try {
            // Esto dispara todo el sistema de builders y config que hiciste
            var db = infrastructure.mongo.connection.MongoDBConnection.getDatabase();

            var taskDAO = new MongoTaskDAOAdapter();
            var taskRepo = new TaskRepositoryImplementation(taskDAO);
            // Si llegamos aquí, la conexión es real

            System.out.println("✅ CONECTADO AL CONTENEDOR DOCKER");
            System.out.println("🔌 Trabajando en la DB: " + db.getName());

            // Una pequeña prueba extra: listar colecciones
            for (String name : db.listCollectionNames()) {
                System.out.println("📁 Colección encontrada: " + name);

            Task newTask = TaskBuilder.newTask()
                    .title("Prueba de Fuego")
                    .description("Verificando inserción desde Java a Compass")
                    .priority(Priority.HIGH)
                    .build();

            System.out.println("⏳ Intentando guardar la tarea...");
            taskRepo.create(newTask);

            System.out.println("✅ ¡Tarea creada con éxito!");
            System.out.println("🆔 ID generado: " + newTask.getId());


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
