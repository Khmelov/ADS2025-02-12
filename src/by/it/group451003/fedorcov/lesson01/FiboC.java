package by.it.group451003.fedorcov.lesson01;

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        long n = 55555;
        int m = 1000;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, int m) {
        if (n == 0) return 0;
        if (n == 1) return 1;

        int period = findPisanoPeriod(m);

        long effectiveIndex = n % period;

        if (effectiveIndex == 0) return 0;
        if (effectiveIndex == 1) return 1;

        long prev = 0;
        long curr = 1;
        for (long i = 2; i <= effectiveIndex; i++) {
            long next = (prev + curr) % m;
            prev = curr;
            curr = next;
        }

        return curr;
    }

    private int findPisanoPeriod(int m) {
        long prev = 0;
        long curr = 1;
        int period = 0;

        for (int i = 0; i < m * m; i++) {
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