package by.it.group451002.kravtsov.lesson1;

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
        if (n<=1) return n;
        long[] PP = GetPP(m);
        int periodLength = PP.length;
        long remainder = n % periodLength;
        return PP[(int) remainder];
    }


    private long[] GetPP(int m) {
        long[] PP = new long[m*m];
        PP[0] = 0;
        PP[1] = 1;

        for (int i = 2; i < m*m; i++) {
            PP[i] = (PP[i - 1] + PP[i - 2]) % m;

            if (PP[i] == 1 && PP[i - 1] == 0) {
                long[] Res = new long[i - 1];
                System.arraycopy(PP, 0, Res, 0, i - 1);
                return Res;
            }
        }
        return PP;
    }

}

