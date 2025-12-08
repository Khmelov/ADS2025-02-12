# Name

Identifying isolated groups in social networks

# Task

Identifying isolated groups in social networks

In a social network, each vertex of the graph represents a user, and each edge represents a friendship connection between them.  
It is required to determine how many isolated groups of users exist and list the participants of each group.  
If two users are connected directly or through other users, they belong to the same connected component.

### **Input Format**

The first line contains two numbers: the number of users (vertices) and the number of connections (edges).  
Then follow the lines with user numbers (0..n-1) who are friends with each other.

### **Output Format**

First, output the number of connected components.  
Then, for each component, output the list of user numbers belonging to it.


# Theory

Connected Components Search

A connected component in a graph is the maximal set of vertices such that any pair is connected by a path through edges, and vertices outside the component are unreachable from it. For a graph  
$$
G = (V, E)
$$
a connected component is defined as a subset of vertices  
$$
C \subseteq V
$$
such that each vertex $u \in C$ is reachable from any other $v \in C$, while vertices outside $C$ are unreachable from it.  

Analyzing connected components helps to identify isolated groups, study the structure of the graph, and make decisions in network design, data clustering, or social network analysis.  

## Main Algorithm Idea

To find connected components, **Depth-First Search (DFS)** is used. The algorithm proceeds as follows:  

1. All vertices are marked as unvisited.  
2. An unvisited vertex is chosen, from which DFS is launched. All vertices reachable from this point are included in one connected component.  
3. After the traversal ends, the marked vertices form a separate component.  
4. The process repeats for the remaining unvisited vertices until the entire graph is processed.  

Using DFS allows recursive exploration of the graph, efficient investigation of structural features of each component, and storage of intermediate results in the call stack.  

## Algorithm Implementation

To store the graph, an adjacency list `adj[v]` is used, along with an array `visited[v]` to mark visited vertices, and an array of lists `components[i]` to store the vertices of each component. This format makes the algorithm simple and efficient: access to neighbors is fast, and tracking visited vertices prevents repeated traversals.

Then the algorithm sequentially scans all vertices of the graph and, upon encountering an unvisited vertex, launches DFS, which forms a new connected component. During DFS, each reachable vertex is marked as visited and added to the current component. After the traversal ends, the component is saved, and the process continues until all vertices are processed. As a result, the graph is automatically divided into separate isolated groups.

## Algorithm Complexity

When storing the graph as adjacency lists, the traversal complexity is  
$$
O(n + m),
$$  
where $n$ is the number of vertices and $m$ is the number of edges. This method is optimal for sparse graphs. If the graph is represented by an adjacency matrix, the complexity increases to  
$$
O(n^2),
$$  
since it is necessary to check the existence of edges between each pair of vertices.  

## Applications of Connected Components

Connected components are applied in social networks to identify groups of users, in infrastructure systems to determine subnetworks, in geography to highlight isolated territories, in biology to analyze interactions between genes or neural networks, and also in data processing and object clustering.  



