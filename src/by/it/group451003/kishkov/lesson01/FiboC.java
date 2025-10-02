package by.it.group451003.kishkov.lesson01;

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
        if(n==0)
            return 0;
        if(n==1)
            return 1;

        int x1 = 0;
        int x2 = 1;
        int remain;
        long period = 0;

        do {
            remain = (x1+x2) % m;
            x1 = x2;
            x2 = remain;
            period++;
        } while (!(x1==0&&x2==1));
        n %= period;

        for (int i = 2; i <= n;++i){
            remain = (x1+x2)% m;
            x1 = x2;
            x2 = remain;
        }
        return x2;
    }


}

