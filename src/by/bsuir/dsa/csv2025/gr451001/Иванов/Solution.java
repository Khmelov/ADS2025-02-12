package by.bsuir.dsa.csv2025.gr451001.Иванов;

import java.util.*;
import java.io.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class Solution {

    private static boolean canFinish(int[] titan, int[] travel, int n, int h, int k) {
        int totalTime = 0;
        for (int i = 0; i < n; i++) {
            int miningTime = (titan[i] + k - 1) / k;
            totalTime += travel[i] + miningTime;
            if (totalTime > h) return false;
        }
        return totalTime <= h;
    }

    public static int minMiningSpeed(int n, int h, int[] titan, int[] travel) {
        int left = 1;
        int right = Arrays.stream(titan).max().getAsInt();
        int answer = right;

        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (canFinish(titan, travel, n, h, mid)) {
                answer = mid;
                right = mid - 1;
            } else {
                left = mid + 1;
            }
        }
        return answer;
    }

    public static void main(String[] args) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int n = Integer.parseInt(br.readLine().trim());
        int h = Integer.parseInt(br.readLine().trim());

        int[] titan = Arrays.stream(br.readLine().trim().split(" "))
                .mapToInt(Integer::parseInt).toArray();
        int[] travel = Arrays.stream(br.readLine().trim().split(" "))
                .mapToInt(Integer::parseInt).toArray();

        int result = minMiningSpeed(n, h, titan, travel);
        System.out.println(result);
    }

    @Test
    public void allTests() {
        // Тест 1
        assertEquals(4, Solution.minMiningSpeed(
                4, 15,
                new int[]{3, 6, 7, 11},
                new int[]{1, 2, 1, 3}
        ));

        // Тест 2
        assertEquals(2, Solution.minMiningSpeed(
                1, 5,
                new int[]{5},
                new int[]{2}
        ));

        // Тест 3
        assertEquals(3, Solution.minMiningSpeed(
                1, 4,
                new int[]{8},
                new int[]{1}
        ));

        // Тест 4
        assertEquals(3, Solution.minMiningSpeed(
                2, 6,
                new int[]{4, 5},
                new int[]{1, 1}
        ));

        // Тест 5
        assertEquals(10, Solution.minMiningSpeed(
                2, 5,
                new int[]{10, 10},
                new int[]{1, 1}
        ));

        // Тест 6
        assertEquals(6, Solution.minMiningSpeed(
                3, 12,
                new int[]{6, 9, 12},
                new int[]{2, 3, 1}
        ));

        // Тест 7
        assertEquals(5, Solution.minMiningSpeed(
                5, 10,
                new int[]{1, 2, 3, 4, 5},
                new int[]{1, 1, 1, 1, 1}
        ));

        // Тест 8
        assertEquals(6, Solution.minMiningSpeed(
                3, 9,
                new int[]{6, 6, 6},
                new int[]{2, 2, 2}
        ));

        // Тест 9
        assertEquals(8, Solution.minMiningSpeed(
                2, 4,
                new int[]{7, 8},
                new int[]{1, 1}
        ));

        // Тест 10
        assertEquals(12, Solution.minMiningSpeed(
                2, 20,
                new int[]{50, 60},
                new int[]{5, 5}
        ));
    }
}
