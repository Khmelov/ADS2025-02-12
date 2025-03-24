package by.it.group410901.kliaus.lesson01;

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
        if (n == 0) return 0;
        if (n == 1) return 1;

        List<Long> arik = new ArrayList<>();
        long first = 0;
        long second = 1;
        arik.add(first);
        arik.add(second);

        for (int i = 2; i < m * 6; i++) {
            long next = (first + second) % m;
            arik.add(next);
            first = second;
            second = next;

            if (first == 0 && second == 1) {
                break;
            }
        }
        return arik.get((int)n % (arik.size() - 2));
    }


}

