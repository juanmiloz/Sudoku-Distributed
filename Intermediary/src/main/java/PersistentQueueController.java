import Sudoku.Node;
import com.zeroc.Ice.Current;
import Sudoku.PersistentQueueControllerI;
import dataStructures.PersistentQueue;

import java.util.Queue;

public class PersistentQueueController implements PersistentQueueControllerI{

    private Queue<Node> queue;


    public PersistentQueueController(){
        queue = new PersistentQueue<>();
    }

    public Node pollNode(Current current){
        return queue.poll();
    }

    public boolean offerNode(Node node, Current current){
        return queue.offer(node);
    }

    public boolean isEmpty(Current current){
        return queue.isEmpty();
    }

}
