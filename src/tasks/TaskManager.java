package tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;

    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }
        Task taskWithId = new Task(task, nextId++);
        tasks.put(taskWithId.getId(), taskWithId);
        return taskWithId;
    }

    public Subtask createSubtask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }

        Epic parentEpic = getEpicById(subtask.getEpicId());
        if (parentEpic == null) {
            return null;
        }

        Subtask subtaskWithId = new Subtask(subtask, nextId++);
        subtasks.put(subtaskWithId.getId(), subtaskWithId);
        link(parentEpic, subtaskWithId);
        return subtaskWithId;
    }

    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }

        Epic epicWithId = new Epic(epic, nextId++);
        epics.put(epicWithId.getId(), epicWithId);
        return epicWithId;
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public void updateTask(Task newTask) {
        if (newTask == null) {
            return;
        }

        int newTaskId = newTask.getId();
        if (!tasks.containsKey(newTaskId)) {
            return;
        }
        tasks.put(newTaskId, newTask);
    }

    public void updateSubtask(Subtask newSubtask) {
        if (newSubtask == null) {
            return;
        }

        Subtask storedSubtask = getSubtaskById(newSubtask.getId());
        if (storedSubtask == null) {
            return;
        }

        Epic newSubtaskEpic = getEpicById(newSubtask.getEpicId());
        if (newSubtaskEpic == null) {
            return;
        }

        Epic storedSubtaskEpic = getEpicById(storedSubtask.getEpicId());
        boolean isEpicChanged = storedSubtaskEpic.getId() != newSubtaskEpic.getId();
        if (isEpicChanged) {
            unlink(storedSubtaskEpic, storedSubtask);
            link(newSubtaskEpic, newSubtask);
        } else {
            storedSubtaskEpic.handleSubtaskStatusChange(storedSubtask.getStatus(), newSubtask.getStatus());
        }
        subtasks.put(newSubtask.getId(), newSubtask);
    }

    public void updateEpic(Epic newEpic) {
        if (newEpic == null) {
            return;
        }

        int newEpicId = newEpic.getId();
        Epic storedEpic = getEpicById(newEpicId);
        if (storedEpic == null) {
            return;
        }

        Epic updatedEpic = storedEpic
                .withName(newEpic.getName())
                .withDescription(newEpic.getDescription());
        epics.put(updatedEpic.getId(), updatedEpic);
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = getEpicById(subtask.getEpicId());
        updateEpicSubtaskLink(epic, subtask, LinkAction.UNLINK);
    }

    public void removeEpicById(int id) {
        removeEpicById(id, false);
    }

    public void removeEpicById(int id, boolean keepSubtasks) {
        Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }

        ArrayList<Integer> epicSubtaskIds = epic.getSubtaskIds();
        for (Integer epicSubtaskId : epicSubtaskIds) {
            Subtask subtask = subtasks.remove(epicSubtaskId);
            if (keepSubtasks) {
                Task task = subtask.convertToTask();
                tasks.put(task.getId(), task);
            }
        }
    }

    public void removeAllTasks() {
        tasks.clear();
    }

    public void removeAllSubtasks() {
        if (subtasks.isEmpty()) {
            return;
        }

        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.removeAllSubtaskIds();
        }
    }

    public void removeAllEpics() {
        removeAllEpics(false);
    }

    public void removeAllEpics(boolean keepSubtasks) {
        if (keepSubtasks) {
            for (Subtask subtask : subtasks.values()) {
                Task task = subtask.convertToTask();
                tasks.put(task.getId(), task);
            }
        }
        subtasks.clear();
        epics.clear();
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        Epic epic = getEpicById(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        ArrayList<Subtask> epicSubtasks = new ArrayList<>(subtaskIds.size());

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                epicSubtasks.add(subtask);
            }
        }
        return epicSubtasks;
    }

    private void link(Epic epic, Subtask subtask) {
        updateEpicSubtaskLink(epic, subtask, LinkAction.LINK);
    }

    private void unlink(Epic epic, Subtask subtask) {
        updateEpicSubtaskLink(epic, subtask, LinkAction.UNLINK);
    }

    private void updateEpicSubtaskLink(Epic epic, Subtask subtask, LinkAction linkAction) {
        int subtaskId = subtask.getId();
        ProgressStatus subtaskStatus = subtask.getStatus();
        switch(linkAction) {
            case LINK:
                epic.addSubtaskId(subtaskId, subtaskStatus);
                break;
            case UNLINK:
                epic.removeSubtaskId(subtaskId, subtaskStatus);
                break;
        }
    }

    private enum LinkAction {
        LINK,
        UNLINK
    }
}
