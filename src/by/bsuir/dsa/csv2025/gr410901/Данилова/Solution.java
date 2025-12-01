package by.bsuir.dsa.csv2025.gr410901.Данилова;

import java.util.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import static org.junit.Assert.*;

public class Solution {

    public static void main(String[] args) {
        Scanner consoleScanner = new Scanner(System.in);
        Solution instance = new Solution();
        int[][] result = instance.analyzeSales(consoleScanner);
        for (int[] counts : result) {
            for (int count : counts) {
                System.out.print(count + " ");
            }
            System.out.println();
        }
    }

    int[][] analyzeSales(Scanner scanner) {
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        Order[] orders = new Order[n];
        int[] queries = new int[m];
        int[][] result = new int[m][];

        for (int i = 0; i < n; i++) {
            orders[i] = new Order(scanner.nextInt(), scanner.nextInt(), scanner.nextInt());
        }
        for (int i = 0; i < m; i++) {
            queries[i] = scanner.nextInt();
        }

        Arrays.sort(orders);

        Map<Integer, int[]> bounds = getCategoryBounds(orders);

        for (int i = 0; i < m; i++) {
            result[i] = processQuery(orders, bounds, queries[i]);
        }

        return result;
    }

    private Map<Integer, int[]> getCategoryBounds(Order[] orders) {
        Map<Integer, int[]> bounds = new TreeMap<>();
        if (orders.length == 0) return bounds;

        int start = 0;
        int currentCat = orders[0].category;

        for (int i = 1; i < orders.length; i++) {
            if (orders[i].category != currentCat) {
                bounds.put(currentCat, new int[]{start, i - 1});
                start = i;
                currentCat = orders[i].category;
            }
        }
        bounds.put(currentCat, new int[]{start, orders.length - 1});

        return bounds;
    }

    private int[] processQuery(Order[] orders, Map<Integer, int[]> bounds, int amount) {
        int[] counts = new int[bounds.size()];
        int idx = 0;

        for (int[] range : bounds.values()) {
            counts[idx++] = countInRange(orders, range[0], range[1], amount);
        }

        return counts;
    }

    private int countInRange(Order[] orders, int start, int end, int amount) {
        int firstIndex = findFirstAbove(orders, start, end, amount);
        if (firstIndex == -1) {
            return 0;
        }
        return end - firstIndex + 1;
    }

    private int findFirstAbove(Order[] orders, int left, int right, int amount) {
        int result = -1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (orders[mid].amount > amount) {
                result = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return result;
    }

    private class Order implements Comparable<Order> {
        int id, amount, category;

        Order(int id, int amount, int category) {
            this.id = id;
            this.amount = amount;
            this.category = category;
        }

        @Override
        public int compareTo(Order o) {
            if (this.category != o.category) {
                return Integer.compare(this.category, o.category);
            }
            return Integer.compare(this.amount, o.amount);
        }
    }
}

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
class SolutionTest {

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