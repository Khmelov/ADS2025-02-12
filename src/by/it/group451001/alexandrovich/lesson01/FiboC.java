package by.it.group451001.alexandrovich.lesson01;

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
        int p = 0;
        long[] rems = new long[6*m*m+2];
        rems[0] = 0;
        rems[1] = 1;
        rems[2] = 1;
        long prev2 = 0;
        long prev1 = 1;
        long num;
       for (int i = 2; i < m*m+3; i++){
           num = prev1 + prev2;
           prev2 = prev1;
           prev1 = num;
           rems[i] = num % m;
            if ((rems[i-1] == 0)&&(rems[i] == 1)){
                p = i-1;
                break;
            }
        }
       return rems[(int)n%p];
    }
}

