package by.it.group410902.varava;

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
        // Step 1: Find the Pisano period for m
        int pisanoPeriod = getPisanoPeriod(m);

        // Step 2: Reduce n using the Pisano period
        int reducedN = (int) (n % pisanoPeriod);

        // Step 3: Compute the Fibonacci number modulo m for the reduced index
        return getFibonacciModulo(reducedN, m);
    }

    // Calculate the Pisano period for m
    private int getPisanoPeriod(int m) {
        int previous = 0;
        int current = 1;

        for (int i = 0; i < m * m; i++) {
            int temp = current;
            current = (previous + current) % m;
            previous = temp;

            // The sequence repeats with 0, 1
            if (previous == 0 && current == 1) {
                return i + 1; // Length of Pisano period
            }
        }
        return 0; // Default value in case something goes wrong
    }

    // Calculate Fibonacci modulo m for reduced n
    private long getFibonacciModulo(int n, int m) {
        if (n <= 1) {
            return n; // Return F(0) or F(1) directly
        }

        long previous = 0;
        long current = 1;

        for (int i = 2; i <= n; i++) {
            long temp = current;
            current = (previous + current) % m;
            previous = temp;
        }
        return current;
    }



}

