package by.it.group410902.sinyutin.lesson01;

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

    public static long fasterC(long n, int m) {
        // Находим период Пизано для m
        int pisanoPeriod = getPisanoPeriod(m);

        // Уменьшаем n по модулю периода Пизано
        n = n % pisanoPeriod;

        // Вычисляем остаток от числа Фибоначчи по модулю m
        return calculateFibMod(n, m);
    }

    // Метод для нахождения периода Пизано для числа m
    private static int getPisanoPeriod(int m) {
        int a = 0;
        int b = 1;
        for (int i = 0; i < m * m; i++) {
            int temp = (a + b) % m;
            a = b;
            b = temp;

            // Период начинается с 01
            if (a == 0 && b == 1) {
                return i + 1;
            }
        }
        return 0;
    }

    // Метод для вычисления числа Фибоначчи по модулю m
    private static long calculateFibMod(long n, int m) {
        if (n <= 1) {
            return n;
        }

        long a = 0;
        long b = 1;

        // Вычисляем число Фибоначчи по модулю m
        for (long i = 2; i <= n; i++) {
            long temp = (a + b) % m;
            a = b;
            b = temp;
        }

        return b;
    }
}

