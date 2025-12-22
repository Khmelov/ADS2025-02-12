# Name

Critical Communication Channels

# Task

---
title: Critical Communication Channels
weight: 1
authors:
- Ivanov Pavel
  created: 2025
---

A country has `n` cities connected by `m` bidirectional communication channels. Each channel connects two cities and allows information to be transmitted in both directions.

The security service is concerned about the reliability of the communication network. They want to identify all **critical communication channels** — channels whose failure would isolate at least one city from the rest of the network, or cause the network to split into several isolated parts.

In other words, a critical channel is the only path between certain cities. If such a channel fails, communication between these cities will be completely lost.

Help the security service compile a list of all critical channels. Each channel is represented by a pair of cities `u` and `v`. Output all critical channels in ascending order of city numbers (if a channel connects cities `u` and `v`, where `u < v`, output `u v`).

# Theory

---
title: Bridges in a Graph
weight: 2
authors:
- Ivanov Pavel
  created: 2025
---

A **bridge** is an edge in an undirected graph whose removal increases the number of connected components in the graph. In other words, a bridge is the only path between two parts of the graph.

#### Key Properties:
- In a tree, all edges are bridges
- In a cycle, there are no bridges
- If a graph contains a cycle, the edges of that cycle are not bridges
- A bridge always belongs to some simple path

#### Bridge Finding Algorithm (DFS)

To find bridges, we use a modified depth-first search (DFS) algorithm with two arrays:
- `tin[v]` — entry time of vertex `v` in DFS
- `low[v]` — minimum entry time among all vertices reachable from `v` via DFS tree edges and one back edge

**Bridge criterion:** An edge `(u, v)` is a bridge if and only if `low[v] > tin[u]` (or `low[u] > tin[v]`).

#### Time Complexity
- **O(V + E)**, where V is the number of vertices, E is the number of edges
- The algorithm performs a single depth-first traversal of the graph

#### Applications
- Network reliability analysis
- Finding vulnerable points in infrastructure
- Constructing biconnected components of a graph

