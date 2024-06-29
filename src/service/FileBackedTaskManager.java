package service;

import model.*;

import java.nio.file.Files;
import java.util.List;
import java.util.ArrayList;
import java.io.*;


public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    private static final String FIRST_LINE = "id,type,name,status,description,epic;";

    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    private void save() {
        List<String> tasksList = new ArrayList<>();

        if (!tasks.isEmpty()) {
            for (Task task: tasks.values()) {
                tasksList.add(taskToString(task));
            }
        }
        if (!epics.isEmpty()) {
            for (Epic task: epics.values()) {
                tasksList.add(taskToString(task));
            }
        }
        if (!subtasks.isEmpty()) {
            for (Subtask task: subtasks.values()) {
                tasksList.add(taskToString(task));
            }
        }

        try (FileWriter fw = new FileWriter(file)) {
            fw.write(FIRST_LINE);
            for (String task: tasksList) {
                fw.write(task);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e);
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws IOException {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);

        String fileText = Files.readString(file.toPath());
        String[] taskArray = fileText.split(";");

        int lastId = 0;
        if (taskArray.length > 1) {
            for (int i = 1; i < taskArray.length; i++) {
                String taskString = taskArray[i];
                String[] str = taskString.split(",");

                if (Integer.parseInt(str[0]) > lastId) {
                    lastId = Integer.parseInt(str[0]);
                }

                Task task = stringToTask(taskString);
                switch (task.getType()) {
                    case TaskType.TASK:
                        manager.tasks.put(task.getId(), task);
                        break;
                    case TaskType.EPIC:
                        manager.epics.put(task.getId(), (Epic)task);
                        break;
                    default:
                        manager.subtasks.put(task.getId(), (Subtask)task);
                }
            }
        }
        manager.seq = lastId;

        for (Subtask subtask: manager.subtasks.values()) {
            final Epic epic = manager.epics.get(subtask.getEpic());
            epic.addSubTask(subtask.getId());
        }

        return manager;
    }

    private <T extends Task> String taskToString(T task) {
        String str = task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," + task.getDescription();
        if (task.getType() == TaskType.SUBTASK) {
            str += "," + task.getEpic();
        }
        str += ";";
        return str;
    }

    @SuppressWarnings("unchecked")
    private static <T extends Task> T stringToTask(String text) {
        String[] str = text.split(",");
        final TaskType type = TaskType.valueOf(str[1]);
        return switch (type) {
            case TaskType.TASK -> (T)(new Task(str[2], TaskStatus.valueOf(str[3]), str[4], Integer.parseInt(str[0])));
            case TaskType.EPIC -> (T)(new Epic(str[2], TaskStatus.valueOf(str[3]), str[4], Integer.parseInt(str[0])));
            default -> (T)(new Subtask(str[2], TaskStatus.valueOf(str[3]), str[4], Integer.parseInt(str[5]), Integer.parseInt(str[0])));
        };
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}
