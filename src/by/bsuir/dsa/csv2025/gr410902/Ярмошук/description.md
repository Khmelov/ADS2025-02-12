# Name

Dependency analysis in a system of software modules

# Task

Analysis of dependencies in the system of software modules

### Problem condition

A software development system has modules (numbered starting from 0). Each module can depend on other modules. Dependencies are represented as a directed graph, where vertices are modules and edges are dependencies.

It is required to find all strongly connected components in the module dependency graph and display their vertices. Each connected component is printed on a new line without spaces or other separating characters in topological order (the first component is the source, the last is the sink). If the vertices in a component are equivalent, the order of their output is ascending.

**Input data:**

Directed graph in the form 1->2, 2->3, 4->5, 6->4, 3->4, 3->1, 5->6

**Output data:**

List of strongly connected components, each with a new line in the form:

123

456

# Theory

Components of strong connectivity

Definition. Two vertices of a directed graph are strongly connected if there is a path from one to the other and vice versa. In other words, they both lie in some kind of cycle.

It is clear that such a relation is transitive: if a and b are strongly connected, and b and c are strongly connected, then a and c are also strongly connected. Therefore, all vertices are split into strongly connected components - such a partition of vertices that within one component all vertices are strongly connected, and between the vertices. The simplest example of a strongly connected component is a cycle. But it can also be a complete graph, or a complex intersection of several cycles.

Kosaraju's algorithm is an algorithm for searching for regions of strong connectivity in a directed graph. To find regions of strong connectivity, first a depth-first search (DFS) is performed on the inversion of the original graph (i.e., the transposed graph), calculating the exit order of the vertices. We then use this order reversal to perform a depth-first search on the original graph (once again taking the vertex with the highest number obtained from the reverse pass). The trees in the DFS forest that are selected as a result represent strong components.

