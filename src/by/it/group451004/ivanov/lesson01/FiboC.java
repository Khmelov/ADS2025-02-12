package by.it.group451004.ivanov.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 10;
        int m = 2;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        long answer = 1;
        long lastValue = 0;

        while (n > 1) {
            n--;

            lastValue += answer;
            while (lastValue >= m)
                lastValue -= m;

            lastValue = lastValue ^ answer;
            answer = lastValue ^ answer;
            lastValue = lastValue ^ answer;
        }

        return answer % m;
    }


}

