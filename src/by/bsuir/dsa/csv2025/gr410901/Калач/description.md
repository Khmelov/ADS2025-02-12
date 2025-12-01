# Name

Graph k-coloring

# Task

Problem: Graph k-coloring

You are given an undirected graph ( G = (V, E) ) with ( n ) vertices and a set of edges.
You are also given a number ( k ).

You must determine whether it is possible to color each vertex of the graph with one of the ( k ) colors such that:

1. No two adjacent vertices share the same color.

---

## Goal

Return:

* **true** if a valid coloring of the graph using ( k ) colors exists
* **false** if no such coloring exists

---

## Input Format

* The first line contains two numbers:
  **n** — the number of vertices,
  **k** — the number of available colors.

* Then follows the description of the graph edges.
  The format may be, for example:

  * the number of edges **m**
  * then **m** lines, each containing two numbers — the vertices connected by an edge

---

## Output Format

* A single word: **true** or **false**.

---

## Constraints

* **1 ≤ n ≤ 20** — number of vertices
* **1 ≤ k ≤ n** — number of colors
* **0 ≤ m ≤ n·(n−1)/2** — number of edges
* Vertices are numbered from **0** to **n−1**
* The graph is undirected

---

## Constraints

* **1 ≤ n ≤ 20** — number of vertices
* **1 ≤ k ≤ n** — number of colors
* **0 ≤ m ≤ n·(n−1)/2** — number of edges
* Vertices are numbered from **0** to **n−1**
* The graph is undirected

---

## Example Input

```
4 2
4
0 1
1 2
2 3
3 0
```

This graph is a cycle of 4 vertices.
You must determine whether it can be colored with two colors.

---

## Example Output

```
true
```

# Theory

Graph k-Coloring

In graph-related problems, we often need to assign labels to objects so that conflicting objects receive different labels.  
In the case of **graph coloring**, the objects are **vertices**, and a conflict occurs between vertices connected by an edge.

Graph coloring is a fundamental problem in graph theory that underlies many real-world applications: resource scheduling, compiler register allocation, frequency assignment, network modeling, optimization, and more.

## Why color the vertices of a graph?

If two vertices are connected by an edge, they are in conflict or interact with each other. Therefore:

- **adjacent vertices must receive different colors**;
- vertices **not connected by an edge** may have the same color.

A color is a conditional label. It can represent:
- a type of resource,
- a time slot for a task,
- a radio frequency channel,
- a group of non-conflicting objects.

## Formal model

Given an undirected graph

$$ G = (V, E), $$

where:
- $V$ — set of vertices,
- $E \subseteq V \times V$ — set of edges.

We need to define a function

$$ \mathrm{color}: V \to \{1, 2, \ldots, k\}, $$

such that for every edge $(u, v) \in E$:

$$ \mathrm{color}(u) \neq \mathrm{color}(v). $$

If such a coloring exists, the graph is said to be **k-colorable**.

## Chromatic number of a graph

The smallest number of colors needed to properly color the graph is called the **chromatic number**:

$$ \chi(G). $$

The k-coloring problem reduces to the question:

> Is $\chi(G) \leq k$?

If yes — a valid k-coloring exists; if no — it is impossible to color the graph with $k$ colors.

## Conflicts between vertices

In this problem, the **conflict graph coincides with the original graph**:

- each vertex represents an object to be colored,
- each edge represents a conflict,
- a conflict forbids assigning the same color to adjacent vertices.

Thus, there is no need to construct an additional conflict graph — the structure of conflicts is completely defined by the given graph $G$.

## Methods for solving the problem

### 1. Backtracking

We sequentially try to assign one of the $k$ colors to each vertex.  
If a conflict arises, we undo the choice and try another color.

**Advantages:**
- finds an exact solution if one exists.

**Disadvantages:**
- exponential time complexity in the worst case.

### 2. Greedy algorithms

Classic example: color vertices in some order, always choosing the smallest available color not used by already-colored neighbors.

**Advantages:**
- extremely fast (O(n + m) with proper implementation).

**Disadvantages:**
- may use more colors than necessary (at most $\Delta + 1$, where $\Delta$ is the maximum degree).

### 3. Advanced algorithms

Used for hard instances:
- DSATUR,
- exact branch-and-bound methods,
- reduction to SAT,
- metaheuristics (tabu search, genetic algorithms),
- local search, etc.

## Standard algorithm for checking k-colorability (backtracking)

1. Iterate through vertices in some order (often useful to order by decreasing degree).
2. For the current vertex, try assigning one of the $k$ colors.
3. Check that no already-colored neighbor has the same color.
4. If the color is safe — move to the next vertex.
5. If no color works — backtrack to the previous vertex.
6. If all vertices are successfully colored — the graph is $k$-colorable.

## Meaning of the result

- If a valid $k$-coloring exists → the graph can be partitioned into $k$ **independent sets** (color classes).
- If not → the graph is too "dense" and requires more than $k$ colors.

Thus, the **k-coloring problem** checks whether it is possible to eliminate all conflicts between vertices using at most $k$ distinct labels/resources/groups.

