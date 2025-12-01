# Name

Kth element in BST

# Task

Imagine a bookshelf with books strictly alphabetically by the author's last name.

When a new book arrives, the librarian inserts it exactly in the place where it should stand in alphabetical order - not at the end and not at the beginning.

It turns out a structure where:
1. On the left there are "smaller" books (in alphabetical order),
2. On the right are "more" books.

If the reader needs, say, the 5th book in order, the librarian does not count all the books in a row.

It goes like this:
1. He looks at some "central" book.
2. Understands - if you need it earlier in alphabetical order, it goes to the left, if later - to the right.
3. And so until he finds the right position.

This bookshelf is an analogue of the Binary Search Tree (BST), and the task of finding the k-th book is to find the k-th smallest element in BST.

K will always be a valid index, which means 1 ≤ k ≤ (Number of nodes in the tree).

The input consists of 2 lines:
1. Node values in the tree
2. Index k

Examples
Input:
2 2 4 1 3
1
Output: 1

Explanation: (1) 2 3 4

# Theory

Binary Search Tree (BST) is a data structure that stores elements in the form of a tree so that you can quickly search, insert and sort.

# The main property of BST

## For each tree node, the following is performed:
1. all values of the left subtree are less than the value of the node,
2. all values of the right subtree are greater than the value of the node.

This allows you to search for elements in time of order O(log n) in a balanced tree.

# Building a tree

## When inserting each number:
1. if the number is less than the current node → go to the left;
2. if more → to the right;
3. when we rest on an empty position, we create a new node there.

This is how the tree is built according to the input data.

# Search for the k-th smallest element

## A very important property of BST:
If we traverse the tree in order of "left → root → right" (in-order traversal), we will get all the elements in ascending order.

That is, if we bypass the in-order tree, then:
1. First, let's collect all the left subtree
2. Then the current node
3. Then the right subtree

