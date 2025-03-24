package by.it.group451001.bolshakova.lesson01;

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
        if (n <= 0) return 0;
        if (n == 1) return 1 % m;
        //период Пизано
        long prev = 0;
        long curr = 1;
        long period = 0;

        for (int i = 1; i < m*m; i++) {
            long next = (curr + prev) % m;
            prev = curr;
            curr = next;
            // Период найден, если снова получили (0, 1)
            if (prev == 0 && curr == 1) {
                period = i;
                break;
            }
        }
        long miniperiod = n % period;
        if (miniperiod == 0) return 0;
        if (miniperiod == 1) return 1 % m;

        prev = 0;
        curr = 1;
        for (int i = 2; i <= miniperiod; i++) {
            long next = (curr + prev) % m;
            prev = curr;
            curr = next;
        }

        return curr;
    }


}
