package tasks;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    private static Epic epic1;
    private static Epic epic2;

    @BeforeAll
    static void beforeAll() {
        epic1 = new Epic("Эпик 1", "Новый эпик 1");
        epic2 = new Epic("ЭПИК 1", "Завершенный эпик 1");
        epic2.setStatus(ProgressStatus.DONE);
    }

    @Test
    void equalsShouldReturnTrueWhenIdsAreEqual() {
        // более наглядно одинаковые айди
        epic1 = new Epic(epic1, 100);
        epic2 = new Epic(epic2, 100);

        assertEquals(epic1, epic2, "Эпики с одинаковым id оказались не равны");
    }

    @Test
    void shouldNotAddSelfIdWhenAddSubtaskId() {
        List<Integer> subtaskIdsBefore = epic1.getSubtaskIds();
        epic1.addSubtaskId(epic1.getId(), ProgressStatus.NEW);
        List<Integer> subtaskIdsAfter = epic1.getSubtaskIds();

        assertEquals(subtaskIdsBefore, subtaskIdsAfter, "Эпик добавил свой id в список сабтасков");
    }
}