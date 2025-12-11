# Name

Maximum Difference

# Description

Maximum Difference

## Task  
You are given an array of integers.  
Find the **maximum difference** between two elements of the array such that the larger element appears **after** the smaller one.  

Formally, for array \(A\), compute:  



max(A[j] - A[i]), where j > i




If no such pair exists (the array is non-increasing), output `0`.

---

## Examples  

| Input                  | Output |
|------------------------|--------|
| `[2, 3, 10, 6, 4, 8, 1]` | `8` (10 - 2) |
| `[7, 9, 5, 6, 3, 2]`     | `2` (9 - 7)  |
| `[1, 2, 3, 4, 5]`        | `4` (5 - 1)  |
| `[5, 4, 3, 2, 1]`        | `0`          |
| `[10, 20, 5, 25]`        | `20` (25 - 5)|

## Input
The first line contains an integer $n$ — the number of elements in the array.  
The second line contains $n$ integers $A[i]$.

## Output
Print a single integer — the maximum difference between two elements of the array such that the larger element appears to the right of the smaller one.  
If no such pair exists, print `0`.



# Theory

---
title: Maximum Difference
weight: 2
authors:
- Pavel Zabelich
created: 2025
---

In array problems, it is often necessary to compare elements at different positions. Here the goal is to find such a pair where the larger element appears **to the right** of the smaller one, and their difference is maximal. This is a classic example where brute force loses to a careful linear scan.

## Intuition and Statement

Formally, for an array $A$ we need to compute

$$
\max(A[j] - A[i]) \quad \text{where } j > i.
$$

If the array is **non-increasing**, then no valid pair exists — the answer is $0$.

## Naive Solution

**Idea:** iterate over all pairs of indices $(i, j)$ with $j > i$, compute $A[j] - A[i]$, and take the maximum.

- **Complexity:** $O(n^2)$.
- **Drawback:** does not scale for large inputs.

## Linear Solution

**Idea:** keep track of the minimum element on the left and update the answer at each step.

- **Steps:**
  1. Initialize **left minimum** as $A[0]$, **answer** as $0$.
  2. For each $j$ from $1$ to $n-1$:
     - Update answer: $ans = \max(ans, A[j] - min\_left)$.
     - Update left minimum: $min\_left = \min(min\_left, A[j])$.
- **Complexity:** $O(n)$ time, $O(1)$ space.

## Why It Works

**Key idea:** for each right element $A[j]$, the best candidate to subtract is the **minimum** among all $A[i]$ to the left ($i < j$).  
By maintaining $min\_left$, we never miss a potentially better pair, while equal or larger left values always give a smaller or equal difference.

## Edge Cases

- **All elements equal:** answer is $0$.
- **Non-increasing array:** answer is $0$.
- **Negative numbers:** algorithm works without changes.
- **Large values:** use integer types with sufficient range (e.g., 64-bit).

### Bonus: short exercise

> Construct an array of 6–8 numbers where the linear algorithm updates the answer **exactly once**. Explain why this happened (which element became the only candidate for the maximum and under which left minimum).


