package by.it.group451003.chveikonstantcin.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

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
        List<BigInteger> sequence = new ArrayList<>();
        sequence.add(BigInteger.ZERO);
        if (n == 0) return sequence.get(0);

        sequence.add(BigInteger.ONE);
        if (n == 1) return sequence.get(1);

        for (int position = 2; position <= n; position++) {
            BigInteger next = sequence.get(position - 1).add(sequence.get(position - 2));
            sequence.add(next);
        }

        return sequence.get(n);
    }

}

