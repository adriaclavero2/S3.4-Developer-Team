package task.repository;

import common.persistance.Repository;
import task.model.Task;
import java.util.List;

public interface TaskRepository extends Repository<Task, String> {

    List<Task> getCompletedTasks();
}
