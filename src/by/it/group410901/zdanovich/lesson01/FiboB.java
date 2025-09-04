package by.it.group410901.zdanovich.lesson01;

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
        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        if (n == 0){
            return BigInteger.ZERO;
        } else if (n == 1){
            return BigInteger.ONE;
        } else {
            BigInteger [] arr = new BigInteger[n+1];
            arr[0] = BigInteger.ZERO;
            arr[1] = BigInteger.ONE;
            for (int i = 2; i <= n; i++) {
                arr[i] = arr[i-1].add(arr[i-2]);

            }
            return arr[n];
        }
    }

}

