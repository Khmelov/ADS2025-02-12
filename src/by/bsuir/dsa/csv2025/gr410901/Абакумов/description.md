# Name

Sum of Elements in Rectangular Area

# Task

Sum of Elements in Rectangular Area

## Description

This task focuses on efficiently computing the sum of elements in rectangular areas of a matrix using the **2D prefix sums** algorithm.

### Practical Applications

- **Image Processing**: calculating the sum of pixel brightness in rectangular regions for filtering and analysis
- **Computer Vision**: computing integral images for fast object detection
- **Data Analysis**: calculating sums of values in data tables for statistical analysis
- **Geographic Information Systems**: processing raster data, calculating values in specified regions

### Problem Statement

Given a matrix of size `n × m` with integer elements, answer `k` queries, each specifying a rectangular area of the matrix. For each query, find the sum of all elements in that area.

### Input Data

- First line contains two integers `n` and `m` (1 ≤ n, m ≤ 1000) — matrix dimensions
- Next `n` lines contain `m` integers each — matrix elements (from -10⁹ to 10⁹)
- Next line contains integer `k` (1 ≤ k ≤ 10⁵) — number of queries
- Each of the next `k` lines contains four integers `x1`, `y1`, `x2`, `y2` (1 ≤ x1 ≤ x2 ≤ n, 1 ≤ y1 ≤ y2 ≤ m) — coordinates of rectangular area, where (x1, y1) is top-left corner, (x2, y2) is bottom-right corner (1-based indexing, inclusive)

### Output Data

For each query, output one integer — the sum of all matrix elements in the specified rectangular area.

### Example

**Input:**
```
3 4
1 2 3 4
5 6 7 8
9 10 11 12
3
1 1 2 3
2 2 3 4
1 1 3 4
```

**Output:**
```
24
45
78
```

**Explanation:**
- Query 1: area from (1,1) to (2,3) contains elements: 1+2+3+5+6+7 = 24
- Query 2: area from (2,2) to (3,4) contains elements: 6+7+8+10+11+12 = 54
- Query 3: area from (1,1) to (3,4) contains all matrix elements: 1+2+3+4+5+6+7+8+9+10+11+12 = 78

### Solution Algorithm

The problem is solved using **2D prefix sums**:

1. **Preprocessing** (O(n×m)):
   - Build array `prefix[i][j]` containing sum of all elements from (0,0) to (i-1, j-1)
   - Construction formula: `prefix[i][j] = matrix[i-1][j-1] + prefix[i-1][j] + prefix[i][j-1] - prefix[i-1][j-1]`

2. **Answering queries** (O(1)):
   - For rectangle from (x1, y1) to (x2, y2) use formula:
   - `sum = prefix[x2+1][y2+1] - prefix[x1][y2+1] - prefix[x2+1][y1] + prefix[x1][y1]`

### Implementation Requirements

- Use 2D prefix sums algorithm
- Time complexity: O(n×m + k) — preprocessing in O(n×m), each query answer in O(1)
- Space complexity: O(n×m) — for storing matrix and prefix sums
- Handle large numbers (use `long` type)

### Algorithm Section

This task belongs to the **"Prefix Sums and Difference Arrays"** section on algorithmica.org, which covers:
- One-dimensional prefix sums
- Two-dimensional prefix sums
- Applications of prefix sums for query optimization
- Integral images

# Theory

---
title: 2D Prefix Sums
weight: 2
authors:
- Abakumov Gennadiy
created: 2025
---

**Definition.** _2D prefix sums_ of a matrix $A$ of size $n \times m$ is a matrix $P$ of size $(n+1) \times (m+1)$, defined as follows:

$$
\begin{aligned}
& P[0][j] = 0 \quad \text{for all } j \\
& P[i][0] = 0 \quad \text{for all } i \\
& P[i][j] = \sum_{x=0}^{i-1} \sum_{y=0}^{j-1} A[x][y]
\end{aligned}
$$

Note that in this indexing:

* $P[i][j]$ equals the sum of all elements of matrix $A$ in the rectangle from $(0, 0)$ to $(i-1, j-1)$ excluding boundaries,
* the size of $P$ is one greater than the size of $A$ in each dimension,
* the zero row and zero column are always zero.

The formula for $P[i][j]$ can be written recursively using the inclusion-exclusion principle:

$$
P[i][j] = A[i-1][j-1] + P[i-1][j] + P[i][j-1] - P[i-1][j-1]
$$

This gives a method to compute 2D prefix sums in $O(n \cdot m)$:

```java
long[][] prefix = new long[n + 1][m + 1];

for (int i = 0; i <= n; i++) {
    prefix[i][0] = 0;
}
for (int j = 0; j <= m; j++) {
    prefix[0][j] = 0;
}

for (int i = 1; i <= n; i++) {
    for (int j = 1; j <= m; j++) {
        prefix[i][j] = matrix[i-1][j-1] 
                     + prefix[i-1][j] 
                     + prefix[i][j-1] 
                     - prefix[i-1][j-1];
    }
}
```

