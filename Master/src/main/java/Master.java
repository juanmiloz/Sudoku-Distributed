import Sudoku.ControllerIPrx;
import Sudoku.SolverPrx;
import com.zeroc.Ice.*;
import com.zeroc.IceGrid.QueryPrx;

import java.lang.Exception;

public class Master {

    public static void main(String[] args) {
        java.util.List<String> extraArgs = new java.util.ArrayList<>();

        try (Communicator communicator = Util.initialize(args, "master.cfg", extraArgs)) {
            //com.zeroc.Ice.ObjectPrx base = communicator.stringToProxy("SimplePrinter:default -p 10000");
            SolverPrx twoway = null;
            try {
                twoway = SolverPrx.checkedCast(communicator.stringToProxy("SudokuSolver"));
            } catch (Exception e) {
                QueryPrx query = QueryPrx.checkedCast(communicator.stringToProxy("DemoIceGrid/Query"));
                twoway = SolverPrx.checkedCast(query.findObjectByType("::Sudoku::SolverI"));
            }

            if (twoway == null) {
                throw new Error("Invalid proxy");
            }

            ObjectAdapter adapter = communicator.createObjectAdapter("ControllerI");
            Controller master = new Controller(twoway);
            String identity = communicator.getProperties().getProperty("Identity");

            ObjectPrx objPrx = adapter.add(master, Util.stringToIdentity("ControllerI"));
            ControllerIPrx controllerIPrx = ControllerIPrx.uncheckedCast(objPrx);
            master.initializeProxy(controllerIPrx);

            master.solve();
            adapter.activate();
        }
    }
}