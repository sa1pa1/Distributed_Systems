import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;

public class CalculatorClient {

    public static void main(String[] args) {
        try {
            OneClientTest();
            MultClientTest();

        } catch (Exception e) {
            e.printStackTrace();    
            
        }
    }

    private static void OneClientTest() throws Exception {
        Calculator stub = (Calculator) Naming.lookup("rmi://localhost:5100/calculator/Client1");

        // testing isEmpty(): TRUE
        System.out.println("begin stack isEmpty: " + stub.isEmpty());

        // testing pop()
        stub.pushValue(8);
        System.out.println("Pop: " + stub.pop());

        // reset
        stub.pushValue(0);

        // tests for pushValue and pushOperation on single client

        // pushOperation: max
        stub.pushValue(20);
        stub.pushValue(30);
        stub.pushOperation("max");
        System.out.println("Max: " + stub.pop());

        // pushOperation: min
        // testing min of same value
        stub.pushValue(10);
        stub.pushValue(10);
        stub.pushOperation("min");
        System.out.println("Min: " + stub.pop());

        // pushOperation: gcd
        stub.pushValue(20);
        stub.pushValue(40);
        stub.pushOperation("gcd");
        System.out.println("GCD: " + stub.pop());

        // pushOperation: lcm
        stub.pushValue(20);
        stub.pushValue(40);
        stub.pushOperation("lcm");
        System.out.println("LCM: " + stub.pop()); // should be 40

        stub.pushValue(50);
        System.out.println("before pop isEmpty: " + stub.isEmpty());
        System.out.println("Delayed pop: " + stub.delayPop(2000));

        // testing isEmpty(): FALSE
        System.out.println("after pop isEmpty: " + stub.isEmpty());
        System.out.println("END OF ONE CLIENT TEST");
    }

    private static void MultClientTest() {
        List<Thread> clients = new ArrayList<>();
        // Client 1, stable client
        String client1 = "Client1";
        Thread clientThread1 = new Thread(() -> {
            try {
                Calculator stub = (Calculator) Naming.lookup("rmi://localhost:5100/calculator/" + client1);

                // Test pushValue
                stub.pushValue(10);
                System.out.println(client1 + " pushed " + stub.pop());

                // Test pushOperation

                stub.pushValue(5);
                stub.pushOperation("max");
                System.out.println(client1 + " pushed operation 'max': " + stub.pop());

                // Test delayPop
                stub.pushValue(20);
                System.out.println(client1 + " delayed pop: " + stub.delayPop(1000));

            } catch (Exception e) {
                System.err.println(client1 + " exception: " + e.toString());
                e.printStackTrace();
            }
        });

        // Client 2 check that can pop and push values on two different servers
        String client2 = "Client2";
        Thread clientThread2 = new Thread(() -> {
            try {
                Calculator stub = (Calculator) Naming.lookup("rmi://localhost:5100/calculator/" + client2);

                // Test pop
                stub.pushValue(5);
                System.out.println(client2 + " pushed " + stub.pop());
                
                //test push operation
                stub.pushValue(5);
                stub.pushValue(28);
                stub.pushOperation("max");
                System.out.println(client2 + " pushed operation 'max': " +stub.pop());

            } catch (Exception e) {
                System.err.println(client2 + " exception: " + e.toString());
                e.printStackTrace();
            }
        });
        
        //client3: test empty stack
        String client3 = "Client3";
        Thread clientThread3 = new Thread(() -> {
            try {
                Calculator stub = (Calculator) Naming.lookup("rmi://localhost:5100/calculator/" + client3);

                // Test pop with no prior values on stack 
   
                System.out.println(client3 + " pushed " + stub.pop());
               
            } catch (Exception e) {
                System.err.println(client3 + " EMPTY STACK ERROR ");
                
            }
        });
        //client4: testing more than 3 clients running 
        String client4 = "Client4";
        Thread clientThread4 = new Thread(() ->{
        	try {
        		Calculator stub = (Calculator) Naming.lookup("rmi://localhost:5100/calculator/" + client4);
        		 // Test pushValue
                stub.pushValue(10);
                System.out.println(client4 + " pushed " + stub.pop());

                // Test pushOperation
               
                stub.pushValue(5);
                stub.pushOperation("max");
                System.out.println(client4 + " pushed operation 'max': " + stub.pop());

                // Test delayPop
                stub.pushValue(20);
                System.out.println(client4 + " delayed pop: " + stub.delayPop(1000));

        		
        	}catch (Exception e) {
                System.err.println(client4 + " exception: " + e.toString());
                e.printStackTrace();
            }
        });

        clients.add(clientThread1);
        clients.add(clientThread2);
        clients.add(clientThread3);
        clients.add(clientThread4);

        for (Thread client : clients) {
            client.start();
        }

        for (Thread client : clients) {
            try {
                client.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("END OF MULTIPLE CLIENT TEST");
    }
}
