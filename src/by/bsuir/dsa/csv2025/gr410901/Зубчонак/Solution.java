package by.bsuir.dsa.csv2025.gr410901.Зубчонак;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import static org.junit.Assert.*;

public class Solution {

    public static int solve(List<List<Integer>> piles) {
        int maxSuits = countSuits(piles);
        for (int from = 0; from < 10; from++) {
            List<Integer> src = piles.get(from);
            if (src.isEmpty()) continue;
            for (int len = 1; len <= src.size(); len++) {
                boolean valid = true;
                for (int i = 0; i < len - 1; i++) {
                    if (src.get(i) != src.get(i + 1) + 1) {
                        valid = false;
                        break;
                    }
                }
                if (!valid) break;
                List<Integer> seq = new ArrayList<>(src.subList(0, len));
                for (int to = 0; to < 10; to++) {
                    if (from == to) continue;
                    List<Integer> dst = piles.get(to);
                    boolean canMove = dst.isEmpty() ||
                            (!seq.isEmpty() && dst.get(0) == seq.get(seq.size() - 1) - 1);
                    if (canMove) {
                        List<List<Integer>> copy = deepCopy(piles);
                        copy.get(from).subList(0, len).clear();
                        copy.get(to).addAll(0, seq);
                        int suits = countSuits(copy);
                        if (suits > maxSuits) maxSuits = suits;
                    }
                }
            }
        }
        return maxSuits;
    }

    public static List<List<Integer>> readPiles(Scanner sc) {
        List<List<Integer>> piles = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int k = sc.nextInt();
            List<Integer> pile = new ArrayList<>();
            for (int j = 0; j < k; j++) {
                pile.add(sc.nextInt());
            }
            piles.add(pile);
        }
        return piles;
    }

    static List<List<Integer>> deepCopy(List<List<Integer>> piles) {
        List<List<Integer>> res = new ArrayList<>();
        for (List<Integer> p : piles) res.add(new ArrayList<>(p));
        return res;
    }

    static int countSuits(List<List<Integer>> piles) {
        int cnt = 0;
        for (List<Integer> p : piles) {
            if (p.size() < 13) continue;
            boolean isSuite = true;
            for (int i = 0; i < 13; i++) {
                if (p.get(i) != 13 - i) {
                    isSuite = false;
                    break;
                }
            }
            if (isSuite) cnt++;
        }
        return cnt;
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        List<List<Integer>> piles = readPiles(sc);
        System.out.println(solve(piles));
    }
}

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SolutionTest.Test1.class,
        SolutionTest.Test2.class,
        SolutionTest.Test3.class,
        SolutionTest.Test4.class,
        SolutionTest.Test5.class,
        SolutionTest.Test6.class,
        SolutionTest.Test7.class,
        SolutionTest.Test8.class,
        SolutionTest.Test9.class,
        SolutionTest.Test10.class
})
class SolutionTest {

    public static class Test1 {
        @Test
        public void test() {
            String input =
                    "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n";
            assertEquals(1, runSolution(input));
        }
    }

    public static class Test2 {
        @Test
        public void test() {
            String input =
                    "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n";
            assertEquals(2, runSolution(input));
        }
    }

    public static class Test3 {
        @Test
        public void test() {
            String input =
                    "7 7 6 5 4 3 2 1\n" +
                            "6 13 12 11 10 9 8\n" +
                            "0\n0\n0\n0\n0\n0\n0\n0\n0\n0\n";
            assertEquals(1, runSolution(input));
        }
    }

    public static class Test4 {
        @Test
        public void test() {
            String input =
                    "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "0\n0\n0\n0\n0\n0\n";
            assertEquals(4, runSolution(input));
        }
    }

    public static class Test5 {
        @Test
        public void test() {
            String input =
                    "5 13 12 11 10 9\n" +
                            "5 8 7 6 5 4\n" +
                            "5 1 13 12 11 10\n" +
                            "5 9 8 7 6 5\n" +
                            "5 4 3 2 1 13\n" +
                            "5 12 11 10 9 8\n" +
                            "5 7 6 5 4 3\n" +
                            "5 2 1 13 12 11\n" +
                            "5 10 9 8 7 6\n" +
                            "5 5 4 3 2 1\n";
            assertEquals(0, runSolution(input));
        }
    }

    public static class Test6 {
        @Test
        public void test() {
            String input =
                    "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "1 1\n" +
                            "0\n0\n0\n0\n0\n0\n0\n0\n";
            assertEquals(1, runSolution(input));
        }
    }

    public static class Test7 {
        @Test
        public void test() {
            String input =
                    "11 13 12 11 10 9 8 7 6 5 4 3\n" +
                            "2 2 1\n" +
                            "0\n0\n0\n0\n0\n0\n0\n0\n";
            assertEquals(1, runSolution(input));
        }
    }

    public static class Test8 {
        @Test
        public void test() {
            String input =
                    "13 13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "0\n0\n0\n0\n0\n0\n0\n0\n0\n";
            assertEquals(0, runSolution(input));
        }
    }

    public static class Test9 {
        @Test
        public void test() {
            String input =
                    "13 13 12 11 10 9 8 7 6 5 4 3 2 1\n" +
                            "7 7 6 5 4 3 2 1\n" +
                            "6 13 12 11 10 9 8\n" +
                            "0\n0\n0\n0\n0\n0\n0\n";
            assertEquals(2, runSolution(input));
        }
    }

    public static class Test10 {
        @Test
        public void test() {
            String input =
                    "1 1\n" +
                            "1 2\n" +
                            "1 3\n" +
                            "1 4\n" +
                            "1 5\n" +
                            "1 6\n" +
                            "1 7\n" +
                            "1 8\n" +
                            "1 9\n" +
                            "4 13 12 11 10\n";
            assertEquals(0, runSolution(input));
        }
    }

    private static int runSolution(String input) {
        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        try {
            System.setIn(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            System.setOut(new PrintStream(baos, false, StandardCharsets.UTF_8));

            // Создаем Scanner из input и запускаем логику напрямую
            Scanner sc = new Scanner(input);
            List<List<Integer>> piles = Solution.readPiles(sc);
            int result = Solution.solve(piles);
            System.out.println(result);

            String out = baos.toString(StandardCharsets.UTF_8).trim();
            return Integer.parseInt(out);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }
    }
}