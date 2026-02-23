package application;

import application.menu.MainMenu;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
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

import java.util.Scanner;

public class CalendarApp {

    private static TaskRepository taskRepository;
    private static TaskService taskService;
    private static Scanner scanner;
    private static MainMenu mainMenu;

    private static void init() {

        try {
            scanner = new Scanner(System.in);

            MongoDatabase database = MongoDBConnection.getDatabase();

            MongoCollection<Document> taskCollection = database.getCollection("tasks");

            TaskDAO taskDao = new MongoTaskDAOAdapter(taskCollection);

            TaskMapper taskMapper = new TaskMapper();
            taskRepository = new TaskRepositoryImpl(taskDao, taskMapper);

            TaskToDTOMapper mapperDTO = new TaskToDTOMapper();
            taskService = new TaskService(taskRepository, mapperDTO);

            mainMenu = new MainMenu(scanner, taskService);

            System.out.println("System initialized correctly");
        } catch (Exception e) {
            System.err.println("Fatal error in init(): " + e.getMessage());
            throw new RuntimeException(e);  // Para interrumpir el programa y evitar que se execute igualmente el menu.
        }
    }
    public static void main(String[] args) {

        try {
            init();
            mainMenu.init();
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR!!!");;
        } finally {
            MongoDBConnection.close();
        }
    }
}
