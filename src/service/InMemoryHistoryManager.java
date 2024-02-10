package service;

import java.util.ArrayList;
import java.util.List;
import model.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> lastViewed;

    public InMemoryHistoryManager() {
        this.lastViewed = new ArrayList<>();
    }

    @Override
    public void add(Task task) {
        if (lastViewed.size() == 10) {
            lastViewed.removeFirst();
            lastViewed.add(task);
        } else {
            lastViewed.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return lastViewed;
    }

    @Override
    public void clearHistory() {
        lastViewed.clear();
    }
}
