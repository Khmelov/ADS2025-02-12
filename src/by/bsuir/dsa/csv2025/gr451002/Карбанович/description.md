# Name

Find the k-th largest non-negative element of an array

# Task

Task: Find the k-th largest non-negative element of an array

## Problem

Given an array of n integers and a number k. Find the k-th largest non-negative element of the array (numbering starts with 1).
If there are fewer than k non-negative elements, print -1.

### Input

The first line contains two integers:

1 <= n <= 100000 — the number of array elements
1 <= k <= n — the number of the element to find

The second line contains n integers — the array elements.

### Output

Print a single number — the k-th largest non-negative element of the array, or -1 if there are fewer than k such elements.

## Examples

### Example 1

**Input:**

5 2
7 -3 10 1 -5

**Output:**

7

### Example 2

**Input:**

6 4
13 1 -9 25 4 -7

**Output:**

25

# Theory

HeapSort (heapsort)

## What is HeapSort?
- HeapSort is a sorting algorithm based on a heap data structure (usually a binary heap or max-heap).
- It is similar to selection sort: at each step, the maximum (or minimum) is selected and then moved to the end (or beginning) of the array.
- The algorithm runs in-place, meaning it requires almost no additional memory (O(1) overhead).
- Worst-, average-, and best-case running time are O(nlogn).

## Main steps of the algorithm

1. Build-Heap
- Convert the input array to a max-heap.
- This is done using the heapify (or sift-down) operation, starting from the first non-leaf node.
- Interesting: asymptotically, this process takes O(n), not O(nlogn).

2. **Sorting**
- After the array has become a max-heap, the largest element is at the root (index 0 or 1, depending on the implementation).
- Swap the root with the last element of the heap, decreasing the "size" of the heap by 1.
- Apply `sift_down` to the root to restore the heap property.
- Repeat until there is more than one element left in the heap.
- At the end (depending on the implementation), you can reverse the array if descending order is achieved.

## Properties

- **Time Complexity**: O(nlogn) guaranteed.
- **Extra Memory**: O(1) (in-place).
- **Instability**: HeapSort is generally not a stable algorithm, i.e., identical elements may change order.
- **Pros**:
- Guaranteed time complexity, unlike QuickSort, which can degrade.
- Does not require significant additional space.
- **Cons**:
- Sometimes slower in practice than a well-implemented QuickSort due to poor memory locality.
- Instability.

