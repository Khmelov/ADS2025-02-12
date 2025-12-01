# Name

Covering with sections

# Task

Covering with sections

## Problem statement

There are n segments on a number line and m points.
For each point, we need to determine how many segments it is covered by.

### Input data format

First line:

n (1 ≤ n ≤ 10⁵) — number of segments
m (1 ≤ m ≤ 10⁵) — number of points

Next n lines describe the segments:

l r — coordinates of the ends of the segment (–10⁹ ≤ l, r ≤ 10⁹, l may be > r)

The last line contains m integers:

coordinates of points (–10⁹ ≤ pi ≤ 10⁹)

### Output data format

One line of m numbers is the number of segments covering each point.

### Example

**Input data:**

2 3
0 5
7 10
1 6 11

**Output data:**

1 0 0

### Restrictions

- Time: O(n log n + m log n)
- Memory: O(n)
- You need to implement your own sorting (quick, hybrid, with 3-way partitioning, etc.)
- Using Arrays.sort() is prohibited.

# Theory

---
title: Сортировки
weight: 5
authors:
  - Veronika Skachkova
created: 2025
---

The presented code implements a hybrid sorting algorithm that combines the advantages of two algorithms: QuickSort and Insertion Sort. This approach demonstrates an important concept in algorithms: leveraging the strengths of different methods to achieve optimal performance.

## QuickSort and three-sided partitioning

QuickSort operates on a divide-and-conquer principle, selecting a pivot element and splitting the array into two parts: elements less than the pivot element and elements greater than the pivot element. This process is applied recursively to the resulting subarrays. QuickSort's main advantage is its average time complexity of O(n log n) and its efficiency when working with large data sets. However, the algorithm has a significant drawback: in the worst case, its complexity degrades to O(n²), which occurs when the pivot element is poorly chosen, especially in already sorted or nearly sorted arrays.

To mitigate this problem, the implementation uses the "median of three" method, which selects the pivot element as the median of the first, middle, and last elements of the array. This approach significantly reduces the likelihood of the worst-case scenario. Additionally, a three-way partition (known as the Dutch flag algorithm) is used, which divides the array not into two, but into three parts: elements less than the pivot, equal to the pivot, and greater than the pivot. This is especially effective when there are duplicate elements in the array.

## Insertion sort

Insertion Sort, unlike QuickSort, works by gradually building a sorted sequence, inserting each new element into the correct position relative to the already sorted part. Although this algorithm generally has quadratic complexity of O(n²), it is exceptionally efficient for small arrays and nearly sorted data. Its advantages include ease of implementation, robustness (preserving the relative order of equal elements), and minimal overhead.

## Hybrid sorting

The hybrid approach presented in the code intelligently combines these two algorithms, using insertion sort for small subarrays (when the size falls below a certain threshold, typically 10-20 elements) and quicksort for larger portions of the array. This choice is based on empirical observations: for small arrays, the overhead of recursion and complex partitioning logic outweighs the benefit of asymptotically better algorithms, while simpler algorithms like insertion sort prove more efficient. Hybrid sorting algorithms are a fine example of practical wisdom in algorithm design: instead of searching for a universal solution, they exploit the situational advantages of different approaches.

