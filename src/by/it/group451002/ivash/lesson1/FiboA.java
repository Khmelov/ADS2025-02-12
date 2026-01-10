package by.it.group451002.ivash.lesson1;

import java.math.BigInteger;

public class FiboA {
    private long startTime = System.currentTimeMillis();

    public FiboA() {
    }

    public static void main(String[] args) {
        int n = 33;
        n = 34;
    }

    private long time() {
        long res = System.currentTimeMillis() - this.startTime;
        this.startTime = System.currentTimeMillis();
        return res;
    }

    private int calc(int n) {
        //здесь простейший вариант, в котором код совпадает
        //с математическим определением чисел Фибоначчи
        //время O(2^n)
        if (n < 2) return n;
        return calc(n-1) + calc(n-2);
    }

    BigInteger slowA(Integer n) {
        //рекурсия
        //здесь нужно реализовать вариант без ограничения на размер числа,
        //в котором код совпадает с математическим определением чисел Фибоначчи
        //время O(2^n)
        if (n <= 0) return BigInteger.ZERO;
        if (n == 1) return BigInteger.ONE;
        return slowA(n-1).add(slowA(n-2));
    }
}