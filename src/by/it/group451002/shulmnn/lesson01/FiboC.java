package by.it.group451002.shulmnn.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;
import java.util.ArrayList;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if(n > 2) {
            long[] f = powMatrix(n - 2, m, new long[]{0, 1, 1, 1});
            return (f[1] + f[3]) % m;
        }
        else{
            return 1 % m;
        }
    }

    long[] powMatrix(long a, long m, long[] Matrix)
    {
        long[] MatrixN = new long[]{0,1,1,1};
        if (a == 1) return MatrixN;
        if (a % 2 == 0) {
            MatrixN = powMatrix(a / 2, m, Matrix);
            return new long[]{(MatrixN[0] * MatrixN[0] % m + MatrixN[1] * MatrixN[2] % m) % m,
                    (MatrixN[0] * MatrixN[1] % m + MatrixN[1] * MatrixN[3] % m) % m,
                    (MatrixN[2] * MatrixN[0] % m + MatrixN[3] * MatrixN[2] % m) % m,
                    (MatrixN[2] * MatrixN[1] % m + MatrixN[3] * MatrixN[3] % m) % m};
        }
        else {
            Matrix = powMatrix(a - 1, m, Matrix);
            return new long[]{(MatrixN[0] * Matrix[0] % m + MatrixN[1] * Matrix[2] % m) % m,
                    (MatrixN[0] * Matrix[1] % m + MatrixN[1] * Matrix[3] % m) % m,
                    (MatrixN[2] * Matrix[0] % m + MatrixN[3] * Matrix[2] % m) % m,
                    (MatrixN[2] * Matrix[1] % m + MatrixN[3] * Matrix[3] % m) % m};
        }
    }


}

