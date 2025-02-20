package by.it.group451002.vysotski.lesson01;

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
        return switch (n) {
            case 0:
                yield 0;
            case 1:
                yield 1;
            default:
                yield (calc(n-1)+calc(n-2));
        };
    }


    public BigInteger slowA(Integer n) {
        return switch (n) {
            case 0 -> BigInteger.ZERO;
            case 1 -> BigInteger.ONE;
            default -> slowA(n - 1).add(slowA(n - 2));
        };

    }


}

