import Sudoku.ControllerI;
import Sudoku.ControllerIPrx;
import Sudoku.PersistentQueueControllerI;
import Sudoku.PersistentQueueControllerIPrx;
import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;

import java.util.ArrayList;
import java.util.List;

public class Intermediary{

    public static void main(String[] args) {
        List<String> extraArgs = new ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "intermediary.cfg", extraArgs)) {

            if (!extraArgs.isEmpty()) {
                System.out.println("too many arguments");
                for (String v : args) {
                    System.out.println(v);
                }
            }
            ObjectAdapter adapter = communicator.createObjectAdapter("SolverQueue");
            PersistentQueueControllerI object = new PersistentQueueController();
            String identity = communicator.getProperties().getProperty("Identity");
            ObjectPrx objectPrx = adapter.add(object, Util.stringToIdentity(identity));
            PersistentQueueControllerIPrx persistentQueueControllerIPrx = PersistentQueueControllerIPrx
                    .uncheckedCast(objectPrx);
            adapter.activate();
            System.out.println("Server running queue...");
            communicator.waitForShutdown();
        }
    }
}
