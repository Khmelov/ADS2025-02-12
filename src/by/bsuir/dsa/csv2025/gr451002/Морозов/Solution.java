package by.bsuir.dsa.csv2025.gr451002.Морозов;



import java.math.BigInteger;
import java.util.Scanner;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Solution {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String s1 = sc.next();
		String s2 = sc.next();
		String resStr = "";
		if (s1.length() <= 50 && s2.length() <= 50) {
			try {
				BigInteger a = new BigInteger(s1, 16);
				BigInteger b = new BigInteger(s2, 16);
				BigInteger res = karatsuba(a, b);
				resStr = res.toString(16).toUpperCase();
			} catch (Exception e) {
				System.out.println("Error");
				sc.close();
			}
		} else {
			resStr = "Error";
		}
		System.out.println(resStr);
		sc.close();
	}
	
	static BigInteger karatsuba(BigInteger a, BigInteger b) {
		
		// Для небольших чисел сразу возвращаем "тривиальное" произведение
        if (a.compareTo(BigInteger.valueOf(16)) < 0 || b.compareTo(BigInteger.valueOf(16)) < 0) {
            return a.multiply(b);
        }

        int size = Math.max(a.toString().length(), b.toString().length());
        int half = size / 2;

        BigInteger value = BigInteger.valueOf(16).pow(half);
        BigInteger highA = a.divide(value);
        BigInteger lowA = a.mod(value);
        BigInteger highB = b.divide(value);
        BigInteger lowB = b.mod(value);

        // Расчёт многочленов
        BigInteger p1 = karatsuba(lowA, lowB);
        BigInteger p2 = karatsuba(highA, highB);
        BigInteger t = karatsuba(lowA.add(highA), lowB.add(highB));

        return p2.multiply(BigInteger.valueOf(16).pow(2 * half))
                .add((t.subtract(p2).subtract(p1)).multiply(value))
                .add(p1);
    }
	
	@Test
	public void test1() {
		BigInteger a = new BigInteger("8", 16);
		BigInteger b = new BigInteger("A", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test2() {
		BigInteger a = new BigInteger("0", 16);
		BigInteger b = new BigInteger("CDa", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test3() {
		BigInteger a = new BigInteger("1", 16);
		BigInteger b = new BigInteger("CDa", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test4() {
		BigInteger a = new BigInteger("D99", 16);
		BigInteger b = new BigInteger("000", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test5() {
		BigInteger a = new BigInteger("a34", 16);
		BigInteger b = new BigInteger("5", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test6() {
		BigInteger a = new BigInteger("777777777777777777777777", 16);
		BigInteger b = new BigInteger("999999999999999999999999", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test7() {
		BigInteger a = new BigInteger("9F", 16);
		BigInteger b = new BigInteger("0", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test8() {
		BigInteger a = new BigInteger("abc", 16);
		BigInteger b = new BigInteger("def", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test9() {
		BigInteger a = new BigInteger("10101", 16);
		BigInteger b = new BigInteger("202020", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
	
	@Test
	public void test10() {
		BigInteger a = new BigInteger("12300000", 16);
		BigInteger b = new BigInteger("456", 16);
		assertEquals(a.multiply(b), karatsuba(a, b));
	}
}