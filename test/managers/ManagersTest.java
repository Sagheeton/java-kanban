package managers;

import org.junit.jupiter.api.Test;
import tasks.ProgressStatus;
import tasks.Task;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {
    @Test
    void shouldCreateInitializedTaskManager() {
        TaskManager manager = Managers.getDefault();
        manager.getTasksHistoryList().clear();

        assertNotNull(manager, "Managers.getDefault() вернул null — менеджер не создан");
        assertNotNull(manager.getTasks(), "Список задач не инициализирован (getTasks() вернул null)");
        assertNotNull(manager.getSubtasks(), "Список подзадач не инициализирован (getSubtasks() вернул null)");
        assertNotNull(manager.getEpics(), "Список эпиков не инициализирован (getEpics() вернул null)");
        assertNotNull(manager.getTasksHistoryList(), "История просмотров не инициализирована (getTasksHistoryList() вернул null)");

        assertTrue(manager.getTasks().isEmpty(), "При инициализации список задач должен быть пустым");
        assertTrue(manager.getSubtasks().isEmpty(), "При инициализации список подзадач должен быть пустым");
        assertTrue(manager.getEpics().isEmpty(), "При инициализации список эпиков должен быть пустым");
        assertTrue(manager.getTasksHistoryList().isEmpty(), "При инициализации история просмотров должна быть пустой");
        Task task = new Task("Задача", "Описание задачи", ProgressStatus.NEW);
        Task created = manager.createTask(task);

        assertEquals(1, created.getId(), "Первая задача должна получить id = 1");
    }


    @Test
    void shouldCreateInitializedHistoryManager() {
        HistoryManager history = Managers.getDefaultHistory();
        history.getHistory().clear();

        assertNotNull(history, "Managers.getDefaultHistory() вернул null — менеджер истории не создан");
        assertNotNull(history.getHistory(), "Список истории не инициализирован (getHistory() вернул null)");

        assertTrue(history.getHistory().isEmpty(), "При инициализации история просмотров должна быть пустой");
    }
}