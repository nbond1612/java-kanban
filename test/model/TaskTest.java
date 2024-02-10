package model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Задачи")
class TaskTest {
    @Test
    @DisplayName("Проверка равенства по ID")
    public void shouldBeEqualWhenSameId() {
        Task task1 = new Task("Задача1", TaskStatus.NEW, "Описание1", 1);
        Task task2 = new Task("Задача2", TaskStatus.DONE, "Описание2", 1);
        assertEquals(task1, task2, "Задачи с одинаковым ID должны быть равны!");
    }
}