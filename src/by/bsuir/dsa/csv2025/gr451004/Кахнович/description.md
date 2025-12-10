# Name

Minimum Subset Path

# Task

Minimum Subset Path
Given n cities and a distance matrix dist of size n × n, where dist[i][j] is the distance from city i to city j.

Find the minimum length of a path that starts at city 0, visits exactly k distinct cities (including the starting city), and returns to city 0.

Input:

First line: n k (2 ≤ k ≤ n ≤ 18)

Next n lines: n × n matrix where dist[i][j] is an integer (0 ≤ dist[i][j] ≤ 10^6)

It is guaranteed that dist[i][j] = dist[j][i] and dist[i][i] = 0

Output:

One number — the minimum path length. If it's impossible to visit exactly k cities and return to 0, output -1.

# Theory

title: Dynamic Programming on Subsets
weight: 3  
authors:
- Kakhnovich Vlad
created: 2025
---

This problem is a variation of the famous **Traveling Salesman Problem (TSP)** — one of the most studied NP-hard problems in combinatorial optimization. Unlike classical TSP that requires visiting all cities, here we need to visit exactly `k` cities, making the problem more flexible for practical applications.

## Solution Method

We use **dynamic programming on subsets** with state:

`dp[mask][i]` — minimum path length that:
- Visited cities from bitmask `mask` (where `mask & (1 << j)` means city `j` is visited)
- Ended at city `i`

**Algorithm:**
1. Initialize `dp[1][0] = 0` (start at city 0)
2. Iterate over all masks in increasing order of bit count
3. For each mask and last city, update states for all unvisited cities
4. Finally, among all masks with exactly `k` bits (including city 0), find the minimum path returning to 0

## Complexity and Optimizations

**Time complexity:** *O(n²·2ⁿ)*  
**Space complexity:** *O(n·2ⁿ)*

For `n ≤ 18` the total number of operations is approximately:
- 18² = 324
- 2¹⁸ = 262,144
- Total: ~85 million operations, feasible within few seconds.

**Optimizations:**
- Precomputing masks with required bit count
- Utilizing distance symmetry  
- Early pruning of impossible states

## Practical Applications

Such problems appear in:
- **Logistics** — planning delivery routes with time/point constraints
- **Tourism** — designing optimal excursion routes
- **Network design** — optimizing network node connections

## Further Development

For larger `n` (up to 25-30) consider:
- **Meet-in-the-middle** approach
- **Heuristic algorithms** (ant colony, genetic algorithms)
- **Approximation algorithms** with guaranteed accuracy

