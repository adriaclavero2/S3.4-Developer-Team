package task.repository;

import common.persistance.TaskDAO;

public class TaskRepositoryImplementation {
    private final TaskDAO dao;

    public TaskRepositoryImplementation(TaskDAO dao) {
        this.dao = dao;
    }
}
