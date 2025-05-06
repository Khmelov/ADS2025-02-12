package by.it.group451002.kita.lesson01;

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
        long num1 = 0, num2 = 1, num3 = 1;
        ArrayList<Long> arr = new ArrayList<>();
        arr.add(0L);
        arr.add(1L);
        boolean flag = true;

        while (flag) {
            num3 = num2;
            num2 += num1;
            num1 = num3;

            if (num1 % m == 0 && num2 % m == 1)
                flag = false;
            else arr.add(num2 % m);
        }
        n %= (arr.size() - 1);
        return arr.get((int)n);
    }


}

