package by.it.group451002.troshin.lesson01;

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
        if (n <= 0) return 0; // F(0) = 0
        if (n == 1) return 1 % m; // F(1) = 1

        long[] fib = new long[6 * m];
        fib[0] = 0;
        fib[1] = 1;
        int period = 0;

        for (int i = 2; i < 6 * m; i++) {
            fib[i] = (fib[i - 1] + fib[i - 2]) % m;
            if (fib[i] == 1 && fib[i - 1] == 0) {
                period = i - 1;
                break;
            }
        }

        if (period == 0) period = 6 * m;

        int reducedN = (int) (n % period);

        if (reducedN <= 1) return reducedN % m;

        long a = 0;
        long b = 1;
        for (int i = 2; i <= reducedN; i++) {
            long temp = (a + b) % m;
            a = b;
            b = temp;
        }

        return b;
    }


}

