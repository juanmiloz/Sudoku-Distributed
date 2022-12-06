package dataStructures;

import interfaces.SudokuAliveNodesPersistenceI;

import java.io.*;
import java.util.Queue;

public class SudokuAliveNodesPersistence<T> implements SudokuAliveNodesPersistenceI<T> {

    private static final String FILE_BASENAME = "data/aliveNodes_";
    private static final String FILE_EXTENSION = ".scgd";

    private int aliveNodesSaved;

    SudokuAliveNodesPersistence() {
        aliveNodesSaved = 0;
    }

    @Override
    public boolean saveAliveNodes(Queue<T> aliveNodes) {
        FileOutputStream file = null;
        try {
            System.out.println("Saving alive nodes to file...");
            long startTime = System.currentTimeMillis();

            increaseAliveNodesSaved();

            file = new FileOutputStream(FILE_BASENAME + aliveNodesSaved + FILE_EXTENSION);
            ObjectOutputStream out = new ObjectOutputStream(file);
            out.writeObject( aliveNodes);

            out.flush();
            file.close();

            System.out.println("Object has been serialized");
            long endTime = System.currentTimeMillis();
            System.out.println("Time to save alive nodes: " + (endTime - startTime) + "ms");
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Queue<T> loadAliveNodes() {

        if (aliveNodesSaved == 0) {
            throw new RuntimeException("No alive nodes saved");
        }

        // Method for deserialization of object
        Queue<T> queue = null;

        try {
            System.out.println("Loading alive nodes to app...");
            long startTime = System.currentTimeMillis();
            //TODO
            FileInputStream file = new FileInputStream(FILE_BASENAME + aliveNodesSaved + FILE_EXTENSION);
            ObjectInputStream in = new ObjectInputStream(file);
            queue = (Queue<T>) in.readObject();
            file.close();

            decreaseAliveNodesSaved();

            System.out.println("Object has been serialized");
            long endTime = System.currentTimeMillis();
            System.out.println("Time to load alive nodes: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return queue;
    }

    @Override
    public boolean checkAliveNodesSaved() {
        return aliveNodesSaved > 0;
    }

    @Override
    public int numAliveNodesSaved() {
        return aliveNodesSaved;
    }

    private void decreaseAliveNodesSaved() {
        aliveNodesSaved--;
    }

    private void increaseAliveNodesSaved() {
        aliveNodesSaved++;
    }
}
