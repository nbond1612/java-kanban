package model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, TaskStatus status, String description, int epicId) {
        super(name, status, description);
        this.epicId = epicId;
    }

    public Subtask(String name, TaskStatus status, String description, int epicId, int id) {
        super(name, status, description, id);
        this.epicId = epicId;
        setId(id);
    }

    @Override
    public int getEpic() {
        return epicId;
    }

    public void setEpic(int epicId) {
        if (epicId != this.getId()) {
            this.epicId = epicId;
        }
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
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
