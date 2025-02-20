package by.it.group451002.yeshmanski.lesson1;

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

        BigInteger prev = BigInteger.ONE;
        BigInteger cur = BigInteger.ZERO;
        for (int i = n; i > 0; i--) {
            final BigInteger next = cur.add(prev);
            prev = cur;
            cur = next;
        } // for
        return cur;


       // return BigInteger.valueOf(-1L);
    }

}

