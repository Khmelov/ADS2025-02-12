# Name

Subarray Sum Queries

# Task

Given an array of integers and several queries. Each query specifies a subarray (contiguous segment) of the array. For each query, output the sum of all elements in the specified subarray.

# Theory

Prefix Sums

## Definition

**Prefix sums** is a preprocessing technique for arrays that allows quick answers to queries about the sum of elements on a segment.

For an array $a[1..n]$, the prefix sum array $prefix[0..n]$ is defined as follows:

$$prefix[0] = 0$$

$$prefix[i] = \sum_{j=1}^{i} a[j] = a[1] + a[2] + \ldots + a[i]$$

## Building Prefix Sums

The prefix sum array is built in $O(n)$ time:

$$prefix[i] = prefix[i-1] + a[i]$$

## Answering Queries

For a query about the sum of elements on segment $[l, r]$ (1-indexed, inclusive), use the formula:

$$sum(l, r) = prefix[r] - prefix[l-1]$$

**Time complexity:** $O(1)$ per query

## Example

Given array: $a = [1, 4, 2, 10, 3]$

Build prefix sums:
- $prefix[0] = 0$
- $prefix[1] = 1$
- $prefix[2] = 1 + 4 = 5$
- $prefix[3] = 5 + 2 = 7$
- $prefix[4] = 7 + 10 = 17$
- $prefix[5] = 17 + 3 = 20$

Query for sum on segment $[2, 4]$:
$$sum(2, 4) = prefix[4] - prefix[1] = 17 - 1 = 16$$

Verification: $4 + 2 + 10 = 16$ ✓

## Overall Complexity

- **Preprocessing:** $O(n)$
- **Answering $q$ queries:** $O(q)$
- **Total:** $O(n + q)$

