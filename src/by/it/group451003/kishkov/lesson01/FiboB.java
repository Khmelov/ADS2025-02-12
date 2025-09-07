package by.it.group451003.kishkov.lesson01;

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
        if((n==1)||(n==2))
            return BigInteger.ONE;
        BigInteger[] arr = new BigInteger[n];
        arr[0] = BigInteger.ONE;
        arr[1] = BigInteger.ONE;
        for (int i = 2; i <n; ++i)
            arr[i]=arr[i-1].add(arr[i-2]);
        return arr[n-1];
    }

}

