import Sudoku.*;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.IceGrid.QueryPrx;
import interfaces.MatrixGeneratorI;
import interfaces.PrinterI;

import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class Controller implements ControllerI {

    private SolverPrx solverPrx;

    private final MatrixGeneratorI matrixGenerator;

    private ControllerIPrx proxy;

    private PersistentQueueControllerIPrx persistentQueueControllerIPrx;

    private String[] args;

    private final Communicator communicator;

    private ThreadPoolExecutor pool;

    private long numSolutions;

    private boolean[][] initial;

    public Controller(Communicator communicator) {
        this.communicator = communicator;
        releasePersistentControllerIPrx();
        releaseSolverPrx();
        matrixGenerator = new Matrix();
        pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();
        numSolutions = 0;
    }


    public void solve() {
        Node node = new Node();
        node.col = new MutableByte((byte) 0);
        node.row = new MutableByte((byte) 0);
        String sudokuToSolve = matrixGenerator.generateMatrix(25);
        MutableByte[][] sudoku = parseString(sudokuToSolve);
        node.sol = sudoku;
        initial = initialize(sudoku);

        if (recursiveSolve(node, initial, proxy)) {
            System.out.println("Something went wrong");
        }
    }

    private boolean recursiveSolve(Node node, boolean[][] initial, ControllerIPrx proxy) {
        if (!solverPrx.solve(node, initial, proxy))
            return false;
        recursiveSolve(node, initial, proxy);
        return true;
    }

    public void notifyFreeNode(Current current) {
        Node node = pollNode();
        if (node != null) {
            solverLauncher(node);
        }
    }

    private void solverLauncher(Node node) {
        pool.execute(() -> {
            releaseSolverPrx();
            if (!solverPrx.solve(node, initial, proxy)){
                offerNode(node);
                notifyFreeNode(null);
            }
        });
    }

    private void offerNode(Node node) {
        persistentQueueControllerIPrx.offerNode(node);
    }

    private Node pollNode() {
        return persistentQueueControllerIPrx.pollNode();
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
            String[] cols = rows[row].split(",", -1);
            for (byte col = 0; col < rows.length; col++) {
                if (cols[col].equals("")) {
                    response[row][col] = new MutableByte((byte) 0);
                } else {
                    response[row][col] = new MutableByte(Byte.parseByte(cols[col]));
                }
            }
        }
        return response;
    }

    @Override
    public void addSolution(MutableByte[][] solution, Current current) {
        numSolutions++;
        PrinterI printerI = new FilePrinter();
        printerI.printMatrix(solution);
        printerI = new ConsolePrinter();
        printerI.printMatrix(solution);
        System.out.println("Solutions found until now: " + numSolutions);
    }

    @Override
    public boolean addElementToQueue(Node element, Current current) {
        if (element == null) {
            return false;
        }

        System.out.println("Adding element to queue");
        PrinterI printerI = new ConsolePrinter();
        printerI.printMatrix(element.sol);

        offerNode(element);
        return true;
    }

    public void initializeProxy(ControllerIPrx proxy) {
        this.proxy = proxy;
    }


    public void releaseSolverPrx() {

        try {
            solverPrx = SolverPrx.checkedCast(communicator.stringToProxy("SudokuSolver"));
        } catch (Exception e) {
            QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("DemoIceGrid/Query"));
            solverPrx = SolverPrx.checkedCast(query.findObjectByType("::Sudoku::SolverI"));
        }

        if (solverPrx == null) {
            throw new Error("Invalid proxy");
        }


    }

    private void releasePersistentControllerIPrx() {

        try {
            persistentQueueControllerIPrx = PersistentQueueControllerIPrx.checkedCast(communicator.stringToProxy("PersistentQueue"));
        } catch (Exception e) {
            QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("DemoIceGrid/Query"));
            persistentQueueControllerIPrx = PersistentQueueControllerIPrx.checkedCast(query.findObjectByType("::Sudoku::PersistentQueueController"));
        }

        if (persistentQueueControllerIPrx == null) {
            throw new Error("Invalid queue proxy");
        }

    }
}
