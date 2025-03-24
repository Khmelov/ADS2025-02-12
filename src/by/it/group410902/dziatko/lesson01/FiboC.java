package by.it.group410902.dziatko.lesson01;

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
        if(n == 0) return 0L;
        else if (n == 1) return 1L;

        long[] Pizano = new long[m*6];
        Pizano[0] = 0L;
        Pizano[1] = 1L;

        for (int i = 2; i < m*6; i++) {
            Pizano[i] = (Pizano[i-1] + Pizano[i-2]) % m;
            if(Pizano[i] == 1 && Pizano[i-1] == 0){
                return Pizano[(int) (n%(i-1))];
            }
        }
        return -1L;
    }


}