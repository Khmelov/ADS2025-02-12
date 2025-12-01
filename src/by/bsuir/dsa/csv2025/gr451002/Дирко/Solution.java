package by.bsuir.dsa.csv2025.gr451002.Дирко;

import org.junit.Test;

import java.math.BigInteger;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

public class Solution {
    public static String sieve(int x) {
        if (x <= 1) return "0";

        boolean[] isComposite = new boolean[x];

        for (int i = 2; i < Math.sqrt(x); i++) {
            for (int j = i * 2; j < isComposite.length; j += i)
                isComposite[j] = true;
        }

        BigInteger bigMul = new BigInteger("1");

        for (int i = 2; i < isComposite.length; i++)
            if (!isComposite[i]) bigMul = bigMul.multiply(BigInteger.valueOf(i));

        return bigMul.toString(10);
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        System.out.println(sieve(n));
    }

    @Test
    public void testA(){
        assertEquals(Solution.sieve(0), "0");
        assertEquals(Solution.sieve(-4), "0");
    }

    @Test
    public void testB(){
        assertEquals(Solution.sieve(10), "210");
        assertEquals(Solution.sieve(50), "614889782588491410");
        assertEquals(Solution.sieve(100), "2305567963945518424753102147331756070");
    }

    @Test(timeout = 1000)
    public void testC() {
        assertEquals(Solution.sieve(1000).length(), 416);
        assertEquals(Solution.sieve(10000).length(), 4298);
        assertEquals(Solution.sieve(100000).length(), 43293);
    }
}