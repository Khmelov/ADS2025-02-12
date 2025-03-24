package by.it.group451002.morozov.lesson01;

import java.math.BigInteger;

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
    	
    	//F(n) % m = F(n % pi(m)) % m
    	
    	//Расчёт периода Пизано pi(m)
    	
    	long pis_f_prpr = 0;
    	long pis_f_prev = 1;
    	long pis_f_curr = 0;
    	
    	long pis = 0;
    	
    	do {
    		pis_f_curr = (pis_f_prpr + pis_f_prev) % m;
    		pis_f_prpr = pis_f_prev;
    		pis_f_prev = pis_f_curr;
    		pis++;
    	} while ((pis_f_prpr != 0) || (pis_f_prev != 1));
    	
    	// Определение нового n
    	long n_new = n % pis;
    	
    	// Расчёт числа Фибоначчи с новым номером n 
    	BigInteger fib_pp = BigInteger.ZERO;
    	BigInteger fib_p = BigInteger.ONE;
    	BigInteger fib_c = BigInteger.ZERO;
    	
    	if (n_new == 0) {
    		return 0;
    	} else if (n_new == 1) {
    		return 1;
    	} else {
    		for (long c = 2; c <= n_new; c++) {
    			fib_c = fib_pp.add(fib_p);
    			fib_pp = fib_p;
    			fib_p = fib_c;
    		}
    		return fib_c.mod(BigInteger.valueOf(m)).longValue();
    	}
    }

    
}

