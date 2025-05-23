package by.it.group451003.chveikonstantcin.lesson01;

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
        if (m == 1) {
            return 0;
        }

        long period = findPisanoPeriod(m);

        long pos = n % period;

        if (pos == 0) {
            return 0;
        }

        long prev = 0;
        long curr = 1;
        for (long i = 2; i <= pos; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr % m;
    }

    private long findPisanoPeriod(int m) {
        long prev = 0;
        long curr = 1;
        long period = 0;

        for (long i = 0; i < m * 6; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;

            if (prev == 0 && curr == 1) {
                period = i + 1;
                break;
            }
        }

        return period;
    }


}

