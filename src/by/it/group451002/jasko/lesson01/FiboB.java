package by.it.group451002.jasko.lesson01;

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
        // Реализация вычисления чисел Фибоначчи за O(n) с использованием массива
        if (n == 0) {
            return BigInteger.ZERO;
        }
        if (n == 1) {
            return BigInteger.ONE;
        }

        // Создаем массив для хранения промежуточных результатов
        BigInteger[] fibArray = new BigInteger[n + 1];
        fibArray[0] = BigInteger.ZERO; // F(0)
        fibArray[1] = BigInteger.ONE;  // F(1)

        // Заполняем массив по формуле F(n) = F(n-1) + F(n-2)
        for (int i = 2; i <= n; i++) {
            fibArray[i] = fibArray[i - 1].add(fibArray[i - 2]);
        }

        // Возвращаем последний элемент массива
        return fibArray[n];
    }
}