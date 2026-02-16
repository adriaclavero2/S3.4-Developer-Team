package task.model;

import java.time.LocalDateTime;
import java.util.Formatter;

import task.enums.Priority;
import task.enums.TaskState;

public class Task {


    private String id;
    private String title;
    private String description;
    private LocalDateTime expireDate;
    // visibility modifier changed;
    private LocalDateTime creationDate;
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

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskState getTaskState() {
        return taskState;
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreationDate(LocalDateTime creationDate) { this.creationDate = creationDate;}

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
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