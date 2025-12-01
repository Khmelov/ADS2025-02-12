# Name

Insertion Sort - Implementation and Testing

# Task

Assignment: Insertion Sort - Implementation and Testing

Create an InsertionSort class with a main method.
Implement the insertion sort algorithm for integer arrays.

Input from console:
- Number of elements N
- N integers separated by spaces

The program must:
1. Print the original array
2. Print all intermediate array states after each iteration of the outer loop
3. Print the sorted array

Insertion sort algorithm:
for i from 1 to n-1:
    key = arr[i]
    j = i - 1
    While j >= 0 and arr[j] > key:
        arr[j + 1] = arr[j]
        j = j - 1
    arr[j + 1] = key

Example:

Input:
6
5 2 4 6 1 3

Output:
Original array: [5, 2, 4, 6, 1, 3]
After iteration 1: [2, 5, 4, 6, 1, 3]
After iteration 2: [2, 4, 5, 6, 1, 3]
After iteration 3: [2, 4, 5, 6, 1, 3]
After iteration 4: [1, 2, 4, 5, 6, 3]
After iteration 5: [1, 2, 3, 4, 5, 6]
Sorted array: [1, 2, 3, 4, 5, 6]

# Theory

---
title: Insertion Sort Analysis
weight: 2
authors:
- Your Name
created: 2025
---

Insertion sort is one of the simplest sorting algorithms that works by building the final sorted array one element at a time. While inefficient for large datasets, it excels in certain scenarios and serves as an excellent educational tool for understanding fundamental sorting concepts.

## Algorithm Complexity

The time complexity of insertion sort reveals its strengths and limitations:

- **Best case:** $O(n)$ - when the array is already sorted
- **Average case:** $O(n^2)$ - for random input data
- **Worst case:** $O(n^2)$ - when the array is reverse sorted
- **Space complexity:** $O(1)$ - in-place sorting algorithm

The quadratic complexity arises from the nested loop structure where each element may need to be compared with all previous elements in the sorted portion.

## Adaptive Nature

Insertion sort is *adaptive* - it becomes more efficient when processing partially sorted arrays. This property makes it practical for real-world scenarios where data often arrives in nearly sorted order.

The algorithm's performance can be analyzed through operation counting:

- **Comparisons:** Elements are compared to find the correct insertion point
- **Swaps:** Elements are shifted right to make space for insertion

For an array of size $n$, the maximum number of comparisons and swaps is:

$$\sum_{i=1}^{n-1} i = \frac{n(n-1)}{2} = O(n^2)$$

## Practical Considerations

**When to use insertion sort:**
- Small datasets ($n < 50$)
- Nearly sorted data
- Online sorting scenarios
- Educational purposes
- As a building block in more complex algorithms (e.g., Timsort)

**Advantages:**
- Simple implementation
- Stable sorting
- In-place operation
- Adaptive performance
- Low memory overhead

**Mathematical Insight:**

The expected number of comparisons for random input is approximately $\frac{n^2}{4}$, which is half the worst-case scenario. This expected performance, combined with its $O(n)$ best-case behavior, makes insertion sort surprisingly effective for many practical applications despite its $O(n^2)$ worst-case complexity.

## Educational Value

Studying insertion sort provides fundamental insights into:
- Algorithmic thinking and problem decomposition
- Time complexity analysis
- The trade-offs between simplicity and efficiency
- Adaptive algorithm design
- Empirical performance measurement through operation counting

The algorithm serves as a gateway to understanding more complex sorting strategies and remains relevant in modern computing despite its simplicity.

