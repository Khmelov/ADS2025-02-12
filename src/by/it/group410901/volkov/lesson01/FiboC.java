package by.it.group410901.volkov.lesson01;

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
        if (n == 0) return 0;
        if (n == 1) return 1 % m;

        long[] fib = fastDoubling(n, m);
        return fib[0];
    }
    private long[] fastDoubling(long n, int m) {
        if (n == 0) return new long[]{0, 1};
        if (n == 1) return new long[]{1 % m, 1 % m};

        long[] fib = fastDoubling(n / 2, m);
        long a = fib[0];  // F(n)
        long b = fib[1];  // F(n+1)

        // Вычисляем F(2n) и F(2n+1) по формулам быстрого удвоения
        long c = (a * ((2 * b - a) % m + m)) % m;  // F(2n)
        long d = (a * a % m + b * b % m) % m;      // F(2n+1)

        if (n % 2 == 0) {
            return new long[]{c, d};
        } else {
            return new long[]{d, (c + d) % m};
        }
    }

}

