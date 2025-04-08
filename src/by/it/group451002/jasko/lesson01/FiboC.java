package by.it.group451002.jasko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d, %d)=%d \n\t time=%d \n\n", n, m, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        // Нахождение периода Пизано для модуля m
        int pisanoPeriod = findPisanoPeriod(m);

        // Сокращаем n по модулю периода Пизано
        n = n % pisanoPeriod;

        // Вычисляем n-е число Фибоначчи по модулю m
        if (n == 0) return 0;
        if (n == 1) return 1;

        long prev = 0; // F(0)
        long curr = 1; // F(1)

        for (long i = 2; i <= n; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr;
    }

    private int findPisanoPeriod(int m) {
        // Находим период Пизано для заданного модуля m
        long prev = 0; // F(0)
        long curr = 1; // F(1)
        int period = 0;

        // Цикл работает до тех пор, пока не найден полный период (0, 1)
        while (!(prev == 0 && curr == 1) || period == 0) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
            period++;
        }

        return period;
    }
}