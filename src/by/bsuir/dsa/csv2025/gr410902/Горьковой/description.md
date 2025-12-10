# Name

Merging k Sorted Arrays

# Task

Merging k Sorted Arrays
## Topics: Binary Heap (Min-Heap), Priority Queue

---

## Problem Description

You are given `k` arrays of integers, each sorted in non-decreasing order. Your task is to merge them into one fully sorted array.

---

## Input Format

The first line contains two integers:

```
k n
```

where
`k` — the number of sorted arrays,
`n` — the maximum possible length of any individual array.

The next `k` lines describe the arrays. Each line has the format:

```
len_i a1 a2 ... a_len_i
```

where
`len_i` — the actual length of the array,
`a_j` — the elements of the array, sorted in non-decreasing order.

It is guaranteed that:

```
M = sum(len_i) ≤ 10^6
```

---

## Output Format

Print all elements of all arrays merged into one sorted array, in a single line, separated by spaces.

---

## Performance Requirements

You must not merge all elements into a single array and sort them using a standard sorting algorithm (`O(M log M)`).

The expected solution must run in:

```
O(M log k)
```

Using a min-heap (binary heap) is recommended.

---

## Example

### Input:

```
3 5
3 1 4 9
2 2 7
4 0 5 6 10
```

### Output:

```
0 1 2 4 5 6 7 9 10
```

# Theory


---
title: Binary Heap
weight: 2
authors:
-Harkavy Nikita
created: 2025
---

A binary heap is used in many algorithms where one needs to repeatedly extract the minimum element from a dynamically changing set of data. Linear structures provide this operation in `O(n)` time, which becomes inefficient on large datasets. In tasks requiring hundreds of thousands of insertions and extractions, such asymptotics become unacceptable. The standard solution is to use a binary heap, which guarantees `O(log n)` time for all fundamental operations.

A binary heap is an almost complete binary tree that satisfies the heap invariant: the value stored in each node is not greater than the values stored in its children. The structure is stored in a plain array. If the root has index `1`, then its children are stored at indices `2` and `3`, their children at `4, 5, 6, 7`, and so on. This representation eliminates the need for pointers and dynamic memory allocation.

The key operations — insertion and extraction of the minimum — are based on local element adjustments (sift-up and sift-down). When inserting, the new element is appended to the end of the array and then moved upward until the heap property is restored. When extracting the minimum, the last element replaces the root and is pushed downward. In both cases, the depth of the tree is `O(log n)`, so each operation runs in `O(log n)` time.

Consider the task of merging `k` sorted arrays. If all elements are simply concatenated and sorted, the complexity becomes `O(M log M)`, where `M` is the total number of elements. But the structure of the task allows a more efficient approach. At each step, the global minimum among all remaining elements must be one of the first elements of the `k` arrays. Thus, there are only `k` candidates. A linear search among them gives `O(k)`, for a total of `O(Mk)`, which is too slow. However, if these candidates are stored in a binary heap, then each minimum-extraction takes only `O(log k)`, giving the overall complexity:

```
O(M log k)
```

This is optimal: no comparison-based structure can find the minimum among `k` elements in sub-logarithmic time.

A heap does not maintain full ordering of its elements — it keeps only the necessary structural minimum. It is not a sorted array; the order of elements that do not lie on the same root-to-leaf path is undefined. This is a deliberate design choice: maintaining minimal structural order enables minimal asymptotic cost.

Unlike hashing, the heap operates fully deterministically. There is no randomness, no collisions, no dependence on remainder operations or hardware behavior. All operations are executed in a strictly deterministic manner and guarantee their stated asymptotics.

## Implementation Choices

In practice, heaps are almost always implemented using arrays. One-based indexing simplifies computing child and parent indices, but many libraries prefer zero-based indexing — this does not change the asymptotic complexity.

A heap can store not only numerical values but also small structured objects, such as triples (value, array index, element index). The key comparison still takes constant time, so operations remain `O(log n)`.

## Asymptotics

A sequence of insert and extract-min operations has an amortized complexity of `O(log n)`.
For tasks such as merging sorted arrays, the final asymptotic complexity becomes:

```
O(M log k)
```

where `M` is the total number of elements.

This bound is optimal for any comparison-based data structure.



