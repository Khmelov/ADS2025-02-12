package by.it.group451002.morozov.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

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
    	BigInteger[] fib_n = new BigInteger[n+1];
    	fib_n[0] = BigInteger.ZERO;
    	fib_n[1] = BigInteger.ONE;
    	for (int i = 2; i < n+1; i++) {
    		fib_n[i] = fib_n[i-1].add(fib_n[i-2]);
    	}
    	return fib_n[n];
    }

}

