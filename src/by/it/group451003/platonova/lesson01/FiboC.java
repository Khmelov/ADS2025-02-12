package by.it.group451003.platonova.lesson01;

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
        if (n == 0) return 0;
        if (n == 1) return 1;
        long[] pisPeriod = getPisano(m);
        long periodLength = pisPeriod.length;
        long index = n % periodLength;
        return pisPeriod[(int) index];
    }
    long[] getPisano(int m) {
        long[] pisano = new long[6*m + 2];
        pisano[0] = 0;
        pisano[1] = 1;
        for (int i = 2; i < pisano.length; i++) {
            pisano[i] = (pisano[i - 1] + pisano[i - 2]) % m;
            if (pisano[i - 1] == 0 && pisano[i] == 1) {
                long[] period = new long[i-1];
                System.arraycopy(pisano, 0, period, 0, i - 1);
                return period;
            }
        }
        return pisano;
    }


}

