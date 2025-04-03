package by.it.group451002.hodysh.lesson01;

public class FiboC {

    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    // Функция для нахождения периода Пизано
    private long pisanoPeriod(int m) {
        long prev = 0, curr = 1;
        // Исправлено: приведение к long
        for (long i = 0; i < (long) m * m; i++) {
            long temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
            // Период начинается, когда найдено (0, 1)
            if (prev == 0 && curr == 1) {
                return i + 1;
            }
        }
        return 0;
    }

    // Функция для нахождения числа Фибоначчи по модулю m
    long fasterC(long n, int m) {
        // Находим период Пизано для числа m
        long period = pisanoPeriod(m);
        // Вычисляем остаток от деления n на период
        n = n % period;

        // Теперь вычисляем число Фибоначчи для меньшего n
        long prev = 0, curr = 1;
        if (n == 0) return prev;
        for (long i = 2; i <= n; i++) {
            long temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
        }
        return curr;
    }
}

