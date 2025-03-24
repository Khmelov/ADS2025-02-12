package by.it.group410902.sivtsov.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.math.BigInteger;

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
        long cur = 1;
        long prev = 0;
        int period = 0;
        while(true){
            long oldcur = cur;
            cur = (prev + cur) % m;
            prev = oldcur;
            if(cur == 1 && prev == 0){
                break;
            }
            period++;
        }

        period = period + 2;
        n = n % period;
        if (n <= 1) {
            return n;
        }
            cur = 1;
            prev = 0;

            for(int i = 2; i <= n; i++){
                long oldcur = cur;
                cur = (prev + cur) % m;
                prev = oldcur;
            }

        return cur;
    }


}

