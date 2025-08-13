import tasks.Epic;
import tasks.ProgressStatus;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = taskManager.createTask(new Task("Задача 1", "Первая задача"));
        Task task2 = taskManager.createTask(new Task("Задача 2", "Вторая задача"));

        Epic epicWithTwoSubtaks = taskManager.createEpic(
                new Epic("Эпик для двух подзадач", "У этого эпика будет две подзадачи")
        );
        Subtask subtask1 = taskManager.createSubtask(
                new Subtask("Подзадача 1", "Первая подзадача для эпика с двумя подзадачами",
                epicWithTwoSubtaks.getId())
        );
        Subtask subtask2 = taskManager.createSubtask(
                new Subtask("Подзадача 2", "Вторая подзадача для эпика с двумя подзадачами",
                epicWithTwoSubtaks.getId())
        );

        Epic epicWithOneSubtask = taskManager.createEpic(
                new Epic("Эпик для одной подзадачи","У этого эпика будет одна подзадача")
        );
        Subtask onlySubtask = taskManager.createSubtask(
                new Subtask("Подзадача", "Единственная подзадача эпика с одной подзадачей",
                epicWithOneSubtask.getId())
        );
        System.out.println("Список созданных задач:");
        printThemAll(taskManager);

        taskManager.updateTask(task1.withStatus(ProgressStatus.IN_PROGRESS));
        taskManager.updateTask(task2.withStatus(ProgressStatus.DONE));
        taskManager.updateSubtask(subtask1.withStatus(ProgressStatus.IN_PROGRESS));
        taskManager.updateSubtask(subtask2.withStatus(ProgressStatus.DONE));
        taskManager.updateSubtask(onlySubtask.withStatus(ProgressStatus.DONE));

        System.out.println("Список обновленных задач:");
        printThemAll(taskManager);

        taskManager.removeTaskById(task1.getId());
        taskManager.removeEpicById(epicWithTwoSubtaks.getId());

        System.out.println("Список задач после удаления:");
        printThemAll(taskManager);
    }

    public static void printThemAll(TaskManager taskManager) {
        System.out.println("Список эпиков:");
        for (Epic epic : taskManager.getEpics()) {
            System.out.println(epic);
        }
        printFancySeparator();

        System.out.println("Список задач:");
        for (Task task : taskManager.getTasks()) {
            System.out.println(task);
        }
        printFancySeparator();

        System.out.println("Список подзадач:");
        for (Subtask subtask : taskManager.getSubtasks()) {
            System.out.println(subtask);
        }
        printFancySeparator();
    }

    public static void printFancySeparator() {
        int separatorLength = 48;

        System.out.println();
        System.out.println("-".repeat(separatorLength));
        System.out.println();
    }
}
