package task.service;


import common.exception.TaskNotFoundException;
import task.model.Task;
import task.repository.TaskRepository;
import task.repository.TaskRepositoryImpl;

public class TaskService {
    private TaskRepository repository;                                         /* La ia me sugiere que el TaskService reciba el TaskRepositopry desde fuera en lugar de crear una nueva instancia adentro del TaskService
                                                                                public class TaskService {
                                                                                    private final TaskRepository repository;

                                                                                    // Constructor para Inyección de Dependencias
                                                                                    public TaskService(TaskRepository repository) {
                                                                                        this.repository = repository;
                                                                                    }
                                                                                }*/

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

    public Task getTaskById(String id) {
        return repository.getById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));

    }

    public void removeTask(String id) {
        try {
            repository.remove(id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
