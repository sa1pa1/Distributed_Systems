import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class CalculatorServer {
    public static void main(String[] args) throws RemoteException {
	try {
		 Registry reg = LocateRegistry.createRegistry(5100);
         //for demonstration, allow up to five clients open 
		 for (int i = 1; i <= 5; i++) {
             String clientID = "calculator/Client" + i;
             Calculator stub = new CalculatorImplementation();
             reg.rebind(clientID, stub);
             System.out.println(clientID + " is ready.");
         }

	    System.err.println("Server ready");
	} catch (Exception e) {
	    System.err.println("Server exception: " + e.toString());
	    e.printStackTrace();
	}
    }
}