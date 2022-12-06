import Sudoku.ControllerIPrx;
import Sudoku.MutableByte;
import Sudoku.Node;
import com.zeroc.Ice.Current;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class SolverI implements Sudoku.Solver {

    private final ThreadPoolExecutor pool;

    public SolverI() {
        this.pool = (ThreadPoolExecutor) Executors.newFixedThreadPool(3);
    }

    @Override
    public boolean solve(Node sudoku, boolean[][] initialSudoku, ControllerIPrx controllerIPrx, Current current) {
        try {
            InetAddress iaLocal;
            iaLocal = InetAddress.getLocalHost();
            String hostname = iaLocal.getHostName();
            if (sudoku.sol.length == 9 && sudoku.sol[0].length == 9) {
                sudokuSolverRAndP(sudoku, initialSudoku, controllerIPrx);
                controllerIPrx.notifyFreeNode();
                return true;
            } else {
                return false;
            }
        } catch (java.net.UnknownHostException e) {
            System.out.println("error");
        }
        return false;
    }

    public void sudokuSolverRAndP(Node currentNode, boolean[][] initial, ControllerIPrx controllerIPrx) {
        if (!initial[currentNode.row.value][currentNode.col.value]) {
            for (byte possibleNumber = 1; possibleNumber < 10; possibleNumber++) {
                MutableByte temp = new MutableByte(currentNode.sol[currentNode.row.value][currentNode.col.value].value);
                currentNode.sol[currentNode.row.value][currentNode.col.value] = new MutableByte(possibleNumber);
                if (isValid(currentNode.row.value, currentNode.col.value, currentNode.sol)) {
                    addNewAliveNode(currentNode, controllerIPrx);
                }
                currentNode.sol[currentNode.row.value][currentNode.col.value] = temp;
            }
        } else {
            addNewAliveNode(currentNode, controllerIPrx);
        }

    }

    private void addNewAliveNode(Node x, ControllerIPrx controllerIPrx) {
        Node y = new Node();
        if (x.row.value == 8 && x.col.value == 8) {
            controllerIPrx.addSolution(x.sol);
        } else if (x.row.value < 8 && x.col.value == 8) {
            y.sol = Arrays.stream(x.sol).map(it -> Arrays.stream(it).toArray(MutableByte[]::new)).toArray(MutableByte[][]::new);
            y.row = new MutableByte((byte) (x.row.value + 1));
            y.col = new MutableByte((byte) 0);
            addElementToQueueRecursive(controllerIPrx, y);
        } else if (x.row.value <= 8 && x.col.value < 8) {
            y.sol = Arrays.stream(x.sol).map(it -> Arrays.stream(it).toArray(MutableByte[]::new)).toArray(MutableByte[][]::new);
            y.row = new MutableByte((x.row.value));
            y.col = new MutableByte((byte) (x.col.value + 1));
            addElementToQueueRecursive(controllerIPrx, y);
        }
    }

    private void addElementToQueueRecursive(ControllerIPrx controllerIPrx, Node y) {
        if (controllerIPrx.addElementToQueue(y))
            return;
        addElementToQueueRecursive(controllerIPrx, y);
    }

    private boolean isValid(byte i, byte j, MutableByte[][] sol) {
        List<Callable<Boolean>> todo = new ArrayList<Callable<Boolean>>(3);
        todo.add(new ValidateCol(sol, i, j));
        todo.add(new ValidateRow(sol, i, j));
        todo.add(new ValidateBlock(sol, i, j));

        try {
            List<Future<Boolean>> answers = pool.invokeAll(todo);
            for (Future<Boolean> answer : answers) {
                if (!answer.get()) {
                    return false;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return true;
    }
}

class ValidateCol implements Callable<Boolean> {

    private MutableByte[][] sol;
    private byte i;
    private byte j;

    public ValidateCol(MutableByte[][] sol, byte i, byte j) {
        this.sol = sol;
        this.i = i;
        this.j = j;
    }

    @Override
    public Boolean call() throws Exception {
        byte k = 0;
        boolean valid = true;
        while (k < 9 && valid) {
            if (sol[i][j].value == sol[k][j].value && k != i) {
                valid = false;
            }
            k++;
        }
        return valid;
    }
}

class ValidateRow implements Callable<Boolean> {

    private MutableByte[][] sol;
    private byte i;
    private byte j;

    public ValidateRow(MutableByte[][] sol, byte i, byte j) {
        this.sol = sol;
        this.i = i;
        this.j = j;
    }

    @Override
    public Boolean call() throws Exception {
        byte k = 0;
        boolean valid = true;
        while (k < 9 && valid) {
            if (sol[i][j].value == sol[i][k].value && k != j) {
                valid = false;
            }
            k++;
        }
        return valid;
    }
}

class ValidateBlock implements Callable<Boolean> {

    private MutableByte[][] sol;
    private byte i;
    private byte j;

    public ValidateBlock(MutableByte[][] sol, byte i, byte j) {
        this.sol = sol;
        this.i = i;
        this.j = j;
    }

    @Override
    public Boolean call() throws Exception {
        boolean valid = true;
        byte k = 0, l;
        k = correspondence3x3(i);

        while (k <= correspondence3x3(i) + 2 && valid) {
            l = correspondence3x3(j);
            while (l <= correspondence3x3(j) + 2 && valid) {
                if (sol[i][j].value == sol[k][l].value && i != k && j != l) {
                    valid = false;
                }
                l++;
            }
            k++;
        }
        return valid;
    }

    private byte correspondence3x3(byte i) {
        byte initialIndex;

        if (i >= 0 && i <= 2) {
            initialIndex = 0;
        } else if (i >= 3 && i <= 5) {
            initialIndex = 3;
        } else {
            initialIndex = 6;
        }

        return initialIndex;
    }
}
