package by.it.group451001.volynets.lesson01;

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
        if (n <= 1)
            return n;

        int period = findPisanoPeriod(m);
        n = n % period;

        // Теперь вычислим F(n) % m обычным способом, так как n уже не больше периода
        long prev = 0;
        long curr = 1;

        if (n == 0)
            return 0;

        for (int i = 2; i <= n; i++)
        {
            long temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
        }

        return curr;
    }

    // Функция для нахождения периода Пизано
    private int findPisanoPeriod(int m) {
        if (m == 1)
            return 1;

        int prev = 0;
        int curr = 1;
        int period = 0;

        // Период начинается с (0,1), поэтому ищем первое повторение (0,1)
        do
        {
            int temp = curr;
            curr = (prev + curr) % m;
            prev = temp;
            period++;
        } while (prev != 0 || curr != 1);

        return period;
    }



}

