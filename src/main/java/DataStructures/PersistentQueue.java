package DataStructures;

import interfaces.SudokuAliveNodesPersistenceI;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class PersistentQueue<Node> implements Queue<Node> {

    private static final long MAX_SIZE = 500000;

    private Queue<Node> queue;
    private final SudokuAliveNodesPersistenceI<Node> storage;


    public PersistentQueue() {
        queue = new LinkedList<>();
        storage = new SudokuAliveNodesPersistence<Node>();
    }

    @Override
    public int size() {
        return queue.size()+storage.numAliveNodesSaved();
    }

    @Override
    public boolean isEmpty() {
        return queue.isEmpty()&&!storage.checkAliveNodesSaved();
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }

    @Override
    public Iterator<Node> iterator() {
        return queue.iterator();
    }

    @Override
    public Object[] toArray() {
        return queue.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return queue.toArray(a);
    }

    @Override
    public boolean add(Node node) {
        if (queue.size() >= MAX_SIZE) {
            storage.saveAliveNodes(queue);
            queue = new LinkedList<>();
        }
        return queue.add(node);
    }

    @Override
    public boolean remove(Object o) {
        boolean response = queue.remove(o);
        if (queue.size()-1 == 0 && storage.checkAliveNodesSaved()) {
            queue = storage.loadAliveNodes();
        }
        return response;
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends Node> c) {
        if (queue.size() + c.size() >= MAX_SIZE) {
            storage.saveAliveNodes(queue);
            queue = new LinkedList<>();
        }
        return queue.addAll(c);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean response = queue.removeAll(c);
        if (queue.size()-c.size() == 0 && storage.checkAliveNodesSaved()) {
            queue = storage.loadAliveNodes();
        }
        return response;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public void clear() {
        queue.clear();
    }

    @Override
    public boolean equals(Object o) {
        return queue.equals(o);
    }

    @Override
    public int hashCode() {
        return queue.hashCode();
    }

    @Override
    public boolean offer(Node node) {
        if (queue.size() >= MAX_SIZE) {
            storage.saveAliveNodes(queue);
            queue = new LinkedList<>();
        }
        return queue.offer(node);
    }

    @Override
    public Node remove() {
        Node response = queue.remove();
        if (queue.size()-1 == 0 && storage.checkAliveNodesSaved()) {
            queue = storage.loadAliveNodes();
        }
        return response;
    }

    @Override
    public Node poll() {
        if (queue.size() == 0 && storage.checkAliveNodesSaved()) {
            queue = storage.loadAliveNodes();
        }
        return queue.poll();
    }

    @Override
    public Node element() {
        return queue.element();
    }

    @Override
    public Node peek() {
        return queue.peek();
    }
}
