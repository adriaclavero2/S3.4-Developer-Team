package application.menu;

import common.utils.MenuPrinter;
import task.service.TaskService;

import java.util.Scanner;

public class MainMenu {
    private final Scanner scanner;
    private final TaskService taskService;
    // NoteService
    // EventService

    public MainMenu(Scanner scanner, TaskService taskService) {
        this.scanner = scanner;
        this.taskService = taskService;
    }

    public void init() {
        int option = -1;

        do {

            MenuPrinter.printMainMenu();

            while(!scanner.hasNextInt()) {
                System.out.println("Insert a valid input. Try again.");
                scanner.nextLine();
            }

            option = scanner.nextInt();
            scanner.nextLine();

            switch (option) {
                case 1 -> {
                     TaskMenu taskMenu = new TaskMenu(scanner, taskService);
                     taskMenu.start();
                }

                case 2 -> {
                    System.out.println("Note feature is working progress...");
                }

                case 3 -> {
                    System.out.println("Event feature is working progress...");
                }

                case 0 -> {
                    System.out.println("By, see you soon!!");
                }

                default -> {
                    System.out.println("Invalid insert option, try again...");
                }
            }

        } while(option != 0);
    }

}
