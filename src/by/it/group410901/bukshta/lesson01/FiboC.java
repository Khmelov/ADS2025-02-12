package by.it.group410901.bukshta.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 10;
        int m = 2;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }


    long fasterC(long n, int m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        if (n <= 1) {
            return n % m;
        }
        long prev = 0;
        long curr = 1;
        long result = 0;

        for (int i = 2; i < n + 1; i++) {
            result = (prev + curr) % m;
            prev = curr;
            curr = result;
            if (prev == 0 && curr == 1) {
                long period = i - 1;
                int remainder = (int) (n % period);
                return fasterC(remainder, m);
            }
        }
        return result;
    }


}

