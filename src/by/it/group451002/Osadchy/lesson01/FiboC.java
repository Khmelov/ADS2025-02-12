package by.it.group451002.Osadchy.lesson01;

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
        int previous = 0;
        int current = 1;
        int period = 0;

        for (int i = 0; i < m * m; i++) {
            int temp = (previous + current) % m;
            previous = current;
            current = temp;
            period++;

            if (previous == 0 && current == 1) {
                period = i + 1;
                break;
            }
        }

        n = n % period;

        previous = 0;
        current = 1;

        for (long i = 2; i <= n; i++) {
            long temp = (previous + current) % m;
            previous = current;
            current = (int) temp;
        }

        return (n == 0) ? previous : current;
    }
}