# Name

Shortest Path with a Limited Number of Edges

# Task

Shortest Path with a Limited Number of Edges.
You are given a directed weighted graph with n vertices and m edges.
Each edge has a non-negative weight.

You are also given an integer K — the maximum allowed number of edges that may be used in the path.

Your task is to find the shortest path from vertex start to vertex end using no more than K edges.
If no such path exists, output -1.

Input format:

Numbers n and m

m lines: u v w

Line: start end K

Output format:
A single number — the shortest path length, or -1 if no path exists.

# Theory

The shortest path problem aims to find the minimum total cost route between two vertices in a graph.
For graphs with non-negative edge weights, Dijkstra’s algorithm is traditionally used.

The main idea is to maintain, for each vertex, a current best-known estimate of its distance:

d[v]=minimum known distance from start to v

At each step, the algorithm picks the vertex with the smallest 

d[v] among the unvisited ones and attempts to improve (“relax”) the distances of its neighbors.

Relaxation is based on the following comparison:

d[v]=min(d[v], d[u]+w(u→v))

This principle is common to many shortest-path algorithms.

However, when a limit on the number of edges 
K is introduced, classical Dijkstra cannot be applied directly, because it does not track how many edges were used.

To handle this constraint, it is useful to consider paths by the number of edges they contain.
For each vertex, we can keep the best distance achievable using exactly 
k edges:

dist[k][v]=minimum cost to reach v using exactly k edges

The cost for the next step can be expressed as:

dist[k][v]=min(dist[k][v], dist[k−1][u]+w(u→v))

This formula describes layer-by-layer exploration of the graph, where each layer corresponds to paths of length 
1,2,…,K.
The edge limit 
K prevents overly long paths from being considered even if they are cheap.

