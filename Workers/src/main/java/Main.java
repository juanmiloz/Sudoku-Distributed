import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("input.txt"));
            MutableByte[][] initial = new MutableByte[9][9];
            String line;
            byte row = 0;
            while ((line = reader.readLine()) != null) {

                String temp[] = line.split(",");

                for (byte col = 0; col < temp.length; col++) {
                    int byteT = Integer.parseInt(temp[col]);
                    initial[row][col] = new MutableByte((byte) byteT);
                }
                row++;
            }

            reader.close();

            printMatrix(initial);

            sudokuSolverRAndP(initial);

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static void sudokuSolverRAndP(MutableByte[][] initialState) {

        Queue<Node> aliveNodes = new LinkedList<Node>();

        Node X = new Node();

        X.row = new MutableByte((byte) 0);
        X.col = new MutableByte((byte) 0);
        X.sol = initialState;

        aliveNodes.add(X);

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

    private static void addNewAliveNode(Queue<Node> aliveNodes, Node x) {
        Node y = new Node();
        if (x.row.value == 8 && x.col.value == 8) {
            printMatrix(x.sol);
        } else if (x.row.value < 8 && x.col.value == 8) {
            y.sol = Arrays.stream(x.sol).map(it -> Arrays.stream(it).toArray(MutableByte[]::new)).toArray(MutableByte[][]::new);
            y.row = new MutableByte((byte) (x.row.value+1));
            y.col = new MutableByte((byte) 0);
            aliveNodes.add(y);
        } else if (x.row.value <= 8 && x.col.value < 8) {
            y.sol = Arrays.stream(x.sol).map(it -> Arrays.stream(it).toArray(MutableByte[]::new)).toArray(MutableByte[][]::new);
            y.row = new MutableByte((x.row.value));
            y.col = new MutableByte((byte) (x.col.value+1));
            aliveNodes.add(y);
        }
    }

    private static boolean isValid(byte i, byte j, MutableByte[][] sol) {
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

    private static byte correspondence3x3(byte i) {
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

    private static boolean[][] initialize(MutableByte[][] initial) {

        boolean[][] response = new boolean[9][9];

        for (byte i = 0; i < 9; i++) {
            for (byte j = 0; j < 9; j++) {
                response[i][j] = initial[i][j].value != 0;
            }
        }

        return response;
    }

    private static void printMatrix(MutableByte[][] sol){
        System.out.println("-".repeat(20));
        Arrays.stream(sol).forEach(it -> System.out.println(Arrays.toString(it)));
        System.out.println("-".repeat(20));
    }

}

class Node {
    public MutableByte row, col;
    public MutableByte[][] sol;
}

class MutableByte {
    public byte value;

    public MutableByte(byte i) {
        value = Byte.parseByte(Byte.toString(i));
    }

    @Override
    public String toString() {
        return Byte.toString(value);
    }
}