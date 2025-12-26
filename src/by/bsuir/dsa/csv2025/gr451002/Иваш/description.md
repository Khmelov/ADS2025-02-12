# Name

Binary heap

# Task

Problem Statement
Implementation and Analysis of a Binary Heap (Min-Heap)

Objective of the Work
To study one of the most important data structures—a binary heap—implement it efficiently in Java, prove the asymptotic complexity of its operations, and demonstrate its correctness using visual tests.

Required implementation
1. BinaryHeap class — a min-heap binary heap storing int elements
2. Support for the following operations:
- void insert(int value)
- int extractMin() — throws NoSuchElementException if the heap is empty
- int peekMin() — look at the minimum without removing
- size(), isEmpty()
- toString() — nicely displays the current heap state
3. BinaryHeap(int[] array) constructor — builds a heap in linear time O(n)
4. Automatically grows the internal array on overflow
5. Demo program (with main or tests) showing clear input and output:
   input: [array of elements]
   output: [values ​​sorted in ascending order]

Performance Requirements
- insert and extractMin — O(log n) in the worst case
- build a heap of n elements — strictly O(n), not O(n log n)
- no more than O(n) additional memory usage

# Theory

Binary Heap
A Complete Guide: From Theory to Implementation

**Author:** Igor Ivash

---

## What is a Heap?

A Heap is an abstract data structure that implements a priority queue.

It supports three main operations:

| Operation | Complexity | Purpose |
|-------------------------|------------|-------------------------------------|
| Find Min/Max| **O(1)** | Just return the root |
| Remove Min/Max | **O(log n)** | Extract the root and rebuild the heap |
| Append an element | **O(log n)** | Insert at the end + pop |

Thanks to these properties, heaps are used everywhere:
- Dijkstra's Algorithm
- Prim's Algorithm (Minimum Spanning Tree)
- Heapsort
- Task schedulers, A* search, etc.

---

## Binary Heap - How Does It Work?

A binary heap is a complete tree that satisfies three conditions:

1. Heap Property (for min-heap):

The value of a parent is ≤ the values of all its children
→ the minimum element is always at the root!

2. Binary Property: Each node has at most two children.

3. Completeness: All levels are completely filled except the last one, and it is filled from left to right.

This allows a heap to be stored perfectly in an array!

### Memory Representation

```text
Indices:     1
            /   \
           2     3
          / \   / \
         4   5 6   7

