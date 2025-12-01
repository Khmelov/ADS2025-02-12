# Name

Loading a ship on two sides

# Task

Loading a ship on two sides

## Description
The ship has two sides: left and right. For safety on the ship during loading, the following constraints apply:
- the mass on the left side must not exceed WL,
- the mass on the right side must not exceed WR,
- the difference of masses between the sides must not exceed B.

Given n cargos with integer masses w[i]. Each cargo can be:
- placed on the left side,
- placed on the right side,
- or not taken.

The goal is to maximize the total mass of loaded cargos while satisfying all constraints.

## Input
The first line contains four integers: WL (the maximum allowable mass on the left side), WR (the maximum allowable mass on the right side), B (the maximum allowable difference of masses between the left and right sides), n (the number of cargos). The second line contains n integers w[i] (the mass of the i-th cargo).

## Output
Print one number — the maximum total mass of cargos on the ship (L + R) subject to the conditions:
- L ≤ WL,
- R ≤ WR,
- |L − R| ≤ B,

where L and R are the total masses on the left and right side respectively.

If only the empty loading is suitable, print 0.

## Notes
All input values are integers. Ranges:
0 ≤ WL, WR ≤ 1000,
0 ≤ B ≤ 1000,
1 ≤ n ≤ 1000,
1 ≤ w[i] ≤ 1000

# Theory

Dynamic Programming

## Introduction
Dynamic programming (DP) is an optimization method in which a complex problem is split into overlapping subproblems, and their solutions are stored and reused. The key idea: build a recurrence and fill a table of values, avoiding repeated computations.

## Core idea
- Decompose the problem into subproblems with optimal substructure.
- Store results to avoid exponential brute force.
- Choose the computation order: bottom-up (iterative) or top-down (memoization).

## Formulating recurrences
- Define a state that fully describes a subproblem.
- Write transitions between states.
- Specify base cases.

For example, for classical DP:
- State: f[i] — the optimal answer on the prefix of i elements.
- Transition: f[i] = min/max over valid actions from f[j], where j < i.
- Base: f[0] = 0 (or another neutral constant).

## Implementation approaches

### Top-down (memoization)
```
def solve(state):
  if memo[state] exists: return memo[state]
  if base(state): return base_value
  ans = combine( solve(next_state_1), solve(next_state_2), ... )
  memo[state] = ans
  return ans
```
- Pros: close to mathematical recursion; computes only required states.
- Cons: stack depth control; harder to guarantee order and invariants.

### Bottom-up (iterative)
```
init dp[...] = INF or neutral
dp[base_state] = base_value
for states in topological_order:
  for transition in outgoing:
    dp[next] = best(dp[next], combine(dp[current], cost))
```
- Pros: stable memory usage; no recursive calls.
- Cons: you must determine a correct state traversal order in advance.

## Practical tips
- Start with defining the state, then bases and transitions.
- Check edge cases on empty or minimal data.
- Profile memory, especially for 2D DP.
- First implement a working O(n^2) version, then optimize.

## Knapsack Problem

### Formulation
- Given n items with weights w_i and values v_i, and capacity W.
- Maximize the total value such that the sum of weights does not exceed W.

### 0/1 knapsack (each item is either taken whole or not taken)
- Classical DP:
```
dp[i][w] = maximum value using the first i items with capacity w
dp[0][w] = 0
dp[i][w] = max( dp[i-1][w], dp[i-1][w - w_i] + v_i )  if w_i ≤ w
dp[i][w] = dp[i-1][w]                                 otherwise
```
- Memory compression to O(W):
```
dp[w] = 0
for i in 1..n:
  for w from W down to w_i:
    dp[w] = max(dp[w], dp[w - w_i] + v_i)
```
- Complexity: O(nW) time, O(W) memory.

### Fractional knapsack (you can take fractions of items)
- Solved greedily: sort by value density v_i / w_i and take until W is filled.
- Important: the greedy approach does not apply to the 0/1 knapsack.

## Takeaways
DP allows you to formally describe states and transitions, and then systematically compute optimal answers. For 0/1 knapsack — use baseline DP with memory compression; for fractional — use a greedy solution.

