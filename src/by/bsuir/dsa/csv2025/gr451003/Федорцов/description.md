# Name

Selection sort

# Task

## Problem Statement

Study and implement the selection sort algorithm. Write a Java function that performs selection sort on an integer array.

# Theory

## Selection Sort

To sort an array, we select the minimum among the unsorted numbers `n` times and place it in its correct position (namely, at the `k`-th position after the `k`-th iteration). To simplify the implementation, on the `k`-th iteration, we will search for the minimum in the segment `[k, n-1]`, swapping it with the current `k`-th element, after which the segment `[0, k]` will be sorted.

## Implementation in Java

public static void selectionSort(int[] a) {
    int n = a.length;
    for (int k = 0; k < n - 1; k++) {
        for (int j = k + 1; j < n; j++) {
            if (a[k] > a[j]) {
                int temp = a[j];
                a[j] = a[k];
                a[k] = temp;
            }
        }
    }
}

