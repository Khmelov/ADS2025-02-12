package by.it.group451001.puzik.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    BigInteger fastB(Integer n) {
        ArrayList<BigInteger> fibList = new ArrayList<BigInteger>(n + 1);
        
        fibList.add(BigInteger.ZERO);
        fibList.add(BigInteger.ONE);

        for (int i = 2; i <= n; i++) {
            BigInteger nextFib = fibList.get(i - 1).add(fibList.get(i - 2));
            fibList.add(nextFib);
        }
        
        return fibList.get(n);
    }
}
