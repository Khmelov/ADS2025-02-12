package by.it.group410902.derzhavskaya_ludmila.lesson01;

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
        if(n==0)
            return 0L;
        if(n==1)
            return 1L;
        long []a=new long[6*m];
        a[0]=0;
        a[1]=1;
        for(int i=2; i<=6*m; i++)
        {
            a[i]=(a[i-1]+a[i-2])%m;
            if(a[i]==1 && a[i-1]==0)
                return a[(int) (n%(i-1))];
        }
        return -1L;
    }


}

