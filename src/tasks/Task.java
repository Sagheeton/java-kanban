package tasks;

public class Task {
    private final int id;
    private final String name;
    private final String description;
    private final ProgressStatus status;

    public Task(String name, String description) {
        this(0, name, description, ProgressStatus.NEW);
    }

    Task(Task task, int id) {
        this(id, task.name, task.description, task.status);
    }

    protected Task(int id, String name, String description, ProgressStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task withName(String newName) {
        return new Task(id, newName, description, status);
    }

    public Task withDescription(String newDescription) {
        return new Task(id, name, newDescription, status);
    }

    public Task withStatus(ProgressStatus newStatus) {
        return new Task(id, name, description, newStatus);
    }

    public int getId() {
        return id;
    }

    public ProgressStatus getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public final int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                '}';
    }
}
