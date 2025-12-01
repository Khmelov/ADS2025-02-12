# Name

Closest Value with Tolerance

# Task

Search with Tolerance in BST

Implement a function to search a binary search tree (BST) for the value closest to a given target value, taking into account the allowable maximum difference. If no such value is found, return "NO". If there are two suitable numbers, output the smallest one.

**Constraints:**
- Node.value > 0

# Theory

Search Trees

Search trees are data structures that allow efficient storage and processing of ordered data. The most common type is binary search trees (BST).

## Basic BST Properties

For each node in a BST, the following conditions hold:
- All values in the **left** subtree are **less than or equal to** the node's value
- All values in the **right** subtree are **greater than or equal to** the node's value

This property allows performing search, insertion, and deletion operations in **O(log n)** time on average.

## Balancing

In the worst case (degenerate tree), time complexity of operations reaches **O(n)**. To solve this problem, **balanced** trees exist:
- **AVL trees** - strict balancing (height difference between subtrees ≤ 1)
- **Red-black trees** - less strict but more efficient balancing
- **B-trees** - optimized for disk operations

