package tasks;

public class Subtask extends Task {
    private final int epicId;

    public Subtask(String name, String description, int epicId) {
        this(0, name, description, ProgressStatus.NEW, epicId);
    }

    Subtask(Subtask subtask, int id) {
        this(id, subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.epicId);
    }

    private Subtask(int id, String name, String description, ProgressStatus status, int epicId) {
        super(id, name, description, status);
        this.epicId = epicId;
    }

    @Override
    public Subtask withName(String newName) {
        return new Subtask(getId(), newName, getDescription(), getStatus(), getEpicId());
    }

    @Override
    public Subtask withDescription(String newDescription) {
        return new Subtask(getId(), getName(), newDescription, getStatus(), getEpicId());
    }

    @Override
    public Subtask withStatus(ProgressStatus newStatus) {
        return new Subtask(getId(), getName(), getDescription(), newStatus, getEpicId());
    }

    public Subtask withEpicId(int newEpicId) {
        return new Subtask(getId(), getName(), getDescription(), getStatus(), newEpicId);
    }

    public int getEpicId() {
        return epicId;
    }

    public Task convertToTask() {
        return new Task(getId(), getName(), getDescription(), getStatus());
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
