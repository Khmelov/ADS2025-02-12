# Name

Courier Route Optimization

# Task

Courier Route Optimization

## Task Description

A delivery company wants to optimize courier routes in a city district.  
The courier must traverse **each street exactly once**, starting and ending at the same point.  
You need to find such a route or report that it is impossible.

Given an **undirected graph**, where each vertex is an intersection and each edge is a street.  
Find a **route that passes through every edge exactly once and returns to the starting vertex.**

**Input format:**
- First line: `n, m` — number of vertices and edges
- Next *m* lines: `u, v` — edge between vertices `u` and `v`

**Example:**
3 3
0 1
1 2
2 0 

**Output format:**  
- If a route exists, print the sequence of vertices forming the route.  
- If no route exists — print `NO`.

**Example:**
0 1 2 0


# Theory

## Eulerian Cycle

Let an undirected graph be given as  
$$
G = (V, E),
$$  
where $V$ is the set of vertices and $E$ is the set of edges.  

An **Eulerian cycle** is a cycle that passes through **every edge exactly once** and returns to the starting vertex.  

### Conditions for an Eulerian cycle:

1. The graph is connected (for all vertices with non-zero degree there exists a path between them).  
2. Each vertex has an even degree:  
$$
\deg(v) \mod 2 = 0 \quad \forall v \in V
$$

### Hierholzer's Algorithm:

1. Choose a starting vertex $v_0 \in V$ with non-zero degree.  
2. While there are unused edges, traverse them and add vertices to the cycle after returning.  
3. After all edges are processed, we obtain a complete Eulerian cycle $C$:  
$$
C = (v_0, v_1, \dots, v_k, v_0)
$$


**Eulerian cycles are useful for:**
- analyzing graph structure and checking connectivity,
- planning routes, logistics, network inspections,
- optimizing tasks where each connection must be traversed exactly once (streets, delivery routes, communication lines).

---

## Complexity

- Building adjacency lists: O(n + m)  
- Hierholzer's algorithm: **O(m)** — each edge is visited exactly once  
- Total complexity: **O(n + m)**


