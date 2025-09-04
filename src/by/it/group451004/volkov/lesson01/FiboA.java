package by.it.group451004.volkov.lesson01;
import java.math.BigInteger;

/*
 * Вам необходимо выполнить рекурсивный способ вычисления чисел Фибоначчи
 */

public class FiboA {


    private long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
        FiboA fibo = new FiboA();
        int n = 33;
        System.out.printf("calc(%d)=%d \n\t time=%d \n\n", n, fibo.calc(n), fibo.time());

        //вычисление чисел фибоначчи медленным методом (рекурсией)
        fibo = new FiboA();
        n = 34;
        System.out.printf("slowA(%d)=%d \n\t time=%d \n\n", n, fibo.slowA(n), fibo.time());
    }

    private long time() {
        long res = System.currentTimeMillis() - startTime;
        startTime = System.currentTimeMillis();
        return res;
    }

    private int calc(int n) {
        int element1 = 1, element2 = 1;
        if (n < 3)
            return 1;
        for (int i = 3; i <= n; i++) {
            if (element1 < element2)
                element1 += element2;
            else
                element2 += element1;
        }
        return Math.max(element1, element2);
    }


    private BigInteger recCalc(BigInteger prev1, BigInteger prev2, Integer nCount, Integer n) {
        nCount++;
        return nCount.equals(n) ? prev1.add(prev2) : recCalc(prev2, prev1.add(prev2), nCount, n);
    }

    BigInteger slowA(Integer n) {
        //рекурсия
        //здесь нужно реализовать вариант без ограничения на размер числа,
        //в котором код совпадает с математическим определением чисел Фибоначчи
        //время O(2^n)

        Integer nCount = 2;
        if (n < 3)
            return BigInteger.ONE;
        else
            return recCalc(BigInteger.ONE, BigInteger.ONE, nCount, n);
    }

}
