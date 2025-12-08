package by.bsuir.dsa.csv2025.gr451003.Шепетюк;

import org.junit.Test;

import java.util.Scanner;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class Solution {

    // Solution quick sort function
    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Find the partition point
            int pivotIndex = partition(arr, low, high);

            // Recursively sort elements before and after the pivot element
            quickSort(arr, low, pivotIndex - 1);
            quickSort(arr, pivotIndex + 1, high);
        }
    }

    // Array partitioning function
    private static int partition(int[] arr, int low, int high) {
        // Select the pivot element (last element)
        int pivot = arr[high];

        // Index of smaller element (indicates the correct position of the pivot element)
        int i = low - 1;

        for (int j = low; j < high; j++) {
            // If current element is less than or equal to pivot
            if (arr[j] <= pivot) {
                i++;
                // Swap arr[i] and arr[j]
                swap(arr, i, j);
            }
        }

        // Place the pivot element in the correct position
        swap(arr, i + 1, high);
        return i + 1;
    }

    // Helper function for swapping elements
    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    // Method for testing with InputStream
    public String quickSortTest(InputStream stream) {
        Scanner scanner = new Scanner(stream);
        int n = scanner.nextInt();

        int[] arr = new int[n];
        for (int i = 0; i < n; i++) {
            arr[i] = scanner.nextInt();
        }

        quickSort(arr, 0, arr.length - 1);

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            result.append(arr[i]);
            if (i < arr.length - 1) {
                result.append(" ");
            }
        }

        scanner.close();
        return result.toString();
    }

    @Test
    public void checkQuickSort() throws Exception {
        testCase(1, "1 1 3 4 5");
        testCase(2, "1 2 3 4 5 6");
        testCase(3, "1 2 3 4 5 6 7");
        testCase(4, "7 7 7 7 7");
        testCase(5, "-7 -3 -1 2 4 5");
        testCase(6, "11 12 22 25 34 50 64 76 88 90");
        testCase(7, "0");
        testCase(8, "-5 -2 -1 0 0 3 4 7");
        testCase(9, "777777 888888 999999 1000000 1000001");
        testCase(10, "11 12 22 23 33 34 44 45 56 66 67 78 89 90 99");
    }

    private void testCase(int testNumber, String expected) throws Exception {
        String testData = getTestData(testNumber);
        InputStream inputStream = new java.io.ByteArrayInputStream(testData.getBytes());

        String result = quickSortTest(inputStream);

        assertEquals(expected, result);
    }

    private String getTestData(int testNumber) {
        switch(testNumber) {
            case 1: return "5\n3 1 4 1 5";
            case 2: return "6\n1 2 3 4 5 6";
            case 3: return "7\n7 6 5 4 3 2 1";
            case 4: return "5\n7 7 7 7 7";
            case 5: return "6\n-3 5 -1 2 -7 4";
            case 6: return "10\n64 34 25 12 22 11 90 88 76 50";
            case 7: return "1\n0";
            case 8: return "8\n-5 0 3 -2 7 0 -1 4";
            case 9: return "5\n1000000 999999 1000001 888888 777777";
            case 10: return "15\n45 23 67 12 89 34 78 56 90 11 33 66 99 22 44";
            default: return "";
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        // Чтение размера массива
        System.out.print("Введите размер массива: ");
        int n = scan.nextInt();

        // Чтение массива
        int[] arr = new int[n];
        System.out.print("Введите элементы массива: ");
        for (int i = 0; i < n; i++) {
            arr[i] = scan.nextInt();
        }

        // Сортировка
        quickSort(arr, 0, arr.length - 1);

        // Вывод результата
        System.out.println("Отсортированный массив:");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i] + " ");
        }

        scan.close();
    }
}