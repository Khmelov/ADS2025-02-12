# Name

Knapsack

# Task

Knapsack
You are given a knapsack with a capacity W and a list of n items. Each i-th item has a weight weight[i] and a value value[i].
Your task is to determine the maximum total value of items that can be put into the knapsack without exceeding its capacity, given that you cannot take more than one instance of each item (the "0-1" constraint).
Input format:
First line: integer W (knapsack capacity).
Second line: integer n (number of items).
Next n lines: two integers separated by a space — weight[i] and value[i] respectively.
Output format:
One integer — the maximum total value.

# Theory

Knapsack Problem
The Knapsack problem is a classic problem in combinatorial optimization and is perfectly solved using Dynamic Programming.
Solution Idea
We create a 2D array dp where:
dp[i][w] represents the maximum value that can be achieved using the first i items and a knapsack of capacity w.
Recurrence Relations
Base case:
If no items or zero capacity: dp[0][w] = 0 and dp[i][0] = 0.
Transition:
If the weight of the current item weight[i-1] is greater than the current capacity w, we cannot take it:
dp[i][w] = dp[i-1][w]
Otherwise, we choose the maximum between taking and not taking the item:
dp[i][w] = max(dp[i-1][w], dp[i-1][w - weight[i-1]] + value[i-1])
Complexity
Time: O(n * W), where n is the number of items, W is the knapsack capacity.
Space: O(n * W), which can be optimized to O(W).

