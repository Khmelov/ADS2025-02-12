# Name

Maximum Flow

# Task

Maximum Flow.

**Problem Statement (Class FlowC):**
Create a class `FlowC` with a `main` method that computes the **Maximum Flow (Max Flow)** between a given Source and Sink in a directed graph with capacities on the edges.

## Input Data

The input data is read from the standard input (`System.in`):

1.  **Line 1 (Graph):** Edges in the format `Node_from-Node_to:Capacity`. Edges are separated by a comma (with optional space).
2.  **Line 2 (Source and Sink):** Names of the Source and Sink, separated by a space.

## Algorithm Requirements

* **Algorithm:** Use the **Edmonds-Karp Algorithm**.
* **Residual Graph:** Upon initialization, every forward edge $(u \to v)$ must have a corresponding **backward edge** $(v \to u)$ with zero capacity.
* **Iterations:** In a `while` loop, use **Breadth-First Search (BFS)** to find an **Augmenting Path** from the Source to the Sink.
* **Update:** If a path is found:
    * Determine the **bottleneck capacity** (minimum capacity) along the path.
    * Update the residual graph: decrease the capacity of forward edges and increase the capacity of backward edges by this amount.

## Output

The program must output a single integer—the value of the **Maximum Flow** (`System.out.print()`).

## Robustness

The implementation must be robust against parsing errors (spaces, delimiters).

# Theory

The **Maximum Flow (Max Flow)** problem is a cornerstone in graph theory. It involves finding the largest possible amount of flow that can be transmitted from one vertex, called the **Source ($s$)**, to another, called the **Sink ($t$)**, through a network where the edges have limited **Capacity**.

The main principle is the **conservation of flow**: the flow entering any vertex (except $s$ and $t$) must equal the flow leaving it.

## The Edmonds-Karp Algorithm

The **Edmonds-Karp Algorithm** is a specific implementation of the more general **Ford-Fulkerson Method**. It is distinguished by its strict use of **Breadth-First Search (BFS)** to find paths. The use of BFS ensures that every augmenting path found has the minimum number of edges, which guarantees a polynomial running time of $O(V E^2)$, where $V$ is the number of vertices and $E$ is the number of edges.



### The Residual Graph ($G_f$)

The **Residual Graph** ($G_f$) is the core component of the algorithm. This graph tracks the *available* capacity and allows the algorithm to "cancel" previously sent flow to find a more optimal overall path that bypasses bottlenecks.

* **Forward Edge ($u \to v$):** The residual capacity $c_f(u, v) = c(u, v) - f(u, v)$, where $f(u, v)$ is the current flow.
* **Backward Edge ($v \to u$):** The residual capacity $c_f(v, u) = f(u, v)$. This edge allows for the reversal (cancellation) of flow previously sent on the forward edge. Initially, backward edges have $c_f=0$.

### Iterations

The Edmonds-Karp algorithm iteratively finds augmenting paths and updates the residual graph:

1.  **Find Augmenting Path:** Using **BFS**, the algorithm finds any path $P$ from $s$ to $t$ in $G_f$ consisting only of edges with a residual capacity $c_f(u, v) > 0$.
2.  **Determine Flow ($\delta$):** The minimum residual capacity (bottleneck) on path $P$ is determined:
    $$\delta = \min_{(u,v) \in P} c_f(u,v)$$
3.  **Update Residual Graph:** The flow $\delta$ is added to the total flow, and the residual graph is updated:
    * **Forward Edge:** $c_f(u, v) \leftarrow c_f(u, v) - \delta$
    * **Backward Edge:** $c_f(v, u) \leftarrow c_f(v, u) + \delta$
4.  The loop repeats until BFS can no longer find a path with $\delta > 0$.

## Max-Flow Min-Cut Theorem

The convergence of the algorithm is guaranteed by the **Max-Flow Min-Cut Theorem**.

A **Cut $(S, T)$** is a partition of the vertices of the graph into two disjoint sets, $S$ and $T$, such that $s \in S$ and $t \in T$.

The **Capacity of a Cut $c(S, T)$** is the sum of the capacities of the edges going from $S$ to $T$.

$$\text{Max Flow} = \text{Min Cut}$$

This theorem states that the maximum flow that can be sent from $s$ to $t$ is always equal to the minimum total capacity of any cut separating $s$ and $t$. Finding the minimum cut serves as proof of the flow's optimality.

