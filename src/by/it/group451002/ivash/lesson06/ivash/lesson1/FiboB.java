package by.it.group451002.ivash.lesson06.ivash.lesson1;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {
    private long startTime = System.currentTimeMillis();

    public FiboB() {
    }

    public static void main(String[] args) {
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    long time() {
        return System.currentTimeMillis() - this.startTime;
    }

    public BigInteger fastB(Integer n) {
        BigInteger[] fib = new BigInteger[n + 1];
        fib[0] = BigInteger.ZERO;
        if (n > 0) {
            fib[1] = BigInteger.ONE;

            for(int i = 2; i <= n; ++i) {
                fib[i] = fib[i - 1].add(fib[i - 2]);
            }
        }

        return fib[n];
    }
}

