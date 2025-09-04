package by.it.group410901.borisdubinin.lesson01;

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
        if (n <= 1) return n;

        // Находим период Пизано для m
        int pisanoPeriod = getPisanoPeriod(m);

        // Находим F(n) % m с использованием периода
        n = n % pisanoPeriod;

        long prev = 0;
        long curr = 1;

        for (int i = 2; i <= n; i++) {
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
        }

        return curr;
    }
    // Функция для нахождения периода Пизано
    private int getPisanoPeriod(int m) {
        long prev = 0;
        long curr = 1;

        for (int i = 0; i < 6*m; i++) { // Период Пизано всегда <= 6m
            long temp = (prev + curr) % m;
            prev = curr;
            curr = temp;

            if (prev == 0 && curr == 1) {
                return i + 1;
            }
        }
        return m; // На случай ошибки (но не должно быть)
    }


}

