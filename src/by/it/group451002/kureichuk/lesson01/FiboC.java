package by.it.group451002.kureichuk.lesson01;

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
        if(n == 0)
            return 0;
        if(n == 1)
            return 1;

        int prev = 0, curr = 1, period = 0;

        for(int i = 0; i < m*m; i++)
        {
            int temp = (curr + prev) % m;
            prev = curr;
            curr = temp;

            if(prev == 0 && curr == 1){
                period = i + 1;
                break;
            }
        }

        n %= period;

        for (int i = 2; i <= n; i++){
            int temp = (curr + prev) % m;
            prev = curr;
            curr = temp;
        }

        return curr;
    }
}


