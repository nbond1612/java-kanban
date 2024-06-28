package service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.io.IOException;
import model.*;

@DisplayName("Менеджер с хранением задач в памяти")
public class FileBackedTaskManagerTest {
    File file;
    FileBackedTaskManager manager;

    @BeforeEach
    public void beforeEach() throws IOException {
        file = File.createTempFile("test", "txt");
        manager = new FileBackedTaskManager(file);
    }

    @DisplayName("FileBackedTaskManager работает для пустого файла")
    @Test
    public void shouldWorkForEmptyFile() throws IOException {
        FileBackedTaskManager f = FileBackedTaskManager.loadFromFile(file);
        List<Task> list = f.getAllTasks();
        List<Epic> list1 = f.getAllEpics();
        List<Subtask> list2 = f.getAllSubtasks();
        int totalSize = list.size() + list1.size() + list2.size();

        assertEquals(0, totalSize, "Из пустого файла зкземпляр класса FileBackedTaskManager создается не пустым.");
    }

    @DisplayName("FileBackedTaskManager успешно сохраняет и загружает несколько задач разных типов")
    @Test
    public void shouldSaveAndLoadMultipleTasks() throws IOException {
        Task task = new Task("Task", TaskStatus.NEW, "Task description");
        manager.createTask(task);

        Epic epic = new Epic("Epic", "Epic description");
        manager.createEpic(epic);

        Subtask subtask = new Subtask("Subtask", TaskStatus.NEW, "Subtask description", 2);
        manager.createSubtask(subtask);

        String text = Files.readString(file.toPath());
        String[] taskArray = text.split(";");

        assertEquals("id,type,name,status,description,epic", taskArray[0], "Первая строка не добавлена");
        assertEquals("1,TASK,Task,NEW,Task description", taskArray[1], "Неверно добавляются задачи");
        assertEquals("2,EPIC,Epic,NEW,Epic description", taskArray[2], "Неверно добавляются эпики");
        assertEquals("3,SUBTASK,Subtask,NEW,Subtask description,2", taskArray[3], "Неверно добавляются подзадачи");

        FileBackedTaskManager newManager = FileBackedTaskManager.loadFromFile(file);

        String actualTask = newManager.getTask(1).toString();
        String expectedTask = "Task{id=1, name='Task', status='NEW', description='Task description'}";
        assertEquals(expectedTask, actualTask, "Неверно загружаются задачи из файла!");

        String actualEpic = newManager.getEpic(2).toString();
        String expectedEpic = "Epic{id=2, name='Epic', status='NEW', description='Epic description', numOfSubtasks='1'}";
        assertEquals(expectedEpic, actualEpic, "Неверно загружаются эпики из файла!");

        String actualSubtask = newManager.getSubtask(3).toString();
        String expectedSubtask = "Subtask{id=3, name='Subtask', status='NEW', description='Subtask description', assignedEpicID='2'}";
        assertEquals(expectedSubtask, actualSubtask, "Неверно загружаются подзадачи из файла!");
    }
}
