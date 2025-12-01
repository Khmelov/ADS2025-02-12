# Name

Parallel Order Processing in an Online Store

# Task

Parallel Order Processing

## Problem Statement

Imagine you work for a large online store. Every day, hundreds of orders come in from customers, and they need to be processed quickly — checking inventory, preparing for shipment, generating documents. To handle such a load, you've decided to implement a multi-threaded processing system.

### How it works:

- Each order is processed separately and independently
- Processing one order takes between 100 and 500 milliseconds (random time, like in real life)
- You have a certain number of "processors" (threads) working in parallel
- All orders must be processed without errors
- At the end, you need to calculate how long the entire operation took



# Theory

---
title: Parallel Order Processing in an Online Store
weight: 1
authors:
- Margarita Bolshakova
created: 2025
---

Flows are one of the fundamental topics in graph theory.  
Intuitively, a flow can be imagined as the amount of water that moves through pipes from a source to a sink.  
Each pipe has a limitation: it cannot transfer more water than its capacity allows.

Formally, the problem is defined as follows: we are given a directed graph  
$G = (V, E)$ with two distinguished vertices — the **source** $s$ and the **sink** $t$.  
Each edge $(u, v)$ has an associated **capacity** — a number $c(u, v)$ representing the maximum possible flow through this edge.

We want to find a function $f(u, v)$ — the actual flow — satisfying the following conditions.

## Capacity Constraint

Each edge cannot carry more flow than its capacity:

$$
0 \le f(u, v) \le c(u, v)
$$

## Flow Conservation (Kirchhoff's Law)

For all vertices except the source $s$ and the sink $t$:

$$
\sum_{v \in V} f(v, u) = \sum_{v \in V} f(u, v)
$$

In other words, the amount of flow entering a vertex equals the amount leaving it.  
Flow does not appear or disappear on its own.

## Goal of the Problem

We want to **maximize the total flow** that leaves the source and reaches the sink:

$$
\max \sum_{v \in V} f(s, v)
$$

This problem is known as the **maximum flow problem**.

## Reverse Edges

For convenience, for each edge we introduce a *reverse edge* $(v, u)$  
with initial capacity 0.  
If a forward edge $(u, v)$ carries flow $f(u, v)$, then on its reverse edge:

$$
f(v, u) = -f(u, v)
$$

This allows us to decrease flow along a chosen route.

## Residual Network

The **residual capacity** is defined as:

$$
c_f(u, v) = c(u, v) - f(u, v)
$$

If $c_f(u, v) > 0$, then we can push additional flow through the edge.

Algorithms like Ford–Fulkerson and Edmonds–Karp rely on finding **augmenting paths** —  
paths from $s$ to $t$ along which the flow can be increased.

## Max-Flow Min-Cut Theorem

One of the most important results in graph theory:

> The maximum flow is equal to the capacity of the minimum cut $(S, T)$,  
> where $s \in S$ and $t \in T$.

In other words, to understand how much “water” can be sent from the source to the sink,  
we need to find the bottleneck — the smallest cut in the system.

## Applications of Flows

* cargo distribution  
* network packet routing  
* production planning  
* scheduling optimization  
* many classic combinatorial problems

