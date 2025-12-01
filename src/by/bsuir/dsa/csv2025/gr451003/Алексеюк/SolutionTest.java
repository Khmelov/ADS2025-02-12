package by.bsuir.dsa.csv2025.gr451003.Алексеюк;

import org.junit.Test;
import static org.junit.Assert.*;

public class SolutionTest {

    @Test
    public void testSolution1() {
        String input = "counting";
        String expected = "cginnotu";
        String result = Solution.countingSort(input);
        assertEquals("Тест 1: counting -> cginnotu", expected, result);
    }

    @Test
    public void testSolution2() {
        String input = "algorithm";
        String expected = "aghilmort";
        String result = Solution.countingSort(input);
        assertEquals("Тест 2: algorithm -> aghilmort", expected, result);
    }

    @Test
    public void testSolution3() {
        String input = "a";
        String expected = "a";
        String result = Solution.countingSort(input);
        assertEquals("Тест 3: single character", expected, result);
    }

    @Test
    public void testSolution4() {
        String input = "zzzzaaaabbbbcccc";
        String expected = "aaaabbbbcccczzzz";
        String result = Solution.countingSort(input);
        assertEquals("Тест 4: blocks of same characters", expected, result);
    }

    @Test
    public void testSolution5() {
        String input = "banana";
        String expected = "aaabnn";
        String result = Solution.countingSort(input);
        assertEquals("Тест 5: banana -> aaabnn", expected, result);
    }

    @Test
    public void testSolution6() {
        String input = "hello";
        String expected = "ehllo";
        String result = Solution.countingSort(input);
        assertEquals("Тест 6: hello -> ehllo", expected, result);
    }

    @Test
    public void testSolution7() {
        String input = "programming";
        String expected = "aggimmnoprr";
        String result = Solution.countingSort(input);
        assertEquals("Тест 7: programming -> aggimmnoprr", expected, result);
    }

    @Test
    public void testSolution8() {
        String input = "test";
        String expected = "estt";
        String result = Solution.countingSort(input);
        assertEquals("Тест 8: test -> estt", expected, result);
    }

    @Test
    public void testSolution9() {
        String input = "zyxwvutsrqponmlkjihgfedcba";
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String result = Solution.countingSort(input);
        assertEquals("Тест 9: reverse alphabet", expected, result);
    }

    @Test
    public void testSolution10() {
        String input = "abcdefghijklmnopqrstuvwxyz";
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String result = Solution.countingSort(input);
        assertEquals("Тест 10: already sorted alphabet", expected, result);
    }

    @Test
    public void testEmptyString() {
        String input = "";
        String expected = "";
        String result = Solution.countingSort(input);
        assertEquals("Тест пустой строки", expected, result);
    }

    @Test
    public void testAllSameCharacters() {
        String input = "aaaaa";
        String expected = "aaaaa";
        String result = Solution.countingSort(input);
        assertEquals("Тест всех одинаковых символов", expected, result);
    }
}