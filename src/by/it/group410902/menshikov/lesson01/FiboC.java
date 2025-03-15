package by.it.group410902.menshikov.lesson01;

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
        long[][] matrix = {{1, 1}, {1, 0}};
        long[][] result = {{1, 0}, {0, 1}};
        n--;
        while (n > 0) {
            if (n % 2 == 1) {
                result = multiply(result, matrix, m);
            }
            matrix = multiply(matrix, matrix, m);
            n /= 2;
        }
        return result[0][0];
    }

    private long[][] multiply(long[][] a, long[][] b, int m) {
        long[][] c = new long[2][2];
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                for (int k = 0; k < 2; k++) {
                    c[i][j] += a[i][k] * b[k][j];
                }
                c[i][j] %= m;
            }
        }
        return c;
    }
}

