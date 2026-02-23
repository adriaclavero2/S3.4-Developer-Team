package task.dto;

public record TaskUpdateDTO(
        String id,
        String title,
        String description,
        String expireDate,
        String priority) {

}
