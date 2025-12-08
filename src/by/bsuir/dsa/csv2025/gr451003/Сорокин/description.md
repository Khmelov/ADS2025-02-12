# Name

Insertion Sort with Performance Analysis

# Task

Insertion Sort with Performance Analysis

## Problem

Implement the insertion sort algorithm that returns not only the sorted array but also performance statistics:
- Number of comparisons
- Number of element swaps

**Conditions:**
- Use only the standard insertion sort version
- Do not use additional data structures for optimization
- Maintain accurate operation counting

## Input Format

First line: integer n (1 ≤ n ≤ 1000) - array size
Second line: n integers separated by spaces

## Output Format

First line: sorted array
Second line: "Comparisons: X, Swaps: Y"

## Example

Input:
6
5 2 4 6 1 3

Output:
1 2 3 4 5 6
Comparisons: 12, Swaps: 12

## Constraints

- Time complexity: O(n²) worst case
- Space complexity: O(1) additional memory

# Theory

---
title: Insertion Sort
weight: 2
---

The insertion sort algorithm is based on sequentially expanding the sorted prefix of the array. At each step, we take the next element and insert it into the correct position in the already sorted part.

## Algorithm

At the k-th step, we already have a sorted prefix of length k. To extend this sorted prefix, we take the element immediately following it and swap it with its left neighbor until this element is no longer smaller than its left neighbor. When this happens, it means the element is greater than all elements to its left and less than all elements in the prefix to its right, indicating we have correctly inserted this element into the sorted portion of the array.

```cpp
void insertion_sort(int *a, int n) {
    for (int k = 1; k < n; k++)
        for (int i = k; i > 0 && a[i - 1] > a[i]; i--)
            // we haven't reached the beginning of the array and the previous element is greater
            swap(a[i], a[i - 1]);
}

