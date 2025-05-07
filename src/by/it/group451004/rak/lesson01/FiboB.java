package by.it.group451004.rak.lesson01;

import java.math.BigInteger;
import java.util.HashMap;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

//    private HashMap<Integer, BigInteger> valuesOfFibo = new HashMap<>();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        Integer n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }


    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        if (n < 2) return BigInteger.valueOf(n);

        BigInteger[] values = new BigInteger[n + 1];
        values[0] = BigInteger.valueOf(0);
        values[1] = BigInteger.valueOf(1);
        for (int i = 2; i < n + 1; i++) {
            values[i] = values[i - 2].add(values[i - 1]);
        }
        return values[n];
//        if (valuesOfFibo.containsKey(n)) {
//            return valuesOfFibo.get(n);
//        }
//        valuesOfFibo.put(n, fastB(n - 1).add(fastB(n - 2)));
//        return valuesOfFibo.get(n);
    }

}

