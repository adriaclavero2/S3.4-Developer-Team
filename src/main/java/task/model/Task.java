package task.model;

import java.time.LocalDateTime;
import task.enums.Priority;
import task.enums.TaskState;

public class Task {

    private String id;
    private String title;
    private String description;
    private LocalDateTime expireDate;
    private final LocalDateTime creationDate;
    private Priority priority;
    private TaskState taskState;

    public Task() {
        this.creationDate = LocalDateTime.now();
        this.priority = Priority.MEDIUM;
        this.taskState = TaskState.NOT_COMPLETED;
    }

    public Task(String title) {
        this();
        this.title = title;
    }

    protected Task(String title, String description, LocalDateTime expireDate, Priority priority, TaskState taskState) {
        this.title = title;
        this.description = description;
        this.expireDate = expireDate;
        this.creationDate = LocalDateTime.now();
        this.priority = priority;
        this.taskState = taskState;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    @Override
    public String toString() {
        return "Task{id=" + id + ", title='" + title + "', priority=" + priority +
                ", state=" + taskState + "}";
    }
}