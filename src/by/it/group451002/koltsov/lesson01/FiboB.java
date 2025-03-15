package by.it.group451002.koltsov.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        if (n < 3)
            return BigInteger.ONE;

        BigInteger[] Array = new BigInteger[n];
        Array[0] = BigInteger.ONE;
        Array[1] = BigInteger.ONE;

        for (int i = 2; i < n; i++){
            Array[i] = Array[i - 1].add(Array[i - 2]);
        }

        return Array[n - 1];
    }

}
