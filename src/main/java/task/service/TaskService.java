package task.service;


import task.model.Task;
import task.repository.TaskRepository;
import task.repository.TaskRepositoryImpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TaskService {
    private TaskRepository repository;

    //Constructor para loa TESTS.
    public TaskService(TaskRepository repository) {
        this.repository = repository;
    }

    //Constructor para el MAIN o el CLIENTE.
    public TaskService() {
        this.repository = new TaskRepositoryImpl();
    }

    public void createTask(Task newTask) {
        try {
           if (newTask.getTitle() == null || newTask.getTitle().isEmpty()) {
               throw new IllegalArgumentException("The title cannot be empty.");
           }
           repository.create(newTask);
           System.out.println("New task " + newTask.getTitle() + " created successfully.");
        } catch (RuntimeException e) {
            System.err.println("Error creating task: " + e.getMessage());
        }
    }

    public List<Task> getAllTasks(){
        List<Task> tasks = new ArrayList<>(repository.getAll());

        if(tasks.isEmpty()) {
            System.out.println("Warning: The list is empty.");
            return tasks;
        }
        try {
            tasks.sort(Comparator.comparing(Task::getExpireDate, Comparator.nullsLast(Comparator.naturalOrder())));
        } catch (Exception e) {
            System.err.println("Warning: The list could not be sorted: " + e.getMessage());
        }
        return tasks;
    }

    public Task getTaskById(String id) {
        return repository.getById(id)
                .orElseThrow(() -> new RuntimeException("The task with ID was not found: " + id));
    }
}
