package task.cli;

import task.model.Task;
import task.service.TaskService;
import task.enums.Priority;
import task.enums.TaskState;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class TaskMenu {

    private TaskService service;
    private Scanner scanner;

    public TaskMenu() {
        this.service = new TaskService();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean exit = false;

        while (!exit) {
            showMenu();
            int option = readOption();

            switch (option) {
                case 1:
                    createTask();
                    break;
                case 2:
                    listTasks();
                    break;
                case 3:
                    searchTask();
                    break;
                case 4:
                    updateTask();
                    break;
                case 5:
                    deleteTask();
                    break;
                case 6:
                    completeTask();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option");
            }
        }
    }

    private void showMenu() {
        System.out.println("\n=== TASK MANAGEMENT ===");
        System.out.println("1. Create task");
        System.out.println("2. List tasks");
        System.out.println("3. Search task by ID");
        System.out.println("4. Update task");
        System.out.println("5. Delete task");
        System.out.println("6. Complete task");
        System.out.println("0. Exit");
        System.out.print("Option: ");
    }

    private int readOption() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void createTask() {
        System.out.println("\n--- CREATE TASK ---");

        System.out.print("Title: ");
        String title = scanner.nextLine();

        System.out.print("Description (optional): ");
        String description = scanner.nextLine();
        if (description.isEmpty()) description = null;

        System.out.print("Expire date dd/MM/yyyy HH:mm (optional): ");
        String dateStr = scanner.nextLine();
        LocalDateTime expireDate = null;
        if (!dateStr.isEmpty()) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                expireDate = LocalDateTime.parse(dateStr, formatter);
            } catch (Exception e) {
                System.out.println("Invalid format, date will be omitted");
            }
        }

        System.out.println("Priority: 1-LOW, 2-MEDIUM, 3-HIGH (Enter for MEDIUM): ");
        String prioStr = scanner.nextLine();
        Priority priority = null;
        if (!prioStr.isEmpty()) {
            int prio = Integer.parseInt(prioStr);
            if (prio == 1) priority = Priority.LOW;
            else if (prio == 3) priority = Priority.HIGH;
        }

        try {
            Task task = service.createTask(title, description, expireDate, priority);
            System.out.println("Task created with ID: " + task.getId());
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void listTasks() {
        System.out.println("\n--- TASK LIST ---");
        List<Task> tasks = service.getAllTasks();

        if (tasks.isEmpty()) {
            System.out.println("No tasks found");
        } else {
            for (Task task : tasks) {
                System.out.println(task);
            }
        }
    }

    private void searchTask() {
        System.out.print("\nTask ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        service.getTask(id).ifPresentOrElse(
                task -> System.out.println(task),
                () -> System.out.println("Task not found")
        );
    }

    private void updateTask() {
        System.out.print("\nTask ID to update: ");
        int id = Integer.parseInt(scanner.nextLine());

        if (service.getTask(id).isEmpty()) {
            System.out.println("Task not found");
            return;
        }

        System.out.print("New title: ");
        String title = scanner.nextLine();

        System.out.print("New description: ");
        String description = scanner.nextLine();

        System.out.println("State: 1-NOT_COMPLETED, 2-COMPLETED");
        int stateNum = Integer.parseInt(scanner.nextLine());
        TaskState state = stateNum == 2 ? TaskState.COMPLETED : TaskState.NOT_COMPLETED;

        service.updateTask(id, title, description, null, Priority.MEDIUM, state);
        System.out.println("Task updated");
    }

    private void deleteTask() {
        System.out.print("\nTask ID to delete: ");
        int id = Integer.parseInt(scanner.nextLine());

        service.deleteTask(id);
        System.out.println("Task deleted");
    }

    private void completeTask() {
        System.out.print("\nTask ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        service.completeTask(id);
        System.out.println("Task completed");
    }

    public static void main(String[] args) {
        TaskMenu menu = new TaskMenu();
        menu.start();
    }
}