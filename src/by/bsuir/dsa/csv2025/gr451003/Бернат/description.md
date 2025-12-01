# Name

Eulerian Cycle in an Edge List

# Task

Eulerian Cycle in an Edge List

## Task

A connected undirected graph without loops is given as a sequence of edges.  
Each edge is defined by a pair of integers `u-v`, where `u` and `v` are vertex indices (positive integers).  
Edges are separated by spaces.

For the given graph:

1. Check whether the graph has an Eulerian cycle.
2. If an Eulerian cycle exists, construct **one** Eulerian cycle using a recursive “edge-removal” algorithm.
3. If an Eulerian cycle does not exist, output a message indicating its absence.

### Input format

- The input line contains a non-empty sequence of edges of the form  
  `u1-v1 u2-v2 ... uk-vk`  
  where `ui` and `vi` are positive integers, `1 ≤ ui, vi ≤ 10^5`.  
- The graph is guaranteed to be connected (considering only vertices that appear in the input).
- The graph has **no loops** (no edges of the form `x-x`), but multiple edges between two vertices are allowed.

### Output format

1. If an Eulerian cycle **exists**:
   - Print `YES` in the first line.
   - In the second line print the sequence of vertices of one Eulerian cycle in traversal order, separated by a single space.  
     The cycle must start and end at the same vertex.
2. If an Eulerian cycle **does not exist**:
   - Print a single line `NO`.

> **Remarks:**
> - The degree of a vertex is the number of edges incident to it (each parallel edge counts separately).
> - By the theorem, an Eulerian cycle exists if and only if all vertex degrees are even.
> - To construct the cycle it is recommended to:
>   - store adjacency lists (e.g., as stacks/lists of edge indices),
>   - mark edges as “used/deleted,”
>   - perform a recursive traversal.
> - Print vertices in the order they are added to the final cycle (usually on recursion unwind).

---

## Example 1

- Input:  
  `1-2 2-3 3-1`

- Expected output:  
  `YES 1 2 3 1`

(Other starting points are allowed, e.g.: `2 3 1 2`.)

---

## Example 2

- Input:  
  `1-2 2-3 3-4`

- Expected output:  
  `NO`

# Theory

Theoretical Info

## Eulerian Cycle in an Undirected Graph

### Definitions

A **graph** is a set of vertices and edges, where each edge connects a pair of vertices.

The **degree of a vertex** is the number of edges incident to that vertex.  
Parallel (multiple) edges are counted separately. In this task, loops (edges of the form `v-v`) are not allowed.

An **Eulerian path** is a path that uses every edge of the graph **exactly once**.

An **Eulerian cycle** is an Eulerian path that starts and ends at the same vertex.

In this task, we consider **connected undirected graphs without loops**, but multiple edges between two vertices are allowed.

### Condition for Existence of an Eulerian Cycle

Theorem (for an undirected graph):

> An Eulerian cycle exists **if and only if**:
> 1. The graph is connected (when considering only the vertices appearing in the input),
> 2. The degrees of **all** vertices are even.

In our setting, connectivity is guaranteed in advance, so it is enough to check **the parity of all vertex degrees**.

### Construction Algorithm (Hierholzer’s Algorithm)

To construct an Eulerian cycle, a recursive algorithm based on sequential “removal” of edges is often used:

1. Choose any vertex from which at least one edge originates (in this task, any vertex appearing in the input can be taken).
2. While there are unused edges from the current vertex `v`:
   - choose any such edge `v–u`,
   - mark it as used (or remove it from the adjacency lists),
   - recursively move to vertex `u`.
3. When there are no unused edges left from vertex `v`, add `v` to the result (usually at the end of the list).

If all vertex degrees are even and all edges are processed, the resulting sequence of vertices (read in reverse order of insertion) will be an Eulerian cycle and will include each edge exactly once.

