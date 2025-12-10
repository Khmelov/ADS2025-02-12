# Name

Route optimization system

# Task

## Route optimization system

## Application Scenario
A logistics company serves a region with thousands of delivery points and hundreds of warehouses.
Changing traffic conditions, movement restrictions, and varying vehicle speeds require not just a one-time route calculation,
but a **reliable system** capable of quickly providing real-time answers to requests for the shortest delivery time and handling large input sizes.

## Problem Statement
The task is to develop a highly efficient program that, using Dijkstra's algorithm, will find the shortest paths in a large sparse graph. This approach allows for quickly determining the minimum delivery time between any two points, which is critically important for a logistics company operating in conditions of dynamically changing traffic and large volumes of data.

Requirements:
- Processing a graph with `n ≤ 200000` vertices and `m ≤ 200000` edges (sparse graph).
- Correct handling of large weights (each weight — 0 ≤ w ≤ 10^12), total distances may exceed 32-bit.
- Support for an early stopping option (in a real system, the shortest path to a specific target is often needed, not to all vertices).
- Support for multi-threaded input reading and optimal memory usage — the code must be as efficient as possible.

## Formal Condition
Input:
1. `n m` — number of vertices and edges.
2. `m` lines: `u v w` — an edge between `u` and `v` with weight `w` (undirected graph, travel is possible in both directions).
3. `q` — number of queries.
4. `q` lines: `s t` — a query for the shortest path from `s` to `t`. If `t = -1`, an array of distances from `s` to all vertices should be output (in one line separated by spaces). If `t != -1`, only the distance `d(s,t)` needs to be output, or `-1` if unreachable.

Constraints:
- `1 ≤ n ≤ 200000`, `0 ≤ m ≤ 200000`.
- `0 ≤ w ≤ 10^12`.
- `1 ≤ q ≤ 200000`.

Output format:
- For each query, output the answer on a separate line. If the query requires the full array of distances, output `n` numbers (separated by spaces), unconnected vertices correspond to `-1`.

## Example
**Input:**
```
5 6
1 2 4
1 3 2
2 3 1
2 4 5
3 4 8
4 5 3
3
1 -1
1 5
5 1
```
**Output:**
```
0 3 2 8 11
11
-1
```

**Explanation:**
- The first query `1 -1` is the complete distance table from vertex 1.
- The second `1 5` is the distance from 1 to 5, which is 11.
- The third `5 1` - there is no path from 5 to 1 (if the graph were directed; in an undirected example, this query would be reachable), in this example, a cascade of edges could make it unreachable - this will be taken into account in the tests.

# Theory

Theory: Dijkstra's Algorithm

## Main Idea
Dijkstra's algorithm finds the shortest paths in a graph with **non-negative edge weights**. It sequentially selects the vertex with the minimum current distance and attempts to improve the distances to its neighbors. For large sparse graphs, a **priority queue** is used, resulting in a time complexity of `O((n + m) log n)`.

### 1. Data Types
- Weights up to `10^12`, so distances must be stored in `long`.
- Check for overflow during addition:
`if (dist[u] != INF && dist[u] + w < dist[v])`.

### 2. Priority Queue (Lazy Updates)
- We don't decrease keys — we simply add a new pair `(d, v)` to the queue.
- When extracting, ignore entries if `d != dist[v]`. 
This is standard for large graphs.

### 3. Early Termination
- If only the path `s → t` is needed, the algorithm can be terminated when `t` is extracted from the queue. 
This significantly speeds up execution for frequent queries.

### 4. Working with a Sparse Graph
- Store the graph using adjacency lists: an array of edge lists.
- For an undirected graph, add the edge in both directions.

### 5. Multigraphs, Zero Weights, Disconnectedness
- All parallel edges can be stored — the algorithm will automatically choose the best one.
- Unreachable vertices — the distance remains equal to `INF`.

### 6. Practical Optimizations
- Fast input: `BufferedReader`.
- Minimizing unnecessary objects: use custom edge structures.
- Don't store anything unnecessary — with `n, m = 200000`, memory is critical.

