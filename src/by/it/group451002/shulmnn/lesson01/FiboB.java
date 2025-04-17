package by.it.group451002.shulmnn.lesson01;

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
        int n = 999999999;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        BigInteger[] N = new BigInteger[3];
        N[0] = BigInteger.ONE;
        N[1] = BigInteger.ONE;
        for(int i = 2; i < n;i++)
        {
            N[2] = N[0].add(N[1]);
            N[0] = N[1];
            N[1] = N[2];
        }

        return N[2];
    }

}

