import Sudoku.MatrixGeneratorI;
import Sudoku.MatrixGeneratorIPrx;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.Util;
import com.zeroc.IceGrid.QueryPrx;

public class Matrix implements MatrixGeneratorI {

    private final String ANSI_GREEN = "\u001B[32m";
    private final String ANSI_RESET = "\u001B[0m";

    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (Communicator communicator = Util.initialize(args, "Master.cfg", extraArgs)) {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            MatrixGeneratorIPrx twoway = null;
            try {
                twoway = MatrixGeneratorIPrx.checkedCast(communicator.stringToProxy("GenerateMatrix"));
                //twoway = MatrixGeneratorIPrx.checkedCast(communicator.propertyToProxy("MatrixGenerator.Proxy")).ice_twoway().ice_secure(false);
            } catch (Exception e) {
                QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("DemoIceGrid/Query"));
                twoway = MatrixGeneratorIPrx.checkedCast(query.findObjectByType("::Sudoku::MatrixGenerator"));
            }

            if(twoway == null){
                throw new Error("Invalid proxy");
            }
            //Sudoku.MatrixGeneratorIPrx matrixGenerator = twoway.ice_twoway();
            twoway.generateStage(10);
        }
    }

    @Override
    public String generateStage(int quantityNumbers, Current current) {
        int[][] matrix = new int[9][9];

        for (int i = 0; i < quantityNumbers; i++) {
            boolean add = false;
            int ranCol = 0;
            int ranRow = 0;
            int ranNum = 0;

            while (!add) {
                ranCol = (int) (Math.random() * 9);
                ranRow = (int) (Math.random() * 9);
                ranNum = (int) (Math.random() * 9 + 1);

                if (matrix[ranRow][ranCol] == 0) {
                    boolean col = evaluateColumn(matrix, ranCol, ranNum);
                    boolean row = evaluateRow(matrix, ranRow, ranNum);
                    boolean square = evaluateSquare(matrix, ranRow, ranCol, ranNum);

                    add = (col && row && square);
                }
            }
            matrix[ranRow][ranCol] = ranNum;
        }
        return printMatrix(matrix);
    }

    private boolean evaluateSquare(int[][] matrix, int row, int col, int num) {
        int minRow;
        int maxRow;
        int minCol;
        int maxCol;

        if (row >= 0 && row <= 2) {
            minRow = 0;
            maxRow = 2;
        } else if (row >= 3 && row <= 5) {
            minRow = 3;
            maxRow = 5;
        } else {
            minRow = 6;
            maxRow = 8;
        }

        if (col >= 0 && col <= 2) {
            minCol = 0;
            maxCol = 2;
        } else if (col >= 3 && col <= 5) {
            minCol = 3;
            maxCol = 5;
        } else {
            minCol = 6;
            maxCol = 8;
        }

        for (int i = minRow; i <= maxRow; i++) {
            for (int j = minCol; j <= maxCol; j++) {
                if (matrix[i][j] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean evaluateRow(int[][] matrix, int row, int num) {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[row][i] == num) {
                return false;
            }
        }
        return true;
    }

    private boolean evaluateColumn(int[][] matrix, int col, int num) {
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i][col] == num) {
                return false;
            }
        }
        return true;
    }

    public String printMatrix(int[][] matrix) {
        String answer = "";

        for (int i = 0; i < matrix.length; i++) {
            if (i != 0 && i % 3 == 0) {
                answer += "=======================================";
            }
            for (int j = 0; j < matrix[i].length; j++) {
                if (j != 0 && j % 3 == 0) {
                    String output = " || ";
                    int num = matrix[i][j];
                    output += (num != 0) ? ANSI_GREEN + num + ANSI_RESET : num;
                    answer += output;
                } else {
                    String output = " | ";
                    int num = matrix[i][j];
                    output += (num != 0) ? ANSI_GREEN + num + ANSI_RESET : num;
                    answer += output;
                }
                if (j == 8) {
                    answer += " |\n";
                }
            }
        }
        return answer;
    }
}