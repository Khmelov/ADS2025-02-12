package by.it.group451002.vysotski.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class fiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        fiboC fibo = new fiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    public long fasterC(long n, int m) {

        if (n == 0) return 0;
        if (n == 1) return 1;

        // 1. Находим период Пизано для m
        int prev = 0, curr = 1, period = 0;
        for (int i = 0; i < m * m; i++) {
            int temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
            if (prev == 0 && curr == 1) { // Начало нового цикла
                period = i + 1;
                break;
            }
        }

        // 2. Сокращаем n по модулю периода Пизано
        n = n % period;

        // 3. Вычисляем F(n) % m через итерацию
        prev = 0;
        curr = 1;
        for (int i = 2; i <= n; i++) {
            int temp = (prev + curr) % m;
            prev = curr;
            curr = temp;
        }

        return curr;
    }


}

