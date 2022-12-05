import com.zeroc.Ice.Current;

import java.nio.channels.UnresolvedAddressException;
import java.net.InetAddress;
public class SolverI implements Sudoku.Solver {

    @Override
    public void claimMatrix(Current current) {
        try {
            System.out.print("Yaaaa hpta ");
            InetAddress iaLocal;
            iaLocal = InetAddress.getLocalHost();
            String hostname = iaLocal.getHostName();
            System.out.println(hostname);
        }catch(java.net.UnknownHostException e){
            System.out.println("error");
        }
    }
}
