import Sudoku.*;
import com.zeroc.Ice.Current;
import interfaces.MatrixGeneratorI;
import interfaces.PrinterI;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Controller implements ControllerI {

    private SolverPrx solverPrx;

    private MatrixGeneratorI matrixGenerator;

    private Iterator<PersistentQueueControllerIPrx> actualQueue;

    private ControllerIPrx proxy;

    private Stack<PersistentQueueControllerIPrx> usedQueues;

    private Queue<PersistentQueueControllerIPrx> unusedQueues;

    private long numSolutions;

    private boolean[][] initial;

    public Controller(SolverPrx solverPrx) {
        this.solverPrx = solverPrx;
        matrixGenerator = new Matrix();
        unusedQueues = new LinkedList<PersistentQueueControllerIPrx>();
        usedQueues = new Stack<>();
        numSolutions = 0;
    }


    public void solve() {
        System.out.print("Number de solutions: ");
        Node node = new Node();
        node.col = new MutableByte((byte) 0);
        node.row = new MutableByte((byte) 0);
        String sudokuToSolve = matrixGenerator.generateMatrix(25);
        MutableByte[][] sudoku = parseString(sudokuToSolve);
        node.sol = sudoku;
        initial = initialize(sudoku);
        solverPrx.solve(node, initial, proxy);
    }

    public void notifyFreeNode(Current current) {
        Node node = pollNode();
        if (node != null) {
            solverPrx.solve(node,initial,proxy);
        }
    }

    private void offerNode(Node node) {
        if (usedQueues.isEmpty()) {
            if (unusedQueues.isEmpty()) {
                throw new RuntimeException("there is not queue nodes");
            }
            usedQueues.push(unusedQueues.poll());
        }
        if (!usedQueues.peek().offerNode(node)) {
            if (!unusedQueues.isEmpty()) {
                usedQueues.push(unusedQueues.poll());
            }
        }
    }

    private Node pollNode() {
        Node node = usedQueues.peek().pollNode();
        if (usedQueues.peek().isEmpty()) {
            PersistentQueueControllerIPrx temp = usedQueues.pop();
            unusedQueues.offer(temp);
        }
        return node;
    }

    private boolean[][] initialize(MutableByte[][] initial) {

        boolean[][] response = new boolean[9][9];

        for (byte i = 0; i < 9; i++) {
            for (byte j = 0; j < 9; j++) {
                response[i][j] = initial[i][j].value != 0;
            }
        }

        return response;
    }

    private MutableByte[][] parseString(String sudoku) {
        MutableByte[][] response = new MutableByte[9][9];
        String[] rows = sudoku.split("\n");
        for (byte row = 0; row < 9; row++) {
            String[] cols = rows[row].split(",");
            for (byte col = 0; col < rows.length; col++) {
                int byteT = Integer.parseInt(cols[col]);
                response[row][col] = new MutableByte((byte) byteT);
            }
        }
        return response;
    }

    @Override
    public void addSolution(MutableByte[][] solution, Current current) {
        numSolutions ++;
        PrinterI printerI = new FilePrinter();
        printerI.printMatrix(solution);
        printerI = new ConsolePrinter();
        printerI.printMatrix(solution);
    }

    @Override
    public void addElementToQueue(Node element, Current current) {
        offerNode(element);
    }

    @Override
    public void registerQueueNode(PersistentQueueControllerIPrx cl, Current current) {
        unusedQueues.add(cl);
        notifyFreeNode(current);
    }

    public void initializeProxy(ControllerIPrx proxy) {
        this.proxy = proxy;
    }
}
