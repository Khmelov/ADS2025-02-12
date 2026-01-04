package by.it.group410901.korneew.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        long pisanoPeriod = pisanoPeriodLength(m);
        long reducedN = n % pisanoPeriod;
        return fibonacciMod(reducedN, m);
    }

    private long pisanoPeriodLength(int m) {
        long previous = 0;
        long current = 1;
        for (int i = 0; i < m * m; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;

            // Период начинается с 0, 1
            if (previous == 0 && current == 1) {
                return i + 1;
            }
        }
        return 0; // Должно быть обнаружено
    }

    private long fibonacciMod(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        long previous = 0;
        long current = 1;

        for (long i = 2; i <= n; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;
        }

        return current;
    }
}