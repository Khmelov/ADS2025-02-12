package by.bsuir.dsa.csv2025.gr451004.Линник;

import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    public static double solve(int n, int[] initialPrices, int[][] updates, int start, int end, int k) {
        List<int[]> relevantUpdates = new ArrayList<>();
        for (int day = 0; day < updates.length; day++) {
            int stockIdx = updates[day][0];
            if (stockIdx >= start && stockIdx <= end) {
                relevantUpdates.add(new int[]{day, stockIdx, updates[day][1]});
            }
        }

        relevantUpdates.sort(Comparator.comparingInt(a -> a[0]));
        if (k < relevantUpdates.size()) {
            relevantUpdates = relevantUpdates.subList(0, k);
        }

        int segmentSize = end - start + 1;
        int[] currentPrices = new int[segmentSize];
        for (int i = 0; i < segmentSize; i++) {
            currentPrices[i] = initialPrices[start + i];
        }

        for (int[] update : relevantUpdates) {
            int stockIdx = update[1];
            int newPrice = update[2];
            currentPrices[stockIdx - start] = newPrice;
        }

        return findMedian(currentPrices);
    }

    private static double findMedian(int[] prices) {
        int[] sorted = prices.clone();
        Arrays.sort(sorted);
        int n = sorted.length;

        if (n % 2 == 1) {
            return sorted[n / 2];
        } else {
            return (sorted[n / 2 - 1] + sorted[n / 2]) / 2.0;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();
        int[] initialPrices = new int[n];
        for (int i = 0; i < n; i++) {
            initialPrices[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        int[][] updates = new int[m][2];
        for (int i = 0; i < m; i++) {
            updates[i][0] = scanner.nextInt();
            updates[i][1] = scanner.nextInt();
        }

        int start = scanner.nextInt();
        int end = scanner.nextInt();
        int k = scanner.nextInt();

        double result = solve(n, initialPrices, updates, start, end, k);

        System.out.println(result);
        scanner.close();
    }


    @Test
    public void test1() {
        int n = 5;
        int[] initialPrices = {100, 200, 300, 400, 500};
        int[][] updates = {{2, 350}, {1, 180}, {4, 550}, {2, 320}, {3, 420}};
        double result = Solution.solve(n, initialPrices, updates, 1, 4, 2);
        assertEquals(375.0, result, 1e-9);
    }

    @Test
    public void test2() {
        int n = 4;
        int[] initialPrices = {1, 1, 1, 1};
        int[][] updates = {{0, 2}, {3, 2}, {1, 2}, {2, 2}};
        double result = Solution.solve(n, initialPrices, updates, 0, 3, 2);
        assertEquals(1.5, result, 1e-9);
    }

    @Test
    public void test3() {
        int n = 3;
        int[] initialPrices = {50, 50, 50};
        int[][] updates = {{0, 60}, {0, 70}, {0, 80}, {1, 65}, {0, 55}};
        double result = Solution.solve(n, initialPrices, updates, 0, 2, 4);
        assertEquals(65.0, result, 1e-9);
    }

    @Test
    public void test4() {
        int n = 1;
        int[] initialPrices = {50};
        int[][] updates = {{0, 60}, {0, 70}, {0, 80}};
        double result = Solution.solve(n, initialPrices, updates, 0, 0, 2);
        assertEquals(70.0, result, 1e-9);
    }

}