# Name

Minimum Maximum Load

# Task

Minimum Maximum Load

## Problem

You are given `n` processes and `m` identical machines (computational nodes).  
Each process `i` requires `tasks[i]` units of time to complete.    

Your task is to assign all processes to the machines so that the **maximum load on any machine is minimized**.

## Input (single line, space-separated):

- `n`: number of processes
- `tasks`: an array of integers of length `n`, where `tasks[i]` is the time required for the i-th process.  
- `m`: an integer (1 ≤ m ≤ n) — the number of machines.

## Output

- An integer — the minimal possible maximum load on any machine.

## Example 1

**Input:**
n = 4;
tasks = [3, 2, 5, 4];
m = 2

**Output:**
7

**Explanation:**
One possible optimal assignment:
Machine 1: [5, 2] → load = 7
Machine 2: [4, 3] → load = 7
The maximum load on any machine is 7, which is minimal.

# Theory

## Greedy Algorithms

### Definition and Essence

Greedy algorithms are a class of combinatorial optimization algorithms in which at each step a **locally optimal choice** is made, with the hope that it will lead to a globally optimal or near-optimal solution.  

They are usually simple to implement and efficient, but **do not always guarantee the exact optimum**.

### Key Properties

1. **Greedy Choice**  
   At each step, the algorithm makes a "greedy" choice — selecting the best option available at the moment.

2. **Optimal Substructure**  
   The solution to a problem can be constructed from solutions to its subproblems:

$$
Opt(X) = f(Opt(X_1), Opt(X_2), \dots, Opt(X_k))
$$

3. **No Backtracking**  
   Greedy algorithms build the solution step by step without trying all possible options.

### Classic Examples

- **Coin change problem** — giving the minimal number of coins when denominations are divisible.  
- **Fractional knapsack** — taking items partially in decreasing order of value-to-weight ratio.  
- **Activity selection** — maximizing the number of non-overlapping tasks by choosing intervals in increasing order of finishing time.

### Advantages and Limitations

**Advantages:**

- Simple structure and implementation  
- High performance — often \(O(n \log n)\) due to sorting

**Limitations:**

- Does not always yield the optimal solution  
- Requires proof of correctness for each specific greedy strategy

### Summary

Greedy algorithms are a powerful tool for building solutions **step by step**, where local optimal choices can lead to a global optimum.

