package by.it.group410902.harkavy.lesson01;

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
        if (n == 0) return 0;
        if( n== 1) return 1;

        int nom1 = 0, nom2 = 1;
        int per = 0;
        while (true){
            int for_time = nom2;
            nom2 = (nom2 + nom1) % m;
            nom1 = for_time;
            per ++;
            if((nom1 == 0) && (nom2 == 1)) break;
        }

        n = n % per;

        nom1 = 0;
        nom2 = 1;
        for(int i = 2; i <= n; i++){
            int temp = nom2;
            nom2 = (nom2 + nom1) % m;
            nom1 = temp;
        }


        return nom2;
    }


}

