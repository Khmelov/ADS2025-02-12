package by.it.group451002.karbanovich.lesson01;

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

    long fasterC(long n, int m)
    {
        int[] periodNums = new int[m * m + 1];
        boolean periodIsFound = false;
        int period = 1;
        int old = 0;
        int curr = 1;
        int new1;
        int new2;

        periodNums[0] = 0;
        periodNums[1] = 1;
        int count = 2;

        while (!periodIsFound)
        {
            new1 = curr;
            new2 = (old + curr) % m;
            old = new1;
            curr = new2;
            periodNums[count] = new2;

            if (new1 == 0 && new2 == 1)
                periodIsFound = true;
            else
                period++;
            count++;
        }
        int i = (int)n % period;
        return periodNums[i];
    }
}

