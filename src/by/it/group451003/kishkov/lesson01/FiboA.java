package by.it.group451003.kishkov.lesson01;

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
        int x1 = 1;
        int x2 = 1;
        n = n -2;
        for (int i = 0; i < n; i++) {
            x2 = x2 + x1;
            x1 = x2;
        }
        return x2;
    }


    BigInteger slowA(Integer n) {
        if ((n == 2) || (n == 1))
            return BigInteger.ONE;
        else
            return slowA(n-1).add(slowA(n-2));
    }


}

