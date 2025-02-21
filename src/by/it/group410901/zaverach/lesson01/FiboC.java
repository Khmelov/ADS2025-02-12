package by.it.group410901.zaverach.lesson01;

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
        int pisanoP = getPisanoP(m);

        n = n % pisanoP;

        return calculateFibMod(n, m);
    }

    private int getPisanoP(int m) {
        int a = 0;
        int b = 1;
        for (int i = 0; i < m * m; i++) {
            int temp = (a + b) % m;
            a = b;
            b = temp;
            if (a == 0 && b == 1) {
                return i + 1;
            }
        }
        return 0;
    }
    private long calculateFibMod(long n, int m) {
        if (n <= 1) {
            return n;
        }

        long a = 0;
        long b = 1;

        for (long i = 2; i <= n; i++) {
            long t = (a + b) % m;
            a = b;
            b = t;
        }

        return b;
    }
}

