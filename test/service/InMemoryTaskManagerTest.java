package service;

import model.*;
import model.TaskStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Менеджер задач")
class InMemoryTaskManagerTest {

    @Test
    @DisplayName("Задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера")
    public void shouldNotHaveConflictBetweenManuallyAndAutomaticallyAssignedId() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = new Task("Задача1", TaskStatus.NEW, "Описание1");
        taskManager.createTask(task1);

        Task task2 = new Task("Задача2", TaskStatus.DONE, "Описание2");
        taskManager.createTask(task2);
        task2.setId(1);

        String actual = taskManager.getTask(1).toString();
        String expected = "Task{id=1, name='Задача1', status='NEW', description='Описание1'}";
        assertEquals(expected, actual, "Должна возвращаться только та задача, у которой ключ соответствует ID!");
    }

    @Test
    @DisplayName("InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id")
    public void shouldBeFunctional() {
        InMemoryTaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task = new Task("Задача", TaskStatus.NEW, "Описание");
        Epic epic = new Epic("Эпик", "Описание");
        Subtask subtask = new Subtask("Подзадача", TaskStatus.NEW, "Описание", 2);

        taskManager.createTask(task);
        taskManager.createEpic(epic);
        taskManager.createSubtask(subtask);

        String actual1 = taskManager.getTask(1).toString();
        String expected1 = "Task{id=1, name='Задача', status='NEW', description='Описание'}";
        assertEquals(expected1, actual1, "Менеджер должен возвращать задачу по ID!");

        String actual2 = taskManager.getEpic(2).toString();
        String expected2 = "Epic{id=2, name='Эпик', status='NEW', description='Описание', numOfSubtasks='1'}";
        assertEquals(expected2, actual2, "Менеджер должен возвращать эпик по ID!");

        String actual3 = taskManager.getSubtask(3).toString();
        String expected3 = "Subtask{id=3, name='Подзадача', status='NEW', description='Описание', assignedEpicID='2'}";
        assertEquals(expected3, actual3, "Менеджер должен возвращать подзадачу по ID!");
    }
}