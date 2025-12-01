# Name

Maze Escape

# Task

Maze Escape

Given an **N×M** maze represented as a matrix where each cell can be:

- `0` - free path
- `1` - wall
- `2` - starting position
- `3` - exit

Find the minimum number of steps from the start position to the exit. Return `-1` if no path exists.

### Input Format
The first line contains two integers N and M - the dimensions of the maze.
The next N lines contain M integers each, representing the maze grid.

### Output Format
One number:
- Minimum number of steps if path exists
- -1 if no path exists

### Constraints
1 ≤ N, M ≤ 1000
All input values are integers
Expected complexity: O(N×M)

# Theory

Breadth-First Search (BFS)

Breadth-First Search is one of the fundamental graph algorithms, used to find shortest paths in unweighted graphs and solve various other problems.

The algorithm visits graph vertices in "waves" — at each step, it processes all vertices at the same distance from the starting vertex.

## How It Works
1. Initialization: start with the source vertex, distance 0
2. Queue: use a queue to store vertices for processing
3. Traversal: while the queue is not empty:
   - Dequeue a vertex from the front
   - Add all its unvisited neighbors to the back of the queue
   - Update distances to neighbors

## Key Properties
- Guarantees finding the shortest path in unweighted graphs
- Time complexity: O(V + E), where V is the number of vertices, E is the number of edges
- Uses a queue to process vertices in the correct order

BFS is particularly effective for grid and maze problems, where the graph is implicitly represented through cell coordinates.

