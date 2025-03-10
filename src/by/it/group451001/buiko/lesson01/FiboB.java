package by.it.group451001.buiko.lesson01;

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

    private long time() {return System.currentTimeMillis() - startTime;
    }
    ArrayList<BigInteger> fiboNum = new ArrayList<>(55555);

    BigInteger fastB(Integer n) {
        fiboNum.add(0, BigInteger.ZERO);
        fiboNum.add(1, BigInteger.ONE);
        for (int i = 2; i <= n; i++) {
            fiboNum.add(fiboNum.get(i - 1).add(fiboNum.get(i - 2)));
        }
        return fiboNum.get(n);
    }
}

