# Name

Minimum Spanning Tree (Kruskal's algorithm)

# Task

## Minimum Spanning Tree (Kruskal's algorithm) #

# Theory

Theory: Minimum Spanning Tree (MST), Kruskal’s Algorithm and DSU

## 1. Minimum Spanning Tree (MST)

Given a connected undirected graph with `n` vertices and `m` edges,  
where each edge has a weight.

A **Minimum Spanning Tree (MST)** is a set of `n−1` edges that:

- connects all vertices (the graph becomes connected),
- contains no cycles,
- has the minimum possible total weight.

MST has real-world applications: optimal network design (fiber-optic lines, roads, electrical grids, communication networks).

---

## 2. Kruskal’s Algorithm

Kruskal’s algorithm is one of the most popular ways to construct an MST.

Main idea:

1. **Sort all edges by weight in ascending order.**
2. **Iterate from the lightest edges to the heaviest.**
3. Add an edge to the MST if it **does not create a cycle**.
4. To quickly check for cycles, a **DSU structure** is used.

If after processing all edges the number of components is greater than 1,  
then the graph is disconnected → MST does not exist.

### Time complexity:
- sorting edges: `O(m log m)`
- DSU operations: almost `O(1)` amortized  
  Total: **O(m log m)**

---

## 3. DSU (Disjoint Set Union)

DSU stores a partition of vertices into disjoint sets (components).

It supports two operations:

### ● `find(x)`
Returns the representative (root) of the component containing `x`.  
Uses **path compression** to speed up further queries.

### ● `union(x, y)`
Merges the components containing `x` and `y`.  
Uses **union by rank**, attaching smaller-height tree under the higher one.

### DSU in Kruskal’s Algorithm

Before adding an edge `(u, v)`:
- if `find(u) != find(v)` → the edge **can** be added without forming a cycle
- otherwise → adding the edge would create a **cycle**, so it is skipped

---

## 4. Detecting a Disconnected Graph

If, after processing all edges, more than one component remains,  
the MST cannot be built.

In this task, the output must be:

GRAPH IS NOT CONNECTED


---

## 5. Important Implementation Details

- The algorithm works even with negative edge weights.
- For `n = 0` or `n = 1`, the MST weight is `0`.
- The graph may be fully connected, partially connected, or disconnected.
- Kruskal’s algorithm is especially efficient when the number of edges is large  
  (or when edges are already sorted).

---

## 6. Summary

Kruskal’s algorithm combined with DSU is a fast and reliable method  
for constructing a Minimum Spanning Tree, widely used in real optimization problems.

