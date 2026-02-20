package task.dto;

public record ErrorOutputDTO(String outputState) implements OutputDTO {
    @Override
    public String getOutputState() {
        return outputState();
    }
}
