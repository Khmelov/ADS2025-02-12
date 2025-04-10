package by.it.group451001.buiko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

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
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (n <= 1) {
            return n;
        }

        long[][] F = {{1, 1}, {1, 0}};
        power(F, n - 1, m);

        return F[0][0];
    }

    private static void multiply(long[][] F, long[][] M, int m) {
        long x = (F[0][0] * M[0][0] + F[0][1] * M[1][0]) % m;
        long y = (F[0][0] * M[0][1] + F[0][1] * M[1][1]) % m;
        long z = (F[1][0] * M[0][0] + F[1][1] * M[1][0]) % m;
        long w = (F[1][0] * M[0][1] + F[1][1] * M[1][1]) % m;

        F[0][0] = x;
        F[0][1] = y;
        F[1][0] = z;
        F[1][1] = w;
    }

    private static void power(long[][] F, long n, int m) {
        if (n == 0 || n == 1) {
            return;
        }

        long[][] M = {{1, 1}, {1, 0}};

        power(F, n / 2, m);
        multiply(F, F, m);

        if (n % 2 != 0) {
            multiply(F, M, m);
        }
    }


}

