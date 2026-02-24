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

import static common.utils.MenuPrinter.*;


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

            printTaskMenu();

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
                case 0 -> System.out.println("Back to main menu...");
                default -> System.out.println("Invalid option.");
            }
        }
        while (option != 0) ;
    }

    private void createTask() {

        System.out.println("\n--- CREATE YOUR TASK ---");

        String title = readString("Title: ");
        String description = readString("Description: ");
        String expirationDate = readValidDate("Expiration date (dd-MM-yyyy)");
        String priority = readValidPriority("Priority (LOW, MEDIUM, HIGH): ");

        TaskDTO dto = new TaskDTO(title, description, expirationDate, priority);

        OutputDTO result = taskService.createTask(dto);

        if (result instanceof OutputTaskDTO success) {
            printCreatedTask( success);
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

        String idTask = readString("Enter _id of the task you want to find: ");

        TaskIdDTO dto = new TaskIdDTO(idTask);

        OutputDTO result = taskService.getTaskById(dto);

        if (result instanceof  OutputTaskDTO success) {
            printCreatedTask( success);
        } else if (result instanceof  ErrorOutputDTO error) {
            System.err.println("Error: " + error.getOutputState());
        }

    }

    private void markTaskCompleted() {

    }

    private void updateTask() {
        int option = -1;
        String newTitle = "";
        String newDescription = "";
        String newExpirationDate = "";
        String newPriority = "";
        System.out.println("\n--- UPDATE  YOUR TASK ---");

        String idTask = readString("Enter _id of the task yuo want to update: ");

        do {
            updateTaskMenu();

            while (!scanner.hasNextInt()) {
                System.out.print("Enter a valid number: ");
                scanner.nextLine();
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                    newTitle = readOptionalString("New title ");
                }
                case 2 -> {
                    newDescription = readOptionalString("New description ");
                }
                case 3 -> {
                    newExpirationDate = readOptionalDate("New expiration date ");
                }
                case 4 -> {
                    newPriority = readOptionalPriority("New priority ");
                }
                case 0 -> System.out.println("Back to Task Menu");
                default -> System.out.println("Invalid option.");
            }
        } while (option != 0);

        TaskUpdateDTO dto = new TaskUpdateDTO(idTask, newTitle, newDescription, newExpirationDate,newPriority);

        OutputDTO result = taskService.updateTask(dto);

        if (result instanceof  OutputTaskDTO success) {
            printUpdatedTask( success);
        } else if (result instanceof  ErrorOutputDTO error) {
            System.err.println("Error: " + error.getOutputState());
        }
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
    private String readOptionalString(String field) {
        System.out.println((field + "(Press Enter to keep current): "));
        return scanner.nextLine();
    }

    private String readValidDate(String inputField) {
        while (true) {
            System.out.println(inputField);
            String input = scanner.nextLine();
            try {
                LocalDate.parse(input, onlyDateFormatter);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use dd-MM-YYYY format (31-12-2026");
            }
        }
    }

    private String readOptionalDate(String field) {
        while (true) {
            System.out.println(field + "(Press Enter to keep current): ");
            String input = scanner.nextLine();
            if(input.isBlank()) return null;
            try {
                LocalDate.parse(input, onlyDateFormatter);
                return input;
            } catch (DateTimeParseException e) {
                System.out.println("Invalid format. Use dd-MM-yyyy format (31-12-2026");
            }
        }
    }



    private String readValidPriority(String inputField) {
        while (true) {
            System.out.println(inputField);
            String input = scanner.nextLine().trim().toUpperCase();

            try{
                return Priority.valueOf(input).name();
            } catch (IllegalArgumentException e){
                System.out.println("Invalid priority. Valid options : LOW, MEDIUM, HIGH");
            }
        }
    }

    private String readOptionalPriority(String field) {
        while (true) {
            System.out.println(field + "(Press Enter to keep current): ");
            String input = scanner.nextLine().trim().toUpperCase();
            if(input.isBlank()) return null;

            try{
                return Priority.valueOf(input).name();
            } catch (IllegalArgumentException e){
                System.out.println("Invalid priority. Valid options : LOW, MEDIUM, HIGH");
            }
        }
    }
}

