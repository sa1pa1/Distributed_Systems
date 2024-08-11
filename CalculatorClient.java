import java.rmi.Naming;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CalculatorClient {

    public static void main(String[] args) {
        try {
            OneClientTest(); //testing edge cases of functions
            MultipleClientTest(); //multiple client stacks implemented

        } catch (Exception e) {
            System.err.println("Main exception: " + e.getMessage());
        }
    }


    private static void OneClientTest() {
        try{
        Calculator stub = (Calculator) Naming.lookup("rmi://localhost:5100/calculator/Client1");
        System.out.println("START ONE CLIENT TEST");
        // testing isEmpty(): TRUE
        System.out.println("begin stack isEmpty: " + stub.isEmpty()+"\n");
        // testing pop()
        stub.pushValue(8);
        System.out.println("Push test 1: positive value");
        System.out.println("Client pushed 8");
        System.out.println("Pop " + stub.pop()+"\n");

        stub.pushValue(-5);
        System.out.println("Push test 2: negative value");
        System.out.println("Client pushed -5");
        System.out.println("Pop " + stub.pop()+"\n");


        // tests for pushValue and pushOperation on single client
        // pushOperation: max
        System.out.println("Max test 1: same value");
        System.out.println("Client pushed 10 twice");
        stub.pushValue(10);
        stub.pushValue(10);
        stub.pushOperation("max");
        System.out.println("Max: " + stub.pop()+"\n");

        System.out.println("Max test 2: two positive values");
        System.out.println("Client pushed 10 and 20");
        stub.pushValue(10);
        stub.pushValue(20);
        stub.pushOperation("max");
        System.out.println("Max: " + stub.pop()+"\n");

        System.out.println("Max test 3: positive and negative values");
        System.out.println("Client pushed -10 and 20");
        stub.pushValue(-10);
        stub.pushValue(20);
        stub.pushOperation("max");
        System.out.println("Max: " + stub.pop()+"\n");

        // pushOperation: min
        // testing min of same value
        System.out.println("Min test 1: same value");
        System.out.println("Client pushed 10 twice");
        stub.pushValue(10);
        stub.pushValue(10);
        stub.pushOperation("min");
        System.out.println("Min: " + stub.pop()+"\n");

        System.out.println("Min test 2: two positive values");
        System.out.println("Client pushed 10 and 20");
        stub.pushValue(10);
        stub.pushValue(20);
        stub.pushOperation("min");
        System.out.println("Min: " + stub.pop()+"\n");

        System.out.println("Min test 3: positive and negative values");
        System.out.println("Client pushed -10 and 20");
        stub.pushValue(-10);
        stub.pushValue(20);
        stub.pushOperation("min");
        System.out.println("Min: " + stub.pop()+"\n");

        // pushOperation: gcd
        System.out.println("GCD test 1: same value");
        System.out.println("Client pushed 10 twice");
        stub.pushValue(10);
        stub.pushValue(10);
        stub.pushOperation("gcd");
        System.out.println("GCD: " + stub.pop()+"\n");

        System.out.println("GCD test 2: positive and zero");
        System.out.println("Client pushed 0 and 20");
        stub.pushValue(0);
        stub.pushValue(20);
        stub.pushOperation("gcd");
        System.out.println("GCD: " + stub.pop()+"\n");

        System.out.println("GCD test 3: negative and zero");
        System.out.println("Client pushed 0 and -20");
        stub.pushValue(0);
        stub.pushValue(-20);
        stub.pushOperation("gcd");
        System.out.println("GCD: " + stub.pop()+"\n");

        System.out.println("GCD test 4: Two prime numbers");
        System.out.println("Client pushed 13 and 17");
        stub.pushValue(13);
        stub.pushValue(17);
        stub.pushOperation("gcd");
        System.out.println("GCD: " + stub.pop()+"\n");


        System.out.println("GCD test 5: positive and negative values");
        System.out.println("Client pushed -5 and 25");
        stub.pushValue(-5);
        stub.pushValue(25);
        stub.pushOperation("gcd");
        System.out.println("GCD: " + stub.pop()+"\n");


        // pushOperation: lcm
        System.out.println("LCM test 1: same value");
        System.out.println("Client pushed 10 twice");
        stub.pushValue(10);
        stub.pushValue(10);
        stub.pushOperation("lcm");
        System.out.println("LCM: " + stub.pop()+"\n");

        System.out.println("LCM test 2: positive and zero");
        System.out.println("Client pushed 0 and 20");
        stub.pushValue(0);
        stub.pushValue(20);
        stub.pushOperation("lcm");
        System.out.println("LCM: " + stub.pop()+"\n");

        System.out.println("LCM test 3: negative and zero");
        System.out.println("Client pushed 0 and -20");
        stub.pushValue(0);
        stub.pushValue(-20);
        stub.pushOperation("lcm");
        System.out.println("LCM: " + stub.pop()+"\n");

        System.out.println("LCM test 4: prime numbers");
        System.out.println("Client pushed 0 and 20");
        stub.pushValue(13);
        stub.pushValue(211);
        stub.pushOperation("lcm");
        System.out.println("LCM: " + stub.pop() +"\n");

        System.out.println("LCM test 5: value against 1");
        System.out.println("Client pushed 0 and 20");
        stub.pushValue(1);
        stub.pushValue(20);
        stub.pushOperation("lcm");
        System.out.println("LCM: " + stub.pop());

        stub.pushValue(50);
        System.out.println("before pop isEmpty: " + stub.isEmpty()+"\n");

        System.out.println("client pushed 50");
        System.out.println("Delayed pop: " + stub.delayPop(2000));

        // testing isEmpty(): FALSE
        System.out.println("after pop isEmpty: " + stub.isEmpty()+"\n");

        System.out.println("Testing negative input for delay pop.\nClient input -1");
        System.out.println("Delayed pop: " + stub.delayPop(-1));


    }catch (Exception e) {
            System.err.println( "Input error: " + e.getMessage());
        }
        System.out.println("END OF ONE CLIENT TEST\n");
    }



    private static Thread createClientThread(String clientID, Consumer<Calculator> action) {
        return new Thread(() -> {
            try {
                Calculator stub = (Calculator) Naming.lookup("rmi://localhost:5100/calculator/" + clientID);
                action.accept(stub);
            } catch (Exception e) {
                System.err.println(clientID + " exception: " + e.getMessage());
            }
        });
    }
    private static void MultipleClientTest() {
        List<Thread> clients = new ArrayList<>();
        System.out.println("START MULTIPLE CLIENT TEST");
        clients.add(createClientThread("Client1", stub -> {
            String client1 = "Client1";
            try {
                // Test pushValue
                stub.pushValue(10);
                System.out.println( client1 + " pushed 10, pop " + stub.pop());

                // Test pushOperation
                stub.pushValue(5);
                stub.pushOperation("max");
                System.out.println(client1 + " has 5 on stack");
                System.out.println(client1+ " pushed operation 'max': " + stub.pop()+"\n");

                // Test delayPop
                stub.pushValue(20);
                System.out.println(client1 +" delayed pop: " + stub.delayPop(1000));


            } catch (Exception e) {
                System.err.println(client1 +" exception " + e.getMessage());
            }
        }));

        // Client 2 check that can pop and push values on two different servers

        clients.add(createClientThread("Client2", stub -> {
            String client2 = "Client2";
            try {

                // Test pop
                stub.pushValue(5);
                System.out.println(client2 + " pushed 5, pop " + stub.pop());
                
                //test push operation
                stub.pushValue(5);
                stub.pushValue(28);
                stub.pushOperation("max");
                System.out.println(client2 + " has 5 and 28 on stack");
                System.out.println(client2 + " pushed operation 'max': " +stub.pop()+"\n");

            } catch (Exception e) {
                System.err.println(client2 + " exception: " + e.getMessage());
            }
        }));
        
        //client3: test empty stack

        clients.add(createClientThread("Client3", stub ->  {
            String client3 = "Client3";
            try {

                // Test pop with no prior values on stack 
   
                System.out.println(client3 + " pushed " + stub.pop());
               
            } catch (Exception e) {
                System.err.println(client3 + " EMPTY STACK ERROR since there were no prior push in this stack: " + e.getMessage());
                
            }
        }));
        //client4: testing more than 3 clients running
        clients.add(createClientThread("Client4", stub -> {
            String client4 = "Client4";
        	try {

        		 // Test pushValue
                stub.pushValue(20);
                System.out.println( client4 + " pushed 20, pop " + stub.pop());

                // Test pushOperation
               
                stub.pushValue(5);
                stub.pushValue(28);
                stub.pushOperation("max");
                System.out.println(client4 + " has 5 and 28 on stack: ");
                System.out.println(client4 + " pushed operation 'max': " + stub.pop()+"\n");

                // Test delayPop
                stub.pushValue(20);
                System.out.println(client4 + " delayed pop: " + stub.delayPop(1000));

        		
        	}catch (Exception e) {
                System.err.println(client4 + " exception: " + e.getMessage());
            }
        }));

        for (Thread client : clients) {
            client.start();
        }

        for (Thread client : clients) {
            try {
                client.join();
            } catch (InterruptedException e) {
                System.err.println("Thread exception: " + e.getMessage());
            }
        }

        System.out.println("END OF MULTIPLE CLIENT TEST\n");
    }
}
