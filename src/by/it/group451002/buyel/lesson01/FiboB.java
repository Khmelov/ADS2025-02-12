package by.it.group451002.buyel.lesson01;

import java.math.BigInteger;
import java.util.Objects;

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
        BigInteger[] FiboNums;
        FiboNums = new BigInteger[n+1];

        // Инициализация переменных
        FiboNums[0] = BigInteger.ZERO;
        FiboNums[1] = BigInteger.ONE;

        if (Objects.equals(FiboNums[n-1], BigInteger.ZERO)) return BigInteger.ZERO;
        if (Objects.equals(FiboNums[n-1], BigInteger.ONE)) return BigInteger.ONE;

        // Вычисление n-ого числа
        for (int i = 2; i <= n; i++) {
            FiboNums[i] = FiboNums[i - 1].add(FiboNums[i - 2]);
        }
        return FiboNums[n];
    }

}

