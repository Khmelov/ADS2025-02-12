package by.bsuir.dsa.csv2025.gr451002.Шандриченко;
// Tests
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SolutionTest {

    @Test
    public void testEmptyString() {
        assertEquals(0, Solution.findSolution(""));
    }

    @Test
    public void testSingleCharacter() {
        assertEquals(1, Solution.findSolution("a"));
        assertEquals(1, Solution.findSolution("x"));
    }

    @Test
    public void testRepeatedSingleCharacter() {
        assertEquals(1, Solution.findSolution("aaaaa"));
        assertEquals(1, Solution.findSolution("xxxxx"));
    }

    @Test
    public void testPeriodicStrings() {
        // "ababab" - период "ab" длиной 2
        assertEquals(2, Solution.findSolution("ababab"));
        
        // "abcabcabc" - период "abc" длиной 3
        assertEquals(3, Solution.findSolution("abcabcabc"));
        
        // "xyzxyz" - период "xyz" длиной 3
        assertEquals(3, Solution.findSolution("xyzxyz"));
    }

    @Test
    public void testNonPeriodicStrings() {
        // "abcde" - нет периода кроме полной строки
        assertEquals(5, Solution.findSolution("abcde"));
        
        // "aabaa" - нет периода кроме полной строки
        assertEquals(5, Solution.findSolution("aabaa"));
    }

    @Test
    public void testTwoCharacterPatterns() {
        assertEquals(2, Solution.findSolution("abab"));
        assertEquals(2, Solution.findSolution("0101"));
        assertEquals(2, Solution.findSolution("+-+-"));
    }

    @Test
    public void testThreeCharacterPatterns() {
        assertEquals(3, Solution.findSolution("abcabc"));
        assertEquals(3, Solution.findSolution("123123"));
    }

    @Test
    public void testIncompletePeriod() {
        // "abcab" - последний период не завершен
        assertEquals(5, Solution.findSolution("abcab"));
        
        // "ababa" - похоже на период "ab", но не совсем
        assertEquals(5, Solution.findSolution("ababa"));
    }

    @Test
    public void testLongerPeriods() {
        assertEquals(4, Solution.findSolution("abcdabcd"));
        assertEquals(6, Solution.findSolution("abcdefabcdef"));
    }

    @Test
    public void testEdgeCases() {
        // Строка где префикс-функция дает кандидата, но длина не делится на него
        assertEquals(5, Solution.findSolution("aabca"));
        
        // Все символы одинаковые
        assertEquals(1, Solution.findSolution("mmmmm"));
    }



    @Test
    public void testIntegrationWithMain() {
        // Тестирование полного потока выполнения
        // Можно использовать System.setIn для эмуляции ввода
        java.io.ByteArrayInputStream in = new java.io.ByteArrayInputStream("ababab\n".getBytes());
        System.setIn(in);
        
        java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));
        
        Solution.main(new String[]{});
        
        assertEquals("2\n", out.toString());
    }
}