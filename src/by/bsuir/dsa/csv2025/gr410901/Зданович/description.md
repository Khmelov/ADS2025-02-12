# Name

Spanning Tree and District Connectivity

# Task

Minimum Spanning Tree and Connectivity Queries

A city is building a network of high‑speed roads between its districts.  
Each road connects two districts and has a construction cost.  
Some roads already exist, others are only planned.

You must write a Java program that:

1. Given an undirected weighted graph, builds a Minimum Spanning Tree (MST) if it exists.  
2. Answers connectivity queries between districts.

## Input format

The first line: list of roads in the format

`A-B(5), B-C(3), C-D(4), A-D(10)`

where `A` and `B` are district names (strings without spaces), and the number in parentheses is the cost (positive integer).

The second line: integer `Q` — number of queries.

Then follow `Q` lines; each query is one of:

- `MST` — print the total cost of a minimum spanning tree, or `NO MST` if not all districts can be connected.  
- `CONNECTED X Y` — check whether districts `X` and `Y` are in the same connected component, print `YES` or `NO`.

## Output format

For each query print one line:

- for `MST`: the integer cost or `NO MST`;  
- for `CONNECTED`: `YES` or `NO`.

# Theory

### Minimum spanning tree and connectivity

A Minimum Spanning Tree (MST) of an undirected weighted graph is a subgraph that contains all vertices, is a tree (connected and acyclic), and has the minimum possible total edge weight.  
Typical applications include designing road or communication networks that connect all locations with minimum construction cost.

Kruskal’s algorithm is a classic greedy algorithm for building an MST.  
It sorts all edges in non‑decreasing order of weight and scans them one by one, adding an edge only if its endpoints belong to different connected components; otherwise the edge is skipped to avoid creating a cycle.  

The connected components are maintained using a Disjoint Set Union (DSU, also called Union‑Find) data structure with operations find and union.  
With path compression and union by rank or by size these operations are very fast in practice, and the overall time complexity of Kruskal’s algorithm is of order m log m, where m is the number of edges in the graph.


