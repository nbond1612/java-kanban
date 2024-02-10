package model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Подзадаачи")
class SubtaskTest {
    @Test
    @DisplayName("Проверка равенства по ID")
    public void shouldBeEqualWhenSameId() {
        Subtask subtask1 = new Subtask("Подзадача1", TaskStatus.NEW, "Описание1", 1);
        Subtask subtask2 = new Subtask("Подзадача2", TaskStatus.DONE, "Описание2", 2);

        assertEquals(subtask1, subtask2, "Подзадачи с одинаковым ID должны быть равны!");
    }
}