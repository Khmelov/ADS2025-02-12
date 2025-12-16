package by.it.group410902.barbashova.lesson01;

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
        if (m == 1) {
            return 0;
        }

        // Находим период Пизано для m
        long pisanoPeriod = getPisanoPeriod(m);

        // Вычисляем F(n mod pisanoPeriod) mod m
        long reducedN = n % pisanoPeriod;
        return fibMod(reducedN, m);
    }

    // Метод для нахождения периода Пизано
    private long getPisanoPeriod(int m) {
        long a = 0, b = 1, c;
        long period = 0;

        for (int i = 0; i < 6 * m; i++) {
            c = (a + b) % m;
            a = b;
            b = c;
            period++;

            if (a == 0 && b == 1) {
                return period;
            }
        }
        return period;
    }

    // Метод для вычисления F(n) mod m
    private long fibMod(long n, int m) {
        if (n == 0) {
            return 0;
        }

        long a = 0, b = 1, c;

        for (long i = 2; i <= n; i++) {
            c = (a + b) % m;
            a = b;
            b = c;
        }

        return b;
    }
}

