package by.bsuir.dsa.csv2025.gr451001.Стасевич;

import java.util.Arrays;

class Solution {
    public int[] specialSort(int[] nums) {
        Integer[] arr = new Integer[nums.length];
        for (int i = 0; i < nums.length; i++) {
            arr[i] = nums[i];
        }
        Arrays.sort(arr, (a, b) -> {
            boolean aEven = (a % 2 == 0);
            boolean bEven = (b % 2 == 0);
            if (aEven && bEven) {
                return b - a; // Even numbers in descending order
            } else if (!aEven && !bEven) {
                return a - b; // Odd numbers in ascending order
            } else {
                return aEven ? -1 : 1; // Even before odd
            }
        });
        for (int i = 0; i < nums.length; i++) {
            nums[i] = arr[i];
        }
        return nums;
    }
}
//
//@Test
//    void testBasicExample() {
//        Solution solution = new Solution();
//        int[] input = {4, 1, 2, 3, 5, 6, 7, 8};
//        int[] expected = {8, 6, 4, 2, 1, 3, 5, 7};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//    @Test
//    void testWithNegativeNumbers() {
//        Solution solution = new Solution();
//        int[] input = {0, -1, -2, 3, 4};
//        int[] expected = {4, 0, -2, -1, 3};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//    @Test
//    void testSingleElement() {
//        Solution solution = new Solution();
//        int[] input = {5};
//        int[] expected = {5};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//
//    @Test
//    void testAllEvenNumbers() {
//        Solution solution = new Solution();
//        int[] input = {2, 8, 4, 6, 10};
//        int[] expected = {10, 8, 6, 4, 2};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//
//    @Test
//    void testAllOddNumbers() {
//        Solution solution = new Solution();
//        int[] input = {7, 3, 9, 1, 5};
//        int[] expected = {1, 3, 5, 7, 9};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//
//    @Test
//    void testMixedWithZeros() {
//        Solution solution = new Solution();
//        int[] input = {0, 1, 0, -1, 2};
//        int[] expected = {2, 0, 0, -1, 1};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//
//    @Test
//    void testLargeNumbers() {
//        Solution solution = new Solution();
//        int[] input = {10000, -10000, 9999, -9999};
//        int[] expected = {10000, -10000, -9999, 9999};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//
//    @Test
//    void testAlreadySorted() {
//        Solution solution = new Solution();
//        int[] input = {6, 4, 2, 1, 3, 5};
//        int[] expected = {6, 4, 2, 1, 3, 5};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//
//    @Test
//    void testReverseSorted() {
//        Solution solution = new Solution();
//        int[] input = {1, 3, 5, 2, 4, 6};
//        int[] expected = {6, 4, 2, 1, 3, 5};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//
//    @Test
//    void testDuplicateNumbers() {
//        Solution solution = new Solution();
//        int[] input = {3, 2, 3, 2, 1, 2};
//        int[] expected = {2, 2, 2, 1, 3, 3};
//        assertArrayEquals(expected, solution.specialSort(input));
//    }
//}