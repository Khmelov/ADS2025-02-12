package by.it.group410901.evtuhovskaya.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    final private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    // Умножение двух матриц 2x2 по модулю mod
    private static long[][] matrixMultiply(long[][] a, long[][] b, long mod) {
        long[][] result = new long[2][2];
        result[0][0] = (a[0][0] * b[0][0] + a[0][1] * b[1][0]) % mod;
        result[0][1] = (a[0][0] * b[0][1] + a[0][1] * b[1][1]) % mod;
        result[1][0] = (a[1][0] * b[0][0] + a[1][1] * b[1][0]) % mod;
        result[1][1] = (a[1][0] * b[0][1] + a[1][1] * b[1][1]) % mod;
        return result;
    }

    // Быстрое возведение матрицы 2x2 в степень power по модулю mod
    private static long[][] matrixPower(long[][] mat, long power, long mod) {
        long[][] result = {{1, 0}, {0, 1}}; // Единичная матрица
        while (power > 0) {
            if (power % 2 == 1) {
                result = matrixMultiply(result, mat, mod);
            }
            mat = matrixMultiply(mat, mat, mod);
            power /= 2;
        }
        return result;
    }

    public static long fibonacciMod(long n, long m) {
        if (n == 0) {
            return 0;
        }
        long[][] mat = {{1, 1}, {1, 0}};
        long[][] resultMat = matrixPower(mat, n - 1, m);
        return resultMat[0][0];
    }

    long fasterC(long n, int m) {
        return fibonacciMod(n, m);

    }
}