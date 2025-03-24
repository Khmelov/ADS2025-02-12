package by.it.group451002.gorbach.lesson01;

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
        // Метод для нахождения периода Пизано для m
        if (n == 0) return 0;

        long pisanoPeriod = getPisanoPeriod(m);

        n = n % pisanoPeriod;

        if (n == 0) return 0;

        long prev = 0;
        long curr = 1;

        for (long i = 2; i <= n; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr % m;
    }

    static long getPisanoPeriod(int m) {
        long prev = 0;
        long curr = 1;
        long period = 0;

        while (true) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
            period++;

            if (prev == 0 && curr == 1) {
                return period;
            }
        }
    }
}
