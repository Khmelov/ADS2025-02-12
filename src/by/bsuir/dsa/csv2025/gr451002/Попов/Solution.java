package by.bsuir.dsa.csv2025.gr451002.Попов;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

import java.util.Scanner;

public class Solution {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int n = scanner.nextInt();

        int[] nums = new int[n];
        for (int i = 0; i < n; i++) {
            nums[i] = scanner.nextInt();
        }

        int target = scanner.nextInt();

        scanner.close();

        Solution solver = new Solution();
        int result = solver.findClosestSubsetSum(nums, target);
        System.out.println(result);
    }

    public int findClosestSubsetSum(int[] nums, int target) {
        int n = nums.length;
        int[] left = Arrays.copyOfRange(nums, 0, n / 2);
        int[] right = Arrays.copyOfRange(nums, n / 2, n);

        List<Integer> leftSums = generateSubsetSums(left);
        List<Integer> rightSums = generateSubsetSums(right);
        Collections.sort(rightSums);

        int closest = 0;
        for (int ls : leftSums) {
            if (ls > target) continue;
            int remaining = target - ls;
            int idx = Collections.binarySearch(rightSums, remaining);
            if (idx >= 0) return target;
            int insertion = -idx - 1;
            if (insertion > 0) {
                int sum = ls + rightSums.get(insertion - 1);
                if (sum <= target && sum > closest) closest = sum;
            }
        }
        return closest;
    }

    private List<Integer> generateSubsetSums(int[] arr) {
        List<Integer> sums = new ArrayList<>();
        sums.add(0);
        for (int num : arr) {
            int size = sums.size();
            for (int i = 0; i < size; i++) {
                sums.add(sums.get(i) + num);
            }
        }
        return sums;
    }

    @Test(timeout = 2000)
    public void testBasicCase() {
        Solution solver = new Solution();
        int[] nums = {200, 300, 700, 800, 1000};
        int target = 1400;
        assertEquals(1300, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testExactMatch() {
        Solution solver = new Solution();
        int[] nums = {100, 200, 300, 400, 500};
        int target = 900;
        assertEquals(900, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testAllElementsTooLarge() {
        Solution solver = new Solution();
        int[] nums = {1500, 2000, 2500};
        int target = 1000;
        assertEquals(0, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testSingleElementMatch() {
        Solution solver = new Solution();
        int[] nums = {500, 800, 1200};
        int target = 800;
        assertEquals(800, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testEmptySubset() {
        Solution solver = new Solution();
        int[] nums = {1100, 1200, 1300};
        int target = 500;
        assertEquals(0, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testDuplicateValues() {
        Solution solver = new Solution();
        int[] nums = {200, 200, 300, 300, 500};
        int target = 800;
        assertEquals(800, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testZeroInArray() {
        Solution solver = new Solution();
        int[] nums = {0, 100, 400, 600};
        int target = 500;
        assertEquals(500, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testWithZerosAndOnes() {
        Solution solver = new Solution();
        int[] nums = {0, 0, 100, 100, 200};
        int target = 300;
        assertEquals(300, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testLargeTargetWithSmallNumbers() {
        Solution solver = new Solution();
        int[] nums = {100, 100, 100, 100, 100, 100, 100, 100};
        int target = 10000;
        assertEquals(800, solver.findClosestSubsetSum(nums, target));
    }

    @Test(timeout = 2000)
    public void testMultipleSameClosestSums() {
        Solution solver = new Solution();
        int[] nums = {100, 200, 300, 400};
        int target = 500;
        assertEquals(500, solver.findClosestSubsetSum(nums, target));
    }
}