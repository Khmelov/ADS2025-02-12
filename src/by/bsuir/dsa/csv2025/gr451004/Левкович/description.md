# Name

Counting sort

# Task

Counting Sort

## Formal Statement
Given an array of integers in the range from 0 to k. Sort the array in linear time.

## Real-world Motivation
Counting sort is widely used in systems that require fast sorting of data with limited value ranges: processing student grades, analyzing age groups, statistical processing of discrete data.

## Input Format
First line: integer n (1 ≤ n ≤ 1000) - number of elements  
Second line: n integers in range [0, 100], separated by spaces

## Output Format
Array sorted in ascending order

## Example
Input:
6
4 2 2 8 3 3

text

Output:
2 2 3 3 4 8

# Theory

Counting Sort Algorithm

## Mathematical Foundation
For array $A$ of length $n$ with elements in range $[0, k]$:

1. **Frequency counting:** Create counter array $C$ of size $k+1$
   $$ C[i] = \text{number of elements equal to } i $$

2. **Prefix sums:** Transform $C$ into position array
   $$ C[i] = C[i] + C[i-1] \quad \text{for } i = 1 \text{ to } k $$

3. **Building result:** Place elements in output array $B$
   $$ B[C[A[j]] - 1] = A[j] $$
   $$ C[A[j]] = C[A[j]] - 1 $$

## Pseudocode
Counting-Sort(A, k):
C = [0] * (k+1)
for j = 1 to length(A):
C[A[j]] = C[A[j]] + 1
for i = 1 to k:
C[i] = C[i] + C[i-1]
for j = length(A) downto 1:
B[C[A[j]]] = A[j]
C[A[j]] = C[A[j]] - 1

## Complexity
- **Time:** $O(n + k)$
- **Space:** $O(n + k)$

## Limitations
Works only for integers with limited range

