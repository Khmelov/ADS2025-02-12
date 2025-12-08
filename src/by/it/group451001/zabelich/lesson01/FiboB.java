package by.it.group451001.zabelich.lesson01;

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
        BigInteger[] fibo = new BigInteger[n+1];
        fibo[0] = BigInteger.ZERO;
        fibo[1] = BigInteger.ONE;
        for (int i=2;i<=n;i++){
           fibo[i] = fibo[i-1].add(fibo[i-2]);
        }
        return fibo[n];
    }

}

