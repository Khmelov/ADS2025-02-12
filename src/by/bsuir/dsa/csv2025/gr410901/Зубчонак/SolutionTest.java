package by.bsuir.dsa.csv2025.gr410901.Зубчонак;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertEquals;

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
public class  SolutionTest {

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
