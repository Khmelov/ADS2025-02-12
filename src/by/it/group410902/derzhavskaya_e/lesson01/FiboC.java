package by.it.group410902.derzhavskaya_e.lesson01;

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

    private static int findPisanoPeriod(int m) {
        int a = 0, b = 1;
        int period = 0;
        do {
            int c = (a + b) % m;
            a = b;
            b = c;
            period++;
        } while (!(a == 0 && b == 1));
        return period;
    }

    private static int fibonacci(long n, int m) {
        if (n == 0) {
            return 0;
        } else if (n == 1) {
            return 1;
        }
        int prev = 0;
        int curr = 1;
        for (long i = 2; i <= n; i++) {
            int next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }
        return curr;
    }


    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (m == 1) {
            return 0;
        }
        int period = findPisanoPeriod(m);
        long N = n % period;
        return fibonacci(N, m);
    }


}

