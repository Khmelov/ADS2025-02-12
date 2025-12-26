# Name

Breadth-first traversal of a graph to find the shortest path

# Task

Breadth-First Search

Given an unweighted directed graph as an adjacency list and a start vertex. Your task is to perform breadth-first search (BFS) and output vertices in the order of visitation.

### Input Format
- First line: `n m s` (number of vertices, edges and start vertex)
- Next `m` lines: `u v` (edge from vertex `u` to vertex `v`)

### Output Format
- Vertices in BFS traversal order starting from `s`, separated by spaces

### Constraints
- 1 ≤ n ≤ 10^5
- 0 ≤ m ≤ 2×10^5
- Time complexity: O(n + m)

# Theory

## BFS Algorithm

1. Create queue and visited array
2. Enqueue start vertex and mark as visited
3. While queue is not empty:
   - Dequeue vertex
   - Process it
   - Enqueue all unvisited neighbors

**Complexity:** O(V + E)
**Memory:** O(V)

