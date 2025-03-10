package by.it.group451001.puzik.lesson01;

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
        if (n <= 1) { return n; }

        long previous = 0;
        long current = 1;
        long pisanoPeriod = 0;

        for (int i = 0; i < m * m; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;

            if (previous == 0 && current == 1) {
                pisanoPeriod = i + 1;
                break;
            }
        }

        n = n % pisanoPeriod;

        if (n <= 1) return n;

        previous = 0;
        current = 1;

        for (int i = 0; i < n - 1; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;
        }

        return current;
    }
}
