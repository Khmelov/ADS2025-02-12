# Name

Maximum Sum BST in Binary Tree

# Task

Maximum Sum BST in Binary Tree

## Task

Given a binary tree `root`, return the maximum sum of all keys of any sub-tree which is also a Binary Search Tree (BST).


Assume a BST is defined as follows:
* The left subtree of a node contains only nodes with keys **less than** the node's key.
* The right subtree of a node contains only nodes with keys **greater than** the node's key.
* Both the left and right subtrees must also be binary search trees.

For a given sequence (array representation of a tree):
1. Construct the binary tree.
2. Find the subtree that is a valid BST with the maximum sum of keys.
3. Output the resulting sum.

> **Remarks:**
> - Input is provided in the array representation format (level-order traversal), where `null` indicates a missing node;
> - If there are no valid BSTs with a positive sum, the result is 0.

## Example

- Input:<br>
  `1 4 3 2 4 2 5 null null null null null null 4 6`
- Expected output:<br>
  `20`

# Theory

Theoretical Info

## Binary Search Tree (BST)

### Definition

**Binary Tree** is a data structure where each node has at most two children.

**Binary Search Tree (BST)** is a specific type of binary tree that satisfies strict ordering conditions. For any node `x`:

1.  All keys in the **left** subtree are strictly less than the key of node `x`.
2.  All keys in the **right** subtree are strictly greater than the key of node `x`.
3.  Both the left and right subtrees must themselves be valid BSTs.

### Subtree Validation Criteria

To check if a tree rooted at node `x` is a valid BST, it is not enough to simply compare `x` with its immediate left and right children. The ranges of values of the entire subtree must be considered.

Node `x` is the root of a BST if and only if:
1.  The left subtree is a BST (or empty).
2.  The right subtree is a BST (or empty).
3.  The value of `x` is greater than the **maximum** value in the left subtree.
4.  The value of `x` is less than the **minimum** value in the right subtree.

