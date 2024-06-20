package service;

import model.*;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Менеджер истории")
class InMemoryHistoryManagerTest {
    InMemoryHistoryManager manager;
    Task task;
    Task task1;
    Task task2;

    @BeforeEach
    public void beforeEach() {
        manager = new InMemoryHistoryManager();
        task = new Task("Задача", TaskStatus.NEW, "Описание", 0);
        task1 = new Task("Задача1", TaskStatus.NEW, "Описание1", 1);
        task2 = new Task("Задача2", TaskStatus.DONE, "Описание2", 2);
    }

    @Test
    @DisplayName("В истории не появляются повторения")
    public void shouldNotRepeatInHistory() {
        manager.add(task);
        manager.add(task1);
        manager.add(task2);

        manager.add(task);
        manager.add(task2);

        assertEquals(manager.getHistory(), List.of(task1, task, task2), "Задачи, которые уже были в истории, должны оттуда удаляться и добавляться в конец!");
    }

    @Test
    @DisplayName("Функция удаления из истории работает, в том числе для начала и конца истории")
    public void shouldRemoveFromHistory() {
        manager.add(task);
        manager.add(task1);
        manager.add(task2);

        manager.remove(task.getId());

        assertEquals(manager.getHistory(), List.of(task1, task2), "Должна удаляться первая задача в истории!");

        manager.add(task);
        manager.remove(task2.getId());

        assertEquals(manager.getHistory(), List.of(task1, task), "Должна удаляться задача в середине истории!");

        manager.add(task2);
        manager.add(task1);
        manager.remove(task1.getId());

        assertEquals(manager.getHistory(), List.of(task, task2), "Должна удаляться задача в конце истории!");
    }

    @Test
    @DisplayName("Функция очистки истории работает")
    public void shouldClearHistory() {
        manager.add(task);
        manager.add(task1);
        manager.add(task2);

        manager.clearHistory();
        assertEquals(manager.getHistory(), List.of(), "История должна очищаться!");
    }
}
