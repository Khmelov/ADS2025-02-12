package by.it.group451002.kureichuk.lesson01;

import java.math.BigInteger;


public class FiboA {


    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    private int calc(int n) {
        if(n < 2)
            return 1;
        else
            return calc(n-1) + calc(n-2);
    }


    BigInteger slowA(Integer n) {
        if(n == 0)
            return BigInteger.ZERO;
        if(n == 1)
            return BigInteger.ONE;
        else
            return slowA(n-1).add(slowA(n-2));
    }


}

