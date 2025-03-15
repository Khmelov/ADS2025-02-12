package by.it.group410902.kavtsevich.lesson01;

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
        if (n<=1)
        return n;
        long[] fib = new long[m*6];
        fib[0]=0;
        fib[1]=1;
        long period=0;
        for(int i=2; i<fib.length; i++)
        {
            fib[i]=(fib[i-1]+fib[i-2])%m;
            if(fib[i]==1 && fib[i-1]==0){
                period=i-1;
                break;
            }
        }
        long remainder=n%period;
        return fib[(int) remainder];
        }

    }

