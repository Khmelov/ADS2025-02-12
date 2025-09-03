package by.it.group410901.getmanchuk.lesson01;

import java.math.BigInteger;

public class FiboA {
    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        // Медленный метод с BigInteger
        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());

        // Демонстрация быстрого метода
        fibo = new FiboA();
        n = 100;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    // Простейшая рекурсивная реализация (int)
    private int calc(int n) {
        if (n < 2) return n;
        return calc(n - 1) + calc(n - 2);
    }

    // Рекурсивная реализация с BigInteger
    BigInteger slowA(Integer n) {
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;
        return slowA(n - 1).add(slowA(n - 2));
    }

    // Быстрая реализация с мемоизацией (дополнительный метод)
    private BigInteger[] fibCache = new BigInteger[1000];

    BigInteger fastB(Integer n) {
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;
        if (fibCache[n] != null) return fibCache[n];

        fibCache[n] = fastB(n - 1).add(fastB(n - 2));
        return fibCache[n];
    }
}