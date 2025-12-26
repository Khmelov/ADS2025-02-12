package by.it.group451004.volynets.lesson01;

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
        if (m == 1) return 0;

        int pisanoPeriod = getPisanoPeriod(m);
        long reducedN = n % pisanoPeriod;
        return getFibonacciModulo(reducedN, m);  // остаток
    }

    private int getPisanoPeriod(int m) {
        int prev = 0, curr = 1;
        int length = 0;
        do {
            int next = (prev + curr) % m;
            prev = curr;
            curr = next;
            length++;
        } while (!(prev == 0 && curr == 1));
        return length;
    }

    private long getFibonacciModulo(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        long prev = 0, curr = 1;
        for (long i = 2; i <= n; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr;
    }
}