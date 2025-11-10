package by.it.group451001.zabelich.lesson01;

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
        int[] fibo = new int[m*m+1];
        int k=0;
        fibo[0] = 0;
        fibo[1] = 1;
        for (int i=2;i<=m*m;i++){
            fibo[i] = (fibo[i-2]+fibo[i-1])%m;
            if ((fibo[i-1]==0)&&(fibo[i]==1)) {
                k = i-1;
                break;
            }
        }
        long ans=0;
        for (int i=0;i<k;i++){
           if ((n-i)%k==0) {
               ans = fibo[i];
               break;
           }
        }
        return ans;
    }


}

