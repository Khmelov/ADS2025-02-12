package by.it.group451001.russu.lesson01;

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

        if (n == 0) return 0;
        else if (n==1) return 1;
        else
        {
            ArrayList<Integer> fiblist = new ArrayList<>();
            fiblist.add(0);fiblist.add(1);
            for(int i = 2; i <= n; i++)
            {
                fiblist.add( (fiblist.get(i-1) + (fiblist.get(i-2))) % m );

                if ((fiblist.get(i) == 1) && (fiblist.get(i-1) == 0))
                    break;
            }
            int period = fiblist.size()-2;
            int ind = (int)(n % period);
            return (long) fiblist.get(ind);
        }

    }


}

