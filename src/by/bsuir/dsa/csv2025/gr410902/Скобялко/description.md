# Name

Minimum-Cost Maximum Flow in a Transportation Network

# Task

## Task Description

You are given a directed graph with *n* vertices and *m* edges.  
Each edge has a capacity and a cost per unit of flow.  
Your task is to compute the maximum flow from the source *s* to the sink *t*, while ensuring that the total cost of the flow is minimal.

Input format:
- First line: n m s t
- Next *m* lines: u v c a  
  (edges with capacity c and cost a)

Output:  
The maximum flow and its minimum total cost.


# Theory

## Theory

The minimum-cost maximum-flow problem combines two optimization goals:  
1. Maximize the total amount of flow sent from *s* to *t*.  
2. Minimize the cost among all possible maximum flows.

### Residual Graph Concept

The algorithm uses a dynamically changing residual graph.  
For every directed edge (u → v), two residual edges are created:

- a forward edge with the remaining capacity,
- a reverse edge (v → u) with negative cost, enabling partial cancellation of previously sent flow.

This structure allows the algorithm to adjust earlier decisions and redirect flow along cheaper paths when discovered later.

### Shortest Paths by Cost

Each augmentation step requires finding the minimum-cost path from *s* to *t*.  
Two algorithms can be used:

- Bellman–Ford — supports negative edges but is slower,  
- Dijkstra with potentials — an optimized method using modified edge weights (potentials) to eliminate negative costs and significantly speed up the search.

Once the cheapest feasible path is found, the algorithm pushes as much flow through it as possible.  
The process repeats until no more augmenting paths remain.

### Properties of the Solution

Upon completion:
- the obtained flow is maximum (no additional flow can be pushed),
- and its total cost is minimal among all maximum flows,
- the residual graph represents the optimal redistribution of flow.


