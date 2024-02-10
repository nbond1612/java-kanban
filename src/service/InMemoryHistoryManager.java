package service;

import java.util.ArrayList;
import model.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final ArrayList<Task> lastViewed = new ArrayList<>();

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
    public ArrayList<Task> getHistory() {
        return lastViewed;
    }

    @Override
    public void clearHistory() {
        lastViewed.clear();
    }
}
