package by.it.group410902.gavlev.lesson01;

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
//        //здесь нужно реализовать вариант с временем O(n) и памятью O(n)
        BigInteger massive_chisel[] = new BigInteger[n+1];
        massive_chisel[0] = BigInteger.ZERO;
        massive_chisel[1] = BigInteger.ONE;
        for (int i = 2; i < n+1; i++)
        {
            massive_chisel[i] = massive_chisel[i-1].add(massive_chisel[i-2]);
        }
        return massive_chisel[n];
    }
}
