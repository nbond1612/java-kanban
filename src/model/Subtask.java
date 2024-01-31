package model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, TaskStatus status, String description, int epicId) {
        super(name, status, description);
        this.epicId = epicId;
    }

    public int getEpic() {
        return epicId;
    }

    public void setEpic(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", assignedEpicID='" + epicId + '\'' +
                '}';
    }
}
