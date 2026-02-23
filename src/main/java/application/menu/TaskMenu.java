package application.menu;

import common.utils.MenuPrinter;
import task.dto.*;
import task.enums.Priority;
import task.enums.TaskState;
import task.service.TaskService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class TaskMenu {
    private final Scanner scanner;
    private final TaskService taskService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private final DateTimeFormatter onlyDateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public TaskMenu(Scanner scanner, TaskService taskService) {
        this.scanner = scanner;
        this.taskService = taskService;
    }

    public void start() {
        int option;

        do {

            MenuPrinter.printTaskMenu();

            while (!scanner.hasNextInt()) {
                System.out.print("Introduce a valid option: ");
                scanner.nextLine();
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> createTask();
                case 2 -> listTasks();
                case 3 -> getTaskById();
                case 4 -> markTaskCompleted();
                case 5 -> updateTask();
                case 6 -> deleteTask();
                case 0 -> System.out.println("Going back to main menu...");
                default -> System.out.println("Invalid option.");
            }
        }
        while (option != 0) ;
    }

    private void createTask() {

        System.out.println("\n--- CREATE YOUR TASK ---");

        String title = readString("Title: ");
        String description = readString("Description: ");
        String expirationDate = readValidDate("Expiration date (dd-MM-YYYY)");
        Priority priority = readValidPriority("Priority (LOW, MEDIUM, HIGH): ");

        TaskDTO dto = new TaskDTO(title, description, expirationDate, priority.name());

        OutputDTO result = taskService.createTask(dto);

        if (result instanceof OutputTaskDTO success) {
            //printCreateTask(success);
        } else if (result instanceof ErrorOutputDTO error) {
            System.err.println("Error: " + error.getOutputState());
        }
    }

    private void listTasks() {
        int option = -1;

        do {
            MenuPrinter.listTaskMenu();

            while (!scanner.hasNextInt()) {
                System.out.print("Introduce a valid option: ");
                scanner.nextLine();
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> listAllTasks();
                case 2 -> listCompletedTasks();
                case 3 -> listPendingTasks();
                case 0 -> System.out.println("Going back to task menu...");
                default -> System.out.println("Invalid option");
            }

        } while(option != 0);
    }

    private void listAllTasks() {
        System.out.println("\n--- LIST ALL TASKS ---");

//        OutputDTO result = taskService.getAllTasks();

//        if (result instanceof TaskListOutputDTO success) {
//            MenuPrinter.printTaskList(success);
//        } else if (result instanceof  ErrorOutputDTO error) {
//            System.err.println("Error: " + error.getOutputState());
//        }
    }

    private void listCompletedTasks() {
        System.out.println("\n--- LIST COMPLETED TASKS ---");

        OutputDTO result = taskService.listTasksByStatus(TaskState.COMPLETED);

        if (result instanceof TaskListOutputDTO success) {
            MenuPrinter.printTaskList(success);
        } else if (result instanceof  ErrorOutputDTO error) {
            System.err.println("Error: " + error.getOutputState());
        }
    }

    private void listPendingTasks() {
        System.out.println("\n--- LIST PENDING TASKS ---");

        OutputDTO result = taskService.listTasksByStatus(TaskState.NOT_COMPLETED);

        if (result instanceof TaskListOutputDTO success) {
            MenuPrinter.printTaskList(success);
        } else if (result instanceof  ErrorOutputDTO error) {
            System.err.println("Error: " + error.getOutputState());
        }
    }

    private void getTaskById() {
        System.out.println("\n--- FIND TASK BY ID ---");

        String idTask = readString("Enter _id of the task you want to find : ");

        TaskIdDTO dto = new TaskIdDTO(idTask);

        OutputDTO result = taskService.getTaskById(dto);

        if (result instanceof  OutputTaskDTO success) {
            //prinFindTaskById
        } else if (result instanceof  ErrorOutputDTO error) {
            System.err.println("Error: " + error.getOutputState());
        }

    }

    private void markTaskCompleted() {

    }

    private void updateTask() {

    }

    private void deleteTask() {

    }
    // ================= MÉTODOS AUXILIARES (CLEAN INPUT) =================

    private String readString(String inputField) {
        String input = "";
        while (input.isBlank()) {
            System.out.println(inputField);
            input = scanner.nextLine();
            if (input.isBlank()) {
                System.out.println("This field cannot be blank");
            }
        }
        return input;
    }

    private String readValidDate(String inputField) {
        while (true) {
            System.out.println(inputField);
            String input = scanner.nextLine();
            try {
                LocalDate.parse(inputField, onlyDateFormatter);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use dd-MM-YYYY format (31-12-2026");
            }
        }
    }

    private Priority readValidPriority(String inputField) {
        while (true) {
            System.out.println(inputField);
            String input = scanner.nextLine().trim().toUpperCase();

            try{
                return Priority.valueOf(input);
            } catch (IllegalArgumentException e){
                System.out.println("Invalid priority. Valid options : LOW, MEDIUM, HIGH");
            }
        }
    }
}

