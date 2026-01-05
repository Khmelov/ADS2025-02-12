package by.it.group410902.varava.lesson02.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;

        // Print the Fibonacci result and time taken
        System.out.printf("fastB(%d)=%s \n\t time=%d ms \n\n", n, fibo.fastB(n), fibo.time());

        // Optional: Print the number of digits in the result (for large numbers)
        System.out.printf("fastB(%d) has %d digits \n\t time=%d ms \n\n", n, fibo.fastB(n).toString().length(), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        // Base cases
        if (n == 0) {
            return BigInteger.ZERO;
        }
        if (n == 1) {
            return BigInteger.ONE;
        }

        // Create an array to store Fibonacci numbers
        BigInteger[] fib = new BigInteger[n + 1];
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        // Fill the array iteratively
        for (int i = 2; i <= n; i++) {
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }

        // Return the nth Fibonacci number
        return fib[n];
    }
}
