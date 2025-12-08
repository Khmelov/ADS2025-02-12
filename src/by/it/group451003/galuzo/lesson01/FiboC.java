package by.it.group451003.galuzo.lesson01;

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
        if (n == 0) return 0;
        if (n == 1) return 1;
        if (n < 6L * m + 2) {
            long i = 2, a = 1, b = 1;
            while (i < n - 1) {
                a = (a + b) % m;
                b = (b + a) % m;
                i += 2;
            }
            if (i == n)
                return b;
            return (b + a) % m;
        } else {
            long[] myCoolArray = new long[6 * m + 2];
            myCoolArray[0] = 0;
            myCoolArray[1] = 1;
            long temp, length;
            if (n + 1 < 6L * m + 2)
                length = n + 1;
            else
                length = 6L * m + 2;
            boolean notFound = true;
            temp = 0;
            for (int i = 2; i < length && notFound; i++) {
                temp = (myCoolArray[i - 1] + myCoolArray[i - 2]) % m;
                if (temp == 1 && myCoolArray[i - 1] == 0) {
                    temp = myCoolArray[(int) (n % (i - 1))];
                    notFound = false;
                }
                myCoolArray[i] = temp;
            }
            return temp;
        }
    }


}

