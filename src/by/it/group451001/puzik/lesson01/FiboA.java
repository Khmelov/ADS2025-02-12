package by.it.group451001.puzik.lesson01;

import java.math.BigInteger;

public class FiboA {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d) = %d \n\t time = %d ms \n\n", n, fibo.calc(n), fibo.time());

        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d) = %s \n\t time = %d ms \n\n", n, fibo.slowA(n).toString(), fibo.time());
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    private int calc(int n) {
        return n < 2
            ? n
            : calc(n - 1) + calc(n - 2);
    }

    BigInteger slowA(Integer n) {
        return n < 2
            ? BigInteger.valueOf(n)
            : slowA(n - 1).add(slowA(n - 2));
    }
}
