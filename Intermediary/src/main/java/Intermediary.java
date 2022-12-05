import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import java.util.ArrayList;
import java.util.List;

public class Intermediary {

    public static void main(String[] args) {
       /* List<String> extraArgs = new ArrayList<String>();

        try (Communicator communicator = Util.initialize(args, "config.server", extraArgs)) {

            if (!extraArgs.isEmpty()) {
                System.out.println("too many arguments");
                for (String v : args) {
                    System.out.println(v);
                }
            }
            ObjectAdapter adapter = communicator.createObjectAdapter("Printer");
            //Object object = new PrinterI();
            String identity = communicator.getProperties().getProperty("Identity");
            //adapter.add(object, com.zeroc.Ice.Util.stringToIdentity("SimplePrinter"));
            adapter.activate();
            System.out.println("Server running ...");
            communicator.waitForShutdown();
        }*/
    }

}
