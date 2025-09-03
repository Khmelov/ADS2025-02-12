package by.it.group451002.shutko.lesson01;

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
        long first_number = 0, second_number = 1;
        ArrayList<Long> lon = new ArrayList<>();
        lon.add(0L);
        lon.add(1L);
        boolean swap_flag = true;

        while (swap_flag) {
            long buffer = second_number;
            second_number += first_number;
            first_number = buffer;

            if (first_number % m == 0 && second_number % m == 1)
                swap_flag = false;
            else
                lon.add(second_number % m);
        }

        n %= lon.size() - 1;
        return lon.get((int) n);
    }


}

