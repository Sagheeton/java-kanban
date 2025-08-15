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
        return new Task(taskWithId);
    }

    public Subtask createSubtask(Subtask subtask) {
        if (subtask == null) {
            return null;
        }

        Epic parentEpic = epics.get(subtask.getEpicId());
        if (parentEpic == null) {
            return null;
        }

        Subtask subtaskWithId = new Subtask(subtask, nextId++);
        subtasks.put(subtaskWithId.getId(), subtaskWithId);
        link(parentEpic, subtaskWithId);
        updateEpicStatus(parentEpic);
        return new Subtask(subtaskWithId);
    }

    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }

        Epic epicWithId = new Epic(epic, nextId++);
        epics.put(epicWithId.getId(), epicWithId);
        return new Epic(epicWithId);
    }

    public Task getTaskById(int id) {
        return new Task(tasks.get(id));
    }

    public Subtask getSubtaskById(int id) {
        return new Subtask(subtasks.get(id));
    }

    public Epic getEpicById(int id) {
        return new Epic(epics.get(id));
    }

    public ArrayList<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>(tasks.size());
        taskList.addAll(tasks.values());
        return taskList;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskList = new ArrayList<>(subtasks.size());
        subtaskList.addAll(subtasks.values());
        return subtaskList;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.size());
        epicList.addAll(epics.values());
        return epicList;
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

        Subtask storedSubtask = subtasks.get(newSubtask.getId());
        if (storedSubtask == null) {
            return;
        }

        Epic newSubtaskEpic = epics.get(newSubtask.getEpicId());
        if (newSubtaskEpic == null) {
            return;
        }

        Epic storedSubtaskEpic = epics.get(storedSubtask.getEpicId());
        boolean isEpicChanged = storedSubtaskEpic.getId() != newSubtaskEpic.getId();
        if (isEpicChanged) {
            unlink(storedSubtaskEpic, storedSubtask);
            link(newSubtaskEpic, newSubtask);
            updateEpicStatus(newSubtaskEpic);
        } else {
            storedSubtaskEpic.handleSubtaskStatusChange(storedSubtask.getStatus(), newSubtask.getStatus());
            updateEpicStatus(storedSubtaskEpic);
        }
        subtasks.put(newSubtask.getId(), newSubtask);
    }

    public void updateEpic(Epic newEpic) {
        if (newEpic == null) {
            return;
        }

        int newEpicId = newEpic.getId();
        Epic storedEpic = epics.get(newEpicId);
        if (storedEpic == null) {
            return;
        }

        storedEpic.setName(newEpic.getName());
        storedEpic.setDescription(newEpic.getDescription());
    }

    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        unlink(epic, subtask);
        updateEpicStatus(epic);
    }

    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }

        ArrayList<Integer> epicSubtaskIds = epic.getSubtaskIds();
        for (Integer epicSubtaskId : epicSubtaskIds) {
            subtasks.remove(epicSubtaskId);
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
            epic.setStatus(ProgressStatus.NEW);
        }
    }

    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public ArrayList<Subtask> getSubtasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        ArrayList<Integer> subtaskIds = epic.getSubtaskIds();
        ArrayList<Subtask> epicSubtasks = new ArrayList<>(subtaskIds.size());

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                epicSubtasks.add(new Subtask(subtask));
            }
        }
        return epicSubtasks;
    }

    private void updateEpicStatus(Epic epic) {
        int totalSubtasks = epic.getSubtaskIds().size();
        boolean noSubtasks = totalSubtasks == 0;
        boolean allNew = totalSubtasks == epic.getNewSubtasksCounter();
        boolean allDone = totalSubtasks == epic.getDoneSubtasksCounter();
        if (noSubtasks || allNew) {
            epic.setStatus(ProgressStatus.NEW);
        } else if (allDone) {
            epic.setStatus(ProgressStatus.DONE);
        } else {
            epic.setStatus(ProgressStatus.IN_PROGRESS);
        }
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
