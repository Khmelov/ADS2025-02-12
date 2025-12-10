package by.bsuir.dsa.csv2025.gr410901.Ковалевич;

import java.util.Scanner;

import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;

public class Solution {

    public static int[] prefixFunction(String s) {
        int n = s.length();
        int[] pi = new int[n];
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s.charAt(i) != s.charAt(j)) {
                j = pi[j - 1];
            }
            if (s.charAt(i) == s.charAt(j)) {
                j++;
            }
            pi[i] = j;
        }
        return pi;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        int[] pi = prefixFunction(s);
        for (int x : pi) {
            System.out.print(x + " ");
        }
    }

    
    @Test
    public void test1() {
        String s = "aaaa";
        int[] expected = {0, 1, 2, 3};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void test2() {
        String s = "abcd";
        int[] expected = {0, 0, 0, 0};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void test3() {
        String s = "ababc";
        int[] expected = {0, 0, 1, 2, 0};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void test4() {
        String s = "aabaaab";
        int[] expected = {0, 1, 0, 1, 2, 2, 3};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void test5() {
        String s = "abcabcabc";
        int[] expected = {0, 0, 0, 1, 2, 3, 4, 5, 6};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void test6() {
        String s = "abababac";
        int[] expected = {0, 0, 1, 2, 3, 4, 5, 0};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void test7() {
        String s = "xyzxyzxy";
        int[] expected = {0, 0, 0, 1, 2, 3, 4, 5};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }
    
    @Test
    public void test8() {
        String s = "a";
        int[] expected = {0};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void test9() {
        String s = "";
        int[] expected = {};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void test10() {
        String s = "abacaba";
        int[] expected = {0, 0, 1, 0, 1, 2, 3};
        int[] actual = prefixFunction(s);
        assertArrayEquals(expected, actual);
    }
}

