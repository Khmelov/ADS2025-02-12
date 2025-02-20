package by.it.group410901.zaverach.lesson01;

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
        // Если n равно 0, сразу возвращаем 0
        if (n == 0) {
            return BigInteger.ZERO;
        }
        BigInteger[] fibArray = new BigInteger[n + 1];

        fibArray[0] = BigInteger.ZERO;
        fibArray[1] = BigInteger.ONE;
        for (int i = 2; i <= n; i++) {
            fibArray[i] = fibArray[i - 1].add(fibArray[i - 2]);
        }

        // Возвращаем F(n)
        return fibArray[n];
    }

}

