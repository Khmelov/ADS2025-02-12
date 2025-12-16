package by.it.group451003.plyushchevich.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }


    public static long pisanoPeriod(int m) {
        long previous = 0, current = 1;
        for (long i = 0; i < (long) m * m; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;

            if (previous == 0 && current == 1) {
                return i + 1;
            }
        }
        return 0; // в теории это не должно происходить
    }

    // Метод для вычисления числа Фибоначчи по модулю m с использованием периода Писано

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        long period = pisanoPeriod(m);
        n %= period; // Уменьшаем n по модулю периода Писано

        long previous = 0, current = 1;
        for (int i = 0; i < n; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;
        }
        return previous;
    }


}

