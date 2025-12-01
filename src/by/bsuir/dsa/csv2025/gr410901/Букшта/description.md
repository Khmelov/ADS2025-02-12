# Name

Problem: Coordinate Compression

# Task

Problem Statement (English)

A company collects GPS coordinates of couriers during the day.
Each coordinate is represented by integers `(x, y)`. There may be up to `10^5` coordinates per day.
To optimize storage, replace each coordinate with its **rank** among 
all unique coordinates for `x` and `y` (coordinate compression).  

**Input:**  
- First line: integer `n` — number of coordinates.  
- Next `n` lines: two integers `x_i` and `y_i`.  

**Output:**  
- `n` lines of compressed coordinates `(x'_i, y'_i)`.  
- Coordinates should be numbers from `0` to `k-1` (where `k` is the number of unique values along the axis).  

**Example:**

Input:

5
100 200
50 300
100 300
50 200
200 100


Output:

2 1
0 2
2 2
0 1
3 0

# Theory

Theory: Coordinate Compression

**Coordinate compression** is the process of replacing original values  
with their ranks in the sorted list of unique coordinates.  
For each axis, all unique values are sorted, and every value receives its index.

Given coordinates:
$$
(x_i, y_i),\quad i = 1..n
$$

After compression each coordinate becomes:
$$
(x'_i, y'_i) = (\mathrm{rankX}(x_i),\ \mathrm{rankY}(y_i))
$$

where `rankX` and `rankY` are the indices of values in the sorted arrays of unique `x` and `y`.

Time complexity:
$$
O(n \log n)
$$

