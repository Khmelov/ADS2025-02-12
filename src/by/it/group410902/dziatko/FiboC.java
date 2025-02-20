package by.it.group410902.dziatko;

import java.math.BigInteger;
/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

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

   // public static BigInteger[][] ONE = {{BigInteger.ZERO, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ONE}};
    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        long resault;
        resault = pow(ONE, n)[0][1].intValue();//m;
        return resault;
    }

    public final static BigInteger[][] ONE = {{BigInteger.ZERO, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ONE}};
    public static BigInteger[][] mul(BigInteger[][] a, BigInteger[][] b) {
        BigInteger[][] res = {
                {a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0])), a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]))},
                {a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0])), a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]))}
        };
        return res;
    }
    public static BigInteger[][] pow(BigInteger[][] a, long k) {

        if (k == 0) return ONE;
        if (k == 1) return a;
        if (k == 2) return mul(a, a);
        if (k % 2 == 1) return mul(ONE, pow(a, k - 1));
        return pow(pow(a, k / 2), 2);
    }

}

