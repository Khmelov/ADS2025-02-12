# Name

Checking the correctness of the course schedule

# Task

Checking the correctness of the course schedule

**Requirement**

A university has a list of courses that students must complete. Some courses have prerequisites. For example, to enroll in the "Algorithms" course, you must first complete the "Data Structures" course.

Given a list of courses and their predecessors, determine whether it is possible to complete all courses without creating a "cyclic dependency" (where Course A requires Course B, and Course B requires Course A).

**Input Data Format**

First number: number of courses n

Second number: number of dependencies m

Next, m pairs of numbers: pairs a b, where course b is a prerequisite for course a

**Output Data Format**

true - if all courses can be completed (no cyclic dependencies)

false - if there are cyclic dependencies

**Example**

Input:

4 3 1 0 2 1 3 2

Output:

true

# Theory

Theory: Graph Traversal and Cycle Detection

## Basic Concepts

### Graphs
**Graph** - a data structure consisting of:
- **Vertices** (nodes) - the main elements
- **Edges** - the connections between vertices

**Directed Graph** - a graph where the edges have a direction (A → B means that you can go from A to B, but not necessarily vice versa).

### Graph Representation
**Adjacency List** - a way of representing a graph where each vertex stores a list of its adjacent vertices:
0: [1, 2]
1: [3]
2: [1]
3: []

## Graph Traversal Algorithms

### Depth-First Search (DFS)
**Idea**: We go "deep" along one path as long as possible, then return.

**Components**:
- **visited** - marks for visited vertices
- **Recursive traversal** of adjacent vertices

### Detecting Cycles in a Directed Graph

**Key Idea**: A cycle exists if, while traversing, we encounter a vertex that is on the current path.

**Arrays used**:
- `visited[]` - vertices that have already been fully processed
- `stack[]` (or `inPath[]`) - vertices in the current recursion path

**Algorithm**:
1. Mark a vertex as visited and add it to the current path
2. For each adjacent vertex:
- If it is already in the current path → a cycle is detected
- If not visited → recursively traverse
3. Remove the vertex from the current path before returning

## Algorithm Complexity
- **Time**: O(V + E), where V is the number of vertices, E is the number of edges
- **Memory**: O(V) for the visited and stack arrays

