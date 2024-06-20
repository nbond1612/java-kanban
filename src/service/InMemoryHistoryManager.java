package service;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import model.*;

public class InMemoryHistoryManager implements HistoryManager {

    private static class Node {
        Task task;
        Node next;
        Node prev;

        public Node(Node prev, Task task, Node next) {
            this.task = task;
            this.next = next;
            this.prev = prev;
        }
    }

    HashMap<Integer, Node> history = new HashMap<>();
    Node first;
    Node last;

    @Override
    public void add(Task task) {
        if (task != null) {
            if (history.containsKey(task.getId())) {
                remove(task.getId());
            }
            history.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> list = new ArrayList<>();
        Node node = first;
        while (node != null) {
            list.add(node.task);
            node = node.next;
        }
        return list;
    }


    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            Node node = history.get(id);
            removeNode(node);
        }
    }

    private void removeNode(Node node) {
        history.remove(node.task.getId());
        Node prevNode = node.prev;
        Node nextNode = node.next;

        if (prevNode != null) {
            if (nextNode != null) {
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            } else {
                prevNode.next = null;
                last = prevNode;
            }
        } else {
            if (nextNode != null) {
                nextNode.prev = null;
                first = nextNode;
            } else {
                first = null;
                last = null;
            }
        }
        // prevNode.next = nextNode;
        // nextNode.prev = prevNode;
    }

    private Node linkLast(Task task) {
        final Node l = last;
        final Node newNode = new Node(l, task, null);
        last = newNode;
        if (l == null) {
            first = newNode;
        } else {
            l.next = newNode;
        }
        return newNode;
    }


}
