package by.it.group410902.latipov.lesson01;

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
        // Находим период Пизано для модуля m
        long period = findPisanoPeriod(m);

        // Находим эквивалентный индекс в пределах периода
        long equivalentIndex = n % period;

        // Вычисляем число Фибоначчи по модулю m для эквивалентного индекса
        return fibMod(equivalentIndex, m);
    }

    // Метод для нахождения периода Пизано
    private long findPisanoPeriod(int m) {
        if (m == 1) return 1;

        long a = 0, b = 1;
        long period = 0;

        // Период Пизано всегда начинается с 0, 1
        for (long i = 0; i <= (long) m * m; i++) {
            long current = (a + b) % m;
            a = b;
            b = current;
            period++;

            // Период найден, когда мы снова получаем 0, 1
            if (a == 0 && b == 1) {
                return period;
            }
        }
        return period;
    }

    // Метод для вычисления n-го числа Фибоначчи по модулю m
    private long fibMod(long n, int m) {
        if (n <= 1) return n % m;

        long a = 0, b = 1;

        for (long i = 2; i <= n; i++) {
            long current = (a + b) % m;
            a = b;
            b = current;
        }

        return b;
    }
}