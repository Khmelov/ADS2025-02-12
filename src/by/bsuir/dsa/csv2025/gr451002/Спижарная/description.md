# Name

Dijkstra's algorithm

# Task

Dijkstra's Algorithm
Given a graph whose vertices represent locations on a map, and edges represent the distance between them.
Initially, you are at vertex 0 of the graph. Find the shortest routes from the starting point to all vertices using Dijkstra's algorithm.

The graph is specified in a text file(console) in the format:

* number of graph vertices

* number of adjacent vertices for vertex 0

* adjacent vertex; weight of the edge between the adjacent vertex and vertex 0

* and so on for other vertices

```
Example:
    5
    2
    0 2
    4 7
    1
    3 6
    ...
    
    g[0] = { {vertex: 0, edge: 2}, {vertex: 4, edge: 7 } }
    g[1] = { {vertex: 3, edge: 6} }
    ...
    g[4]
```

You need to implement the `alg()` method of the `Main` class, which takes an `InputStream` with the graph as an argument, and returns an array of strings in the form:  
`res[i] = "path sum: { shortest route distance } path: { vertex 0 - ... - vertex i+1}"`  
Routes to all vertices except vertex 0 ("path sum: 0 path 0" is **NOT** needed)

```
    Example:
       String[] res = {
          "path sum: 5 path: 0 - 1",
          "path sum: 11 path: 0 - 3 - 2",
          "path sum: 8 path: 0 - 3" };           *for a graph of 4 vertices
```

Vertices are numbered in the order they are written in the file, starting from 0.  
Write the results to the output array in the same order.

# Theory

Dijkstra's Algorithm

## Purpose
Finding the shortest paths from the starting vertex to all others in a weighted graph with **non-negative weights**.

## Basic Concepts
- **Graph** — a set of vertices and edges between them
- **The shortest route in a graph** — is the path between two given vertices that has the smallest total length (weight)

## Operating Principle

### Initialization
1. Assign the starting vertex a distance of 0
2. Assign all other vertices a distance of ∞
3. Place all vertices in the unvisited set

### Main Loop
While there are unvisited vertices:
1. Select the vertex with the **minimum distance** from the unvisited set
2. For each neighbor of the selected vertex:
   - Calculate new distance = distance to current vertex + edge weight
   - If the new distance is less than the current one - update it
3. Mark the current vertex as visited

*The only way to speed up the algorithm is by finding the minimum faster. For this, structures such as a heap or tree are used*

## Complexity
- **Array-based implementation**: O(n²)
- **Binary tree implementation**: O(mlog n)
- **Heap-based implementation**: O(nlog n + m)

    where n is the number of vertices, m is the number of edges

