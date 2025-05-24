package by.it.group410902.sulimov.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

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

        long n1 = 0, n2 = 1, temp, len = 0;

        do {
            temp = (n1 + n2) % m;
            n1 = n2;
            n2 = temp;
            len++;


        }
        while (n1 != 0 || n2 != 1);

        n %= len;

        for(int i = 1; i < n; i++){
            temp = (n1 + n2) % m;
            n1 = n2;
            n2 = temp;
        }

        return n2;
    }

}

