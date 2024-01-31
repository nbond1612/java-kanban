package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private static HashMap<Integer, Task> tasks = new HashMap<>();
    private static HashMap<Integer, Epic> epics = new HashMap<>();
    private static HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private static int seq = 0;

    private int generateId() {
        return ++seq;
    }

    public Task create(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(generateId());
        Epic epic = getEpic(subtask.getEpic());
        epic.addSubTask(subtask.getId());
        subtasks.put(subtask.getId(), subtask);
        calculateEpicStatus(epic);
        return subtask;
    }

    public Task get(int id) {
        return tasks.get(id);
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void update(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic saved = epics.get(epic.getId());
            saved.setName(epic.getName());
            saved.setDescription(epic.getDescription());
            epics.put(saved.getId(), saved);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask saved = subtasks.get(subtask.getId());
            Epic savedEpic = epics.get(saved.getEpic());
            calculateEpicStatus(savedEpic);
        }
    }

    public ArrayList<Task> getAll() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Subtask> getAllSubtasksInEpic(int id) {
        Epic epic = getEpic(id);
        ArrayList<Subtask> subtasksInEpic = new ArrayList<>();
        for (int subtaskId: epic.getSubTasks()) {
            subtasksInEpic.add(getSubtask(subtaskId));
        }
        subtasksInEpic.removeIf(Objects::isNull);
        return subtasksInEpic;
    }

    public void delete(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            Epic epic = epics.remove(id);
            for (int subtaskId : epic.getSubTasks()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask removedSubtask = subtasks.remove(id);

            Epic epic = getEpic(removedSubtask.getEpic());
            Epic epicSaved = epics.get(epic.getId());

            epicSaved.removeSubtask(id);
            calculateEpicStatus(epic);
        }
    }

    public void deleteAll() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    public void deleteAllSubtasks() {
        subtasks.clear();
        for(Epic epic: epics.values()) {
            calculateEpicStatus(epic);
            epic.setSubTasks(new ArrayList<>());
        }
    }

    private void calculateEpicStatus(Epic epic) {
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
