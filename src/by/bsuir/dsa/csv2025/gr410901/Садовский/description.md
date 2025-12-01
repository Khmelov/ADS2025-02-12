# Name

Prim's algorithm

# Task

#Prim's algorithm

    Level B Assignment

    Create a class MyPrimMST that constructs a Minimum Spanning Tree (MST)
    of a graph using Prim’s algorithm.
    
    The graph must be stored inside the class using your own data structure
    (adjacency lists or adjacency matrix), WITHOUT using Java standard
    collection classes (such as ArrayList, HashMap, etc.).

    The toString() method must output the MST edges in ascending order
    of their weights, in the following format:
    {u1-v1=w1, u2-v2=w2, ...}

    /////////////////////////////////////////////////////////////////////////
    //////                Required Methods to Implement               ///////
    /////////////////////////////////////////////////////////////////////////

                addVertex(int v)
                addEdge(int u, int v, int weight)
                vertexCount()
                edgeCount()

                runPrim(int startVertex)
                getMSTWeight()
                getMSTEdges()          // returns an array of MST edges

                toString()

                clear()
                isEmpty()

                hasVertex(int v)
                hasEdge(int u, int v)

                getNeighbors(int v)    // returns a list of adjacent vertices
                getEdgeWeight(int u, int v)

    /////////////////////////////////////////////////////////////////////////
    //////                   Assumptions and Notes                     ///////
    /////////////////////////////////////////////////////////////////////////

        • The graph is undirected  
        • All weights are positive  
        • The graph is connected  
        • The MST is stored inside the object after calling runPrim()


# Theory

Brief Theory

Prim’s algorithm is a greedy method for constructing a **Minimum Spanning Tree (MST)** in a connected, weighted, undirected graph. Its goal is to select a set of edges with the minimum possible total weight that connects all vertices without forming cycles.

## Main Idea

The algorithm starts from an arbitrary vertex and gradually expands the tree by repeatedly selecting the **minimum-weight edge** that connects a vertex inside the current tree to a vertex outside it.

## Formal Description

Given a graph  
\[
G = (V, E), \quad w(e) > 0
\]

we aim to build a spanning tree  
\[
T \subseteq E, \quad |T| = |V|-1
\]
such that its total weight is minimal.

The algorithm works as follows:

1. Choose a starting vertex  
   \[
   s \in V
   \]

2. Initialize the set of vertices already included in the MST:  
   \[
   A = \{s\}
   \]

3. While \(|A| < |V|\), select the minimum-weight edge  
   \[
   (u, v) = \arg\min_{\substack{u \in A \\ v \notin A}} w(u, v)
   \]

4. Add the vertex and the edge to the tree:  
   \[
   A = A \cup \{v\}, \quad T = T \cup \{(u, v)\}
   \]

## Properties

- The algorithm always produces an MST.  
- Its time complexity is  
  \[
  O(E \log V)
  \]
  when using a priority queue,  
  or  
  \[
  O(V^2)
  \]
  when using an adjacency matrix.

## Result

The output is the set of MST edges and its total weight:  
\[
W(T) = \sum_{e \in T} w(e)
\]

The algorithm guarantees the minimal possible cost.


