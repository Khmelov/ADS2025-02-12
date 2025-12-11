package by.bsuir.dsa.csv2025.gr451003.Попович;

import java.io.*;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class SortingWithConstraints {
    private static long comparisonCount = 0;
    private static int memoryUsage = 0;

    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = SortingWithConstraints.class.getResourceAsStream("data.txt");
        SortingWithConstraints instance = new SortingWithConstraints();
        String[] result = instance.processSorting(stream);
        for (String res : result) {
            System.out.println(res);
        }
    }

    String[] processSorting(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int testCount = scanner.nextInt();
        scanner.nextLine(); 

        String[] results = new String[testCount];

        for (int i = 0; i < testCount; i++) {
            String input = scanner.nextLine();
            String[] parts = input.split(";");

            int[] nums = parseArray(parts[0]);
            int maxValue = Integer.parseInt(parts[1]);
            long maxCount = Long.parseLong(parts[2]);
            int maxMemory = Integer.parseInt(parts[3]);

            try {
                int[] sorted = sortWithConstraints(nums, maxValue, maxCount, maxMemory);
                results[i] = arrayToString(sorted);
            } catch (Exception e) {
                results[i] = "error: " + e.getMessage();
            }
        }

        scanner.close();
        return results;
    }

    public static int[] sortWithConstraints(int[] nums, int maxValue, long maxCount, int maxMemory) {
        int n = nums.length;

        comparisonCount = 0;
        memoryUsage = 0;

        int minValue = Integer.MAX_VALUE;
        for (int num : nums) {
            if (num < minValue) minValue = num;
        }

        if (canUseCountingSort(n, minValue, maxValue, maxMemory)) {
            return countingSort(nums, minValue, maxValue, maxCount, maxMemory);
        } else if (canUseRadixSort(n, maxMemory)) {
            return radixSort(nums, maxCount, maxMemory);
        } else if (maxMemory >= n) {
            return mergeSort(nums, 0, nums.length - 1, maxCount, maxMemory);
        } else {
            if (n <= 1000 && maxCount >= (long) n * n / 2) {
                return insertionSort(nums, maxCount);
            } else {
                return heapSort(nums, maxCount);
            }
        }
    }

    private static boolean canUseCountingSort(int n, int minValue, int maxValue, int maxMemory) {
        long range = (long) maxValue - minValue + 1;
        return range <= maxMemory / 2 && range <= 200000;
    }

    private static boolean canUseRadixSort(int n, int maxMemory) {
        return n * 2 <= maxMemory;
    }

    private static int[] countingSort(int[] nums, int minValue, int maxValue, long maxCount, int maxMemory) {
        int range = maxValue - minValue + 1;

        memoryUsage += range;
        if (memoryUsage > maxMemory) {
            throw new RuntimeException("Memory limit exceeded");
        }

        int[] count = new int[range];
        int[] output = new int[nums.length];
        memoryUsage += nums.length;

        for (int num : nums) {
            count[num - minValue]++;
        }

        for (int i = 1; i < range; i++) {
            count[i] += count[i - 1];
            comparisonCount++;
            if (comparisonCount > maxCount) {
                throw new RuntimeException("Comparison limit exceeded");
            }
        }

        for (int i = nums.length - 1; i >= 0; i--) {
            int num = nums[i];
            int pos = count[num - minValue] - 1;
            output[pos] = num;
            count[num - minValue]--;
        }

        return output;
    }

    private static int[] radixSort(int[] nums, long maxCount, int maxMemory) {
        int[] arr = nums.clone();
        memoryUsage += arr.length;

        int max = Math.abs(arr[0]);
        for (int i = 1; i < arr.length; i++) {
            if (Math.abs(arr[i]) > max) max = Math.abs(arr[i]);
        }

        for (int exp = 1; max / exp > 0; exp *= 10) {
            arr = countingSortForRadix(arr, exp, maxCount, maxMemory);
        }

        return arr;
    }

    private static int[] countingSortForRadix(int[] arr, int exp, long maxCount, int maxMemory) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[19];

        memoryUsage += n + 19;
        if (memoryUsage > maxMemory) {
            throw new RuntimeException("Memory limit exceeded");
        }

        for (int i = 0; i < n; i++) {
            int digit = (arr[i] / exp) % 10;
            count[digit + 9]++;
        }

        for (int i = 1; i < 19; i++) {
            count[i] += count[i - 1];
            comparisonCount++;
            if (comparisonCount > maxCount) {
                throw new RuntimeException("Comparison limit exceeded");
            }
        }

        for (int i = n - 1; i >= 0; i--) {
            int digit = (arr[i] / exp) % 10;
            output[count[digit + 9] - 1] = arr[i];
            count[digit + 9]--;
        }

        return output;
    }

    private static int[] mergeSort(int[] arr, int left, int right, long maxCount, int maxMemory) {
        if (left == right) {
            return new int[]{arr[left]};
        }

        comparisonCount++;
        if (comparisonCount > maxCount) {
            throw new RuntimeException("Comparison limit exceeded");
        }

        int mid = left + (right - left) / 2;
        int[] leftArr = mergeSort(arr, left, mid, maxCount, maxMemory);
        int[] rightArr = mergeSort(arr, mid + 1, right, maxCount, maxMemory);

        return merge(leftArr, rightArr, maxCount, maxMemory);
    }

    private static int[] merge(int[] left, int[] right, long maxCount, int maxMemory) {
        int[] result = new int[left.length + right.length];
        memoryUsage += result.length;

        if (memoryUsage > maxMemory) {
            throw new RuntimeException("Memory limit exceeded");
        }

        int i = 0, j = 0, k = 0;

        while (i < left.length && j < right.length) {
            comparisonCount++;
            if (comparisonCount > maxCount) {
                throw new RuntimeException("Comparison limit exceeded");
            }

            if (left[i] <= right[j]) {
                result[k++] = left[i++];
            } else {
                result[k++] = right[j++];
            }
        }

        while (i < left.length) {
            result[k++] = left[i++];
        }

        while (j < right.length) {
            result[k++] = right[j++];
        }

        return result;
    }

    private static int[] heapSort(int[] arr, long maxCount) {
        int n = arr.length;
        int[] result = arr.clone();
        memoryUsage += n;

        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(result, n, i, maxCount);
        }

        for (int i = n - 1; i > 0; i--) {
            int temp = result[0];
            result[0] = result[i];
            result[i] = temp;

            heapify(result, i, 0, maxCount);
        }

        return result;
    }

    private static void heapify(int[] arr, int n, int i, long maxCount) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;

        if (left < n) {
            comparisonCount++;
            if (comparisonCount > maxCount) {
                throw new RuntimeException("Comparison limit exceeded");
            }
            if (arr[left] > arr[largest]) {
                largest = left;
            }
        }

        if (right < n) {
            comparisonCount++;
            if (comparisonCount > maxCount) {
                throw new RuntimeException("Comparison limit exceeded");
            }
            if (arr[right] > arr[largest]) {
                largest = right;
            }
        }

        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            heapify(arr, n, largest, maxCount);
        }
    }

    private static int[] insertionSort(int[] arr, long maxCount) {
        int[] result = arr.clone();
        memoryUsage += arr.length;

        for (int i = 1; i < result.length; i++) {
            int key = result[i];
            int j = i - 1;

            while (j >= 0) {
                comparisonCount++;
                if (comparisonCount > maxCount) {
                    throw new RuntimeException("Comparison limit exceeded");
                }

                if (result[j] > key) {
                    result[j + 1] = result[j];
                    j--;
                } else {
                    break;
                }
            }
            result[j + 1] = key;
        }

        return result;
    }

    private static int[] parseArray(String str) {
        String[] parts = str.split(",");
        int[] arr = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            arr[i] = Integer.parseInt(parts[i].trim());
        }
        return arr;
    }

    private static String arrayToString(int[] arr) {
        if (arr.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            if (i > 0) sb.append(" ");
            sb.append(arr[i]);
        }
        return sb.toString();
    }

    @Test
    public void checkAllTests() throws Exception {
        testCase(1, new String[]{"1 2 3", "200 500 1000 1500 1800", "-15 -10 -5 0 5 10", "1 2 3 4 5 6 7 8 9"});
        testCase(2, new String[]{"1 2 2 5 5 5 8 8 8 8", "42", "-100 -50 0 50 100", "1 1 1 1 2 2 2 2 3 3 3 3"});
        testCase(3, new String[]{"1 2 3 4 5 6 7 8 9 10 11 12 13 14 15", "5 10 15 20 25", "-3 -2 -1 0 1 2 3", "100 200 300 400 500"});
        testCase(4, new String[]{"1", "10 20 30", "5 5 5 10 10 15", "2 4 6 8 10 12 14 16 18 20"});
        testCase(5, new String[]{"-1000 -500 0 500 1000", "1 3 5 7 9 11 13 15", "100 100 100 200 200 300", "50 40 30 20 10"});
        testCase(6, new String[]{"0 1 2 3 4 5", "999 1000 1001", "-5 -4 -3 -2 -1", "7 14 21 28 35 42"});
        testCase(7, new String[]{"1 10 100 1000 10000", "2 3 5 7 11 13 17", "4 8 12 16 20 24", "9 8 7 6 5 4 3 2 1"});
        testCase(8, new String[]{"15 25 35 45 55", "100 90 80 70 60", "0 0 0 1 1 1", "123 456 789"});
        testCase(9, new String[]{"11 22 33 44 55 66", "5 4 3 2 1 0 -1 -2", "50 100 150 200 250", "8 6 4 2 0 -2 -4"});
        testCase(10, new String[]{"1 2 3 4 5 6 7 8 9 10", "20 40 60 80 100", "-10 -8 -6 -4 -2 0", "99 88 77 66 55 44 33 22 11"});
    }

    private void testCase(int testNumber, String[] expected) throws Exception {
        String testData = getTestData(testNumber);
        InputStream inputStream = new ByteArrayInputStream(testData.getBytes());

        SortingWithConstraints instance = new SortingWithConstraints();
        String[] result = instance.processSorting(inputStream);

        assertArrayEquals("Test " + testNumber + " failed", expected, result);
    }

    private String getTestData(int testNumber) {
        switch(testNumber) {
            case 1: return "4\n" +
                    "3,1,2;100;1000;1000\n" +
                    "1000,500,1500,200,1800;2000;10000;5000\n" +
                    "-5,-10,0,5,10,-15;10;5;1000000\n" +
                    "9,7,5,3,1,8,6,4,2;9;100;3";

            case 2: return "4\n" +
                    "5,5,5,2,2,8,8,8,8,1;8;50;20\n" +
                    "42;100;1;1\n" +
                    "100,-100,50,-50,0;100;10;250\n" +
                    "1,3,2,1,3,2,1,3,2,1,3,2;3;100;10";

            case 3: return "4\n" +
                    "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15;1000;14;1000\n" +
                    "25,10,5,20,15;30;50;100\n" +
                    "3,1,2,-1,0,-2,-3;5;20;50\n" +
                    "500,200,400,100,300;600;1000;500";

            case 4: return "4\n" +
                    "1;10;1;1\n" +
                    "20,10,30;40;10;50\n" +
                    "10,5,15,5,10,5;20;15;30\n" +
                    "20,4,16,12,8,2,18,14,10,6;25;100;20";

            case 5: return "4\n" +
                    "0,-500,1000,-1000,500;1500;20;2000\n" +
                    "15,5,11,9,13,7,3,1;20;30;10\n" +
                    "200,100,300,100,200,100;350;10;400\n" +
                    "10,20,30,40,50;60;15;5";

            case 6: return "4\n" +
                    "5,3,1,4,2,0;10;20;15\n" +
                    "1001,999,1000;1100;5;1500\n" +
                    "-1,-3,-5,-2,-4;0;10;8\n" +
                    "42,14,28,7,21,35;50;30;25";

            case 7: return "4\n" +
                    "1000,100,10,1,10000;15000;8;20000\n" +
                    "17,3,11,5,13,2,7;20;20;15\n" +
                    "16,8,20,4,12,24;30;15;20\n" +
                    "9,1,7,3,5,8,2,6,4;10;45;5";

            case 8: return "4\n" +
                    "45,25,55,15,35;60;15;30\n" +
                    "70,100,80,90,60;110;12;10\n" +
                    "1,0,1,0,0,1;2;5;10\n" +
                    "789,123,456;800;5;900";

            case 9: return "4\n" +
                    "66,33,44,11,55,22;70;20;40\n" +
                    "-1,0,-2,2,1,5,4,3;6;30;8\n" +
                    "200,50,150,100,250;300;12;350\n" +
                    "-4,2,0,6,-2,4,8;10;25;12";

            case 10: return "4\n" +
                    "10,9,8,7,6,5,4,3,2,1;15;50;8\n" +
                    "100,40,80,20,60;120;10;60\n" +
                    "-8,-2,-10,-4,0,-6;0;12;15\n" +
                    "55,22,88,44,11,99,77,33,66;100;35;20";

            default: return "0\n";
        }
    }

    // Interactive mode for manual testing
    public static void interactiveMode() {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Interactive Mode - Enter data manually:");
        System.out.println("Format: array;maxValue;maxCount;maxMemory");
        System.out.println("Example: 3,1,2;100;1000;1000");
        System.out.println("Type 'exit' to quit");

        while (true) {
            System.out.print("Input: ");
            String input = scanner.nextLine();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            try {
                String[] parts = input.split(";");
                int[] nums = parseArray(parts[0]);
                int maxValue = Integer.parseInt(parts[1]);
                long maxCount = Long.parseLong(parts[2]);
                int maxMemory = Integer.parseInt(parts[3]);

                int[] result = sortWithConstraints(nums, maxValue, maxCount, maxMemory);
                System.out.println("Output: " + arrayToString(result));

            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        scanner.close();
    }
}