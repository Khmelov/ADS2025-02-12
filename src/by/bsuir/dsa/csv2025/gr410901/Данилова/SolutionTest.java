package by.bsuir.dsa.csv2025.gr410901.Данилова;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.util.Scanner;

import static org.junit.Assert.*;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        SolutionTest.Test1_Basic.class,
        SolutionTest.Test2_SingleCategory.class,
        SolutionTest.Test3_MultipleCategories.class,
        SolutionTest.Test4_NoMatches.class,
        SolutionTest.Test5_AllMatches.class,
        SolutionTest.Test6_EmptyData.class,
        SolutionTest.Test7_SingleOrder.class,
        SolutionTest.Test8_BoundaryValues.class,
        SolutionTest.Test9_MultipleQueries.class,
        SolutionTest.Test10_UnsortedInput.class
})
public class SolutionTest {

    public static class Test1_Basic {
        @Test
        public void test() {
            String input = "3 2\n1 100 1\n2 200 1\n3 150 2\n100\n200";
            int[][] expected = {{1, 1}, {0, 0}};
            testCase(input, expected);
        }
    }

    public static class Test2_SingleCategory {
        @Test
        public void test() {
            String input = "4 1\n1 50 1\n2 100 1\n3 150 1\n4 200 1\n100";
            int[][] expected = {{2}};
            testCase(input, expected);
        }
    }

    public static class Test3_MultipleCategories {
        @Test
        public void test() {
            String input = "5 2\n1 100 1\n2 200 2\n3 150 3\n4 250 1\n5 300 2\n150\n250";
            int[][] expected = {{1, 2, 0}, {0, 1, 0}};
            testCase(input, expected);
        }
    }

    public static class Test4_NoMatches {
        @Test
        public void test() {
            String input = "3 1\n1 100 1\n2 200 2\n3 150 1\n500";
            int[][] expected = {{0, 0}};
            testCase(input, expected);
        }
    }

    public static class Test5_AllMatches {
        @Test
        public void test() {
            String input = "3 1\n1 100 1\n2 200 2\n3 150 1\n50";
            int[][] expected = {{2, 1}};
            testCase(input, expected);
        }
    }

    public static class Test6_EmptyData {
        @Test
        public void test() {
            String input = "0 1\n100";
            int[][] expected = new int[1][0];
            testCase(input, expected);
        }
    }

    public static class Test7_SingleOrder {
        @Test
        public void test() {
            String input = "1 1\n1 150 1\n100";
            int[][] expected = {{1}};
            testCase(input, expected);
        }
    }

    public static class Test8_BoundaryValues {
        @Test
        public void test() {
            String input = "4 2\n1 100 1\n2 100 2\n3 200 1\n4 200 2\n100\n200";
            int[][] expected = {{1, 1}, {0, 0}};
            testCase(input, expected);
        }
    }

    public static class Test9_MultipleQueries {
        @Test
        public void test() {
            String input = "3 3\n1 100 1\n2 200 2\n3 150 1\n50\n150\n250";
            int[][] expected = {{2, 1}, {0, 1}, {0, 0}};
            testCase(input, expected);
        }
    }

    public static class Test10_UnsortedInput {
        @Test
        public void test() {
            String input = "5 1\n1 300 3\n2 100 1\n3 200 2\n4 150 1\n5 250 3\n150";
            int[][] expected = {{0, 1, 2}};
            testCase(input, expected);
        }
    }

    private static void testCase(String input, int[][] expected) {
        try {
            Scanner inputScanner = new Scanner(input);
            Solution instance = new Solution();
            int[][] result = instance.analyzeSales(inputScanner);

            assertEquals("Array length mismatch", expected.length, result.length);

            for (int i = 0; i < expected.length; i++) {
                assertArrayEquals("Inner array mismatch at index " + i, expected[i], result[i]);
            }
        } catch (Exception e) {
            fail("Тест завершился с ошибкой: " + e.getMessage());
        }
    }
}
