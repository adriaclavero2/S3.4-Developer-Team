package task.repository;

import common.persistance.TaskDAO;

public class TaskRepository {
    private final TaskDAO dao;

    public TaskRepository(TaskDAO dao) {
        this.dao = dao;
    }
}
