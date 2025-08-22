package tasks;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void equalsShouldReturnTrueWhenIdsAreEqual() {
        Task task1 = new Task("Задача 1", "Новая задача 1", ProgressStatus.NEW);
        Task task2 = new Task("ЗАДАЧА 1", "Завершенная задача 1", ProgressStatus.DONE);

        // более наглядно одинаковые айди
        task1 = new Task(task1, 100);
        task2 = new Task(task2, 100);

        assertEquals(task1, task2, "Задачи с одинаковым id оказались не равны");
    }
}