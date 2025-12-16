package by.it.group451002.jasko.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить рекурсивный способ вычисления чисел Фибоначчи
 */

public class FiboA {

    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        // Вычисление чисел фибоначчи медленным методом (рекурсией)
        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    private int calc(int n) { // Вычисляет n-е число Фибоначчи с помощью рекурсии.
        // Простейший вариант рекурсивного вычисления чисел Фибоначчи. Временная сложность: O(2^n)
        if (n <= 1) {
            return n;
        }
        return calc(n - 1) + calc(n - 2);
    }

    BigInteger slowA(Integer n) { // Так же вычисляет n-е число Фибоначчи с помощью рекурсии, но использует класс BigInteger для работы с большими числами.
        // Рекурсивное вычисление чисел Фибоначчи с использованием BigInteger. Временная сложность: O(2^n)
        if (n == 0) {
            return BigInteger.ZERO;
        }
        if (n == 1) {
            return BigInteger.ONE;
        }
        return slowA(n - 1).add(slowA(n - 2));
    }
}