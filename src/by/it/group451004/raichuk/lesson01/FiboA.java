package by.it.group451004.raichuk.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить рекурсивный способ вычисления чисел Фибоначчи
 */

public class FiboA {


    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 50;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        //вычисление чисел фибоначчи медленным методом (рекурсией)
        fibo = new FiboA();
        n = 55;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    private int calc(int n) {
        int pred = 0;
        int temp = 0;
        int res = 1;
        for(int i = 2; i <= n; i++)
        {
            temp = res;
            res += pred;
            pred = temp;
        }
        return res;
    }


    BigInteger slowA(Integer n) {
        BigInteger res = BigInteger.ZERO;
        if(n > 2) {
            res = slowA(n - 1).add(slowA(n - 2));
        }
        else {
            res = BigInteger.ONE;
        }


        return res;
    }


}

