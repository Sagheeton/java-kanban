package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;
    private int newSubtasksCounter;
    private int doneSubtasksCounter;

    Epic(Epic epic, int id) {
        this(id, epic.getName(), epic.getDescription(), ProgressStatus.NEW, new ArrayList<>(), 0, 0);
    }

    public Epic(Epic epic) {
        this(epic.id, epic.name, epic.description, epic.status, epic.subtaskIds, epic.newSubtasksCounter, epic.doneSubtasksCounter);
    }

    public Epic(String name, String description) {
        this(0, name, description, null, new ArrayList<>(), 0, 0);
    }

    private Epic(int id, String name, String description, ProgressStatus status, ArrayList<Integer> subtaskIds,
                 int newSubtasksCounter, int doneSubtasksCounter) {
        super(id, name, description, status);
        this.subtaskIds = new ArrayList<>(subtaskIds);
        this.newSubtasksCounter = newSubtasksCounter;
        this.doneSubtasksCounter = doneSubtasksCounter;
    }

    public ArrayList<Integer> getSubtaskIds() {
        return new ArrayList<>(this.subtaskIds);
    }

    int getNewSubtasksCounter() {
        return newSubtasksCounter;
    }

    int getDoneSubtasksCounter() {
        return doneSubtasksCounter;
    }

    void removeAllSubtaskIds() {
        subtaskIds.clear();
        newSubtasksCounter = 0;
        doneSubtasksCounter = 0;
    }

    void addSubtaskId(int subtaskId, ProgressStatus status) {
        if (!subtaskIds.contains(subtaskId)) {
            subtaskIds.add(subtaskId);
            increaseStatusCounter(status);
        }
    }

    void removeSubtaskId(int subtaskId, ProgressStatus status) {
        boolean removed = subtaskIds.remove(Integer.valueOf(subtaskId));
        if (removed) {
            decreaseStatusCounter(status);
        }
    }

    void handleSubtaskStatusChange(ProgressStatus oldStatus, ProgressStatus newStatus) {
        if (oldStatus == newStatus) return;
        decreaseStatusCounter(oldStatus);
        increaseStatusCounter(newStatus);
    }

    private void increaseStatusCounter(ProgressStatus status) {
        updateStatusCounter(status, SubtaskCountDelta.INCREASE);
    }

    private void decreaseStatusCounter(ProgressStatus status) {
        updateStatusCounter(status, SubtaskCountDelta.DECREASE);
    }

    private void updateStatusCounter(ProgressStatus status, SubtaskCountDelta subtaskCountDelta) {
        int delta = 1;
        if (subtaskCountDelta == SubtaskCountDelta.DECREASE) {
            delta = -1;
        }

        switch (status) {
            case NEW:
                newSubtasksCounter += delta;
                break;
            case DONE:
                doneSubtasksCounter += delta;
                break;
        }
    }

    private enum SubtaskCountDelta {
        INCREASE,
        DECREASE
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status=" + getStatus() +
                ", subtaskIds=" + subtaskIds +
                '}';
    }
}
