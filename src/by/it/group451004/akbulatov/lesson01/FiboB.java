package by.it.group451004.akbulatov.lesson01;

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

    public BigInteger[] array;

    BigInteger fastB(Integer n) {

        array = new BigInteger[n];
        array[0] = BigInteger.ONE;
        array[1] = BigInteger.ONE;

        for (int i = 2; i < n; i++) {
            array[i] = array[i - 2].add(array[i - 1]);
        }

        return array[n - 1];
    }
}

//TODO       THIS WAS ORIGINAL CODE -> STACK OVERFLOW EXCEPTION ON PROVIDED INPUT 555555
/*
BigInteger fastB(Integer n) {
    lastIndexFound = 2;

    array = new BigInteger[n];
    array[2] = BigInteger.ONE;
    array[1] = BigInteger.ONE;
    array[0] = BigInteger.ZERO;
    return fastBSubTask(n - 2).add(fastBSubTask(n - 1));
}

BigInteger fastBSubTask(Integer n) {

    if (n > lastIndexFound) {
        array[n] = fastBSubTask(n - 2).add(fastBSubTask(n - 1));
        lastIndexFound++;
    }

    return array[n];
}
*/