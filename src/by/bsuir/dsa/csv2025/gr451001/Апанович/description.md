# Name

Number of Provinces

# Task

Number of Provinces

There are `n` cities. Some of them are connected, while some are not. If city `a` is connected directly with city `b`, and city `b` is connected directly with city `c`, then city `a` is connected indirectly with city `c`.

A **province** is a group of directly or indirectly connected cities and no other cities outside of the group.

You are given an `n x n` matrix `isConnected` where `isConnected[i][j] = 1` if the `ith` city and the `jth` city are directly connected, and `isConnected[i][j] = 0` otherwise.

Return the total number of provinces.

# Theory

## Finding Number of Provinces (Connected Components)

**Graph** - structure consisting of vertices (cities) and edges (connections between them). In this problem:
- **n vertices** - cities
- **Adjacency matrix** - `isConnected[i][j] = 1` means direct connection

**Connected component** (province) - set of cities where:
- Any two cities are connected directly or indirectly
- No connections to cities outside the set

## Main Algorithms

### 1. Depth-First Search (DFS)
Recursively traverse all connected cities from current one

### 2. Breadth-First Search (BFS)
Use queue to traverse level by level

### 3. DSU (Union-Find)
Merge connected cities into sets

**Complexity:** O(n²) time, O(n) space

