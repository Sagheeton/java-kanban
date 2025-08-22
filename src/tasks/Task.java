package tasks;

public class Task {
    protected final int id;
    protected String name;
    protected String description;
    protected ProgressStatus status;

    public Task(Task task, int id) {
        this(id, task.name, task.description, task.status);
    }

    public Task(Task task) {
        this(task.id, task.name, task.description, task.status);
    }

    public Task(String name, String description, ProgressStatus status) {
        this(0, name, description, status);
    }

    protected Task(int id, String name, String description, ProgressStatus status) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ProgressStatus getStatus() {
        return status;
    }

    public void setStatus(ProgressStatus status) {
        this.status = status;
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
