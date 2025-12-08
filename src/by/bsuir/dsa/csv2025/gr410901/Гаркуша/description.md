# Name

Finding the Shortest Path. Dijkstra's Algorithm

# Task

## Given:
- The starting and ending vertices of the path
- A list of edges of the graph (in the format source vertex - destination vertex weight)

## Need to:
- Using Dijkstra's algorithm, find the shortest path from the starting vertex to the ending vertex
- Output this path and its weight

### Simple input:
```
A D
A - B 4
A - C 1
B - D 1
C - B 2
C - D 5
```

### Simple output:
```
A - C - B - D 4
```

# Theory

Pathfinding Algorithms

Pathfinding algorithms use the structure of a graph and the weights of its edges
to find the most optimal path between two vertices.
"Optimality" can be defined differently depending on the problem:
It can be the shortest path, the cheapest path, the path with the fewest obstacles, and so on.

## Dijkstra's Algorithm

Dijkstra's Algorithm is a classic pathfinding algorithm,
developed by the Dutch scientist Edsger Dijkstra in 1959.
This algorithm is used to find the shortest path in a weighted graph from one vertex
(denoted as the starting vertex) to all other vertices.

### How it works

Dijkstra's Algorithm begins with a starting vertex and works from there.
It operates as a greedy algorithm, meaning that at each step,
it seeks to minimize the current total cost of the path.

First, two sets are initialized:

- A set containing the vertices already processed (initially empty).
- A set containing all remaining vertices in the graph (initially contains all vertices in the graph).

Each vertex in the graph is also assigned a weight,
which represents the minimum known path cost from the starting vertex to the given vertex.
For the starting vertex, this weight is 0; for all other vertices, it is infinity.

At each step, the algorithm selects a vertex from the unvisited set with the lowest weight,
moves this vertex to the set of visited vertices, and updates the weights of all neighbors of the selected vertex.
The weight of a neighbor is updated if the selected vertex provides a lower-cost path to this neighbor.

The process continues until all vertices have been visited
or until a path to the destination vertex (if specified) is found.

### Where is it used?

This algorithm is universal, so it can be used in a variety of fields.

- **In navigation systems and cartography**, Dijkstra's algorithm can help plan routes
for pedestrians or cars, avoiding traffic jams and choosing optimal roads.

- **In robotics**, this algorithm is used to plan the movements of robots,
so they can navigate through space using the shortest paths and avoiding obstacles.

- **In booking systems**, it helps find the fastest and cheapest tickets, taking into account possible transfers.

- **In computer networks**, Dijkstra's algorithm is used
to determine the optimal route for transmitting data between network nodes,
minimizing latency and increasing transmission efficiency.

