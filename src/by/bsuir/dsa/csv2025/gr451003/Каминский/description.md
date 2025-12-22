# Name

Minimum Spanning Trees in Connected Components

# Task

Minimum Spanning Trees in Connected Components

## Description
In an undirected graph, a connected component is a set of vertices where each vertex is reachable from any other vertex in the same set.  
If a graph consists of multiple connected components, a minimum spanning tree can be built only inside each component individually.

A Minimum Spanning Tree (MST) is a spanning tree whose total edge weight is minimal among all spanning trees of that component.

In this assignment, you must:
1. Find all connected components of the given undirected graph.
2. Build a minimum spanning tree for each connected component.
3. Compute and output the total weight of all minimum spanning trees.

---

## Input Format
The first line contains two integers:
- `n` — number of vertices,
- `m` — number of edges.

Each of the next `m` lines contains three integers:
`u v w` — an edge between vertices `u` and `v` with weight `w`.

The graph is:
- undirected,
- all edge weights are positive.

---

## Requirements
### 1. Finding connected components
Perform a traversal (DFS or BFS) to assign a component number to each vertex.

The result of this stage:
- the number of connected components,
- the component index for each vertex.

### 2. Minimum spanning tree construction
For each connected component:
- extract all edges belonging to it,
- build its minimum spanning tree (Prim’s or Kruskal’s algorithm),
- compute the MST total weight.

The final answer is the sum of all MST weights in the graph.

---

## Constraints
- `1 ≤ n ≤ 10^5`
- `0 ≤ m ≤ 2 * 10^5`
- `1 ≤ w ≤ 10^9`

Expected complexity:  
`O(n + m) + O(m log m)`.

---

## Example
**Input:**

6 4
1 2 5
2 3 7
4 5 2
5 6 3

**Output:**
2
17

# Theory

Minimum Spanning Tree in Connected Component

## Definitions

A **spanning tree** of an undirected graph is a subgraph that contains all vertices and contains no cycles. If the graph is disconnected, a spanning tree exists only for each individual connected component.

A **minimum spanning tree (MST)** is a spanning tree with the minimum possible total edge weight.

## Problem Statement

Given an undirected weighted graph $G$ with $n$ vertices and $m$ edges, we need to:

1. Find all connected components of the graph
2. Construct a minimum spanning tree for each component

## Solution Algorithm

### 1. Finding Connected Components

Connected components are found using Depth-First Search (DFS):

```java
final int MAXN = 100000;
int[] comp = new int[MAXN];
List<Integer>[] g;

void dfs(int v, int num) {
    comp[v] = num;
    for (int u : g[v])
        if (comp[u] == 0)
            dfs(u, num);
}
```

Launch DFS from each unvisited vertex:

```java
int num = 0;
for (int v = 0; v < n; v++)
    if (comp[v] == 0)
        dfs(v, ++num);
```

After completion:
- `num` — number of connected components
- `comp[v]` — component number of vertex $v$

**Time complexity for finding connected components:** $O(n + m)$, since each vertex and edge is processed exactly once.

### 2. Constructing Minimum Spanning Tree

After identifying connected components, we can independently construct a minimum spanning tree for each component.

#### Main algorithms:

1. **Kruskal's Algorithm** — sorts edges by weight and adds them if they don't create a cycle. Uses DSU (Union-Find) for cycle detection.

2. **Prim's Algorithm** — starts from an arbitrary vertex in the component and gradually adds the cheapest edge connecting to a new vertex.

#### MST construction principle:

From all possible spanning trees, select the one with minimum total weight:

$$
W = \min \sum w_i
$$

## Time Complexity

- **Kruskal's Algorithm:** $O(m \log m)$ (due to edge sorting)
- **Prim's Algorithm with binary heap:** $O(m \log n)$

Since the graph may be disconnected, the minimum spanning tree is built separately for each connected component. The total weight of all MSTs is calculated as:

$$
W_{total} = \sum_{i=1}^{k} W_i
$$

where $k$ is the number of connected components.
## Overall Time Complexity

$$
O(n + m) + \sum O(m_i \log n_i)
$$


