# Name

Search in Binary Search Tree

# Task

Search in Binary Search Tree
Implement a function to search for an element in a binary search tree. The function should return true if the element is found, and false otherwise.
Input:
Pointer to the root of the tree
Target value to search for
Output:
Boolean value: true if element found, false if not

# Theory

Binary Search Trees
Binary Search Tree (BST) is a data structure with the following properties:
Each node has at most two children
All nodes have keys with defined comparison operation
All nodes in the left subtree of node $v$ have keys less than or equal to $v$'s key
All nodes in the right subtree of node $v$ have keys greater than $v$'s key
Both subtrees are binary search trees
Search Algorithm:
If current node is empty — element not found
If current node's key equals target — element found
If target key is less than current node's key — search left subtree
If target key is greater than current node's key — search right subtree
Complexity: O(h), where h is the height of the tree

