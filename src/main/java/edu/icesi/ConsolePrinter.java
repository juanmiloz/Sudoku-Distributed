package edu.icesi;

import DataStructures.MutableByte;
import interfaces.PrinterI;

import java.util.Arrays;

public class ConsolePrinter implements PrinterI {

    @Override
    public void printMatrix(MutableByte[][] sol) {
        System.out.println("-".repeat(20));
        Arrays.stream(sol).forEach(it -> System.out.println(Arrays.toString(it)));
        System.out.println("-".repeat(20));
    }
}
