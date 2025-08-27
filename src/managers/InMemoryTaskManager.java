package managers;

import tasks.Epic;
import tasks.ProgressStatus;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks = new HashMap<>();
    private final Map<Integer, Subtask> subtasks = new HashMap<>();
    private final Map<Integer, Epic> epics = new HashMap<>();
    private int nextId = 1;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public Task createTask(Task task) {
        if (task == null) {
            return null;
        }
        Task taskWithId = new Task(task, nextId++);
        tasks.put(taskWithId.getId(), taskWithId);
        return new Task(taskWithId);
    }

    @Override
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

    @Override
    public Epic createEpic(Epic epic) {
        if (epic == null) {
            return null;
        }

        Epic epicWithId = new Epic(epic, nextId++);
        epics.put(epicWithId.getId(), epicWithId);
        return new Epic(epicWithId);
    }

    @Override
    public Task getTaskById(int id) {
        Task task = new Task(tasks.get(id));
        historyManager.add(new Task(task));
        return task;
    }

    @Override
    public Subtask getSubtaskById(int id) {
        Subtask subtask = new Subtask(subtasks.get(id));
        historyManager.add(new Subtask(subtask));
        return subtask;
    }

    @Override
    public Epic getEpicById(int id) {
        Epic epic = new Epic(epics.get(id));
        historyManager.add(new Epic(epic));
        return epic;
    }

    @Override
    public List<Task> getTasks() {
        ArrayList<Task> taskList = new ArrayList<>(tasks.size());
        taskList.addAll(tasks.values());
        return taskList;
    }

    @Override
    public List<Subtask> getSubtasks() {
        ArrayList<Subtask> subtaskList = new ArrayList<>(subtasks.size());
        subtaskList.addAll(subtasks.values());
        return subtaskList;
    }

    @Override
    public List<Epic> getEpics() {
        ArrayList<Epic> epicList = new ArrayList<>(epics.size());
        epicList.addAll(epics.values());
        return epicList;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
    public void removeTaskById(int id) {
        tasks.remove(id);
    }

    @Override
    public void removeSubtaskById(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        unlink(epic, subtask);
        updateEpicStatus(epic);
    }

    @Override
    public void removeEpicById(int id) {
        Epic epic = epics.remove(id);
        if (epic == null) {
            return;
        }

        List<Integer> epicSubtaskIds = epic.getSubtaskIds();
        for (Integer epicSubtaskId : epicSubtaskIds) {
            subtasks.remove(epicSubtaskId);
        }
    }

    @Override
    public void removeAllTasks() {
        tasks.clear();
    }

    @Override
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

    @Override
    public void removeAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    @Override
    public List<Subtask> getSubtasksByEpicId(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return new ArrayList<>();
        }
        List<Integer> subtaskIds = epic.getSubtaskIds();
        List<Subtask> epicSubtasks = new ArrayList<>(subtaskIds.size());

        for (Integer subtaskId : subtaskIds) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask != null) {
                epicSubtasks.add(new Subtask(subtask));
            }
        }
        return epicSubtasks;
    }

    @Override
    public List<Task> getTasksHistoryList() {
        return historyManager.getHistory();
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
