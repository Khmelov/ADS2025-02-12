package by.it.group451003.popovich.lesson01;

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
        if (n == 0) return 0;
        if (m == 1) return 0;
        int a = 0, b = 1, c;
        int period = 0;
        do {
            c = (a + b) % m;
            a = b;
            b = c;
            period++;
        } while (!(a == 0 && b == 1));
        long new_n = n % period;
        if (new_n == 0) return 0;
        for (long i = 2; i <= new_n; i++) {
            c = (a + b) % m;
            a = b;
            b = c;
        }
        return b % m;
    }
}

