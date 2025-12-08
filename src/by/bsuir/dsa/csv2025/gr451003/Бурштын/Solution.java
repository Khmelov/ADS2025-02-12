package by.bsuir.dsa.csv2025.gr451003.Бурштын;



import org.junit.Test;
import org.junit.After;
import org.junit.Before;
import java.io.*;
import java.util.*;
import static org.junit.Assert.*;

public class Solution {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    @Before
    public void setUpStreams() {
        System.setOut(new PrintStream(outContent));
    }

    @After
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    public static String findAnagramIndexes(String text, String pattern) {
        if (text == null || pattern == null || pattern.length() > text.length()) {
            return "?";
        }

        if (text.isEmpty() || pattern.isEmpty()) {
            return "?";
        }

        int[] patternFreq = new int[26];
        int[] windowFreq = new int[26];
        StringBuilder result = new StringBuilder();
        boolean found = false;

        for (int i = 0; i < pattern.length(); i++) {
            patternFreq[pattern.charAt(i) - 'a']++;
            windowFreq[text.charAt(i) - 'a']++;
        }

        if (arraysEqual(patternFreq, windowFreq)) {
            result.append("0");
            found = true;
        }

        for (int i = 1; i <= text.length() - pattern.length(); i++) {
            char leftChar = text.charAt(i - 1);
            windowFreq[leftChar - 'a']--;

            char rightChar = text.charAt(i + pattern.length() - 1);
            windowFreq[rightChar - 'a']++;

            if (arraysEqual(patternFreq, windowFreq)) {
                if (found) {
                    result.append(" ");
                }
                result.append(i);
                found = true;
            }
        }

        return found ? result.toString() : "?";
    }

    private static boolean arraysEqual(int[] arr1, int[] arr2) {
        for (int i = 0; i < 26; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        try {
            String input = scan.nextLine().trim();
            String[] parts = input.split("\\s+");

            if (parts.length < 2) {
                System.out.println("?");
                return;
            }

            String text = parts[0];
            String pattern = parts[1];

            if (pattern.length() > text.length()) {
                System.out.println("?");
                return;
            }

            String result = findAnagramIndexes(text, pattern);
            System.out.println(result);
        } finally {
            scan.close();
        }
    }

    public static void runProgram(String input) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes("UTF-8"));
            System.setIn(in);
            main(new String[0]);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testCase1() {
        String input = "cbaebabacd abc";
        runProgram(input);
        assertEquals("0 6", outContent.toString().trim());
    }

    @Test
    public void testCase2() {
        String input = "abab ab";
        runProgram(input);
        assertEquals("0 1 2", outContent.toString().trim());
    }

    @Test
    public void testCase3() {
        String input = "hello world";
        runProgram(input);
        assertEquals("?", outContent.toString().trim());
    }

    @Test
    public void testCase4() {
        String input = "a a";
        runProgram(input);
        assertEquals("0", outContent.toString().trim());
    }

    @Test
    public void testCase5() {
        String input = "aa a";
        runProgram(input);
        assertEquals("0 1", outContent.toString().trim());
    }

    @Test
    public void testCase6() {
        String input = "aaa aa";
        runProgram(input);
        assertEquals("0 1", outContent.toString().trim());
    }

    @Test
    public void testCase7() {
        String input = "abcde edcba";
        runProgram(input);
        assertEquals("0", outContent.toString().trim());
    }

    @Test
    public void testCase8() {
        String input = "xyzxyz xyz";
        runProgram(input);
        assertEquals("0 1 2 3", outContent.toString().trim());
    }

    @Test
    public void testCase9() {
        String input = "bacb abc";
        runProgram(input);
        assertEquals("0 1", outContent.toString().trim());
    }

    @Test
    public void testCase10() {
        String input = "abacaba aba";
        runProgram(input);
        assertEquals("0 4", outContent.toString().trim());
    }

    @Test
    public void testCase11() {
        String input = "teststring ring";
        runProgram(input);
        assertEquals("6", outContent.toString().trim());
    }

    @Test
    public void testCase12() {
        String input = "algorithm log";
        runProgram(input);
        assertEquals("1", outContent.toString().trim());
    }

    @Test
    public void testCase13() {
        String input = "programming gram";
        runProgram(input);
        assertEquals("3", outContent.toString().trim());
    }

    @Test
    public void testCase14() {
        String input = "bbbbb bbb";
        runProgram(input);
        assertEquals("0 1 2", outContent.toString().trim());
    }

    @Test
    public void testCase15() {
        String input = "a b";
        runProgram(input);
        assertEquals("?", outContent.toString().trim());
    }



    @Test
    public void testPatternLongerThanText() {
        String input = "abc abcd";
        runProgram(input);
        assertEquals("?", outContent.toString().trim());
    }

    @Test
    public void testSingleCharacter() {
        String input = "z z";
        runProgram(input);
        assertEquals("0", outContent.toString().trim());
    }
}