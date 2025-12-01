# Name

Problem: Optimal arrangement of modules in the device firmware

# Task

Problem Statement

You are developing firmware for an embedded device that has a strict memory limit of `target` kilobytes.
You are given a set of independent firmware modules, each having a fixed size specified in the array `nums`.
You must choose a subset of these modules such that their total size is as close as possible
to the memory limit without exceeding it. Return the total size of the selected subset.
If multiple subsets provide the same closeness to the target, return the largest sum.
(Only positive numbers — module sizes — are provided as input)

**Input:**

* The first number `n` — the number of available modules
* Array `nums` of `n` elements (1 ≤ n ≤ 40, nums[i] > 0)
* Memory limit `target` (0 ≤ target ≤ 10^9)

**Output:**

* A single integer — the total size of the subset closest to `target` without exceeding it.

**Example:**

Input:

n = 5, nums = [200, 300, 700, 800, 1000], target = 1400

Output:

1300

Explanation: Subset `[300, 1000]` or `[200, 300, 800]` gives a total of 1300.


# Theory

Theory: Meet-in-the-Middle Method for the Subset Sum Problem

## The Brute-Force Problem

The total number of subsets is $2^n$, making a brute-force search inefficient for $n > 40$.

---

## The Meet-in-the-Middle Idea

Split the original set into two parts:
$$
A = \{a_1,\ldots,a_k\}, \quad B = \{a_{k+1},\ldots,a_n\}
$$
where typically $k = \lfloor n/2 \rfloor$.

For each part, independently compute the sets of all possible sums:
$$
S_A = \left\{ \sum_{i\in I} a_i \mid I \subseteq A \right\}, \quad
S_B = \left\{ \sum_{j\in J} a_j \mid J \subseteq B \right\}
$$

Each set contains approximately $2^{n/2}$ elements.

---

## Finding the Optimal Solution

For each $x \in S_A$, find the maximum $y \in S_B$ such that:
$$
x + y \le T
$$

After sorting $S_B$, the search is performed using binary search in $O(\log |S_B|)$ time.

---

## Complexity

- Generating sums: $O(2^{n/2})$
- Searching for pairs: $O(2^{n/2} \log 2^{n/2}) = O(n \cdot 2^{n/2})$

Final complexity: $O(n \cdot 2^{n/2})$, which is significantly better than $O(2^n)$.


