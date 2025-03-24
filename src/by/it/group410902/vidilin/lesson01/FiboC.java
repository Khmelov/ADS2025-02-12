package by.it.group410902.vidilin.lesson01;

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
        long var1 = 0, var2 = 1, temp, count = 0;
        do {
            temp = (var1 + var2) % m;
            var1 = var2;
            var2 = temp;
            count++;
        }
        while (var1 != 0 || var2 != 1);
        n %= count;

        for(int i = 1; i < n; i++){
            temp = (var1 + var2) % m;
            var1 = var2;
            var2 = temp;
        }
        return var2;
    }


}

