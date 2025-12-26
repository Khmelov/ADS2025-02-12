# Name

Task Scheduling with Deadlines and Penalties

# Task

Task C: Task Scheduling with Deadlines and Penalties
Problem Description
You need to develop a Java program that solves the problem of optimal task scheduling considering deadlines and penalties for late completion.

Practical Application: This problem has wide applications in project management, production planning, scheduling, and other areas where it's necessary to optimize the order of task execution to minimize losses from deadline violations.

Problem Statement
Given:

An integer 1 <= n <= 20 (number of tasks)
For each task i (from 1 to n):
Execution time 1 <= duration[i] <= 100
Deadline 1 <= deadline[i] <= 1000
Penalty for lateness 1 <= penalty[i] <= 1000
Required:

Find the optimal order of executing all tasks that minimizes the total penalty
Penalty is charged only if a task completes after its deadline
Penalty formula: penalty[i] * (completion_time - deadline[i]), if completion_time > deadline[i]
Penalty equals 0, if completion_time <= deadline[i]


# Theory

---

title: Task Scheduling with Deadlines and Penalties

weight: 3

authors:

- Volkov

created: 2025

---

The problem of optimal task scheduling with deadlines and penalties is a classic combinatorial optimization problem with wide practical applications in project management, production planning, and scheduling.

## Problem Statement

Given $n$ tasks, each task $i$ is characterized by three parameters:
- $d_i$ — execution time of the task
- $D_i$ — deadline (desired completion time)
- $p_i$ — penalty per unit time of lateness

We need to find an order of executing all tasks that minimizes the total penalty.

The penalty for task $i$ is calculated as follows:

$$
\text{penalty}_i = \begin{cases}
0 & \text{if } t_i \leq D_i \\
p_i \cdot (t_i - D_i) & \text{if } t_i > D_i
\end{cases}
$$

where $t_i$ is the completion time of task $i$, equal to the sum of execution times of all tasks executed before it (inclusive).

## Dynamic Programming with Bitmasks

A brute force enumeration of all $n!$ permutations is inefficient even for small $n$. Instead, we use dynamic programming with bitmasks.

### State

Let $dp[mask]$ denote the minimum penalty for executing the subset of tasks specified by bitmask $mask$. Bit $i$ in the mask is 1 if task $i$ is included in the subset.

### Base Case

$$
dp[0] = 0
$$

An empty subset of tasks has zero penalty.

### Transition

For each mask $mask$ and each task $i$ included in this mask, consider the subset without task $i$:

$$
mask' = mask \setminus \{i\}
$$

The completion time of task $i$ in the current subset:

$$
t_i = \text{finishTime}(mask') + d_i
$$

where $\text{finishTime}(mask')$ is the total execution time of all tasks in subset $mask'$.

The penalty for task $i$:

$$
\text{penalty}_i = \max(0, p_i \cdot (t_i - D_i))
$$

Then the transition is:

$$
dp[mask] = \min_{i \in mask} \left( dp[mask'] + \text{penalty}_i \right)
$$

### Reconstructing the Answer

To reconstruct the optimal task execution order, we use an array $parent[mask]$, which stores the index of the last task added to the optimal solution for mask $mask$.

We reconstruct the order by moving from the full mask $2^n - 1$ to the empty mask $0$:

```cpp
vector<int> order;
int mask = (1 << n) - 1;
while (mask > 0) {
    int last = parent[mask];
    order.push_back(last);
    mask ^= (1 << last);
}
reverse(order.begin(), order.end());
```

## Computational Complexity

**Time:** $O(n \cdot 2^n)$

- Number of states: $2^n$
- For each state, we iterate over $n$ tasks
- Computing $\text{finishTime}(mask)$ can be optimized to $O(1)$ with precomputation

**Space:** $O(2^n)$

- Array $dp$ of size $2^n$
- Array $parent$ of size $2^n$
- Array $\text{finishTime}$ of size $2^n$

## Optimizing Completion Time Computation

Instead of recalculating $\text{finishTime}(mask)$ each time, we can precompute it for all masks:

```cpp
int finishTime[1 << n];
for (int mask = 0; mask < (1 << n); mask++) {
    finishTime[mask] = 0;
    for (int i = 0; i < n; i++) {
        if (mask & (1 << i)) {
            finishTime[mask] += d[i];
        }
    }
}
```

Or compute it dynamically when filling $dp$:

```cpp
for (int mask = 1; mask < (1 << n); mask++) {
    int totalTime = 0;
    for (int i = 0; i < n; i++) {
        if (mask & (1 << i)) {
            totalTime += d[i];
        }
    }
    // ... use totalTime
}
```

## Practical Considerations

### Constraints on $n$

Due to exponential complexity, the algorithm is only applicable for small $n$ (usually $n \leq 20$). For larger values of $n$, the problem becomes NP-hard, and heuristic or approximation algorithms are required.

### Overflow

When computing penalties, overflow may occur if $p_i \cdot (t_i - D_i)$ is large. Use `long long` to store penalties.

### Multiple Optimal Solutions

If multiple orders yield the same minimum penalty, the algorithm will return one of them (depends on the order of task iteration).

## Relation to Other Problems

This problem is closely related to:

- **Knapsack problem** — both use DP with bitmasks
- **Traveling Salesman Problem (TSP)** — similar state structure
- **Single-processor scheduling** — a special case

## Applications

- **Project management:** optimizing project task schedules
- **Production planning:** minimizing penalties for late orders
- **Real-time systems:** scheduling tasks with hard deadlines
- **Resource optimization:** task allocation to minimize losses

## Algorithm Improvements

For practical applications, consider:

1. **Heuristics:** greedy algorithms for large $n$
2. **Branch and bound:** pruning obviously suboptimal solutions
3. **Approximation algorithms:** guaranteed accuracy in exchange for speed
4. **Parallelization:** independent computations for different masks


