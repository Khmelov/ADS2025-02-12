# Name

Heap Sort

# Task

Heap Sort

Implement the heap sort algorithm for an integer array.

**Input Format:**
- First line: an integer `n` (1 ≤ n ≤ 1000) - the number of elements in the array.
- Second line: `n` integers separated by spaces - the elements of the array.

**Output Format:**
- One line: the array sorted in non-decreasing order, elements separated by spaces.

# Theory

---
title: Heap Sort
weight: 3
prerequisites:
- Binary Heap
- Selection Sort
---

**Heap sort** is an efficient sorting algorithm based on the binary heap data structure. It runs in $O(n \log n)$ time in worst, average, and best cases and uses $O(1)$ additional memory.

## Algorithm Idea

The reason why selection sort runs in quadratic time is the linear search for the minimum element at each step.

To optimize the algorithm's running time, we can use a special data structure that supports fast extraction of the minimum element from an unordered set, add all elements of the original array to it, and then extract the minimum elements one by one into a new sorted array.

This is exactly what a binary heap does — in $O(\log n)$ per operation. Using it, we obtain an algorithm that runs in $O(n \log n)$.

## Algorithm

Heap sort consists of two main phases:

1. **Building a max-heap** from the input array
2. **Sequentially extracting maximum elements** and rebuilding the heap

### Heap Construction

To build a max-heap from an arbitrary array, it's sufficient to apply the `sift_down` operation to all elements from indices $\lfloor n/2 \rfloor$ down to $1$:

```cpp
void build_heap() {
    for (int i = n/2; i >= 1; i--) {
        sift_down(i);
    }
}

