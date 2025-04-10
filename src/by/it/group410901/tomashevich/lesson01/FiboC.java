package by.it.group410901.tomashevich.lesson01;

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

    private long pizanoPeriod(int m) {
        long prev = 0;
        long curr = 1;
        long res = 0;
        for (int i = 0; i <= m * m; i++) {
            long temp;
            temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
            if (prev == 0 && curr == 1) res = i + 1;
        }
        return res;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        n = n % pizanoPeriod(m);
        long prev = 0;
        long curr = 1;
        if (n == 0) return 0;
        if (n == 1) return 1;
        for (int i = 2; i <= n; i++) {
            long temp;
            temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
        }
        return curr;
    }


}

