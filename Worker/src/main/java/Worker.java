import Sudoku.SolverI;
import com.zeroc.Ice.Object;

import java.util.ArrayList;
import java.util.List;

public class Worker implements SolverI{

    public static void main(String[] args) {
        List<String> extraArgs = new ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "Worker.cfg", extraArgs)) {

            if (!extraArgs.isEmpty()) {
                System.out.println("too many arguments");
                for (String v : args) {
                    System.out.println(v);
                }
            }
            ObjectAdapter adapter = communicator.createObjectAdapter("SolverI");
            com.zeroc.Ice.Object object = new Solver();
            String identity = communicator.getProperties().getProperty("Identity");
            adapter.add(object, Util.stringToIdentity(identity));
            adapter.activate();
            System.out.println("Server running ...");
            communicator.waitForShutdown();
        }
    }

    @Override
    public void callMatrix(Current current) {

    }
}
