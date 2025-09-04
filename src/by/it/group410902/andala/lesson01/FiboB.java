package by.it.group410902.andala.lesson01;

import java.math.BigInteger;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {


    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {

        FiboB fibo = new FiboB();


        int n = 55555;


        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }


    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    // Метод для вычисления чисел Фибоначчи с использованием массива и BigInteger
    BigInteger fastB(Integer n) {
        // Базовые случаи: F(0) = 0, F(1) = 1
        if (n == 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;

        // Создаем массив для хранения значений чисел Фибоначчи от 0 до n
        BigInteger[] fib = new BigInteger[n + 1];

        // Инициализируем первые два значения
        fib[0] = BigInteger.ZERO;
        fib[1] = BigInteger.ONE;

        // Заполняем массив от 2 до n
        for (int i = 2; i <= n; i++) {
            // F(i) = F(i-1) + F(i-2)
            fib[i] = fib[i - 1].add(fib[i - 2]);
        }

        // Возвращаем n-е число Фибоначчи
        return fib[n];
    }
}
