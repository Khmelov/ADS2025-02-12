# Name

Quick sort

# Task

Quick Sort

## Algorithm

Quick Sort is an efficient sorting algorithm that uses the "divide and conquer" strategy. The algorithm selects a pivot element and partitions the array into two parts: elements less than the pivot and elements greater than the pivot, then recursively sorts both parts.

## Implementation

```java
public class Main {

    // Main quick sort function
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
}
```
## Correctness

By induction, it can be shown that after each call to the partition function, the pivot element occupies its final position in the sorted array, with all elements to the left being less than or equal to it, and all elements to the right being greater than it. Recursive application of this process to all subarrays guarantees complete sorting of the array.

## Asymptotic Analysis

Since the algorithm uses the "divide and conquer" strategy with recursive calls, and in the worst case the partition may be unbalanced, the asymptotic complexity is:

- **Worst case**: O(n²) - when the array is already sorted or all elements are equal
- **Best case**: O(n log n) - when the pivot element divides the array in half
- **Average case**: O(n log n)

The partition function performs a single pass through the array in O(n) time, and recursive calls in the average case have O(log n) depth, so the overall complexity is O(n log n).

# Theory

```markdown
---
title: Quick Sort
weight: 2
authors:
- Article Author
created: 2023
---

# Quick Sort

## Algorithm

Quick Sort is an efficient sorting algorithm that uses the "divide and conquer" strategy. The algorithm selects a pivot element and partitions the array into two parts: elements less than the pivot and elements greater than the pivot, then recursively sorts both parts.

## Implementation

```java
public class QuickSort {

    // Main quick sort function
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
}
```

## Example Usage

```java
int[] arr = {5, 2, 1, 3, 1};
QuickSort.quickSort(arr, 0, arr.length - 1);

for (int i = 0; i < arr.length; i++)
    System.out.print(arr[i] + " ");
```

## Correctness

By induction, it can be shown that after each call to the `partition` function, the pivot element occupies its final position in the sorted array, with all elements to the left being less than or equal to it, and all elements to the right being greater than it. Recursive application of this process to all subarrays guarantees complete sorting of the array.

## Asymptotic Analysis

Since the algorithm uses the "divide and conquer" strategy with recursive calls, and in the worst case the partition may be unbalanced, the asymptotic complexity is:

- **Worst case**: $O(n^2)$ - when the array is already sorted or all elements are equal
- **Best case**: $O(n \log n)$ - when the pivot element divides the array in half
- **Average case**: $O(n \log n)$

The `partition` function performs a single pass through the array in $O(n)$ time, and recursive calls in the average case have $O(\log n)$ depth, so the overall complexity is $O(n \log n)$.

### Runtime Analysis

Let $T(n)$ be the algorithm's runtime on an array of length $n$. Then:

$$
T(n) = T(k) + T(n-k-1) + O(n)
$$

where $k$ is the number of elements smaller than the pivot.

**Ideal case**: when the partition is always balanced ($k \approx \frac{n}{2}$):

$$
T(n) = 2T(\frac{n}{2}) + O(n)
$$

By the master theorem: $T(n) = O(n \log n)$

**Worst case**: when the partition is maximally unbalanced ($k = 0$ or $k = n-1$):

$$
T(n) = T(n-1) + O(n)
$$

Solution to this equation: $T(n) = O(n^2)$
```

