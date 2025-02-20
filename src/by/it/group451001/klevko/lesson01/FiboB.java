package by.it.group451001.klevko.lesson01;

import java.math.BigInteger;
import java.util.ArrayList;

/*
 * Вам необходимо выполнить способ вычисления чисел Фибоначчи со вспомогательным массивом
 * без ограничений на размер результата (BigInteger)
 */

public class FiboB {

    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        //вычисление чисел простым быстрым методом
        FiboB fibo = new FiboB();
        int n = 55555;
        System.out.printf("fastB(%d)=%d \n\t time=%d \n\n", n, fibo.fastB(n), fibo.time());
    }

    private long time() {
        return System.currentTimeMillis() - startTime;
    }

    ArrayList<BigInteger> fibNum = new ArrayList<>(1000000);

    /*BigInteger FindFib(Integer k){
        if (fibNum.size()-1 >= k) {
            return fibNum.get(k);
        }
        else {
            BigInteger my1 = FindFib(k-1);
            BigInteger my2 = FindFib(k-2);
            BigInteger my = my1.add(my2);
            fibNum.add(k, my);
            return fibNum.get(k);
        }
    }*/

    BigInteger fastB(Integer n) {
        fibNum.add(0, BigInteger.ZERO);
        fibNum.add(1, BigInteger.ONE);
        for(int i = 2; i <= n; i++){
            fibNum.add(fibNum.get(i-1).add(fibNum.get(i-2)));
        }
        //System.out.println(FindFib(55555));
        //System.out.println("hello world " + fibNum.get(0) + " " + fibNum.get(1) + " " + fibNum.size());
        return fibNum.get(n);
    }

}

