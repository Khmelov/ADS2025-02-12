# Name

Minimum Number of Coins

# Task

Minimum Number of Coins

---

### Problem Statement
You are given several distinct coin denominations and a target amount. Find the minimum number of coins needed to obtain exactly this amount (each coin may be used an unlimited number of times).  
If it is impossible to form the amount, return `-1`.

### Input Format
- The first line contains two integers: `n` and `amount` — the number of coin types and the target amount (1 ≤ n ≤ 100, 0 ≤ amount ≤ 10^5).  
- The second line contains `n` positive integers — the coin denominations (each in the range 1..10^5).

### Output Format
Print a single integer — the minimum number of coins needed to form `amount`, or `-1` if it is impossible.

### Example
**Input:**  
3 11  
1 2 5

**Output:**  
3

### Explanation
11 = 5 + 5 + 1

# Theory

Theory: General Dynamic Programming (DP) Techniques

Dynamic programming is an optimization technique used when a problem has:

1. **Optimal substructure** — the overall solution can be built from optimal solutions of its subproblems.  
2. **Overlapping subproblems** — the same smaller tasks appear multiple times.

---

## 1. State Definition
For problems like “minimum number of elements to reach a sum,” a natural DP state is:

dp[s] — minimum number of operations/elements needed to obtain sum s


The state must describe the subproblem fully and allow transitions to smaller states.

---

## 2. Recurrence (Transition)

dp[s] = min(dp[s], dp[s - value] + 1)


General idea:  
> To reach sum s, try every action that leads to s − value.

---

## 3. Initialization (Base cases)
- `dp[0] = 0` — zero sum requires zero actions.  
- `dp[s] = ∞` for all other sums — initially unreachable states.

---

## 4. Computation Order
DP is computed **bottom-up**, ensuring that smaller states are processed before larger ones.

---

## 5. Key DP Patterns Used Here
- **One-dimensional DP** — array of size `amount + 1`.  
- **Minimization across actions** — choose the best option.  
- **Infinity for unreachable states** — for safe comparisons.  
- **Unbounded DP** — each value/action can be used any number of times.

---

## 6. Mathematical View
The task is equivalent to finding the **shortest path** in a graph:

- nodes = sums  
- edges = adding one value  
- edge weight = 1  

DP computes this shortest path efficiently in an iterative manner.

