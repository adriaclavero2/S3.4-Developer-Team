package task.service;

import com.mongodb.client.MongoDatabase;
import common.persistance.Repository;
import infrastructure.mongo.dao.MongoTaskDAOAdapter;
import task.model.Task;
import task.model.TaskDTO;
import task.repository.TaskRepository;
import task.repository.TaskRepositoryImplementation;

public class TaskService {
    private TaskRepository repository;

    public TaskService(MongoDatabase database) {
        this.repository = new TaskRepositoryImplementation(
                new MongoTaskDAOAdapter(database.getCollection("tasks")));
    }

    public void createTask(Task newTask) {
        try {
            repository.create(newTask);
            System.out.println("New task " + newTask.getTitle() + " created");
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
