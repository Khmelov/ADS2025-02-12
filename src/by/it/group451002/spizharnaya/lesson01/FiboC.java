package by.it.group451002.spizharnaya.lesson01;

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
        long curr, prev, temp;
        ArrayList<Long> modlist = new ArrayList<>();
        boolean flag = true;
        modlist.add(0L);
        modlist.add(1L);
        prev = 0;
        curr = 1;
        while (flag) {
            temp = curr;
            curr = curr+prev;
            prev = temp;

            if (prev % m == 0 && curr % m == 1)
                flag = false;
            else
                modlist.add(curr % m);
        }
        n %= modlist.size()-1;
        return modlist.get((int) n);
    }


}

