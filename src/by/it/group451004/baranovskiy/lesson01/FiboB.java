package by.it.group451004.baranovskiy.lesson01;

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
        if (n == 0) {
            return BigInteger.ZERO;
        }
        else if (n == 1) {
            return BigInteger.ONE;
        }
        BigInteger[] arrN = new BigInteger[n + 1];
        arrN[0] = BigInteger.ZERO;
        arrN[1] = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            arrN[i] = arrN[i - 1].add(arrN[i - 2]);
        }

        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        return arrN[n];
    }

}

