package by.it.group451004.matyrka.lesson01;

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
        if (n <= 1) return n % m;

        long prev = 0;
        long curr = 1;

        long period = 0;
        do {
            long tmp = (prev + curr) % m;
            prev = curr;
            curr = tmp;
            period++;
        } while (!(prev == 0 && curr == 1));

        n %= period;

        prev = 0;
        curr = 1;
        if (n == 0) return 0;

        for (int i = 1; i < n; i++) {
            long tmp = (prev + curr) % m;
            prev = curr;
            curr = tmp;
        }
        return curr;
    }
}
