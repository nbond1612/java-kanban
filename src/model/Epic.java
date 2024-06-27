package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        setStatus(TaskStatus.NEW);
    }

    public Epic(String name, TaskStatus status, String description, int id) {
        super(name, status, description, id);
    }

    public ArrayList<Integer> getSubTasks() {
        return subtasks;
    }

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subtasks = subTasks;
    }

    public void addSubTask(int subtaskId) {
        if (subtaskId != this.getId()) {
            subtasks.add(subtaskId);
        }
    }

    public void removeSubtask(int subtaskId) {
        if (subtasks.contains(subtaskId)) {
            subtasks.remove(Integer.valueOf(subtaskId));
        }
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", numOfSubtasks='" + getSubTasks().size() + '\'' +
                '}';
    }
}
