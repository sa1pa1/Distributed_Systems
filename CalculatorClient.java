// package assignment1;
import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class CalculatorClient {

    public static void main(String[] args) {
        try {
            
         
		Calculator stub = (Calculator) Naming.lookup("rmi://localhost:1099/calculator");
            		
		OneClientTest(stub);
		MultClientTest();

        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        
    }
}


		private static void OneClientTest(Calculator stub) throws Exception {
			//testing isEmpty(): TRUE
			System.out.println("Empty: " + stub.isEmpty());
			//testing pop()
			stub.pushValue(8);
			System.out.println("Pop: " + stub.pop());
			
			//reset
			stub.pushValue(0);

			//tests for pushValue and pushOperation on single client
			
			//pushOperation: max
			stub.pushValue(20);
            stub.pushValue(30);
            stub.pushOperation("max");
            System.out.println("Max: " + stub.pop()); 
            
            //pushOperation: min
            //testing min of same value 
            stub.pushValue(10);
            stub.pushValue(10);
            stub.pushOperation("min");
            System.out.println("Min: " + stub.pop());
            
            //pushOperation: gcd
            stub.pushValue(20);
            stub.pushValue(40);
            stub.pushOperation("gcd");
            System.out.println("GCD: " + stub.pop());
            
          //pushOperation: lcd
            stub.pushValue(20);
            stub.pushValue(40);
            stub.pushOperation("lcm");
            System.out.println("LCM: " + stub.pop()); //should be 40 
            System.out.println("Empty: " + stub.isEmpty());
            stub.pushValue(50);
            
            System.out.println("Delayed pop: " + stub.delayPop(2000));
            
          //testing isEmpty(): FALSE
			System.out.println("Empty: " + stub.isEmpty());
			System.out.println("END OF ONE CLIENT TEST");
            

            
            
			
		}
		//testing more than 3 clients
		private static void MultClientTest() {
	        List<Thread> clients = new ArrayList<>();
	        for (int i = 0; i < 3; i++) {
	        	String clientID = "Client" + (i+1);
	            Thread clientThread = new Thread(() -> {
	                try {
	                    Calculator stub = (Calculator) Naming.lookup("rmi://localhost:1099/calculator");

	                    // Test pushValue
	                    stub.pushValue(10);
	                    System.out.println(clientID + " pushed 10");

	                    // Test pushOperation
	                    
	                    stub.pushValue(5);
	                    stub.pushOperation("min");
	                    System.out.println(clientID+" pushed operation 'min':" + stub.pop());

	                
	                    // Test delayPop
	                    stub.pushValue(20);
	                    System.out.println(clientID+" delayed pop: " + stub.delayPop(1000));

	                } catch (Exception e) {
	                    System.err.println(clientID+" exception: " + e.toString());
	                    e.printStackTrace();
	                }
	            });
	            clients.add(clientThread);
	            clientThread.start();
	        }

	        
	        for (Thread clientThread : clients) {
	            try {
	                clientThread.join();
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }
	        }
	    }
		}