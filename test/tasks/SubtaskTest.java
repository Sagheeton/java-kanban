package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    private static Subtask subtask1;
    private static Subtask subtask2;

    @BeforeAll
    static void beforeAll() {
        subtask1 = new Subtask("Подзадача 1", "Новая подзадача 1", ProgressStatus.NEW, 1);
        subtask2 = new Subtask("ПОДЗАДАЧА 1", "Завершенная подзадача 1", ProgressStatus.DONE, 2);
    }

    @Test
    void equalsShouldReturnTrueWhenIdsAreEqual() {
        // более наглядно одинаковые айди
        subtask1 = new Subtask(subtask1, 100);
        subtask2 = new Subtask(subtask2, 100);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым id оказались не равны");
    }

    @Test
    void shouldResetEpicIdWhenSubtaskIdEqualsEpicId() {
        subtask1 = new Subtask(subtask1, 1);

        assertEquals(0, subtask1.getEpicId(), "Подзадача добавила свой id в своей epicId");
    }


}