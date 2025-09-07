package by.it.group410902.siomchena.lesson01;

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
        //
        long curr = 1;
        long prev = 0;
        int per = 2;
        while (true){
            long oldCurr = curr;
            curr = (curr + prev) % m;
            prev = oldCurr;
            if (curr == 0 && prev ==1) break;
            per = per+1;

        }

        System.out.printf("%d\t",per);
        n = n % per;
        if (n <= 1) return n;

        curr = 1;
        prev = 0;

        for (int i = 2; i < n+1; i++){
            long oldCurr = curr;
            curr = (curr + prev) % m;
            prev = oldCurr;
        }

        return curr;
    }


}


