package managers;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;

public interface TaskManager {
    Task createTask(Task task);

    Subtask createSubtask(Subtask subtask);

    Epic createEpic(Epic epic);

    Task getTaskById(int id);

    Subtask getSubtaskById(int id);

    Epic getEpicById(int id);

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    void updateTask(Task newTask);

    void updateSubtask(Subtask newSubtask);

    void updateEpic(Epic newEpic);

    void removeTaskById(int id);

    void removeSubtaskById(int id);

    void removeEpicById(int id);

    void removeAllTasks();

    void removeAllSubtasks();

    void removeAllEpics();

    List<Subtask> getSubtasksByEpicId(int epicId);

    List<Task> getTasksHistoryList();
}
