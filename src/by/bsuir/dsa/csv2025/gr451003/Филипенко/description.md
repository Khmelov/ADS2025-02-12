# Name

Shortest Path in Graph

# Task

Given a directed weighted graph, find the shortest path from vertex `s` to vertex `t`.  
Input format:  
- First line: `n m` — number of vertices and edges.  
- Next `m` lines: `u v w` — edge from `u` to `v` with weight `w`.  
- Last line: `s t` — start and target vertices.  

Output format:  
- A single number — the length of the shortest path, or `-1` if no path exists.

# Theory

To find the shortest path in a graph with non-negative weights, Dijkstra's algorithm is commonly used.  
The idea: iteratively expand the set of vertices with known minimal distance, always picking the vertex with the smallest current label.

Formally:
- Initialize distances: `dist[s] = 0`, others `∞`.
- Use a priority queue to select the vertex with the smallest distance.
- For each neighbor, update distances:  
  dist[v] = min(dist[v], dist[u] + w)

