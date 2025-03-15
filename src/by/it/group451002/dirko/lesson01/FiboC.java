package by.it.group451002.dirko.lesson01;

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
        long first = 0, second = 1, third, period = 0;

        // Поиск периода Пизано
        do {
            third = (first + second) % m;
            first = second;
            second = third;
            period++;
        }while(first != 0 || second != 1);

        // Находим остаток от деления n-ого числа Фибоначчи на m
        first = 0;
        second = 1;
        for(long i = 0; i < n % period; i++){
            third = (first + second) % m;
            first = second;
            second = third;
        }
        return first;
    }


}

