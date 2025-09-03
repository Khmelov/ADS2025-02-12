package by.it.group410901.papou.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        by.it.group410901.papou.lesson01.FiboB fibo = new by.it.group410901.papou.lesson01.FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        BigInteger[] arr = new BigInteger[n+1];
        if (n==0) return BigInteger.ZERO;
        if (n==1) return BigInteger.ONE;

        arr[0] = BigInteger.valueOf(0);
        arr[1] = BigInteger.valueOf(1);

        for (int i = 2; i <= n; i++) {
            arr[i]=arr[i -1].add(arr[i -2 ]);
        }
        return arr[n];
    }

}

