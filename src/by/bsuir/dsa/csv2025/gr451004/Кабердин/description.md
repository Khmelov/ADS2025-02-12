# Name

Taxi Driver's Maze

# Task

Taxi Driver's Maze

A taxi driver is in a city with a rectangular layout, which can be represented as an N x M grid. Some intersections (grid cells) are blocked due to construction and are impassable. The driver knows their current location (startX, startY) and the destination (endX, endY).

# Theory

## Breadth-First Search (BFS) Algorithm

### Algorithm Idea
BFS traverses a graph layer by layer. It starts from the initial vertex, then visits all its immediate neighbors, then neighbors of those neighbors, and so on. It uses a queue for this purpose.

### Why BFS?
The taxi driver needs the path with minimum number of blocks. Each move has the same cost (1 block). BFS guarantees finding the shortest path in an unweighted graph.

### Algorithm for the Maze
1. Create queue and add start cell with distance 0
2. Create visited array to mark visited cells
3. While queue is not empty:
   - Dequeue current cell
   - If this is the end cell, return its distance
   - For each of four neighboring cells:
     * If cell is within bounds, free and not visited:
       - Mark as visited
       - Set distance = current distance + 1
       - Enqueue it
4. If target not found, return -1

### Complexity
Time complexity: O(N * M)

