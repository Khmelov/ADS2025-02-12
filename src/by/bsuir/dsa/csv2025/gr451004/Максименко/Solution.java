package by.bsuir.dsa.csv2025.gr451004.Максименко;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;


public class Solution {
    public static int countUniqueWords(String line) {
        line = line.trim();
        if (line.isEmpty()) return 0;

        String[] words = line.split("\\s+");
        Set<String> unique = new HashSet<>(Arrays.asList(words));

        return unique.size();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String line = sc.nextLine();

        int result = countUniqueWords(line);
        System.out.println(result);
    }

    @Test
    public void test1() {
        assertEquals(3, Solution.countUniqueWords("апельсин яблоко яблоко груша апельсин"));
    }

    @Test
    public void test2() {
        assertEquals(2, Solution.countUniqueWords("a a A A a"));
    }

    @Test
    public void test3() {
        assertEquals(3, Solution.countUniqueWords("dog cat dog bird cat cat"));
    }

    @Test
    public void test4() {
        assertEquals(5, Solution.countUniqueWords("one two three four five"));
    }

    @Test
    public void test5() {
        assertEquals(2, Solution.countUniqueWords("hello   world   hello"));
    }

    @Test
    public void test6() {
        assertEquals(3, Solution.countUniqueWords("123 123 456 789 456"));
    }

    @Test
    public void test7() {
        assertEquals(4, Solution.countUniqueWords("Кот кот КОТ котик"));
    }

    @Test
    public void test8() {
        assertEquals(3, Solution.countUniqueWords("x y z x y x y z"));
    }

    @Test
    public  void test9() {
        assertEquals(1, Solution.countUniqueWords("word"));
    }

    @Test
    public void test10() {
        assertEquals(2, Solution.countUniqueWords("w WW w WW"));
    }
}
