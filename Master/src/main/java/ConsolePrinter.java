import Sudoku.MutableByte;
import interfaces.PrinterI;

import java.util.Arrays;

public class ConsolePrinter implements PrinterI {

    @Override
    public void printMatrix(MutableByte[][] sol) {
        System.out.println("-".repeat(20));
        Arrays.stream(sol).forEach(it -> System.out.println(Arrays.toString(Arrays.stream(it).map(itD2->itD2.value).toArray())));
        System.out.println("-".repeat(20));
    }
}
