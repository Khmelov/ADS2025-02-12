# Name

Escape from the guarded maze

# Task

You have found yourself in a maze guarded by guard dogs. The dogs are very alert and can smell you if you get too close. Your goal is to find the shortest way out of the maze without being detected.

### Important rules:

- You can only move up, down, left, and right
- You are not allowed to enter the cells directly adjacent to the dogs (4 directions: above, below, left, and right of the dog)
- The walls are impassable
- The starting position and the exit are always safe (not near the dog)

### Input data:

The maze map is represented as a two-dimensional array of N x M cells.
The first line contains two natural numbers N and M, which are the size of the array.
Next, N lines are entered, each containing M characters.

The characters on the map are:
- 'S' — the starting position
- 'E' — the exit from the maze
- '.' — a free cell
- '#' — wall (impassable obstacle)
- 'D' — watchdog

### Output data:
The output line contains a single number: the length of the shortest path from 'S' to 'E'. If the path is impossible, output -1.

## Example

### Sample Input:
3 4
S.D.
...E
#...

### Sample Output:
6


# Theory

Breadth-first search (англ. breadth-first search) is one of the main algorithms on graphs, which allows to find all shortest paths from a given vertex and solve many other problems.

Breadth-first search is also called a traversal - just like depth-first search and all other traversals, it visits all vertices of the graph once, only in a different order: by increasing the distance to the initial vertex.

### Description of the algorithm

The algorithm takes as input an unweighted graph and the number of the starting vertex s. The graph can be either directed or undirected — it is not important for the algorithm.

The main idea of the algorithm can be understood as a process of "setting fire" to the graph: at the zero step we set fire to vertex s, and at each next step the fire from each already burning vertex is transferred to all its neighbors, eventually setting the whole graph on fire.

If we simulate this process, then for each iteration of the algorithm, the "ring of fire" will expand in width by one. The step number at which vertex v starts to burn is exactly equal to the length of its minimum path from vertex s.

We can simulate this as follows. We will create a queue where we will place the burning vertices, and we will also have a boolean array where we will mark whether each vertex is burning or not, or in other words, whether it has already been visited. Initially, only the vertex s is placed in the queue, which is immediately marked as burning.

Then, the algorithm consists of the following loop: while the queue is not empty, remove one vertex v from the head of the queue, traverse all the edges outgoing from this vertex, and if any of the adjacent vertices u are not yet burning, set them on fire and add them to the end of the queue.

As a result, when the queue is empty, we will traverse all the vertices reachable from s once, using the shortest path to each vertex. The lengths of the shortest paths can be calculated by creating a separate array d for each vertex and updating it according to the rule du=dv+1. Additionally, we can store additional information for reconstructing the paths by creating an "ancestors" array that stores the vertex number from which each vertex was reached.