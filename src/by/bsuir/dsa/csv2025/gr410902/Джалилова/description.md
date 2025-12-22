# Name

Server Connectivity

# Task

Server Connectivity

In a computing system, there are **n servers**, each assigned a unique number from 1 to n.

Two servers are considered **directly connected** if there exists a number **k** greater than a given **threshold**, such that both server numbers are divisible by **k**. In this case, the servers can exchange data directly.

In addition to direct connections, **indirect connections** are allowed: if server A is connected to server B, and server B is connected to server C, then A and C are also considered connected, even if there is no direct number **k** between them.

## Input

- The first line contains two integers **n** and **threshold** — the number of servers and the minimum divisor that can create a connection.
- The second line contains an integer **q** — the number of queries.
- The following **q** lines each contain two integers **ai** and **bi** — the server numbers for which you need to determine whether they are in the same connected group.

## Output

For each query, print **true** if the servers can exchange data (either directly or through a chain of other servers), and **false** otherwise. Each answer should be printed on a separate line.

# Theory

Implement Disjoint Set in Java

## Overview

When dealing with problems involving connected components or partitioning elements into distinct groups, efficiently tracking these relationships is crucial. The Disjoint Set Union (DSU) data structure, also known as the Union-Find data structure, offers an elegant solution.

## Disjoint Set Union (DSU) Basics

Disjoint Set Union is a data structure designed to manage a collection of non-overlapping sets. Its power lies in two core operations:
- **find**: determining which set an element belongs to
- **union**: merging two distinct sets

Consider tracking connected components in a graph. Initially, each vertex is in its own set. When processing an edge between vertex u and vertex v, you'd use `union(u, v)` to merge their respective sets, effectively indicating they are now connected.

## Complete Java Implementation

```java
public class DisjointSet {
    private int[] parent;
    private int[] rank;
    private int count; // number of disjoint sets

    public DisjointSet(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Number of elements must be positive");
        }
        
        parent = new int[n];
        rank = new int[n];
        count = n;
        
        // Initialize each element as its own parent
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0; // Initial rank is 0
        }
    }

    /**
     * Find operation with path compression
     * Time Complexity: O(α(n)) - nearly constant
     */
    public int find(int i) {
        if (i < 0 || i >= parent.length) {
            throw new IllegalArgumentException("Invalid element index: " + i);
        }
        
        // Path compression: make every node point directly to root
        if (parent[i] != i) {
            parent[i] = find(parent[i]);
        }
        return parent[i];
    }

    /**
     * Union operation with union by rank
     * Time Complexity: O(α(n)) - nearly constant
     */
    public void union(int i, int j) {
        if (i < 0 || i >= parent.length || j < 0 || j >= parent.length) {
            throw new IllegalArgumentException("Invalid element indices");
        }
        
        int rootI = find(i);
        int rootJ = find(j);
        
        // If already in same set, no need to union
        if (rootI == rootJ) {
            return;
        }
        
        // Union by rank: attach smaller rank tree under root of higher rank tree
        if (rank[rootI] < rank[rootJ]) {
            parent[rootI] = rootJ;
        } else if (rank[rootI] > rank[rootJ]) {
            parent[rootJ] = rootI;
        } else {
            // If ranks are same, make one as root and increment its rank
            parent[rootJ] = rootI;
            rank[rootI]++;
        }
        
        count--; // Decrease number of disjoint sets
    }

    /**
     * Check if two elements are connected
     */
    public boolean isConnected(int i, int j) {
        return find(i) == find(j);
    }

    /**
     * Get the number of disjoint sets
     */
    public int getCount() {
        return count;
    }

    /**
     * Get the size of each component
     */
    public int getComponentSize(int i) {
        int root = find(i);
        int size = 0;
        for (int j = 0; j < parent.length; j++) {
            if (find(j) == root) {
                size++;
            }
        }
        return size;
    }
}
```

