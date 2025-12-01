# Name

Bad vertices

# Task


# Bad Vertices
You are given a tree with vertices numbered from 1 to n. Some vertices are "bad". The task is to find the number of leaves (except possibly vertex 1) such that the path from vertex 1 to them does not contain m consecutive bad vertices in a row.

## Input Format:
The first line contains two integers n and m (2 ≤ n ≤ 10^5, 1 ≤ m ≤ n) — the number of vertices in the tree and the maximum allowed number of consecutive bad vertices.

The second line contains n integers a₁, a₂, ..., aₙ, where each aᵢ is either 0 (meaning vertex i is not bad) or 1 (meaning vertex i is bad).

The next n - 1 lines describe the edges of the tree in the format "xᵢ yᵢ" (without quotes) (1 ≤ xᵢ, yᵢ ≤ n, xᵢ ≠ yᵢ), where xᵢ and yᵢ are the vertices connected by an edge.

It is guaranteed that the given set of edges forms a tree.

## Output Format:
A single integer — the answer to the problem.

# Theory


### Basic Concepts

#### Tree

A **tree** is a connected undirected graph without cycles. Key properties:

-   There is exactly one path between any two vertices
    
-   Number of edges is always $n - 1$, where $n$ is the number of vertices
    
-   It is minimally connected (removing any edge disconnects the graph)
    

#### Leaf

A **leaf** (or pendant vertex) is a tree vertex with degree 1 (only one incident edge). In this problem:

-   Leaves are defined relative to the entire tree
    
-   Vertex 1 (root) is not considered a leaf, even if it has degree 1
    

#### Depth-First Search (DFS)

**DFS** is a graph traversal algorithm that:

-   Recursively visits "deep" vertices before their neighbors
    
-   Uses a stack (explicit or implicit) to track vertices
    
-   Time complexity: $O(n + m)$ for a graph with $n$ vertices and $m$ edges


