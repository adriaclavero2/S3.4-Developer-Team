package task.model;

import common.exception.InvalidTaskDescriptionException;
import common.exception.InvalidTaskTitleException;
import org.junit.jupiter.api.Test;
import task.enums.Priority;
import task.enums.TaskState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class TaskBuilderTest {
    Priority DEFAULT_PRIORITY = Priority.MEDIUM;
    TaskState DEFAULT_STATE = TaskState.NOT_COMPLETED;

    @Test
    void defaultObjectTest() {
        Task task = TaskBuilder.newTask()
                .withTitle("Go to shopping with my friends")
                .withDescription("Go to the penny market and look what are there")
                .build();

        assertNull(task.getId());
        assertEquals("Go to shopping with my friends", task.getTitle());
        assertEquals("Go to the penny market and look what are there", task.getDescription());
        assertNotEquals(null, task.getCreationDate());
        assertNull(task.getExpireDate());
        assertEquals(DEFAULT_PRIORITY, task.getPriority());
        assertEquals(DEFAULT_STATE, task.getTaskState());

    }

    @Test
    void allParametersObjectTest() {
        LocalDate aDate = LocalDate.of(2026, 9, 24);
        LocalTime aTime = LocalTime.of(20, 34, 0);

        LocalDateTime aDateTime = LocalDateTime.of(aDate, aTime);

        Task task = TaskBuilder.newTask()
                .withTitle("Title")
                .withDescription("Description")
                .withPriority(Priority.HIGH)
                .withTaskState(TaskState.COMPLETED)
                .withExpireDate(aDateTime)
                .build();

        assertNull(task.getId());
        assertEquals("Title", task.getTitle());
        assertEquals("Description", task.getDescription());
        assertNotEquals(null, task.getCreationDate());
        assertEquals(aDateTime, task.getExpireDate());
        assertEquals(Priority.HIGH, task.getPriority());
        assertEquals(TaskState.COMPLETED, task.getTaskState());
    }

    @Test
    void nullTitleTest() {
        assertThrows(InvalidTaskTitleException.class,
                () -> TaskBuilder.newTask()
                        .withTitle(null)
                        .withDescription("long description...")
                        .build());
    }

    @Test
    void emptyTitleTest() {
        assertThrows(InvalidTaskTitleException.class,
                () -> TaskBuilder.newTask()
                        .withTitle("")
                        .withDescription("long description...")
                        .build());
    }

    @Test
    void nullDescriptionTest() {
        assertThrows(InvalidTaskDescriptionException.class,
                () -> TaskBuilder.newTask()
                        .withTitle("Title")
                        .withDescription(null)
                        .build());
    }

    @Test
    void emptyDescriptionTest() {
        assertThrows(InvalidTaskDescriptionException.class,
                () -> TaskBuilder.newTask()
                        .withTitle("Title")
                        .withDescription("")
                        .build());
    }
}