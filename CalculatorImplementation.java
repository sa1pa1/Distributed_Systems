
import java.io.Serial;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    @Serial
    private static final long serialVersionUID = 7052739515724697623L;
	private final Stack<Integer> stack;
	
	//A new stack initialized for every call  
    protected CalculatorImplementation() throws RemoteException {
        stack = new Stack<>();
    }
    //pushing value onto the stack
    @Override
    public synchronized void pushValue(int val) throws RemoteException {
        stack.push(val);
    }
    //push operations:
    @Override
    public synchronized void pushOperation(String operator) throws RemoteException {
    	//empty stack does not call 
        if (stack.isEmpty()) return;

        int result = switch (operator) {
            /*perform min, input from push value, takes the two values and return
             *the smaller value. This can be accessed through pop()*/
            case "min" -> stack.stream().min(Integer::compare).orElse(0);
            /*perform max, input from push value, takes the two values and return
             *the larger value. This can be accessed through pop()*/
            case "max" -> stack.stream().max(Integer::compare).orElse(0);
            /*perform the lowest common multiple*/
            case "lcm" -> lcm(stack);
            /*perform the greatest common divisor*/
            case "gcd" -> gcd(stack);
            default -> throw new RemoteException("Invalid operator");
        };
        stack.clear();
        stack.push(result);
    }
    
    //implementation for LCM and GCD//
    
    private int gcd(Stack<Integer> stack) {
    	//reduce to integers
        return stack.stream().reduce(this::gcd).orElse(0);
    }
    	//implementation, receive two values from stack, perform operation. 
    private int gcd(int a, int b) {
        while (b != 0) {
            if (b<0){
                b = -b;
            }
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }
  
    private int lcm(Stack<Integer> stack) {
    	//reduce to integers
        return stack.stream().reduce(1, this::lcm);
    }
    	//implementation, receive two values from stack, perform operation. 
    private int lcm(int a, int b) {
        return a * (b / gcd(a, b));
    }
    //POP: popping the top, results of the stack return to clients. Require no input.
    @Override
    public synchronized int pop() throws RemoteException {
    	//throw exception if attempt to pop when stack is empty
    	 if (stack.isEmpty()) {
             throw new RemoteException("Stack is empty.");
         }
        return stack.pop();
    }
    //ISEMPTY: checking if stack is empty. Require no input.
    @Override
    public synchronized boolean isEmpty() throws RemoteException {
        return stack.isEmpty();
    }

    //SLEEP THREAD: allow a delay in Pop for millis time. Input: millis, output: wait millis milliseconds. 
    @Override
    public synchronized int delayPop(int millis) throws RemoteException {
        if (millis < 0) {
            throw new RemoteException("Invalid milliseconds: cannot be negative time");

        }
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        	throw new RemoteException("Interrupted while delaying pop.", e);
        }
        return pop();
    }

}
