package common.utils;

import task.dto.OutputTaskDTO;
import task.dto.TaskListOutputDTO;

import java.time.LocalDate;

public class MenuPrinter {
    public static void printMainMenu() {
        System.out.println("\\========== Calendar =========\\");
        System.out.println("\\                             \\");
        System.out.println("\\       Select an option:     \\");
        System.out.println("\\     1. Tasks                \\");
        System.out.println("\\     2. Notes                \\");
        System.out.println("\\     3. Events               \\");
        System.out.println("\\     0. See you soon...      \\");
        System.out.println("\\                             \\");
        System.out.println("\\=============================\\");
    }

    public static void printTaskMenu() {
        System.out.println("\\=============== TASK ===============\\");
        System.out.println("\\  1. Create new task                \\");
        System.out.println("\\  2. List tasks                     \\");
        System.out.println("\\  3. Find task by ID                \\");
        System.out.println("\\  4. Mark task as completed         \\");
        System.out.println("\\  5. Update task                    \\");
        System.out.println("\\  6. Delete task                    \\");
        System.out.println("\\  0. Back to main menu              \\");
        System.out.println("\\====================================\\");
    }

    public static void printTaskList(TaskListOutputDTO tasks) {

        if (tasks.tasks().isEmpty()) {
            System.out.println("The tasks list is empty.");
            return;
        }

        for (OutputTaskDTO dto : tasks.tasks()) {
            System.out.println("-----------------------------------");
            System.out.println("Id: " + dto.id());
            System.out.println("Title: " + dto.title());
            System.out.println("Description: " + dto.description());
            System.out.println("Priority: " + dto.priority());
            System.out.println("Expire date: " + (dto.expireDate() != null ? dto.expireDate() : "unknown"));
            System.out.println("State: " + dto.taskState());
        }
    }

    public static void printCreatedTask(OutputTaskDTO dto) {
        String expireDate = dto.expireDate() != null ? dto.expireDate() : "unknown";

        System.out.println("|=================== CREATED TASK =================|");
        System.out.println("| Id: " + dto.id() +                              "|");
        System.out.println("| Title: " + dto.title() +                        "|");
        System.out.println("| Description: " + dto.description() +            "|");
        System.out.println("| Expire date " + expireDate +                    "|");
        System.out.println("| Priority: " + dto.id() +                        "|");
        System.out.println("| State: " + dto.taskState() +                    "|");
        System.out.println("|==================================================|");
    }

    public static void printATask(OutputTaskDTO dto) {
        String expireDate = dto.expireDate() != null ? dto.expireDate() : "unknown";

        System.out.println("|======================= TASK =====================|");
        System.out.println("| Id: " + dto.id() +                              "|");
        System.out.println("| Title: " + dto.title() +                        "|");
        System.out.println("| Description: " + dto.description() +            "|");
        System.out.println("| Expire date " + expireDate +                    "|");
        System.out.println("| Priority: " + dto.priority() +                  "|");
        System.out.println("| State: " + dto.taskState() +                    "|");
        System.out.println("|==================================================|");
    }

    public static void listTaskMenu() {
        System.out.println("\\=============== TASK ===============\\");
        System.out.println("\\  1. List all tasks                 \\");
        System.out.println("\\  2. List completed tasks           \\");
        System.out.println("\\  3. List pending tasks             \\");
        System.out.println("\\  0. Back to task menu              \\");
        System.out.println("\\====================================\\");
    }

    public static void updateTaskMenu() {
        System.out.println("\\============ UPDATE TASK ============\\");
        System.out.println("\\  1. Change title                    \\");
        System.out.println("\\  2. Change description              \\");
        System.out.println("\\  3. Change expiration date          \\");
        System.out.println("\\  4. Change priority                 \\");
        System.out.println("\\  0. Back to task menu               \\");
        System.out.println("\\=====================================\\");

    }

    public static void printUpdatedTask(OutputTaskDTO dto) {
        String expireDate = dto.expireDate() != null ? dto.expireDate() : "unknown";

        System.out.println("|================== UPDATED TASK ==================|");
        System.out.println("| Id: " + dto.id() +                              "|");
        System.out.println("| Title: " + dto.title() +                        "|");
        System.out.println("| Description: " + dto.description() +            "|");
        System.out.println("| Expire date " + expireDate +                    "|");
        System.out.println("| Priority: " + dto.priority() +                  "|");
        System.out.println("| State: " + dto.taskState() +                    "|");
        System.out.println("|==================================================|");
    }

    public static void printMarkTask(OutputTaskDTO dto){
        System.out.println("|==============================|");
        System.out.println("| Task with id: " + dto.id() +"|");
        System.out.println("| " + dto.title() + "          |");
        System.out.println("| Marcada como completada!     |");
        System.out.println("|==============================|");
    }

    public static void printMenuUpdateTask(){
        System.out.println("| What do you want to modify ?");
        System.out.println("| 1- Title");
        System.out.println("| 2- Description");
        System.out.println("| 3- Expire date");
        System.out.println("| 4- Priority");
        System.out.println("| 0- Exit and accept changes");
    }

    public static void printDeleteTask(int id){
        System.out.println("|===================================================|");
        System.out.println("| Task with id : " + id + " successfully deleted!   |");
        System.out.println("|===================================================|");
    }

}
