package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        setStatus(TaskStatus.NEW);
    }

    public ArrayList<Integer> getSubTasks() {
        return subtasks;
    }

    public void setSubTasks(ArrayList<Integer> subTasks) {
        this.subtasks = subTasks;
    }

    public void addSubTask(int subtaskId) {
        subtasks.add(subtaskId);
    }

    public void removeSubtask(int subtaskId) {
        if (subtasks.contains(subtaskId)) {
            subtasks.remove(Integer.valueOf(subtaskId));
        }
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
