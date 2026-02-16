package task.service;


import task.model.Task;
import task.repository.TaskRepository;
import task.repository.TaskRepositoryImpl;

public class TaskService {
    private TaskRepository repository;

    public TaskService() {
        this.repository = new TaskRepositoryImpl();
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
