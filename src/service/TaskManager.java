package service;

import model.*;

import java.util.ArrayList;
import java.util.HashMap;

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
        calculateEpicStatus(subtask.getEpic());
        subtasks.put(subtask.getId(), subtask);
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
        tasks.put(task.getId(), task);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
        epics.put(saved.getId(), saved);
    }

    public void updateSubtask(Subtask subtask) {
        Subtask saved = subtasks.get(subtask.getId());
        Epic savedEpic = epics.get(saved.getEpic().getId());
        calculateEpicStatus(savedEpic);
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
        return new ArrayList<>(getEpic(id).getSubTasks());
    }

    public void delete(int id) {
        tasks.remove(id);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        for (Subtask subtask: epic.getSubTasks()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }

    public void deleteSubtask(int id) {
        Subtask removedSubtask = subtasks.remove(id);

        Epic epic = removedSubtask.getEpic();
        Epic epicSaved = epics.get(epic.getId());

        epicSaved.getSubTasks().remove(removedSubtask);
        calculateEpicStatus(epic);
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
        }
    }

    private void calculateEpicStatus(Epic epic) {
        int newCounter = 0;
        int doneCounter = 0;

        ArrayList<Subtask> subtasksToCheck = epic.getSubTasks();
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
