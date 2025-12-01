# Name

 Cartesian tree with duplicate key support

# Task

Challenge: Cartesian tree with duplicate key support

## Setting the task

It is required to implement a modified version of the Cartesian tree capable of efficiently processing operations on a multiset of integers with support for statistical queries.

## Terms of Reference

Develop a Cartesian tree-based data structure to:

- * * insert x * * - add element x to a multiset
- * * erase x * * - remove one occurrence of element x from a multiset
- * * count x * * - count the number of occurrences of the element x
- * * sum L R * * - calculation of the sum of all elements in the range [L, R]
- * * kth k * * - finding the k-th element in the sorted order of the multiset
- * * next x * * - search for a minimum element strictly greater than x
- * * prev x * * - search for the maximum element strictly less than x

## Specification of requirements

### Functional requirements

1. * * Duplicate support * *: The structure must correctly handle duplicate keys, preserving information about the number of occurrences of each element.

2. * * Efficiency of operations * *: All operations must be performed in logarithmic time relative to the size of the multiset in the average case.

3. * * Statistical queries * *: Ensure that queries for ranges and ordinal statistics work correctly, taking into account repeating elements.

4. * * Boundary value handling * *: Correct operation with minimum and maximum integer values, empty sets and non-existent elements.

### Structure Requirements

1. * * Tree node * * must contain:
   - Key (key)
   - Priority to support heap property
   - Repetition count
   - Subtree sum (sum)
   - Subtree size (size)
   - Child references

2. * * The split * * operation must split the tree by key, preserving the properties of the Cartesian tree.

3. * * Merge * * must combine two Cartesian trees into one.

4. * * Update of * * metadata should occur with each modification of the structure.

## Implementation features

### Duplicate Accounting

Unlike the classical Cartesian tree, where each key is unique, in this implementation:
- Inserting an existing key increases the repetition count
- When deleted, the counter decreases, the node is deleted only when it reaches zero
- All statistical operations consider multiple occurrences

### Process large numbers

- Amounts are of type'long 'to prevent overflow
- Special attention is paid to operations with extreme values   of the integer range

## Input format

The first line contains an integer 'n' - the number of operations.

This is followed by'n 'strings, each representing one operation in the format:
- `insert x`
- `erase x`
- `count x`
- `sum L R`
- `kth k`
- `next x`
- `prev x`

where'x ',' L ',' R ',' k 'are integers.

### Sample input:
                7
                1 5
                1 3
                1 5
                3 5
                3 3
                3 1
                3 6
## Output Format

For each operation 'count', 'sum', 'kth', 'next', 'prev', the result of execution is displayed.

The results are displayed in one line, separated by spaces, in the order in which they were performed.

### Sample output: 2 1 0 0

# Theory

---
title: Cartesian tree
weight: 3
authors:
- Andrew
created: 2025
---

## Definition

* * Cartesian tree (treap, deramide) * * - a data structure that combines:
- properties * of the binary search tree * by the key 'x';
- properties * heap * by priority 'y'.

Each node stores a pair of '(x, y)':
- in the left subtree the keys are less than 'x';
- in the right subtree, the keys are greater than 'x';
- priorities form a heap: the parent has less priority than the descendants.

## Properties

- Average time of operations:
  - Search: $O (\log n) $
  - Insert: $O (\log n) $
  - Delete: $O (\log n) $
- Building a tree from an array: $O (n) $
- Balancing is achieved by random selection of priorities.

## Basic Operations

### Split
Divides the 'T' tree into two:
- 'L' with keys ≤ 'x'
- 'R' with keys> 'x'

* * Formula: * *


\[
T = L \cup R,\quad \max(L) \leq x < \min(R)
\]



---

### Merge
Combines two trees, where all keys in 'L' are less than keys in 'R'.

* * Formula: * *


\[
\forall u \in L, v \in R: u.key < v.key
\]



---

### Insert
Inserting node '(x, y)':
- Uses' split 'and' merge '.
- Balancing is saved automatically.

* * Formula: * *


\[
Insert(T, (x,y)) = Merge(Merge(L, newNode), R), \quad (L,R) = Split(T, x)
\]



---

### Erase
Delete node by key:
- Finds a node.
- Merges his left and right subtrees.

* * Formula: * *


\[
Erase(T, x) = Merge(T.left, T.right)
\]



---

## Formulas and ratings

- Average node depth:


\[
E[depth] = O(\log n)
\]



- Probability of poor balancing:


\[
P (\text {depth}> c\cdot\log n )\to 0\quad\text {at} n\to\infty
\]



---

## Application

- Fast implementation of dynamic sets.
- Support for operations on arrays (for example, working with subtrips).
- Used in online processing tasks and balanced structures.

