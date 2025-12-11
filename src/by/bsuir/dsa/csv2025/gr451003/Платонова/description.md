# Name

Optimizing Event Chains

# Task

Optimizing Event Chains

In a game, there is a set of events.  
Each event can depend on one or more other events: event \(B\) cannot be executed until event \(A\) has been completed.

Your task:

1. Read the list of events and their dependencies.  
2. Construct an **optimal execution sequence**, where:
   - all dependencies of each event are satisfied,
   - there are no cycles (if a cycle exists, output \(\text{"CYCLE"}\)).

---

### Input Format

\[
\begin{aligned}
&N\ M \\
&A\ B \\
&C\ D \\
&\dots
\end{aligned}
\]

Where:  
- \(N\) — the number of events (numbered from 0 to \(N-1\)),  
- \(M\) — the number of dependencies,  
- each line \(A\ B\) means:  
  **to execute \(B\), you must first execute \(A\)**.

---

### Output Format

- A single line: a valid sequence of events (topological order).  
- If a cycle exists:

\[
\text{CYCLE}
\]

---

### Example

**Input:**

\[
\begin{aligned}
4\ 3 \\
0\ 1 \\
1\ 2 \\
0\ 3
\end{aligned}
\]

**Output:**

\[
0\ 1\ 2\ 3
\]
##Attention! A task can have several correct answers!

# Theory

Topological Sorting of Events

### Directed Acyclic Graph (DAG) of Events

Game events with dependencies can be represented as a directed graph 
\( G = (V, E) \), where:

- \( V \) — the set of events (vertices),  
- \( E \) — the set of dependencies (edges).

Each edge \( u \rightarrow v \) means that **event \( v \) can only be executed after event \( u \)**.

---

### Topological Order

A **topological order** is a sequence of vertices \( (v_1, v_2, \dots, v_N) \) such that:

\[
\forall (u, v) \in E: \quad u \text{ comes before } v
\]

A topological order exists **only if the graph is acyclic** (a DAG).

---

### Kahn's Algorithm for Topological Sorting

1. Compute the **in-degree** of each vertex:

\[
indeg(v) = \text{number of incoming edges to } v
\]

2. Initialize a queue \( Q \) with all vertices of zero in-degree:

\[
Q = \{ v \in V \;|\; indeg(v) = 0 \}
\]

3. While \( Q \) is not empty:

   1. Remove a vertex \( u \) from \( Q \) and append it to the result list.  
   2. For each neighbor \( v \) of \( u \) (i.e., \( u \rightarrow v \)):
   
   \[
   indeg(v) := indeg(v) - 1
   \]
   
   If \( indeg(v) = 0 \), add \( v \) to \( Q \).

4. If the number of vertices in the result list is less than \( |V| \), the graph contains a **cycle**.

---

### Complexity

Let \( N = |V| \) and \( M = |E| \):

- **Time complexity:** 

\[
O(N + M)
\]

- **Space complexity:**

\[
O(N + M)
\]

This algorithm efficiently finds a valid execution order of events respecting all dependencies.


