package common.persistance;

import task.model.Task;
import java.util.List;

public interface TaskDAO extends GenericDAO<Task, String> {

    List<Task> findCompletedTasks();
}
