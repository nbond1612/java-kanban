import service.InMemoryHistoryManager;
import service.InMemoryTaskManager;
import service.TaskManager;
import model.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("Проверка.");
        System.out.println();

        TaskManager taskManager = new InMemoryTaskManager(new InMemoryHistoryManager());
        Task task1 = taskManager.createTask(new Task("Задача 1", TaskStatus.NEW, "Описание"));
        Task task2 = taskManager.createTask(new Task("Задача 2", TaskStatus.IN_PROGRESS, "Описание"));

        Epic epic1 = taskManager.createEpic(new Epic("Эпик 1", "Описание"));
        Subtask subtask1 = taskManager.createSubtask(new Subtask("Подзадача 1", TaskStatus.NEW, "Описание", epic1.getId()));
        Subtask subtask2 = taskManager.createSubtask(new Subtask("Подзадача 2", TaskStatus.DONE, "Описание", epic1.getId()));

        Epic epic2 = taskManager.createEpic(new Epic("Эпик 2", "Описание"));
        Subtask subtask3 = taskManager.createSubtask(new Subtask("Подзадача 3", TaskStatus.NEW, "Описание", epic2.getId()));

        Epic epic3 = taskManager.createEpic(new Epic("Пустой эпик", "Описание"));

        System.out.println("Задачи: ");
        System.out.println(taskManager.getAllTasks());

        System.out.println("Эпики:");
        System.out.println(taskManager.getAllEpics());

        System.out.println("Подзадачи: ");
        System.out.println(taskManager.getAllSubtasks());

        System.out.println();
        Task taskFromManager = taskManager.getTask(task1.getId());
        System.out.println("Получили задачу 1: " + taskFromManager);
        taskFromManager.setStatus(TaskStatus.DONE);
        taskManager.updateTask(taskFromManager);
        System.out.println("Обновили задачу 1: " + taskManager.getTask(task1.getId()));

        System.out.println();
        System.out.println("Проверка вывода истории:");
        System.out.println(taskManager.getHistory());

        System.out.println();
        System.out.println("Получим все подзадачи эпика 1: " + taskManager.getAllSubtasksInEpic(epic1.getId()));
        Subtask subtaskFromManager = taskManager.getSubtask(subtask1.getId());
        System.out.println("Получили подзадачу 1: " + subtaskFromManager);
        subtaskFromManager.setStatus(TaskStatus.DONE);
        taskManager.updateSubtask(subtaskFromManager);
        System.out.println("Обновили подзадачу 1: " + taskManager.getSubtask(subtask1.getId()));
        System.out.println("Проверяем статус эпика: " + taskManager.getEpic(subtask1.getEpic()));

        System.out.println();
        taskManager.deleteTask(task1.getId());
        System.out.println("Удалили задачу 1: " + task1);
        System.out.println("Проверяем список задач:" + taskManager.getAllTasks());

        System.out.println();
        taskManager.deleteSubtask(subtask1.getId());
        System.out.println("Удалили подзадачу 1: " + subtask1);
        System.out.println("Проверяем список подзадач: " + taskManager.getAllSubtasks());
        System.out.println("Проверяем список подзадач эпика 1: " + taskManager.getAllSubtasksInEpic(subtask1.getEpic()));

        System.out.println();
        taskManager.deleteEpic(epic1.getId());
        System.out.println("Удалили эпик 1: " + epic1);
        System.out.println("Проверяем список эпиков: " + taskManager.getAllEpics());
        System.out.println("Проверяем список подзадач: " + taskManager.getAllSubtasks());

        System.out.println();
        System.out.println("Проверка вывода истории:");
        System.out.println(taskManager.getHistory());
    }
}
