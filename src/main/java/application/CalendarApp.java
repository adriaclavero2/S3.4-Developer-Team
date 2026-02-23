package application;

import com.mongodb.client.MongoCollection;
import common.persistance.TaskDAO;
import infrastructure.mongo.connection.MongoDBConnection;
import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import org.bson.Document;
import task.mapper.TaskMapper;
import task.mapper.TaskToDTOMapper;
import task.model.Task;
import task.model.TaskBuilder;
import task.repository.TaskRepository;
import task.repository.TaskRepositoryImpl;
import task.service.TaskService;

public class CalendarApp {

    private static TaskRepository taskRepository;
    private static TaskService taskService;

    private static void init() {

        // 1. Obtenemos la colección (Infraestructura)
        MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("tasks");
        // 2. Creamos el adaptador (DAO)
        //GenericDAO<?, ?> taskDao = new MongoTaskDAOAdapter(database.getCollection("tasks"));
        TaskDAO dao = new MongoTaskDAOAdapter((collection));

        // 3. Creamos el Repositorio con su Mapper (Persistencia/Datos)
        TaskMapper mapper = new TaskMapper();
        taskRepository = new TaskRepositoryImpl(dao, mapper);

        // 4. Creamos el Servicio (Negocio)
        TaskToDTOMapper mapperDTO = new TaskToDTOMapper();
        taskService = new TaskService(taskRepository, mapperDTO);
    }
    public static void main(String[] args) {

        try {
            // Inicializamos todas las capas antes de empezar
            init();

            // Si llegamos aquí, la conexión es real
            System.out.println("✅ CONNECTED TO THE DOCKER CONTAINER");
            System.out.println("🔌 Working on the DB: " + MongoDBConnection.getDatabase().getName());


            // Una pequeña prueba extra: listar colecciones
            for (String name : MongoDBConnection.getDatabase().listCollectionNames()) {
                System.out.println("📁 Collection found: " + name);
            }

            Task newTask = TaskBuilder.newTask()
                .withTitle("Comprar el pan")
                .withDescription("Comprar dos baguette y dos catalanas")
                .build();


            //taskService.createTask(new);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Cerramos la conexion al terminar
            MongoDBConnection.close();
        }
    }
}
