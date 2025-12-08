# Name

Range Minimum Queries with Updates

# Task

## Problem Statement (Range Minimum Queries with Updates)

### Problem: "Range Minimum Queries with Updates"

Implement a data structure that processes two types of queries on an integer array:

1. **Minimum query** - `MIN l r`
   - Find the minimum element in range `[l, r]` (inclusive)
   - 0-based indexing

2. **Update query** - `UPDATE i val`
   - Set the element at position `i` to `val`

#### Input:
- First line: integer `n` - array size (1 ≤ n ≤ 100,000)
- Second line: `n` integers separated by spaces - array elements (-10^9 ≤ arr[i] ≤ 10^9)
- Third line: integer `q` - number of queries (1 ≤ q ≤ 100,000)
- Next `q` lines: queries in format:
  - `MIN l r` - range minimum query
  - `UPDATE i val` - point update

#### Output:
- For each `MIN` query, output the minimum element in the specified range

#### Example:
```
Input:
5
1 3 2 5 4
6
MIN 0 4
MIN 1 3
UPDATE 2 0
MIN 0 4
UPDATE 0 6
MIN 0 2

Output:
1
2
0
0
```

# Theory

## Theory (Range Minimum Queries with Updates)

### Segment Tree

**Segment Tree** is a versatile data structure that enables efficient range operations on arrays. Common applications include range minimum/maximum/sum queries and point updates.

#### Key Features:
- **Structure**: Binary tree where each node stores information about a specific array segment
- **Construction**: O(n)
- **Range query**: O(log n)
- **Point update**: O(log n)

#### How it works:
1. **Construction**: Tree is built recursively from bottom to top
2. **Queries**: Range is split into O(log n) precomputed segments
3. **Updates**: Changes propagate from leaves to the root

#### Tree Structure:
- Root: entire array [0, n-1]
- Left child: [0, n/2]
- Right child: [n/2+1, n-1]
- Leaves: individual elements

