import Sudoku.Solver;
import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;

import java.util.ArrayList;
import java.util.List;

public class Worker{

    public static void main(String[] args) {
        List<String> extraArgs = new ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "worker.cfg", extraArgs)) {

            if (!extraArgs.isEmpty()) {
                System.out.println("too many arguments");
                for (String v : args) {
                    System.out.println(v);
                }
            }
            ObjectAdapter adapter = communicator.createObjectAdapter("ControllerI");
            Object object = new SolverI();
            String identity = communicator.getProperties().getProperty("Identity");
            adapter.add(object, Util.stringToIdentity(identity));
            adapter.activate();
            System.out.println("Server running worker...");
            communicator.waitForShutdown();
        }
    }
}
