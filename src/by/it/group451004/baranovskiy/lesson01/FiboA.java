package by.it.group451004.baranovskiy.lesson01;

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

        //вычисление чисел фибоначчи медленным методом (рекурсией)
        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    private int calc(int n) {
        if (n <= 1) {
            return n;
        }
        int num2 = 1;
        int num1 = 0;
        for (int i = 2; i <= n; i++) {
            int next = num1 + num2;
            num1 = num2;
            num2 = next;
        }
        //здесь простейший вариант, в котором код совпадает
        //с математическим определением чисел Фибоначчи
        //время O(2^n)
        return num2;
    }


    BigInteger slowA(Integer n) {
        if (n == 0) {
            return BigInteger.ZERO;
        }
        else if (n == 1) {
            return BigInteger.ONE;
        }
        //рекурсия
        //здесь нужно реализовать вариант без ограничения на размер числа,
        //в котором код совпадает с математическим определением чисел Фибоначчи
        //время O(2^n)
        return slowA(n - 1).add(slowA(n - 2));
    }
}

