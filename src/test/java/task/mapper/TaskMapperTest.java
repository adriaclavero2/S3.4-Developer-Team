package task.mapper;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import task.enums.Priority;
import task.enums.TaskState;
import task.model.Task;
import task.model.TaskBuilder;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskMapperTest {
    TaskMapper taskMapper = new TaskMapper();

    @Test
    public void toDocumentTest_WithoutId() {
        Task task = TaskBuilder.newTask()
                .withTitle("Study")
                .withDescription("study design patterns")
                .build();

        Document doc = taskMapper.toDocument(task);

        LocalDateTime expDate = doc.getString("expiredAt") != null ? LocalDateTime.parse(doc.getString("expiredAt")) : null;
        LocalDateTime creationDate =LocalDateTime.parse(doc.getString("createdAt"));



        assertEquals(task.getTitle(), doc.getString("title"));
        assertEquals(task.getDescription(), doc.getString("description"));
        assertEquals(task.getExpireDate(),expDate);
        assertEquals(task.getCreationDate(), creationDate);
        assertEquals(task.getPriority(), Priority.valueOf(doc.getString("priority")));
        assertEquals(task.getTaskState(), TaskState.valueOf(doc.getString("status")));
        assertFalse(doc.containsKey("_id"), "Document should not contain _id field");
    }

    @Test
    public void toDocumentTest_WithId() {
        Task task = TaskBuilder.newTask()
                .withTitle("Study")
                .withDescription("study design patterns")
                .build();
        task.setId("69949f595f811f0d2276b457");

        Document doc = taskMapper.toDocument(task);

        LocalDateTime expDate = doc.getString("expiredAt") != null ? LocalDateTime.parse(doc.getString("expiredAt")) : null;
        LocalDateTime creationDate =LocalDateTime.parse(doc.getString("createdAt"));


        assertTrue(doc.containsKey("_id"), "Document should contain _id");
        assertEquals(task.getTitle(), doc.getString("title"));
        assertEquals(task.getDescription(), doc.getString("description"));
        assertEquals(task.getExpireDate(),expDate);
        assertEquals(task.getCreationDate(), creationDate);
        assertEquals(task.getPriority(), Priority.valueOf(doc.getString("priority")));
        assertEquals(task.getTaskState(), TaskState.valueOf(doc.getString("status")));
    }

    @Test
    public void toDomainTest() {
        Document doc = Document.parse(
                "{\"_id\": {\"$oid\": \"6990e9b370a7995f9586f160\"},\"title\": \"Comprar el pan\",\"description\": null,\"expiredAt\": null,\"priority\": \"MEDIUM\",\"status\": \"NOT_COMPLETED\",\"createdAt\": \"2026-02-14T22:31:31.985215\"}"
        );

        Task task = taskMapper.toDomain(doc);

        String objectId = doc.getObjectId("_id").toHexString();
        LocalDateTime expDate = doc.getString("expiredAt") != null ? LocalDateTime.parse(doc.getString("expiredAt")) : null;
        LocalDateTime creationDate =LocalDateTime.parse(doc.getString("createdAt"));

        assertEquals(task.getId(), objectId);
        assertEquals(task.getTitle(), doc.getString("title"));
        assertEquals(task.getDescription(), doc.getString("description"));
        assertEquals(task.getExpireDate(),expDate);
        assertEquals(task.getCreationDate(), creationDate);
        assertEquals(task.getPriority(), Priority.valueOf(doc.getString("priority")));
        assertEquals(task.getTaskState(), TaskState.valueOf(doc.getString("status")));
    }

}