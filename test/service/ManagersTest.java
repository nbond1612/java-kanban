package service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.*;

@DisplayName("Утилитарный класс Managers")
class ManagersTest {
    @Test
    @DisplayName("Функциональность возвращаемого менеджера задач")
    public void shouldReturnFunctionalTaskManager() {
        InMemoryTaskManager taskManager = (InMemoryTaskManager) Managers.getDefault();
        Task testTask = new Task("Name", TaskStatus.NEW, "Description",  1);
        taskManager.createTask(testTask);
        String actual = taskManager.getAllTasks().toString();
        String expected = "[Task{id=1, name='Name', status='NEW', description='Description'}]";
        assertEquals(expected, actual, "Возвращаемый утилитарным классом менеджер задач работает неправильно!");
    }

    @Test
    @DisplayName("Функциональность возвращаемого менеджера истории")
    public void shouldReturnFunctionalHistoryManager() {
        InMemoryHistoryManager historyManager = (InMemoryHistoryManager) Managers.getDefaultHistory();
        Task testTask = new Task("Name", TaskStatus.NEW, "Description",  1);
        historyManager.add(testTask);
        String actual = historyManager.getHistory().toString();
        String expected = "[Task{id=1, name='Name', status='NEW', description='Description'}]";
        assertEquals(expected, actual, "Возвращаемый утилитарным классом менеджер истории работает неправильно!");
    }
}