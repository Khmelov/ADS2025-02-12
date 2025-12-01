# Name

Search for public transport routes

# Task

Search for public transportation routes
A new neighborhood has been built in the city with its own dedicated public transportation stop. We need to determine which stations the new bus or trolleybus should use to reach the new neighborhood as quickly as possible.
## Input data format
- The first line contains four space-separated numbers:
1. Number of stops, 'n'
2. Number of roads between stops, 'm'
3. Number of the starting stop on the route
4. Number of the ending stop on the route
- The next n lines must match the station numbers with their numbers. First comes the station name (if two words, separate it with an underscore) and then the corresponding number.
- Each of the following lines must list all existing routes between stops; three values ​​must be space-separated:
1. Number of the first stop
2. Number of the second stop
3. Distance between them.
---
## Output format
- The first line must contain the names of all stops along the shortest route (the format is: Stop -> Stop -> ...)
- The second line must display the total distance traveled by public transportation.
---
## Additional conditions
- The implementation must use Dijkstra's algorithm, and for dense and sparse graphs, optimal algorithms must be used specifically for them.
- Since public transportation always travels in both directions, we treat the graphs as undirected.

# Theory

Dijkstra's Algorithm
Dijkstra's Algorithm finds the shortest paths from a given vertex $s$ to all other vertices in a graph without negative-weight edges.
There are two main variants of the algorithm, each with a running time of $O(n^2)$ and $O(m \log n)$, where $n$ is the number of vertices and $m$ is the number of edges.
## Basic Idea
We'll create an array $d$ that, for each vertex $v$, stores the current length $d_v$ of the shortest path from $s$ to $v$. Initially, $d_s = 0$, and for all other vertices, the distance is infinity (or any number known to be greater than the maximum possible distance).
As the algorithm runs, we'll gradually update this array, finding more optimal paths to vertices and reducing the distance to them. When we determine that the path found to a vertex $v$ is optimal, we mark this vertex by placing a one ($a_v=1$) in a special array $a$, initially filled with zeros.
The algorithm itself consists of $n$ iterations, at each of which the vertex $v$ with the smallest $d_v$ value among those not yet marked is selected:
$$
v = \argmin_{u | a_u=0} d_u
$$
The selected vertex is marked in array $a$, after which *relaxations* are performed from vertex $v$: we examine all outgoing edges $(v,u)$ and, for each such vertex $u$, we try to improve the value of $d_u$ by assigning
$$
d_u = \min (d_u, d_v + w)
$$
where $w$ is the length of edge $(v, u)$.
At this point, the current iteration ends, and the algorithm moves on to the next: the vertex with the smallest $d$ value is again selected, relaxations are performed from it, and so on. After $n$ iterations, all vertices of the graph are labeled, and the algorithm terminates.
### For dense graphs
If $m \approx n^2$, then at each iteration, we can simply traverse the entire array and find $\argmin d_v$.
The asymptotics of such an algorithm is $O(n^2)$: at each iteration, we find the argminimum in $O(n)$ and perform $O(n)$ relaxations.
### For sparse graphs
If $m \approx n$, then the minimum can be found faster. Instead of a linear traversal, we introduce a structure into which we can add elements and find the minimum—for example, `std::set` can do this. We will maintain pairs $(d_v, v)$ in this structure, removing the old $(d_u, u)$ and adding a new $(d_v + w, u)$ during relaxation, and simply taking the minimum (the first element) when finding the optimal $v$.
We no longer need to maintain the array $a$: the structure itself will act as the set of yet-to-be-considered vertices for finding the minimum.
For each edge, two queries must be made to the binary tree storing $O(n)$ elements, each requiring $O(\log n)$, so the asymptotics of this algorithm is $O(m \log n)$. Note that for complete graphs, this will be $O(n^2 \log n)$, so the previous algorithm should not be forgotten.
### Path Recovery
Often, it is necessary to know not only the lengths of the shortest paths, but also to obtain the paths themselves. To do this, you can create an array $p$, in which the cell $p_v$ will store the *parent* of vertex $v$—the vertex from which the last relaxation along edge $(p_v, v)$ occurred. This can be updated in parallel with the array $d$.
To restore the path, simply iterate through the ancestors of vertex $v$:

