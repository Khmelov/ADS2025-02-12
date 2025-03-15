package by.it.group451004.akbulatov.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 999999999;
        int m = 321;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        long a = 1, b = 1, nHalf;
        nHalf = (n - 1) / 2;

        for (int i = 0; i < nHalf; i++) {
            a = (a + b);
            if (a > m)
                a %= m;

            b = (a + b);
            if (b > m)
                b %= m;
        }
        if ((n & 1) == 1)
            return a;
        return b;
    }
}

