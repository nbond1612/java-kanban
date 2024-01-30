package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description);
        setStatus(TaskStatus.NEW);
    }

    public ArrayList<Subtask> getSubTasks() {
        return subtasks;
    }

    public void setSubTasks(ArrayList<Subtask> subTasks) {
        this.subtasks = subTasks;
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
