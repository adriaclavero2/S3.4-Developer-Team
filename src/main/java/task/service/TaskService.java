package task.service;

import common.exception.TaskNotFoundException;
import task.enums.TaskState;
import task.model.Task;
import task.repository.TaskRepository;
import task.repository.TaskRepositoryImpl;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TaskService {
    private final TaskRepository repository;

    public TaskService() {
        this.repository = new TaskRepositoryImpl();
    }

    public TaskService(TaskRepository repository) {
        this.repository = repository;
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
                .orElseThrow(() -> new TaskNotFoundException("Error 404: Task not found with ID: " + id));
    }

    public TaskListOutputDTO getAllTasks() {
        List<Task> tasks = repository.getAll().stream()
                .sorted(Comparator.comparing(Task::getExpireDate, Comparator.nullsLast(Comparator.naturalOrder())))
                .toList();

        List<OutputTaskDTO> tasksDTOs = tasks.stream()
                .map(task -> taskMapper.toOutputDTO(task))
                .collect(Collectors.toList());

        return new TaskListOutputDTO(taskDTOs);
    }

    public void completeTask(String id) {
        try{
            Task task = getTaskById(id);
            if(task.getTaskState() == TaskState.COMPLETED) {
                System.out.println("Information: The task was already marked as completed.");
                return;
            }
            task.setTaskState(TaskState.COMPLETED);
            repository.modify(task);
            System.out.println("Success: Task marked as completed correctly.");

        }catch(TaskNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error while updating the task: " + e.getMessage());
        }
    }
}