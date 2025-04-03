package by.it.group451001.serganovskij.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */
public class FiboC {

    private final long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
FiboC fibo= new FiboC();
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

        // 1. Находим период Пизано для числа m
        int pisanoPeriod = getPisanoPeriod(m);

        // 2. Сокращаем n по модулю периода Пизано
        int reducedN = (int) (n % pisanoPeriod);

        // 3. Вычисляем F(reducedN) % m
        return fibonacciMod(reducedN, m);
    }

    private int getPisanoPeriod(int m) {
        int prev = 0, curr = 1;
        for (int i = 0; i < m * m; i++) { // Период Пизано не больше m*m
            int temp = (prev + curr) % m;
            prev = curr;
            curr = temp;

            // Найден период (начало повторения 0,1)
            if (prev == 0 && curr == 1) return i + 1;
        }
        return m;
    }

    private long fibonacciMod(int n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        long prev = 0, curr = 1;
        for (int i = 2; i <= n; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
        }
        return curr;
    }
}



