package by.it.group451001.alexandrovich.lesson01;

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
        BigInteger num1 = BigInteger.ONE;
        BigInteger num2 = BigInteger.ONE;
        BigInteger num = BigInteger.TWO;
        for (int i = 3; i <= n; i++){
            num = num1.add(num2);
            num1 = num2;
            num2 = num;
        }
        return num;
    }

}

