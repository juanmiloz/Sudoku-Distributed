import Sudoku.ControllerI;
import Sudoku.ControllerIPrx;
import Sudoku.PersistentQueueControllerIPrx;
import Sudoku.SolverPrx;
import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;
import com.zeroc.IceGrid.QueryPrx;

import java.lang.Exception;

public class Master {

    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (Communicator communicator = Util.initialize(args, "master.cfg", extraArgs)) {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");

            ObjectAdapter adapter = communicator.createObjectAdapter("ControllerI");
            Controller controller = new Controller(communicator);
            ObjectPrx objectPrx = adapter.add(controller, Util.stringToIdentity("ControllerI"));
            ControllerIPrx controllerIPrx = ControllerIPrx.uncheckedCast(objectPrx);
            adapter.activate();
            System.out.println("Server running Client/Master...");

            controller.initializeProxy(controllerIPrx);

            controller.solve();

            communicator.waitForShutdown();
        }
    }
}