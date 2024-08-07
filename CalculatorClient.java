// package assignment1;

import java.rmi.Naming;

public class CalculatorClient {

    public static void main(String[] args) {
        try {
            
         
		Calculator stub = (Calculator) Naming.lookup("rmi://localhost:1099/calculator");
            
            stub.pushValue(20);
            stub.pushValue(30);
            stub.pushOperation("max");
            System.out.println("Max: " + stub.pop()); 
            
            


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }
    }
}
