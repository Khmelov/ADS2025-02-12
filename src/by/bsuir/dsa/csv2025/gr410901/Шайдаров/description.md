# Name

Fast delivery

# Task

Fast Delivery

A courier service needs to deliver an order from a restaurant to a customer. The city is represented as a graph, where intersections are vertices, and roads between them are edges with weights (time in minutes).

You need to find the minimum delivery time from the restaurant to the customer.

## Input

First line: \[n\] and \[m\] - number of intersections and roads (\[1 \leq n \leq 1000\], \[0 \leq m \leq 10000\])

Next \[m\] lines: \[u\] \[v\] \[w\] - road from intersection \[u\] to \[v\] with time \[w\] minutes (\[1 \leq u, v \leq n\], \[1 \leq w \leq 100\])

Last line: \[s\] \[t\] - intersection numbers of restaurant and customer

## Output

Minimum delivery time in minutes or -1 if delivery is impossible

## Example

**Input:**
4 5
1 2 10
1 3 20
2 4 50
3 4 20
2 3 10
1 4

**Output:**
40


# Theory

Dijkstra's Algorithm for Finding Shortest Paths

Dijkstra's algorithm finds the shortest paths from one vertex to all other vertices in a weighted graph with non-negative edge weights.

## Main Idea

1. Initialize distances: \[d[start] = 0\], others \[d[v] = \infty\]
2. Use a priority queue to select the vertex with minimum distance
3. Relax edges: if \[d[u] + w(u,v) < d[v]\], then \[d[v] = d[u] + w(u,v)\]

## Complexity

\[O((V + E) \log V)\] with binary heap.

## Applications

The algorithm is used in navigation systems, network routing, and logistics planning.



