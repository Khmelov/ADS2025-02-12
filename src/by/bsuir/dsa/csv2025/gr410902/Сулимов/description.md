# Name

Social Network Analysis

# Task

Social Network Analysis

## Task Description
Develop a program for analyzing a social network represented as a graph. The program should determine the size of the largest community where all members are directly connected to each other (everyone with everyone).

The graph is defined by an adjacency matrix where:
- 1 indicates a connection between users
- 0 indicates no connection

You need to find the size of the maximum clique - the largest group of users where all are pairwise connected.

```java
Input : "4 0 1 1 0 1 0 1 1 1 1 0 0 0 1 0 0"
Output : "3"

# Theory

## Theoretical Background
A **clique** in graph theory is a subset of vertices of a graph such that every two distinct vertices in the clique are adjacent.

A **maximal clique** is a clique that cannot be extended by adding an adjacent vertex.

An **adjacency matrix** is a square matrix used to represent a finite graph, where the element a[i][j] indicates whether pairs of vertices are adjacent or not.

For undirected graphs, the adjacency matrix is symmetric about the main diagonal.

**Depth-first search** (DFS) or Eulerian traversal is a recursive algorithm for traversing a rooted tree or graph, starting at the root vertex (in the case of a graph, an arbitrary vertex can be chosen) and recursively traversing the entire graph, visiting each vertex exactly once.

