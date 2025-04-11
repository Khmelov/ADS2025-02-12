package by.it.group451002.spitsyna.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

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

        long prev = 0;
        long curr = 1;
        ArrayList<Long> arr = new ArrayList<>();
        arr.add(0L);
        arr.add(1L);
        boolean flag = false;

        while (!flag) {
            long tem = curr;
            curr += prev;
            prev = tem;

            if (prev % m == 0 && curr % m == 1)
                flag = true;
            else
                arr.add(curr % m);
        }
        n %= arr.size() - 1;
        return arr.get((int) n);
    }
}
