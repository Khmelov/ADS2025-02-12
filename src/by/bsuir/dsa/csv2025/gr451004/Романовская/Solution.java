package by.bsuir.dsa.csv2025.gr451004.Романовская;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.*;

import static org.junit.Assert.*;

public class Solution {

    private static String normalize(String s) {
        StringBuilder sb = new StringBuilder();
        for (char c : s.toCharArray()) {
            if (Character.isLetter(c)) {
                sb.append(Character.toLowerCase(c));
            }
        }
        return sb.toString();
    }

    private static boolean isIsomorphic(String s1, String s2) {
        if (s1.length() != s2.length()) return false;

        Map<Character, Character> map1 = new HashMap<>();
        Map<Character, Character> map2 = new HashMap<>();

        for (int i = 0; i < s1.length(); i++) {
            char a = s1.charAt(i);
            char b = s2.charAt(i);

            if (map1.containsKey(a)) {
                if (map1.get(a) != b) return false;
            } else {
                map1.put(a, b);
            }

            if (map2.containsKey(b)) {
                if (map2.get(b) != a) return false;
            } else {
                map2.put(b, a);
            }
        }

        return true;
    }
    private static boolean freqMatch(String s1, String s2) {
        Map<Character, Integer> f1 = new HashMap<>();
        Map<Character, Integer> f2 = new HashMap<>();

        for (char c : s1.toCharArray()) f1.put(c, f1.getOrDefault(c, 0) + 1);
        for (char c : s2.toCharArray()) f2.put(c, f2.getOrDefault(c, 0) + 1);

        List<Integer> L1 = new ArrayList<>(f1.values());
        List<Integer> L2 = new ArrayList<>(f2.values());

        Collections.sort(L1);
        Collections.sort(L2);

        return L1.equals(L2);
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String s1 = normalize(sc.nextLine());
        String s2 = normalize(sc.nextLine());

        if (isIsomorphic(s1, s2) && freqMatch(s1, s2)) {
            System.out.println(1);
        } else {
            System.out.println(0);
        }
    }

    @Test
    public void testMainWithSampleInput() throws Exception {
        String input = "Hello, World!\nxxxxooollldd\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        System.setIn(in);
        System.setOut(ps);
        Solution.main(null);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = out.toString().trim();
        assertEquals("0", output);
    }
    @Test
    public void testMainPaperTitle() throws Exception {
        String input = "paper!!\ntitle???\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        System.setIn(in);
        System.setOut(ps);

        Solution.main(null);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = out.toString().trim();
        assertEquals("1", output);
    }

    @Test
    public void testMainAaabbAaab() throws Exception {
        String input = "aaabb\naaab\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        System.setIn(in);
        System.setOut(ps);

        Solution.main(null);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = out.toString().trim();
        assertEquals("0", output);
    }
    @Test
    public void testMainAaabbbCccddd() throws Exception {
        String input = "hhhbbb\nrrrddd\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        System.setIn(in);
        System.setOut(ps);

        Solution.main(null);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = out.toString().trim();
        assertEquals("1", output);
    }
    @Test
    public void testMainAbcdZbcz() throws Exception {
        String input = "abcdfdg\nzbcz\n";

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;

        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(out);

        System.setIn(in);
        System.setOut(ps);

        Solution.main(null);

        System.setIn(originalIn);
        System.setOut(originalOut);

        String output = out.toString().trim();
        assertEquals("0", output);
    }
}