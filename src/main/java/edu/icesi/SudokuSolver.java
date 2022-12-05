package edu.icesi;

import DataStructures.MutableByte;
import DataStructures.Node;
import DataStructures.PersistentQueue;
import interfaces.SudokuSolverI;

import java.util.Arrays;
import java.util.Queue;

public class SudokuSolver implements SudokuSolverI {

    private int numSolutions;

    public void sudokuSolverRAndP(MutableByte[][] initialState) {

        Queue<Node> aliveNodes = new PersistentQueue<>();

        Node X = new Node();

        X.row = new MutableByte((byte) 0);
        X.col = new MutableByte((byte) 0);
        X.sol = initialState;

        aliveNodes.offer(X);

        boolean[][] initial = initialize(initialState);

        while (!aliveNodes.isEmpty()) {
            X = aliveNodes.poll();
            if (!initial[X.row.value][X.col.value]) {
                for (byte k = 1; k < 10; k++) {
                    MutableByte temp = new MutableByte(X.sol[X.row.value][X.col.value].value);
                    X.sol[X.row.value][X.col.value] = new MutableByte(k);
                    if (isValid(X.row.value, X.col.value, X.sol)) {
                        addNewAliveNode(aliveNodes, X);
                    }
                    X.sol[X.row.value][X.col.value] = temp;
                }
            } else {
                addNewAliveNode(aliveNodes, X);
            }
        }
    }

    private void addNewAliveNode(Queue<Node> aliveNodes, Node x) {
        Node y = new Node();
        if (x.row.value == 8 && x.col.value == 8) {
            printMatrix(x.sol);
            numSolutions++;
        } else if (x.row.value < 8 && x.col.value == 8) {
            y.sol = Arrays.stream(x.sol).map(it -> Arrays.stream(it).toArray(MutableByte[]::new)).toArray(MutableByte[][]::new);
            y.row = new MutableByte((byte) (x.row.value + 1));
            y.col = new MutableByte((byte) 0);
            aliveNodes.offer(y);
        } else if (x.row.value <= 8 && x.col.value < 8) {
            y.sol = Arrays.stream(x.sol).map(it -> Arrays.stream(it).toArray(MutableByte[]::new)).toArray(MutableByte[][]::new);
            y.row = new MutableByte((x.row.value));
            y.col = new MutableByte((byte) (x.col.value + 1));
            aliveNodes.offer(y);
        }
    }

    private boolean isValid(byte i, byte j, MutableByte[][] sol) {
        boolean valid = true;
        byte k = 0, l;

        while (k < 9 && valid) {
            if (sol[i][j].value == sol[i][k].value && k != j) {
                valid = false;
            }
            k++;
        }

        k = 0;

        while (k < 9 && valid) {
            if (sol[i][j].value == sol[k][j].value && k != i) {
                valid = false;
            }
            k++;
        }

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

    private boolean[][] initialize(MutableByte[][] initial) {

        boolean[][] response = new boolean[9][9];

        for (byte i = 0; i < 9; i++) {
            for (byte j = 0; j < 9; j++) {
                response[i][j] = initial[i][j].value != 0;
            }
        }

        return response;
    }

    public void printMatrix(MutableByte[][] sol) {
        System.out.println("-".repeat(20));
        Arrays.stream(sol).forEach(it -> System.out.println(Arrays.toString(it)));
        System.out.println("-".repeat(20));
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
    public int solve(String sudoku) {
        numSolutions = 0;
        MutableByte[][] initial = parseString(sudoku);
        sudokuSolverRAndP(initial);
        return numSolutions;
    }
}

