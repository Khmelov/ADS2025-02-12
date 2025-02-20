package by.it.group451002.kureichuk.lesson01;

import java.math.BigInteger;
import java.util.Vector;

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {

        Vector <BigInteger> fibonacciNumbers = new Vector<>(n);
        fibonacciNumbers.insertElementAt(BigInteger.ONE, 0);
        fibonacciNumbers.insertElementAt(BigInteger.ONE, 1);

        for(int i = 2; i < n; i++){
            BigInteger tmp = fibonacciNumbers.get(i-1).add(fibonacciNumbers.get(i-2));
            fibonacciNumbers.insertElementAt(tmp, i);
        }

        return fibonacciNumbers.getLast();
    }

}

