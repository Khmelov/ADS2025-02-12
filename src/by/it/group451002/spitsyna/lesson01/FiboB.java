package by.it.group451002.spitsyna.lesson01;

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
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        int i = 2;

        BigInteger[] array = new BigInteger[n];

        array[0] = BigInteger.ONE;
        array[1] = BigInteger.ONE;

        if (n == 1 || n == 2) {
            return BigInteger.ONE;
        }
        if (n == 0) {
            return BigInteger.ZERO;
        }
        while (i < n) {
            array[i] = array[i-1].add(array[i-2]);
            i++;
        }
        return array[n-1];
    }

}

