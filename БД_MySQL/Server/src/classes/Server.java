package classes;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * This is the <b>main</b> class of programm
 * @author Dashchynski Nikita
 * @version 1.0
 */
public class Server{
    /**
     * The enter point into the programm
     * @param args comes from command line
     */
    public static void main(String[] args) {
        //String localhost = "10.150.3.228";
        String localhost = "127.0.0.1";
        String RMI_HOSTNAME = "java.rmi.server.hostname";
        try {
            System.setProperty(RMI_HOSTNAME, localhost);

            WorkersServ workersServ = new WorkersServ();
            //регистрация
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("Workers", workersServ);
            System.out.println("Start Workers");
        } catch (RemoteException e) {
            System.err.println("RemoteException : "+e.getMessage());
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Exception : " + e.getMessage());
            System.exit(2);
        }
    }
}