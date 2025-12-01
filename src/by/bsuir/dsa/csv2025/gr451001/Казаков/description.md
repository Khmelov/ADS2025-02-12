# Name

Topological sorting

# Task

Topological Sorting

You are given a directed graph representing dependencies between modules of a software system. Each vertex of the graph corresponds to a module, and a directed edge u → v means that module u must be loaded/processed before module v.

## Task:

1. Implement the topological sorting algorithm using two methods:
   - Based on depth-first search (DFS)
   - Based on Kahn's algorithm (using counting of incoming degrees of vertices)

2. Cycle detection:
   - If the graph contains a cycle, output the message:
     "Graph contains cycle, topological sorting impossible"

3. For an acyclic graph:
   - Output a correct topological order for each algorithm
   - Compare the results of the two methods (the orders may differ)


# Theory

---
title: Topological Sorting
weight: 5
---

The problem of topological sorting of a graph is stated as follows: given a directed graph, it is required to find an order of vertices such that all edges of the graph lead from an earlier vertex to a later one.

This can be useful, for example, when scheduling related tasks: you need to get dressed, putting on shorts (1), pants (2), shoes (3), tucking the pants (4) — like hipsters do — and tying the shoelaces (5) in the correct order.

![](../img/sorting.png)

First, note immediately that a graph with a cycle cannot be topologically sorted — no matter how you arrange the cycle in the array, you cannot consistently move to the right along the edges of the cycle.

Secondly, the converse is true. If there is no cycle, then it can always be topologically sorted — we will now show how.

Note that a vertex with no outgoing edges can always be placed last, and such a vertex always exists in an acyclic graph (otherwise, one could follow reverse edges infinitely). This immediately suggests a constructive proof: iteratively add to the array a vertex that has no outgoing edges, and remove it from the graph. After this process, the array will need to be reversed.

This algorithm is easier to implement by considering the exit times of vertices in dfs. The vertex we exit first is the one that has no new outgoing edges. Then we will only exit from vertices that, if they have outgoing edges, only lead to vertices we have already exited.

Therefore, it is sufficient to simply list the vertices in the order of exiting dfs, and then reverse the obtained list, and we will get one of the valid topological sorts.

```cpp
const int maxn = 1e5;

bool used[maxn];
vector<int> t;

void dfs(int v) {
    used[v] = true;
    for (int u : g[v])
        if (!used[u])
            dfs(u);
    t.push_back(v);
}

void topological_sort() {
    for (int v = 0; v < n; v++)
        if (!used[v])
            dfs(v);
    reverse(t.begin(), t.end());
}
```

Topological sorting can be used to check reachability by comparing the indices of vertices in the resulting array. The fact that vertex $a$ comes after vertex $b$ indicates that $b$ is unreachable from $a$ — however, $a$ may or may not be reachable from $b$.


