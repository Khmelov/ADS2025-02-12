# Name

Rescue in the deep labyrinth

# Task

Rescue in the deep labyrinth
## Problem description
There is a network of tunnels represented by a directed graph with n vertices and m edges. Vertices are numbered from 1 to n. Movement along tunnels is allowed only in the given direction. A courier starts at vertex s and must reach the target vertex t. The courier explores tunnels depth-first — going as far as possible, and when reaching a dead end, backtracks. If vertex t is reachable, output the path from s to t, otherwise output NO PATH. Also output the order in which vertices were visited.
## Input format
```
n m
u1 v1
u2 v2
...
um vm
s t
```
Each pair `ui vi` is a directed edge from `ui` to `vi`.
## Output format
```
path: p1 p2 ... pk
order: a1 a2 ... ar
```
Where `path` is the sequence of vertices from s to t, or NO PATH. `order` is the visitation order of all vertices reachable from s. Neighbors should be visited in ascending order of their numbers.
## Example
Input:
```
6 6
1 2
1 3
2 4
3 5
5 4
4 6
1 6
```
Output:
```
order 1 2 4 6 3 5
path 1 2 4 6
```

# Theory

Depth-First search
Depth-First Search (DFS) is an algorithm for traversing or searching through graph or tree data structures. Its strategy is to explore as far as possible along each branch before performing backtracking and exploring other branches.
## Core principles
1. “Go Deep” Strategy: The algorithm starts from a chosen initial vertex and follows one path as far as it can.
2. Data Structure Used: DFS is naturally implemented using a stack to store vertices that need to be visited, or using recursion (which implicitly uses the call stack).
3. Visited Vertices: To prevent infinite loops in graphs (especially those containing cycles) and to avoid revisiting vertices, it is necessary to maintain a list or set of visited vertices (e.g., using `Set<Integer>` or a `boolean[]` array).
## Algorithm
The recursive implementation is the most common and elegant.
1. Start: Choose a starting vertex v.
2. Mark: Mark v as visited.
3. Process: Perform any necessary action with vertex v (for example, print it).
4. Recursive Call: For each unvisited adjacent vertex w (a neighbor of v), recursively call DFS on w.

Example for Java:
```java
public void DFS(int v) {
    visited[v] = true;
    for (int neighbor : adj.get(v)) {
        if (!visited[neighbor]) {
            DFS(neighbor);
        }
    }
}
```

