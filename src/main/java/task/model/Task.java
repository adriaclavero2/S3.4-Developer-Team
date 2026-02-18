package task.model;

import java.time.LocalDateTime;

import task.enums.Priority;
import task.enums.TaskState;
import task.model.BuilderSteps.DescriptionStep;
import task.model.BuilderSteps.OptionalSteps;
import task.model.BuilderSteps.TitleStep;

public class Task {

    private String id;
    private String title;
    private String description;
    private LocalDateTime creationDate;
    private LocalDateTime expireDate;
    private Priority priority;
    private TaskState taskState;

    protected Task() {
        this.creationDate = LocalDateTime.now();
        this.priority = Priority.MEDIUM;
        this.taskState = TaskState.NOT_COMPLETED;
    }

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