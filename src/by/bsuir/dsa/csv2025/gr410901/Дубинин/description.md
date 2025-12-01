# Name

Arrangement of Pairs

# Task

Arrangement of Pairs

You are given a 0-indexed 2D integer array pairs where `pairs[i] = [start_i, end_i]`. An arrangement of pairs is valid if for every index `i` where `1 <= i < pairs.length`, we have `end_{i-1} == start_i`.

Return valid arrangement of pairs. If multiple valid arrangements exist, the algorithm must return the arrangement that is **lexicographically smallest**, i.e. the starting pair must have the **smallest possible first element**. If first elements are equal - the smallest second element. Each subsequent pair must minimize the lexicographical order of the entire sequence.

**Note:** The inputs will be generated such that there exists a valid arrangement of pairs.

## Input Format

The input is read from the console in the following format:

- The first line contains an integer `n` - the number of pairs
- The next `n` lines each contain two integers separated by a space, representing `start_i` and `end_i`

### Constraints

- 1 <= n <= 10^5
- 0 <= start_i, end_i <= 10^9
- The inputs guarantee that a valid arrangement exists

## Output Format

Output the valid arrangement to the console in the following format:

- Output `n` lines
- Each line should contain two integers separated by a space, representing `start_i` and `end_i` of the arranged pairs
- The arrangement must satisfy the condition: for every i from 1 to n-1, the end of the (i-1)-th pair equals the start of the i-th pair

## Examples

### Example 1:

**Input:**
5
5 1
4 5
1 3
3 2
2 4

**Output:**
1 3
3 2
2 4
4 5
5 1

### Example 2:

**Input:**
3
1 2
2 3
3 4

**Output:**
1 2
2 3
3 4

# Theory

Arrangement of Pairs

This problem reduces to finding the **lexicographically smallest Eulerian path** in a directed graph:

- **Graph vertices** - integers from the pairs
- **Edges** - directed connections `start_i → end_i`
- **Connection condition** `end_{i-1} = start_i` means edges must form a continuous path

## Problem Complexity:
- **Algorithmic**: Finding Eulerian path with lexicographical constraints
- **Time Complexity**: O(n log n) due to edge sorting
- **Space Complexity**: O(n)

## Key Insights:
1. **Graph Type**:
   - Eulerian cycle (all vertices balanced: `out_degree = in_degree`)
   - Eulerian path (one vertex with `out_degree = in_degree + 1`, another with `in_degree = out_degree + 1`)

2. **Lexicographical Constraints**:
   - Starting vertex must be the **smallest possible**
   - Always choose the **smallest neighbor** during traversal
   - Guarantee **deterministic output**

## Modified Hierholzer's Algorithm:
1. **Build graph** with PriorityQueue for automatic neighbor sorting
2. **Find starting vertex** by priority:
   - Vertices with `out_degree = in_degree + 1` (path start)
   - Smallest vertex with outgoing edges (cycle)
3. **DFS with lexicographical order** - always poll smallest neighbor
4. **Reverse path** and construct result

## Common Pitfalls:
- Incorrect starting vertex selection
- Violating lexicographical order during traversal
- Forgetting to handle Eulerian cycle case

