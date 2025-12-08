# Name

Cell Tower Placement Optimization

# Task

Optimal Cell Tower Placement

## Problem Statement

**Given:**
- Two-dimensional array `cityMap[M][N]`, where `cityMap[i][j]` is the population density at coordinates `(i,j)`
- Integer `numTowers` is the number of towers to place
- Integer `coverageRadius` is the coverage radius of each tower

**Required:**

Place a given number of towers on the city map to maximize the number of covered users. Each tower covers all cells within a given radius of its location. A user located within the coverage area of ​​several towers is counted only once.

**Required input:**

- First row: `M` `N` - map dimensions (number of rows and columns)
- Next `M` rows: `N` integers each - population density in the corresponding map cells
- Last row: `numTowers` `coverageRadius` - number of towers to place and coverage radius

**Required output:**
- First row: coordinates of the placed towers in the order they were placed, separated by spaces (format: x1 y1 x2 y2 ...)
- Second row: total number of users reached

***Note***: If several positions provide the same increase, select the position with the lower coordinates (the smaller row first; if the rows are equal, the smaller column).This might be useful for you: Chebyshev distance: a tower at position (i,j) covers all cells (x,y) such that max(|x-i|, |y-j|) <= coverageRadius

##### Simple Input
3 3

10 20 30

40 50 60

70 80 90

2 1
##### Simple Output
1 1 0 0

450

# Theory

<center> Greedy Algorithm </center>

Greedy algorithms are a class of algorithms that make locally optimal decisions at each stage. Since locally optimal solutions are much easier to compute than globally optimal ones, such algorithms typically have good asymptotic behavior.

In some cases, greedy algorithms lead to optimal final solutions, while in others they do not. Designing and, especially, proving the correctness of greedy algorithms is often very difficult.

## Example of a problem using the greedy algorithm

### Backpack with Divisible Items

**Problem.** Suppose we have a backpack with a capacity of no more than $W$ grams and $n$ items weighing $w_i$ grams and costing $c_i$ per gram. We can cut off an integer number of grams from any item. The goal is to fill the backpack with the maximum possible value.

Let's sort the items by descending "value density" $\frac{c_i}{w_i}$ and greedily take them in that order. We'll take a portion of the last item, which won't fit completely.

**Correctness.** Mentally divide all the items into $w_i$ 1-gram pieces, so that their value becomes $\frac{c_i}{w_i}$. Clearly, from pieces of the same weight of 1 gram, it's always optimal to simply take the pieces with the highest value, which is what we actually do in our algorithm.

The final asymptotics are $O(n \log n)$ for sorting.


