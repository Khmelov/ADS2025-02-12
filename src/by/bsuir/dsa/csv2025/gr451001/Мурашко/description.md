# Name

The numismatist

# Task

Numismatist

There are **n** greedy coin collectors who agree to trade only if in exchange for a coin type they have more than one of, they receive a coin type they don't have at all. There are **k** types of coins in total. The number of coins of each type for each collector and for collector Seryozha is known. Seryozha wants to maximize the number of distinct coin types in his collection through trading with the greedy collectors. Assume that greedy collectors don't trade among themselves.

Devise any polynomial-time algorithm.

# Theory

Coin Trading Problem - Theory

## Problem Type
**Bipartite Matching with Iterative Improvement**

## Key Concepts

### Graph Representation
- **Vertices**: Coin types (k vertices)
- **Edges**: Possible trades between coin types
- **Bipartite Structure**: "Coins to receive" ↔ "Coins to give"

### Trade Conditions
For a valid trade (collector gives type A, receives type B):
1. `collector[A] ≥ 2` (has duplicate to trade)
2. `seryozha[B] ≥ 1` (can give this type)
3. `collector[B] = 0` (doesn't have this type)

### Algorithm Approach
1. **Model trades as bipartite graph edges**
2. **Find maximum matching** using standard algorithms:
   - Kuhn's algorithm (O(k³))
   - Hopcroft-Karp (O(k√k))
3. **Iterate until no improvements**

## Complexity Analysis
- **Time**: O(n × k³) - polynomial
- **Space**: O(k²) for graph storage

## Why It Works
- Each matching edge = one successful trade
- Trades are independent and commutative
- Iteration ensures all possible trade chains are explored
- Greedy collectors' constraints are respected

## Optimization Insight
The problem reduces to finding the maximum number of disjoint trade cycles in a directed multigraph where nodes represent coin types and edges represent possible trades through different collectors.

