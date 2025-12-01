# Name

Validate binaty search tree

# Task

Validate binary search tree

Given binary tree, *determine if it is a valid binary search tree (BST)*.

## Input

Length n of an input, n tree nodes, which can be either *null*, or integer, where first node  is a root, 2nd and 3rd - its left and right descendants, 4-7 - their descendants etc.

## Output

*true*, if this tree is BST, *false*, if it isn't.

## Examples

> 3 2 1 3
>  true

> 7 5 1 6 null null 3 7
> false

# Theory

Trees

A tree is one of the most common data structures in programming.

Trees consist of a set of nodes and oriented edges (links) between them. The vertices are connected in such a way that from one node, called the root node, it is possible to reach all the others in the only way.

## Binary search trees
Binary search tree (BST) is a tree for which the following properties are fulfilled:

- Any node can not have more than 2 children
- The left subtree of a node contains only nodes with keys strictly less than the node's key.
- The right subtree of a node contains only nodes with keys strictly greater than the node's key.
- Both the left and right subtrees must also be binary search trees.

