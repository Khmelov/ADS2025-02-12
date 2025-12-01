# Name

Reconstruct Selection Sort State After k Swaps

# Task

Reconstructing the State of Selection Sort After k Swaps

## Problem  
You are given two arrays of equal length:  
- the original array **A**,  
- the array **B**, which represents the state of the array **after exactly k swaps** performed by the selection sort algorithm on **A**.

The value of **k is unknown**.

## Task  
Determine **all possible values of k** for which the classical selection sort applied to array **A** could produce array **B** after exactly **k swaps**, i.e., at some intermediate moment during the execution of the algorithm before it finishes.

If no such values exist, output an empty list.

## Details  
- In selection sort, at iteration `i`, the algorithm finds the minimum element on the segment `[i..n−1]` and performs exactly one swap with position `i` if the element at position `i` is not already the minimum.  
- Array **B** represents an **intermediate state**, not the final sorted result.  
- A full simulation in `O(n²)` is not allowed for large n.  
- A solution with complexity at least `O(n log n)` is required.

## Input Format  
Two arrays `A` and `B` of equal length `n` are given.

Example:

A = [5, 3, 4, 1, 2]
B = [1, 3, 4, 5, 2]


## Output Format  
Print all possible values of `k` in increasing order.

Example:

[1, 4]


## Explanation  
When performing selection sort on array `A`, the array becomes equal to `B` after the first swap and also after the fourth swap.  
Thus, the valid values of `k` are `1` and `4`.


# Theory

## Theory (Brief)

Selection Sort is an in-place comparison-based algorithm that repeatedly selects the minimum element from the unsorted portion of the array and places it at its correct position. On iteration `i`, the algorithm:

1. Scans the suffix `[i..n−1]` to find the minimum.  
2. Performs **one swap** between positions `i` and `minIndex`, **but only if** `A[i]` is not already the minimum.

### Key Properties
- After each swap, **exactly two positions** in the array change.
- After iteration `i`, the prefix `[0..i]` is already in its final sorted form.
- The algorithm performs as many swaps as there are positions where the current element is not equal to the minimum of the suffix.
- Since array **B** is an intermediate state, it must correspond to the array **immediately after one of these swaps**.

### The challenge
Because classic Selection Sort may perform up to `n` swaps, and each swap produces a new intermediate array, we need to determine whether **B** matches the array after the `k`-th swap for some possible `k`.

We cannot simulate all iterations in `O(n²)`, so the solution must reconstruct the swap sequence more efficiently by analyzing:
- where each minimum would move,
- which swaps actually occur,
- and which intermediate states can match B.

This is what makes the task significantly more complex than standard Selection Sort analysis.


