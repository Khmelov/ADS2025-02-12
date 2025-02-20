package by.it.group451001.ivanov.lesson01;

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
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        int[] arr =new int[6*m+2];
        arr[0] = 0;
        arr[1] = 1;
        if(n == 1) return 1;
        int p=0;
        boolean flag;
        flag = true;
        for (int i = 2; (i < ((6 * m) + 2)) && flag; i++){ arr[i] = (arr[i - 1] + arr[i - 2]) % m;
            if (arr[i] == 1 && arr[i-1] == 0) {
                p = arr[(int) (n % (i - 1))];
                flag = false;
            }
        }
        return p;
    }


}

