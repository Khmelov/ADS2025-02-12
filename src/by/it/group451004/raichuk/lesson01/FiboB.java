package by.it.group451004.raichuk.lesson01;

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
        List<BigInteger> helpArr = new ArrayList<BigInteger>();
        helpArr.add(BigInteger.ZERO);
        helpArr.add(BigInteger.ONE);
        for(int i = helpArr.size(); i <= n; i++) {
            helpArr.add(helpArr.get(i-1).add(helpArr.get(i-2)));
        }
        return helpArr.get(n);
    }

}

