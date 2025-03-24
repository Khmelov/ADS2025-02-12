package by.it.group451002.hodysh.lesson01;

import java.math.BigInteger;

public class FiboB {

    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        // вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%s \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        // создаем массив для хранения чисел Фибоначчи
        BigInteger[] fib = new BigInteger[n + 1];

        // начальные значения
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        // вычисляем числа Фибоначчи
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }

        // возвращаем n-е число Фибоначчи
        return fib[n];
    }
}
