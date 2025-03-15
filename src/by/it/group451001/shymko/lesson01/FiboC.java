package by.it.group451001.shymko.lesson01;

/*
 * Даны целые числа 1<=n<=1E18 и 2<=m<=1E5,
 * необходимо найти остаток от деления n-го числа Фибоначчи на m
 * время расчета должно быть не более 2 секунд
 */

public class FiboC {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboC fibo = new FiboC();
        int n = 999999999;
        int m = 321;
        System.out.printf("fasterC(%d)=%d \n\t time=%d \n\n", n, fibo.fasterC(n, m), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    long fasterC(long n, long m) {
        //Интуитивно найти решение не всегда просто и
        //возможно потребуется дополнительный поиск информации
        long[][] matr = {{1, 1},
                         {1, 0}};
        n += 2;
        while(n > 1){
            long[][] temp = {{matr[0][0], matr[0][1]}, {matr[1][0], matr[1][1]}};
            if(n % 2 == 0){
                matr[0][0] = (temp[0][0]*temp[0][0] % m + temp[0][1]*temp[1][0] % m) % m;
                matr[0][1] = (temp[0][0]*temp[0][1] % m + temp[0][1]*temp[1][1] % m) % m;
                matr[1][0] = (temp[1][0]*temp[0][0] % m + temp[1][1]*temp[1][0] % m) % m;
                matr[1][1] = (temp[1][0]*temp[0][1] % m + temp[1][1]*temp[1][1] % m) % m;
                n /= 2;
            }
            else {
                matr[0][0] = (temp[0][0] + temp[0][1]) % m;
                matr[0][1] = (temp[0][0]) % m;
                matr[1][0] = (temp[1][0] + temp[1][1]) % m;
                matr[1][1] = (temp[1][0]) % m;
                n--;
            }
        }
        return matr[0][0];
    }


}

