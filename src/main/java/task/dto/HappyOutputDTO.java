package task.dto;

public record HappyOutputDTO(String outputState) implements OutputDTO {
    @Override
    public String getOutputState() {
        return outputState();
    }
}
