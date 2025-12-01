# Name

Shortest paths with edge deletion

# Task

authors: - Andrei Kliaus created: 2025 title: Shortest Paths with
Removable Edges weight: 3

# Shortest Paths with Removable Edges

In the classical shortest-path problem, we look for the minimum distance
between two vertices of a directed weighted graph.\
But what if we are allowed to **remove** (i.e., set the weight to zero)
up to **K** edges encountered along the path?\
This variant requires a modified Dijkstra's algorithm and careful state
management.

## Input Format

1.  **A line with the graph** in the following format:

```
    A->B(5), B->C(3), A->C(10)
```

Each edge is given as `X->Y(w)`, where `w` is the edge weight.

2.  **Start vertex** --- a string\

```
    A
```
    

3.  **End vertex** --- a string\

```
    C
```
    

4.  **Integer K** --- the maximum number of edges whose weight can be
    removed (set to zero).\

```
    0
```


## Task

Find the **minimum distance from START to END** if you are allowed to
remove (zero out) the weight of **up to K edges** along the path.\
If no path exists --- output `-1`.

Removing an edge means that traversing it adds a cost of **0**, but the
number of used "removals" increases by 1.

## Output Format

A single number --- the minimum distance.

## Examples

### Example 1

**Input:**

    A->B(5), B->C(3), A->C(10)
    A
    C
    0

**Output:**

    8

### Example 2

**Input:**

    A->B(5), B->C(3), A->C(10)
    A
    C
    1

**Output:**

    0

### Example 3

**Input:**

    A->B(5), B->C(3), C->D(7), A->D(20)
    A
    D
    2

**Output:**

    0

### Example 4

**Input:**

    A->B(10), B->C(10), C->D(10), D->E(10)
    A
    E
    3

**Output:**

    10

### Example 5

**Input:**

    A->B(5), C->D(3)
    A
    D
    5

**Output:**

    -1

## Hint

To solve the problem, use a modified Dijkstra where the state is\
**(vertex, number of used removals)**,\
and for each vertex you store a distance array of size `K + 1`.

# Theory

---
title: Shortest Paths with Edge Removal
weight: 3
authors:
- Andrey Klyaus
created: 2025
---

In the classical shortest path problem, we need to find the minimum distance between two vertices in a weighted directed graph.

However, in this variant we are allowed to **set the weight of up to $K$ edges to zero**, meaning we can traverse them for free.

This approach requires using a modified Dijkstra's algorithm.

## Main Idea

Regular Dijkstra stores one distance per vertex.

Here, the state must account for the remaining edge "removals". We introduce the state

$$
(v, k),
$$

where $v$ is a vertex, and $k$ is the number of zeroing operations already used.

Then distances form an array:

$$
\text{dist}[v][k], \quad 0 \le k \le K.
$$

### Transitions

For each edge $v \to u$ with weight $w$, there are two options:

1. **Go honestly**

$$
\text{dist}[u][k] = \min\bigl(\text{dist}[u][k], \text{dist}[v][k] + w\bigr)
$$

2. **Remove the edge weight (if possible)**

   If $k < K$,

$$
\text{dist}[u][k+1] = \min\bigl(\text{dist}[u][k+1], \text{dist}[v][k]\bigr)
$$

A priority queue is used to process states, just like in the standard Dijkstra's algorithm.

## Answer

The minimum distance to the final vertex:

$$
\min_{0 \le k \le K} \text{dist}[\text{END}][k].
$$

If all values are infinity — the path does not exist, output `-1`.

## When This Works Efficiently

Algorithm complexity:

$$
O\bigl((V \cdot K + E \cdot K) \log(V \cdot K)\bigr),
$$

which is suitable for cases when $K$ is small or moderate (for example, up to several hundred).

## Common Mistakes

* Storing only one distance per vertex — **incorrect**.
* Enumerating all subsets of edges to remove — **exponentially expensive**.
* Modifying the graph in advance — meaningless, since removed edges differ for different paths.

## Brief Example

If there is a path

$$
A \to B \to C, \quad w = 5, 7,
$$

and $K = 1$, then it's better to zero out the more expensive edge:

$$
0 + 7 = 7 \quad \text{or} \quad 5 + 0 = 5.
$$

The algorithm will automatically find the optimal variant using states $(v, k)$.

