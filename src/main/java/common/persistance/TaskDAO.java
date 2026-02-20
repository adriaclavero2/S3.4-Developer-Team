package common.persistance;

import org.bson.Document;
import task.enums.TaskState;
import java.util.List;

public interface TaskDAO extends GenericDAO<Document, String> {

    List<Document> findTasksByStatus(TaskState state);
}
