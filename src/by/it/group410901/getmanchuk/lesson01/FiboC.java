package by.it.group410901.getmanchuk.lesson01;

import java.util.ArrayList;
import java.util.List;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (m == 1) return 0;
        if (n == 1) return 1;

        // Находим период Пизано для m
        List<Long> pisano = new ArrayList<>();
        pisano.add(0L);
        pisano.add(1L);

        for (int i = 2; i < m * 6; i++) {
            long next = (pisano.get(i - 1) + pisano.get(i - 2)) % m;
            pisano.add(next);

            // Проверяем, не начался ли новый период
            if (pisano.get(i) == 1 && pisano.get(i - 1) == 0) {
                pisano.remove(i);
                pisano.remove(i - 1);
                break;
            }
        }

        int period = pisano.size();
        int index = (int)(n % period);

        return pisano.get(index);
    }
}