# Name

Shortest distances in an unweighted graph (BFS traversal)

# Task

Shortest distances in an unweighted graph (BFS traversal)

You are given an undirected unweighted graph with \(n\) vertices and \(m\) edges.  
The vertices are numbered from \(1\) to \(n\).

Given a starting vertex \(s\), your task is to find the minimum number of edges  
(i.e. the shortest distance) from \(s\) to every other vertex of the graph.  
If a vertex is not reachable from \(s\), you must output \(-1\) for it.

There are no self-loops and no multiple edges in the graph.

## Input format

The first line contains three integers  
\(n, m, s\) — the number of vertices, the number of edges, and the index of the starting vertex  
\((1 \le n \le 2 \cdot 10^5,\ 0 \le m \le 2 \cdot 10^5,\ 1 \le s \le n)\).

Each of the next \(m\) lines contains two integers \(u_i, v_i\)  
\((1 \le u_i, v_i \le n,\ u_i \ne v_i)\) — the endpoints of an edge.

The graph is undirected, so each edge connects vertices \(u_i\) and \(v_i\) in both directions.

## Output format

Print \(n\) integers — the distances from vertex \(s\) to vertices \(1, 2, \dots, n\).  
If a vertex is not reachable from \(s\), print \(-1\) for it.

All numbers must be printed in a single line, separated by spaces.

## Example

**Input:**
4 3 1
1 2
2 3
3 4

**Output:**
0 1 2 3

In this example, the shortest path from vertex \(1\) to vertex \(4\) goes through vertices \(2\) and \(3\) and has length \(3\), so the distance is \(3\).

# Theory

A graph \(G = (V, E)\) consists of a set of vertices \(V\) and a set of edges \(E\).  
Graph traversals are algorithms that visit vertices of a graph starting from a given source.

Two basic traversals:

1. **Depth-First Search (DFS)** — goes as deep as possible along one path, then backtracks.
2. **Breadth-First Search (BFS)** — first visits all vertices at distance \(1\), then distance \(2\), and so on.

For finding shortest paths in an *unweighted* graph we use BFS.

### BFS idea

1. Put the starting vertex \(s\) into a queue.
2. Set the distance to \(s\) as \(0\), and all other distances to \(-1\) (not visited yet).
3. While the queue is not empty:
   - take a vertex \(v\) from the front of the queue;
   - for each neighbor \(u\) of \(v\):
     - if \(u\) is not visited yet (\(\text{dist}[u] = -1\)):
       - set \(\text{dist}[u] = \text{dist}[v] + 1\);
       - push \(u\) to the back of the queue.

Because all edges have equal weight (each edge costs \(1\)), BFS guarantees that \(\text{dist}[u]\) equals the minimum number of edges in any path from \(s\) to \(u\).

Time complexity: \(O(n + m)\),  
where \(n = |V|\), \(m = |E|\).  
Memory usage: \(O(n + m)\) with adjacency list representation.

