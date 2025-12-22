# Name

Inversions and Partial Permutations (LEVEL C)

# Task

Inversions and Partial Permutations (LEVEL C)

# Theory

## Problem Statement

An array of integers \(a\) of length \(n\) is given.
It is known that the array is almost sorted in descending order in the following sense:

- The array contains at most \(k\) *inversions*—pairs of indices \((i, j)\) such that \(i < j\), but \(a[i] < a[j]\).

Required:

1. Sort the array in descending order using a modified insertion sort.
2. Speed ​​up insertion sort by replacing the linear search for the insertion position with a binary search.
3. Implement the algorithm:
- binary search for a position in a sorted descending prefix
- shifting elements using a regular loop
4. Estimate the asymptotics:
- in the worst case
- in terms of the number of inversions \(k\)

The program should:

- read \(n\) and an array of \(n\) numbers
- sort it using binary insertion sort
- output the result

## Simple input and output example

### Sample Input
5
4 2 7 1 3

### Sample Output
7 4 3 2 1

