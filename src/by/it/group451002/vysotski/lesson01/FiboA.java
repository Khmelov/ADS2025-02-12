

package by.it.group451002.vysotski.lesson01;

import java.math.BigInteger;

public class FiboA {
    private long startTime = System.currentTimeMillis();

    public FiboA() {
    }

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());
        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    public long time() {
        long res = System.currentTimeMillis() - this.startTime;
        this.startTime = System.currentTimeMillis();
        return res;
    }

    public int calc(int n) {
        int var10000;
        switch (n) {
            case 0 -> var10000 = 0;
            case 1 -> var10000 = 1;
            default -> var10000 = this.calc(n - 1) + this.calc(n - 2);
        }

        return var10000;
    }

    public BigInteger slowA(Integer n) {
        BigInteger var10000;
        switch (n) {
            case 0 -> var10000 = BigInteger.ZERO;
            case 1 -> var10000 = BigInteger.ONE;
            default -> var10000 = this.slowA(n - 1).add(this.slowA(n - 2));
        }

        return var10000;
    }
}
