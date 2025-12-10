# Name

Minimum Edge Set to Decrease the Maximum s–t Flow

# Task

**Minimum Edge Removal for Reducing Flow**

## **Task**

You are given a directed graph ( G = (V, E) ) with non-negative edge capacities. It is known that the maximum flow from vertex ( s ) to vertex ( t ) equals ( F ).

You must find a set of edges ( R ⊆ E ) of _minimum cardinality_ such that removing these edges results in the new maximum flow in the modified graph being **no greater than ( F/2 )**.

In other words, the goal is to minimize the number of removed edges so that the network’s throughput decreases significantly.

## **Input Format**

- The first line: three integers  
    ( n ) — number of vertices,  
    ( m ) — number of edges,  
    ( s, t ) — source and sink.
    
- The next ( m ) lines describe edges:  
    ( u, v, c ) — a directed edge from ( u ) to ( v ) with capacity ( c ).
    
- It is guaranteed that the initial graph has an ( s )-to-( t ) flow.
    
- The value of the initial maximum flow ( F ) must be computed by the solution.
    

## **Output Format**

- A single number: the minimum number of edges that must be removed so that the maximum flow becomes no greater than ( F/2 ).  
    (Optionally, you may output the edges themselves, but this is not required.)
    

## **Notes and Hints**

- Note that the goal is to reduce the flow **not to zero**, but **at least by half**.
    
- A minimum cut blocks all flow completely, but deleting its edges may not be minimal in terms of count.
    
- Consider cuts of small capacity (≤ ( F/2 )) and search among them for those containing a _minimum number of edges_.
    
- Analyze the structure of the residual network after computing the maximum flow — it reveals which edges are critical for maintaining a significant portion of the flow.

# Theory

---
title: Minimum Edge Removal for Reducing Flow — Brief Theory
authors:
darkon28
---

In problems involving network flows, it is often important not only to determine the maximum flow but also to understand **how resilient** the network is to local modifications. One approach is to find a *minimum set of edges* whose removal significantly reduces the network’s ability to carry flow.

Let a directed graph be given with a maximum flow from `s` to `t` equal to \( F \). We are interested in determining the minimum number of edges whose removal ensures that the new maximum flow becomes no greater than \( F/2 \). This formulation models the network’s resistance to failures and helps identify its vulnerable points.

## Main Ideas

### Relation to Cuts
The max-flow min-cut theorem states that the value of the maximum flow equals the minimum capacity of an s–t cut. However, in this problem the goal is not to collapse the flow completely but to reduce it **at least by half**, which means that a standard minimum cut is not always optimal in terms of the *number* of edges it contains.

### Bicriteria Optimization
We look for cuts whose total capacity does not exceed \( F/2 \), and among them we choose one with as few edges as possible.  
This combination of two criteria makes the problem significantly more complex than a standard min-cut.

### Residual Network
After computing the maximum flow, analyzing the residual network is useful. It reveals:
* the paths that carry the main part of the flow,
* which edges are critical,
* where alternative detours exist.

Often, the minimal-by-count “bottleneck” can be identified precisely through the structure of the residual network.

## Practical Notes

* Removing a single edge may either completely destroy a flow path or have almost no effect — understanding the global structure of the network is crucial.
* The sets of edges that reduce the flow by half may be very different from those in the minimum cut.
* In real-world networks, this theoretical framework is used in resilience analysis and the design of critical infrastructures.

