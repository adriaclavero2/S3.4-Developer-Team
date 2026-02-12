package task.application;

import task.model.Task;
import task.service.TaskService;
import task.enums.Priority;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        TaskService service = new TaskService();

        System.out.println("=== CREATING TASKS ===");
        Task t1 = service.createTask("Exercise", "Go to the gym", null, Priority.MEDIUM);
        Task t2 = service.createTask("Study Java", "Review design patterns", null, Priority.HIGH);
        Task t3 = service.createTask("Buy groceries", null, null, Priority.LOW);

        System.out.println("Task 1 created: " + t1);
        System.out.println("Task 2 created: " + t2);
        System.out.println("Task 3 created: " + t3);

        System.out.println("\n=== ALL TASKS ===");
        List<Task> all = service.getAllTasks();
        all.forEach(System.out::println);

        System.out.println("\n=== COMPLETING TASK 1 ===");
        service.completeTask(1);
        service.getTask(1).ifPresent(System.out::println);

        System.out.println("\n=== HIGH PRIORITY TASKS ===");
        List<Task> high = service.getTasksByPriority(Priority.HIGH);
        high.forEach(System.out::println);

        System.out.println("\n=== DELETING TASK 3 ===");
        service.deleteTask(3);

        System.out.println("\n=== FINAL TASKS ===");
        service.getAllTasks().forEach(System.out::println);
    }
}