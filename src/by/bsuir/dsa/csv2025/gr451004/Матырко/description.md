# Name

Building and Analyzing a Binary Search Tree from a List of Commands

# Task

Write a Java program that receives a single line of text containing a sequence of commands. Each command performs a specific operation on a Binary Search Tree (BST). All commands appear in one line and are separated by spaces. The program must process the commands in order and print outputs only for the commands that require producing a result.
Supported commands:
The command add X inserts the number X into the tree.
The command del X deletes the number X from the tree if it exists.
The command find X checks whether the number X exists in the tree and prints true or false.
The command min finds and prints the minimum value in the tree.
The command max finds and prints the maximum value in the tree.
The command sum calculates and prints the sum of all values stored in the tree. If the tree is empty, the result must be 0.
The tree must follow the standard rules of BST insertion: if a value is less than the node’s value, it goes to the left subtree; if greater or equal, it goes to the right subtree. Deletion must correctly handle all cases: deleting a leaf node, deleting a node with one child, and deleting a node with two children.
The program must print results only for output-producing commands (find, min, max, sum), in the exact order in which these commands appear in the input line.
Example:
Input:
add 8 add 4 add 12 add 4 sum del 4 sum
Output:
28
24

# Theory

A Binary Search Tree (BST) is a data structure in which each node contains a value and references to its left and right subtrees. The key property of a BST is that all values in the left subtree of a node are smaller than the node’s value, while all values in the right subtree are greater than or equal to it. This property allows efficient search, insertion, and deletion. Insertion of a new value is performed by moving from the root toward a leaf: if the value is smaller than the current node, the process continues to the left; otherwise, to the right, until an empty position is found. Searching works the same way, directing movement based on comparisons. Deletion must handle three cases: if the node is a leaf, it is removed directly; if it has one child, it is replaced by that child; if it has two children, it is replaced either by the minimum value from its right subtree or the maximum value from its left subtree. The minimum value in a BST is found by following left pointers from the root, and the maximum by following right pointers. The sum of all values is computed through a traversal of the tree, typically using recursion.

