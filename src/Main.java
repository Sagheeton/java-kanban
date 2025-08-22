import tasks.Epic;
import managers.Managers;
import tasks.ProgressStatus;
import tasks.Subtask;
import tasks.Task;
import managers.TaskManager;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = taskManager.createTask(new Task("Задача 1", "Первая задача", ProgressStatus.NEW));
        Task task2 = taskManager.createTask(new Task("Задача 2", "Вторая задача", ProgressStatus.NEW));

        Epic epicWithTwoSubtaks = taskManager.createEpic(
                new Epic("Эпик для двух подзадач", "У этого эпика будет две подзадачи")
        );
        Subtask subtask1 = taskManager.createSubtask(
                new Subtask("Подзадача 1", "Первая подзадача для эпика с двумя подзадачами",
                        ProgressStatus.NEW, epicWithTwoSubtaks.getId())
        );
        Subtask subtask2 = taskManager.createSubtask(
                new Subtask("Подзадача 2", "Вторая подзадача для эпика с двумя подзадачами",
                        ProgressStatus.NEW, epicWithTwoSubtaks.getId())
        );

        Epic epicWithOneSubtask = taskManager.createEpic(
                new Epic("Эпик для одной подзадачи","У этого эпика будет одна подзадача")
        );
        Subtask onlySubtask = taskManager.createSubtask(
                new Subtask("Подзадача", "Единственная подзадача эпика с одной подзадачей",
                        ProgressStatus.NEW, epicWithOneSubtask.getId())
        );

        System.out.println("Список созданных задач:");
        printAllTasks(taskManager);
        printFancySeparator();

        Task getTask1ById = taskManager.getTaskById(task1.getId());
        getTask1ById.setStatus(ProgressStatus.IN_PROGRESS);
        taskManager.updateTask(getTask1ById);
        task2.setStatus(ProgressStatus.DONE);
        taskManager.updateTask(task2);
        Subtask getSubtask1ById = taskManager.getSubtaskById(subtask1.getId());
        getSubtask1ById.setStatus(ProgressStatus.IN_PROGRESS);
        taskManager.updateSubtask(getSubtask1ById);
        subtask2.setStatus(ProgressStatus.DONE);
        taskManager.updateSubtask(subtask2);
        onlySubtask.setStatus(ProgressStatus.DONE);
        taskManager.updateSubtask(onlySubtask);

        System.out.println("Список обновленных задач:");
        printAllTasks(taskManager);
        printFancySeparator();

        taskManager.removeTaskById(task1.getId());
        taskManager.removeEpicById(epicWithTwoSubtaks.getId());

        System.out.println("Список задач после удаления:");
        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        printFancySeparator();
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        printFancySeparator();
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getSubtasksByEpicId(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        printFancySeparator();
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }
        printFancySeparator();
        System.out.println("История:");
        for (Task task : manager.getTasksHistoryList()) {
            System.out.println(task);
        }
    }

    public static void printFancySeparator() {
        int separatorLength = 48;

        System.out.println();
        System.out.println("-".repeat(separatorLength));
        System.out.println();
    }
}
