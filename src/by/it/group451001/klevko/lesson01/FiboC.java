package by.it.group451001.klevko.lesson01;

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
        int n = 999999999;
        int m = 321;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }



    long fasterC(long n, int m) {
        //long myStartTime = System.currentTimeMillis();
        ArrayList<Integer> fibNum = new ArrayList<>(10000);
        if (n == 0) return 0L;
        if (n == 1) return 1L;
        fibNum.add(0, 0);
        fibNum.add(1, 1);
        for(int i = 2; i <= n; i++){
            fibNum.add( (fibNum.get(i-1) + (fibNum.get(i-2))) % m );
            if ((fibNum.get(i) == 1) && (fibNum.get(i-1) == 0)) {
                break;
            }
        }
        //System.out.println((long) fibNum.get( (int) (n % (fibNum.size()-2))));
        //System.out.println(System.currentTimeMillis() - myStartTime);
        //if ((long) fibNum.get( (int) (n % (fibNum.size()-2))) == 34L) System.out.println("yes");
        return (long) fibNum.get( (int) (n % (fibNum.size()-2)));
    }


}

