# Name

Labyrinth with teleports

# Task

Task condition
A maze is given, represented by a rectangular grid measuring N by M. Each cell can be:

Passable (.)

Impassable (#)

Teleporter (any lowercase Latin letter from a to z)

The player starts in cell S and must get to cell E. In one turn, the player can move one square up, down, left or right if it is passable or teleported.

If a player enters a cell with a teleporter, he can instantly (on the same turn) move to any other cell with the same letter designation of the teleporter. Teleport activation is optional.

It is required to find the minimum number of moves required to achieve the goal. If it is impossible to achieve the goal, print -1.Task condition
A maze is given, represented by a rectangular grid measuring N by M. Each cell can be:

Passable (.)

Impassable (#)

Teleporter (any lowercase Latin letter from a to z)

The player starts in cell S and must get to cell E. In one turn, the player can move one square up, down, left or right if it is passable or teleported.

If a player enters a cell with a teleporter, he can instantly (on the same turn) move to any other cell with the same letter designation of the teleporter. Teleport activation is optional.

It is required to find the minimum number of moves required to achieve the goal. If it is impossible to achieve the goal, print -1.

# Theory

title: The maze with teleporters
weight: 3
authors:
Danila
Kisko created: 2025

The tasks of traversing graphs in the form of mazes are classical. However, a simple wall arrangement can make the task trivial. If you add teleporters to the maze that instantly move the player from one cell to another, then the task of finding the shortest path ceases to be a simple breadth-first detour and requires careful modification of the standard algorithm.

Decision
The idea is to modify the breadth-first search algorithm (BFS). The key point is the correct processing of teleports.

Graph modeling: Imagine each cell as a vertex of the graph.

Regular edges: There are edges from each passable cell to adjacent passable cells (edge weight = 1 turn).

Teleporter edges: When hitting teleporter cell X, we can add edges of weight 0 to all other cells with teleporter X. However, if you do this naively, the number of edges can become very large (up to $O(N^2\cdot M^2)$ in the worst case).

Optimization: To avoid a quadratic number of edges, we can process teleports "lazily". When the BFS first reaches any teleporter with the letter X, we can instantly "activate" all other X teleporters. That is, add all cells with teleport X to the BFS queue with the same distance, but only if we have not processed this type of teleport yet.

Algorithm:

Initialize the dist array of size N by M with the value -1 (not visited).

Let's create a queue q for BFS and put the starting vertex S with a distance of 0 in it.

Create a boolean array used_portal[26] to track the types of teleports that have already been activated.

While the queue is not empty:

Extract the current cell (x, y) with distance d.

If it is cell E, we return d.

If it is a teleporter with the letter ch and this type of teleporter is not activated yet:

We mark used_portal[ch] = true.

For all cells (tx, ty) with teleporter ch that have not yet been visited, add them to the queue with distance d (teleporter weight 0).

We iterate over all four neighbors (nx, ny). If a cell is passable or teleported, and we haven't visited it yet, add it to the queue with a distance of d + 1.

Theory: Traversing graphs with zero edges
The standard BFS algorithm finds shortest paths in an unweighted graph, where the weight of each edge is assumed to be 1. However, edges with a weight of 0 (teleporters) appear in this problem. To process such graphs, BFS on the 0-1 graph (0-1 BFS) is effectively used.

0-1 BFS
This is a modification of BFS designed for graphs whose edges have weights 0 or 1. The algorithm uses a two-way queue (deque) to ensure that shortest paths are found correctly.

The principle of operation:
A deck is used instead of the usual queue.
When processing an edge with a weight of 0, the vertex is added to the beginning of the deck.
When processing an edge with a weight of 1, the vertex is added to the end of the deck.

