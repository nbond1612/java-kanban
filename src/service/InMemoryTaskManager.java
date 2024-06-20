package service;

import model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasks;
    private final Map<Integer, Epic> epics;
    private final Map<Integer, Subtask> subtasks;

    private final HistoryManager historyManager;

    public InMemoryTaskManager() {
        this.historyManager = Managers.getDefaultHistory();
        this.tasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.subtasks = new HashMap<>();
    }

    private int seq = 0;

    private int generateId() {
        return ++seq;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        Epic epic = epics.get(subtask.getEpic());
        if (subtask.getId() == subtask.getEpic()) {
            return null;
        }
        epic.addSubTask(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        calculateEpicStatus(epic);
        return subtask;
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (tasks.containsKey(id)) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epics.containsKey(id)) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtasks.containsKey(id)) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic saved = epics.get(epic.getId());
            saved.setName(epic.getName());
            saved.setDescription(epic.getDescription());
            epics.put(saved.getId(), saved);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask saved = subtasks.get(subtask.getId());
            Epic savedEpic = epics.get(saved.getEpic());
            calculateEpicStatus(savedEpic);
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Subtask> getAllSubtasksInEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        for (int subtaskId: epic.getSubTasks()) {
            subtasksInEpic.add(subtasks.get(subtaskId));
        }
        subtasksInEpic.removeIf(Objects::isNull);
        return subtasksInEpic;
    }

    @Override
    public void deleteTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            historyManager.remove(id);
            Epic epic = epics.remove(id);
            for (int subtaskId : epic.getSubTasks()) {
                historyManager.remove(subtaskId);
                subtasks.remove(subtaskId);
            }
        }
    }

    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask removedSubtask = subtasks.remove(id);

            Epic epic = epics.get(removedSubtask.getEpic());

            historyManager.remove(id);
            epic.removeSubtask(id);
            calculateEpicStatus(epic);
        }
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for(Epic epic: epics.values()) {
            epic.setSubTasks(new ArrayList<>());
            calculateEpicStatus(epic);
        }
    }

    @Override
    public void calculateEpicStatus(Epic epic) {
        int newCounter = 0;
        int doneCounter = 0;

        ArrayList<Subtask> subtasksToCheck = getAllSubtasksInEpic(epic.getId());
        if (subtasksToCheck.isEmpty()) {
            epic.setStatus(TaskStatus.NEW);
        } else {
            for (Subtask subtask : subtasksToCheck) {
                if (subtask.getStatus() == TaskStatus.NEW) {
                    newCounter += 1;
                } else if (subtask.getStatus() == TaskStatus.DONE) {
                    doneCounter += 1;
                }
            }

            if (newCounter == subtasksToCheck.size()) {
                epic.setStatus(TaskStatus.NEW);
            } else if (doneCounter == subtasksToCheck.size()) {
                epic.setStatus(TaskStatus.DONE);
            } else {
                epic.setStatus(TaskStatus.IN_PROGRESS);
            }
        }
    }
}
