package by.it.group410901.bukshta.lesson04;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

/*
В первой строке источника данных даны:
        - целое число 1<=n<=100000 (размер массива)
        - сам массив A[1...n] из n различных натуральных чисел,
          не превышающих 10E9, в порядке возрастания,
Во второй строке
        - целое число 1<=k<=10000 (сколько чисел нужно найти)
        - k натуральных чисел b1,...,bk не превышающих 10E9 (сами числа)
Для каждого i от 1 до kk необходимо вывести индекс 1<=j<=n,
для которого A[j]=bi, или -1, если такого j нет.

        Sample Input:
        5 1 5 8 12 13
        5 8 1 23 1 11

        Sample Output:
        3 1 -1 1 -1
*/
public class A_BinaryFind {
    public static void main(String[] args) throws FileNotFoundException {
        InputStream stream = A_BinaryFind.class.getResourceAsStream("dataA.txt");
        if (stream == null) {
            stream = System.in; // Fallback to standard input if file not found
        }
        A_BinaryFind instance = new A_BinaryFind();
        int[] result = instance.findIndex(stream);
        for (int index : result) {
            System.out.print(index + " ");
        }
    }

    int[] findIndex(InputStream stream) throws FileNotFoundException {
        // Initialize scanner for input
        Scanner scanner = new Scanner(stream);

        // Read size of sorted array
        int n = scanner.nextInt();
        // Read sorted array
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        // Read number of queries
        int k = scanner.nextInt();
        int[] result = new int[k];
        // Process each query
        for (int i = 0; i < k; i++) {
            int value = scanner.nextInt();
            // Binary search for the value
            int left = 0;
            int right = n - 1;
            int index = -1;

            while (left <= right) {
                int mid = left + (right - left) / 2; // Avoid potential overflow
                if (a[mid] == value) {
                    index = mid + 1; // Adjust to 1-based indexing
                    break;
                } else if (a[mid] < value) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }

            result[i] = index;
        }

        scanner.close();
        return result;
    }
}