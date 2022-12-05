package edu.icesi;

import DataStructures.MutableByte;
import interfaces.PrinterI;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;

public class FilePrinter implements PrinterI {

    FileOutputStream fos;
    OutputStreamWriter osw;

    public FilePrinter() {
        try {
            fos = new FileOutputStream("data/solution.txt");
            osw = new OutputStreamWriter(fos);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void printMatrix(MutableByte[][] sol) {
        try {
            for (MutableByte[] it : sol) {
                fos.write(Arrays.toString(it).getBytes());
                fos.write("\n".getBytes());
            }
            fos.write("-".repeat(20).getBytes());
            fos.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
