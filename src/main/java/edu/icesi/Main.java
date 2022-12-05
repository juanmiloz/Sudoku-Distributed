package edu.icesi;

import interfaces.MatrixGeneratorI;
import interfaces.SudokuSolverI;

public class Main {


    public static void main(String[] args) {

        SudokuSolverI solver = new SudokuSolver();

        MatrixGeneratorI generator = new Matrix();

        try {
            String sudokuToSolve = generator.generateMatrix(4);
            System.out.println(sudokuToSolve);
            long startTime = System.currentTimeMillis();
            System.out.println("Start time: " + startTime);
            System.out.println(solver.solve(sudokuToSolve));
            long endTime = System.currentTimeMillis();
            System.out.println("End time: " + endTime);
            System.out.println("Time to solve: " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            System.err.println(e);
        }
    }

}
