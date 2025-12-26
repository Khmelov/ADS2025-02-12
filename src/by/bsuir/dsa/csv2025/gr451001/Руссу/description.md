# Name

Sum of nodes

# Task

Sum of Nodes

An engineer has a list of parts and a certain amount of metal for them. For convenience, from each previous part there can be at most two parts that can be made next. Each part consumes a certain amount of metal. However, the engineer needs to produce a finished blank from the parts, using up all the provided metal. The task is to find the number of possible blanks.

You are given a binary tree and a number **K**.  
It is required to find the number of branches (paths from the root to a leaf) whose sum of nodes equals **K**. The path must be complete (ending at a node with no children).

## Input format
1. The first line contains the number **K**.  
2. The second line contains the description of the binary tree in the format  

root[left[left,right],right[left,right]]

## Output format
A single number — the number of branches whose sum of nodes equals **K**.

## Constraints
- Number of nodes: no more than 10^5  
- Node values: from 0 to 10^4

# Theory

---
title: Sum of Nodes
weight: 1
---

## Theory
A binary tree is a hierarchical data structure, a type of graph, where each node has no more than two children: left and right.  
**Branch** — a path from the root of the tree to a leaf.  
**Branch sum** — the sum of all node values along this path.

## Complexity

- **Time:** O(N), where N is the number of nodes (each node is visited once).  
- **Memory:** O(H), where H is the height of the tree (recursion depth).


