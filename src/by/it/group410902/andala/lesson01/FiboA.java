package by.it.group410902.andala.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить рекурсивный способ вычисления чисел Фибоначчи
 */

public class FiboA {


    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        FiboA fibo = new FiboA();


        int n = 33;


        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        fibo = new FiboA();
        n = 34;

        System.out.printf("slowA(%d)=%s \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    // Метод для вычисления времени, прошедшего с последнего замера
    private long time() {
        long res = System.currentTimeMillis() - startTime; // разница времени
        startTime = System.currentTimeMillis(); // сброс таймера на текущее время
        return res;
    }

    // Рекурсивный метод вычисления чисел Фибоначчи с использованием int
    private int calc(int n) {
        // Базовые случаи: F(0) = 0, F(1) = 1
        if (n <= 1) return n;

        // Рекурсивный вызов: F(n) = F(n-1) + F(n-2)
        return calc(n - 1) + calc(n - 2);
    }

    // Рекурсивный метод вычисления чисел Фибоначчи с использованием BigInteger
    BigInteger slowA(Integer n) {
        // Базовые случаи: F(0) = 0, F(1) = 1
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        // Рекурсивный вызов: F(n) = F(n-1) + F(n-2), только с использованием BigInteger
        return slowA(n - 1).add(slowA(n - 2));
    }
}
