// package assignment1;

import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
import java.util.Stack;

public class CalculatorImplementation extends UnicastRemoteObject implements Calculator {
    private static final long serialVersionUID = 7052739515724697623L;
	private Stack<Integer> stack;
    protected CalculatorImplementation() throws RemoteException {
        stack = new Stack<>();
    }

    @Override
    public synchronized void pushValue(int val) throws RemoteException {
        stack.push(val);
    }

    @Override
    public synchronized void pushOperation(String operator) throws RemoteException {
        if (stack.isEmpty()) return;

        int result;
        switch (operator) {
            case "min":
                result = stack.stream().min(Integer::compare).orElse(0);
                break;
            case "max":
                result = stack.stream().max(Integer::compare).orElse(0);
                break;
            case "lcm":
                result = lcm(stack);
                break;
            case "gcd":
                result = gcd(stack);
                break;
            default:
                throw new IllegalArgumentException("Invalid operator");
        }
        stack.clear();
        stack.push(result);
    }

    @Override
    public synchronized int pop() throws RemoteException {
        return stack.pop();
    }

    @Override
    public synchronized boolean isEmpty() throws RemoteException {
        return stack.isEmpty();
    }

    @Override
    public synchronized int delayPop(int millis) throws RemoteException {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return pop();
    }

    private int gcd(Stack<Integer> stack) {
        return stack.stream().reduce(this::gcd).orElse(0);
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private int lcm(Stack<Integer> stack) {
        return stack.stream().reduce(1, this::lcm);
    }

    private int lcm(int a, int b) {
        return a * (b / gcd(a, b));
    }
}
