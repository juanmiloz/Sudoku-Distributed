import interfaces.MatrixGeneratorI;
import interfaces.SudokuSolverI;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {


    public static void main(String[] args) {

        SudokuSolverI solver = new SudokuSolver();

        MatrixGeneratorI generator = new Matrix();

        try {
            String sudokuToSolve = generator.generateMatrix(20);
            System.out.println(sudokuToSolve);
            System.out.println(solver.solve(sudokuToSolve));

        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
