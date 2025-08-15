package tasks;

public class Subtask extends Task {
    private final int epicId;

    Subtask(Subtask subtask, int id) {
        this(id, subtask.name, subtask.description, subtask.status, subtask.epicId);
    }

    public Subtask(Subtask subtask) {
        this(subtask.id, subtask.name, subtask.description, subtask.status, subtask.epicId);
    }

    public Subtask(String name, String description, ProgressStatus status, int epicId) {
        this(0, name, description, status, epicId);
    }

    private Subtask(int id, String name, String description, ProgressStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}';
    }
}
