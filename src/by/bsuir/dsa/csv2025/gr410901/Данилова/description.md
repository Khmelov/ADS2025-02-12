# Name

Sales sorting

# Task

Sales Sorting

## Problem Statement

A company analyzes sales data. You have information about orders, each belonging to a specific product category. You need to process a series of queries that determine the number of orders for each category whose total exceeds a specified value.

### Input Data Format

The first row contains two integers:
- `n` (1 ≤ n ≤ 10^5) - number of orders
- `m` (1 ≤ m ≤ 10^3) ​​- number of queries

The next `n` rows describe orders in the following format:
- `id` - unique order identifier
- `amount` (1 ≤ amount ≤ 10^9) - order total
- `category` (1 ≤ category ≤ 10^5) - product category

The last `m` rows contain threshold values ​​for the queries.

### Output Format

For each of the `m` queries, print on a separate line the number of orders in each category (in ascending order of category numbers) whose sum exceeds the threshold.

### Example

**Input:**

3 2

1 100 1

2 200 1

3 150 2

100

200

**Output:**

1 1

0 0

### Limitations

- Runtime: O(n log n + m log n)
- Memory: O(n)
- Uniqueness of order IDs is guaranteed

# Theory

---
title: Sorting
created: 2024
---

# Sorting

**Sorting** is a fundamental algorithmic primitive, consisting of ordering the elements of a set according to a given criterion. Efficient sorting algorithms are the basis for many data processing, searching, and analysis tasks.

## Basic Concepts and Terminology

### Key Definitions

- **Sort Key** — the value by which elements are compared
- **Stability** — the property of an algorithm to preserve the relative order of elements with the same keys
- **Adaptability** — the algorithm works effectively with partially sorted data

## Classification of Sorting Algorithms

### Bubble Sort

#### Operating Principle
The algorithm iterates through the array, comparing adjacent elements and swapping them if they are in the wrong order. On each pass, the largest element "floats" to the end of the array.

### Selection Sort

#### How it works
At each step, the algorithm finds the minimum element in the unsorted portion of the array and places it at the end of the sorted portion.

### Insertion Sort

#### How it works
The algorithm builds a sorted sequence, one element at a time, inserting each new element in the correct position.

### QuickSort

#### How it works
Pivot selection.
Dividing the array into elements less than, equal to, and greater than the pivot. Recursive Subarray Sorting

### MergeSort

#### How it works
The algorithm recursively splits the array in half, sorts each half, and then merges them into a single sorted array.

### HeapSort

#### How it works
The algorithm builds a max-heap from the array, then repeatedly extracts the maximum element and rebuilds the heap.

