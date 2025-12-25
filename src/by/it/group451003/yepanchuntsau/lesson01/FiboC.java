package by.it.group451003.yepanchuntsau.lesson01;

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
        // 1. Найдём период Пизано для модуля m
        int period = getPisanoPeriod(m);
        // 2. Сократим индекс n по найденному периоду
        long reducedIndex = n % period;
        // 3. Вычислим F(reducedIndex) mod m итеративно
        return fibMod((int) reducedIndex, m);
    }

    /**
     * Вычисляет период Пизано для модуля m.
     */
    private int getPisanoPeriod(int m) {
        int previous = 0, current = 1;
        // Период гарантированно не больше 6*m
        for (int i = 0; i < 6 * m; i++) {
            int temp = (previous + current) % m;
            previous = current;
            current = temp;
            // Начало нового периода: 0, 1
            if (previous == 0 && current == 1) {
                return i + 1;
            }
        }
        // Если не найдено (теоретически никогда не произойдёт), возвращаем верхнюю границу
        return 6 * m;
    }

    /**
     * Итеративно вычисляет n-е число Фибоначчи по модулю m.
     */
    private long fibMod(int n, int m) {
        if (n <= 1)
            return n;
        long a = 0;
        long b = 1;
        for (int i = 2; i <= n; i++) {
            long temp = (a + b) % m;
            a = b;
            b = temp;
        }
        return b;
    }
}

