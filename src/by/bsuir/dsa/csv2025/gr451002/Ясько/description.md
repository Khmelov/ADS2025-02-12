# Name

Transport Routes Optimization

# Task

## Problem Statement

**Real part:** In a city's public transport system, different routes may use different transport types (bus, metro, tram). When transferring between transport types, passengers spend additional waiting time for the next transport. Find the route from start to end stop with minimum total travel time, considering transfer times.

**Formal part:** Given an undirected weighted graph where each edge has a transport type. When moving along an edge with transport type different from previous one, penalty (transfer time for target transport) is added to total time. Find shortest path between two vertices considering transfer penalties.

## Input Format

**Input line by line:**
1. `N M T` - number of stops, routes, transport types
2. `T numbers` - transfer time for each transport type
3. `M lines` with 4 numbers: `u v w type` - routes
4. `start end` - start and end stop

## Example

**Input example:**
3 2 2
5 3
0 1 10 0
1 2 8 1
0 2

**Input breakdown:**
- `3 2 2` - 3 stops, 2 routes, 2 transport types
- `5 3` - transfer time: 5 minutes for type 0, 3 minutes for type 1
- `0 1 10 0` - route from stop 0 to stop 1 takes 10 minutes on transport type 0
- `1 2 8 1` - route from stop 1 to stop 2 takes 8 minutes on transport type 1
- `0 2` - find path from stop 0 to stop 2

**Calculation example:**
Path: 0 → 1 → 2
- 0→1: 10 minutes (transport type 0)
- 1→2: 8 + 3 = 11 minutes (8 minutes travel + 3 minutes transfer to transport type 1)
- **Total: 21 minutes**

# Theory

### Dijkstra's Algorithm
Dijkstra's algorithm finds shortest paths from a source vertex to all other vertices in a graph with non-negative weights.

**Main steps:**
1. Assign distance 0 to source, infinity to others
2. Select vertex with minimum distance
3. Update distances to all neighbors
4. Mark vertex as processed
5. Repeat steps 2-4 for all vertices

### Dijkstra's Algorithm with State Tracking
Standard Dijkstra finds shortest paths in graphs but doesn't account for additional constraints. In this problem we need to track not only vertex but also last used transport type.

**Modification:**
- Store distances as `dist[vertex][last_transport]`
- When traversing edge, check if transport type matches previous `lastTransport != currentTransport`
- If types differ, add transfer time `transferTime[currentTransport]` for target transport

