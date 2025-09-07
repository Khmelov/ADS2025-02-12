package by.it.group410902.gavlev.lesson01;

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

    long fasterC(long n, int m) {
        if (n <= 1) return n;
        int pisano_Period = getPisanoPeriod(m);
        n = n % pisano_Period;

        long num_before = 0, num_after = 1;
        for (int i = 2; i <= n; i++) {
            long temp = (num_before + num_after) % m;
            num_before = num_after;
            num_after = temp;
        }
        return num_after;
    }

    private int getPisanoPeriod(int m) {
        long num_before = 0, num_after = 1;
        for (int i = 0; i < m * m; i++) {
            long temp = (num_before + num_after) % m;
            num_before = num_after;
            num_after = temp; //p

            if (num_before == 0 && num_after == 1) {
                return i + 1;
            }
        }
        return 1;
    }
}

