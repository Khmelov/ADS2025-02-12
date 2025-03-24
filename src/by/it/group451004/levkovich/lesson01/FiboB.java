package by.it.group451004.levkovich.lesson01;

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
        BigInteger answer = BigInteger.valueOf(1);
        BigInteger lastValue = BigInteger.valueOf(0);

        while (n > 1) {
            n--;

            lastValue = answer.add(lastValue);
            lastValue = lastValue.xor(answer);
            answer = lastValue.xor(answer);
            lastValue = lastValue.xor(answer);
        }

        return answer;
    }

}

