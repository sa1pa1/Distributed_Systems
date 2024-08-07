// package assignment1;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class CalculatorServer {
    public static void main(String args[]) throws RemoteException {
	try {
		 Registry reg = LocateRegistry.createRegistry(1099);
		 reg.rebind("calculator", new CalculatorImplementation());
		
        Calculator stub = new CalculatorImplementation();
        Naming.rebind("localhost", stub);

	    System.err.println("Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}