package task.mapper;

import common.utils.Mapper;
import org.bson.Document;
import org.bson.types.ObjectId;
import task.enums.Priority;
import task.enums.TaskState;
import task.model.Task;
import task.model.TaskBuilder;

import java.time.LocalDateTime;

public class TaskMapper implements Mapper<Task, Document> {
    @Override
    public Document toDocument(Task task) {
        Document doc = new Document()
                .append("title", task.getTitle())
                .append("description", task.getDescription())
                .append("expiredAt", task.getExpireDate() != null ? task.getExpireDate().toString() : null)
                .append("priority", task.getPriority().name())
                .append("status", task.getTaskState().name())
                .append("createdAt", task.getCreationDate().toString());

        // Lógica para el ID (Crucial para el UPDATE):
        // Si la Task ya tiene un ID (no es nulo ni está vacío), lo añadimos al Document.
        // Esto permite que tu DAO encuentre el documento correcto en MongoDB.
        if (task.getId() != null && !task.getId().isEmpty()) {
            doc.append("_id", new ObjectId(task.getId()));
        }

        return doc;

    }

    @Override
    public Task toDomain(Document document) {

        LocalDateTime expDate = document.getString("expiredAt") != null ? LocalDateTime.parse(document.getString("expiredAt")) : null;
        LocalDateTime creationDAte = LocalDateTime.parse(document.getString("createdAt"));
        ObjectId objID = document.getObjectId("_id");

        Task task = TaskBuilder.newTask()
                .withTitle(document.getString("title"))
                .withDescription(document.getString("description"))
                .withExpireDate(expDate)
                .withTaskState(TaskState.valueOf(document.getString("status")))
                .withPriority(Priority.valueOf(document.getString("priority")))
                .build();

        task.setId(objID.toHexString());
        task.setCreationDate(creationDAte);


        return task;
    }

}
