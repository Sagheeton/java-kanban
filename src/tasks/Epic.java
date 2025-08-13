package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtaskIds;
    private int newSubtasksCounter;
    private int doneSubtasksCounter;
    private ProgressStatus statusCache;

    public Epic(String name, String description) {
        this(0, name, description, ProgressStatus.NEW, new ArrayList<>(), 0, 0);
    }

    Epic(Epic epic, int id) {
        this(id, epic.getName(), epic.getDescription(), ProgressStatus.NEW, new ArrayList<>(), 0, 0);
    }

    private Epic(int id, String name, String description, ProgressStatus status, ArrayList<Integer> subtaskIds,
                 int newSubtasksCounter, int doneSubtasksCounter) {
        super(id, name, description, status);
        this.subtaskIds = new ArrayList<>(subtaskIds);
        this.newSubtasksCounter = newSubtasksCounter;
        this.doneSubtasksCounter = doneSubtasksCounter;
    }

    @Override
    public Epic withName(String newName) {
        return new Epic(getId(), newName, getDescription(), getStatus(), subtaskIds,
                newSubtasksCounter, doneSubtasksCounter);
    }

    @Override
    public Epic withDescription(String newDescription) {
        return new Epic(getId(), getName(), newDescription, getStatus(),  subtaskIds,
                newSubtasksCounter, doneSubtasksCounter);
    }

    @Override
    // статус вычисляется, а не устанавливается
    public Epic withStatus(ProgressStatus newStatus) {
        return this;
    }

    @Override
    public ProgressStatus getStatus() {
        if (statusCache != null) {
            return statusCache;
        }

        int totalSubtasks = subtaskIds.size();
        boolean noSubtasks = totalSubtasks == 0;
        boolean allNew = totalSubtasks == newSubtasksCounter;
        boolean allDone = totalSubtasks == doneSubtasksCounter;
        if (noSubtasks || allNew) {
            statusCache = ProgressStatus.NEW;
        } else if (allDone) {
            statusCache = ProgressStatus.DONE;
        } else {
            statusCache = ProgressStatus.IN_PROGRESS;
        }
        return statusCache;
    }

    private void resetStatusCache(){
        statusCache = null;
    }

    ArrayList<Integer> getSubtaskIds() {
        return new ArrayList<>(this.subtaskIds);
    }

    void removeAllSubtaskIds() {
        subtaskIds.clear();
        newSubtasksCounter = 0;
        doneSubtasksCounter = 0;
        resetStatusCache();
    }

    void addSubtaskId(int subtaskId, ProgressStatus status) {
        if (!subtaskIds.contains(subtaskId)) {
            subtaskIds.add(subtaskId);
            increaseStatusCounter(status);
            resetStatusCache();
        }
    }

    void removeSubtaskId(int subtaskId, ProgressStatus status) {
        boolean removed = subtaskIds.remove(Integer.valueOf(subtaskId));
        if (removed) {
            decreaseStatusCounter(status);
            resetStatusCache();
        }
    }

    void handleSubtaskStatusChange(ProgressStatus oldStatus, ProgressStatus newStatus) {
        if (oldStatus == newStatus) return;
        decreaseStatusCounter(oldStatus);
        increaseStatusCounter(newStatus);
        resetStatusCache();
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
