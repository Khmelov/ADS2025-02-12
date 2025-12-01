# Name

Optimal Project Selection

# Task

Optimal Project Selection

## Task Condition

A company has a budget of $1,000,000 and must select from n available projects for investment. Each project has:

- `cost[i]` - project implementation cost
- `profit[i]` - expected profit from the project
- `manpower[i]` - required number of employees

### Constraints:

1. The total cost of selected projects must not exceed the budget ($1,000,000)
2. The total number of employees involved must not exceed 50 people
3. You can either select the entire project or not select it (fractional projects are not allowed)

### Objective:

Maximize total profit while satisfying the constraints.

## Mathematical Formulation

Let:
- `x[i] ∈ {0, 1}` - binary variable (1 - project selected, 0 - not selected)
- `C = 1,000,000` - budget
- `M = 50` - maximum number of employees

**Optimization Problem:**

Maximize: `∑(profit[i] * x[i])`

Subject to:
- `∑(cost[i] * x[i]) ≤ C`
- `∑(manpower[i] * x[i]) ≤ M`
- `x[i] ∈ {0, 1}` for all i

## Requirements

1. Study the theoretical foundations of the knapsack problem with multiple constraints
2. Study the discrete Lagrangian method for solving integer programming problems
3. Develop an algorithm to solve the problem using the discrete Lagrangian method
4. Provide examples of the algorithm's operation on various datasets

## Solution Format

- Mathematical description of the discrete Lagrangian method
- Algorithm for solving the problem
- Examples for various project sets
- Analysis of computational complexity

---

### Features of this task:

- **Knapsack problem with two constraints** - a classic discrete optimization problem
- **Discrete Lagrangian method** - a heuristic method for approximate solution of integer programming problems
- **Practical applicability** - the problem has many applications in business and economics
- **Good for learning** - allows understanding the basics of constrained optimization methods

This task is excellent for studying discrete optimization methods and heuristic algorithms.


# Theory


# Optimal Project Selection: Discrete Lagrangian Method

## Problem Statement

A company has a budget of $1,000,000 and must select from n available projects for investment. Each project has an implementation cost, expected profit, and required number of employees.

**Constraints:**
- Total cost of selected projects ≤ $1,000,000
- Total number of employees ≤ 50
- Each project is either fully selected or not selected

**Objective:** Maximize total profit.

## Theoretical Analysis

### Problem Formulation as Integer Programming

Formally, the problem is stated as follows:

**Maximize:**
```
Z = ∑(i=1 to n) profit[i] * x[i]
```

**Subject to:**
```
∑(i=1 to n) cost[i] * x[i] ≤ C
∑(i=1 to n) manpower[i] * x[i] ≤ M
x[i] ∈ {0, 1}, i = 1, 2, ..., n
```

where:
- `x[i]` - binary variable (1 if project selected, 0 otherwise)
- `C = 1,000,000` - budget
- `M = 50` - maximum number of employees

### Connection to Knapsack Problem

This problem is a generalization of the classical 0-1 Knapsack Problem with two constraints instead of one. This makes the problem NP-hard, meaning that an exact solution for large n requires exponential time.

### Discrete Lagrangian Method

The discrete Lagrangian method is a heuristic approach for solving integer programming problems with constraints. The main idea is to transform a constrained problem into an unconstrained one by introducing Lagrangian multipliers.

#### Lagrangian Construction

For our problem, the Lagrangian has the form:

```
L(x, λ₁, λ₂) = ∑(profit[i] * x[i]) 
               - λ₁ * (∑(cost[i] * x[i]) - C)
               - λ₂ * (∑(manpower[i] * x[i]) - M)
```

where `λ₁ ≥ 0` and `λ₂ ≥ 0` are Lagrangian multipliers.

After transformation, we get:

```
L(x, λ₁, λ₂) = ∑(profit[i] * x[i]) 
               - λ₁ * ∑(cost[i] * x[i]) 
               - λ₂ * ∑(manpower[i] * x[i])
               + λ₁ * C + λ₂ * M
```

Since the last two terms do not depend on x, the maximization problem is equivalent to maximizing:

```
L'(x, λ₁, λ₂) = ∑((profit[i] - λ₁ * cost[i] - λ₂ * manpower[i]) * x[i])
```

#### Interpretation of Lagrangian Multipliers

- `λ₁` - "price" of a unit of budget. Shows how much the objective function decreases when cost increases by one unit.
- `λ₂` - "price" of a unit of manpower. Shows how much the objective function decreases when the number of employees increases by one unit.

#### Discrete Lagrangian Method Algorithm

**Step 1: Initialization**
- Choose initial values for multipliers `λ₁⁽⁰⁾` and `λ₂⁽⁰⁾`
- Set best solution `x* = ∅`, `Z* = 0`

**Step 2: Compute Modified Profit**
For each project i, compute:
```
modified_profit[i] = profit[i] - λ₁ * cost[i] - λ₂ * manpower[i]
```

**Step 3: Sorting and Greedy Selection**
- Sort projects by decreasing `modified_profit[i]`
- Greedily select projects starting from the highest modified profit until constraints are violated

**Step 4: Update Multipliers**
If the solution violates constraints, update multipliers:
```
λ₁^(k+1) = max(0, λ₁^(k) + α * (∑cost[i] * x[i] - C))
λ₂^(k+1) = max(0, λ₂^(k) + α * (∑manpower[i] * x[i] - M))
```
where `α > 0` is the learning step.

**Step 5: Iteration**
Repeat steps 2-4 until convergence or maximum number of iterations.

**Step 6: Local Improvement**
Apply local search to improve the found solution:
- Try adding projects that were not selected
- Try replacing selected projects with more profitable ones

### Computational Complexity

- **Time:** O(n log n) per iteration (due to sorting) × number of iterations
- **Memory:** O(n) for storing projects and solution

For k iterations, total complexity: **O(k * n log n)**

### Theoretical Properties

#### Duality Theorem

For a maximization problem with "≤" constraints, the following holds:

```
max Z(x) ≤ min L(λ₁, λ₂)
```

where the minimum is taken over all non-negative `λ₁, λ₂`.

#### Monotonicity Property

When Lagrangian multipliers increase:
- Probability of selecting expensive projects decreases
- Probability of selecting projects requiring many employees decreases
- Constraint satisfaction improves

### Generalizations and Modifications

#### Multiple Constraints

The method easily generalizes to m constraints:

```
L(x, λ) = ∑profit[i] * x[i] - ∑(j=1 to m) λⱼ * (∑aⱼᵢ * x[i] - bⱼ)
```

where `aⱼᵢ` is the coefficient of the j-th constraint for project i, `bⱼ` is the right-hand side of the j-th constraint.

#### Adaptive Learning Steps

Instead of a fixed step `α`, adaptive strategies can be used:
- Decreasing step over time: `α(k) = α₀ / (1 + k)`
- Newton's method for updating multipliers

#### Hybrid Methods

Combination of discrete Lagrangian method with other methods:
- Initial solution by Lagrangian method, then exact method for small subproblems
- Using Lagrangian solution as upper bound in branch-and-bound method

## Practical Implementation Aspects

### Choosing Initial Multipliers

Recommended strategies:
1. **Based on average ratios:**
   ```
   λ₁ = average(profit[i] / cost[i]) / 2
   λ₂ = average(profit[i] / manpower[i]) / 2
   ```

2. **Based on dual estimates:**
   If a linear programming relaxation solution is available, use dual variables.

3. **Iterative search:**
   Try different multiplier combinations and choose the best.

### Stopping Criteria

1. **Convergence:** `|λ₁^(k+1) - λ₁^(k)| < ε` and `|λ₂^(k+1) - λ₂^(k)| < ε`
2. **Maximum iterations:** k ≥ K_max
3. **Solution improvement:** no improvement for N iterations

### Handling Degenerate Cases

- **All projects fit:** trivial solution - select all
- **No project fits:** empty solution
- **Equal modified profit:** use additional criterion (e.g., profit/cost ratio)

## Connection to Other Optimization Methods

### Branch-and-Bound Method

The discrete Lagrangian method can be used to obtain an upper bound in branch-and-bound, accelerating the search for an exact solution.

### Greedy Algorithms

The discrete Lagrangian method can be viewed as a generalization of greedy algorithms, where "greediness" is determined by modified profit depending on Lagrangian multipliers.

### Dynamic Programming

For small problems, dynamic programming with two dimensions (budget and employees) can be used, providing an exact solution but requiring O(n * C * M) time and memory.

## Conclusion

The discrete Lagrangian method is a powerful heuristic tool for solving integer programming problems with constraints. Although it does not guarantee solution optimality, it often provides good approximations in reasonable time, making it a practical choice for real-world problems.

The optimal project selection problem demonstrates the application of the method to a practical business problem, showing how theoretical optimization methods can be used for decision-making under resource constraints.


