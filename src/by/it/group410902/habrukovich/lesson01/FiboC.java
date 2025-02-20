package by.it.group410902.habrukovich.lesson01;

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
    private int Pisano(int m) {
        int f1 = 0, f2 = 1, f;
        for (int i = 0; i < m * m; i++) {
            f = (f1 + f2) % m;
            f1 = f2;
            f2 = f;
            if (f1 == 0 && f2 == 1) return i + 1;
        }
        return m;
    }

    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        int pis = Pisano(m);
        n = n % pis;
        if (n == 0) return 0;
        if (n == 1) return 1;
        long f1 = 0, f2 = 1, f = 0;
        for (int i = 2; i <= n; i++) {
            f = (f1 + f2) % m;
            f1 = f2;
            f2 = f;
        }
        return f2;

    }


}

