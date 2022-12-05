package interfaces;

import java.util.Queue;

public interface SudokuAliveNodesPersistenceI<Node> {
    public boolean saveAliveNodes(Queue<Node> aliveNodes);

    public Queue<Node> loadAliveNodes();

    public boolean checkAliveNodesSaved();

}
