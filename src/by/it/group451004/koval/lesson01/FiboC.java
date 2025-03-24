package by.it.group451004.koval.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

import java.util.ArrayList;
import java.util.List;

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
        long prev = 0, curr = 1;
        if (n <= 1) return n % m;
        List<Long> pisanoPeriod = new ArrayList<>();
        pisanoPeriod.add(0L);
        pisanoPeriod.add(1L);
        for (int i = 2; i < m * 6; i++) {
            long next = curr;
            curr += prev;
            prev = next;
            if (prev % m == 0 && curr % m == 1)
                break;
            else
                pisanoPeriod.add(curr % m);
        }
        n %= pisanoPeriod.size() - 1;
        return pisanoPeriod.get((int) n);
    }
}

