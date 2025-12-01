# Name

Find Peak Element

# Task

Find Peak Element

Given an array of integers that can be represented as "mountain terrain". A **peak element** is an element that is greater than its neighbors (if they exist).

For example, in the array `[1, 2, 3, 1]`, the element `3` is a peak because it is greater than both neighbors.

**Task:** Find the index of any peak element in the array. The array may contain multiple peaks - it's sufficient to find any one of them.

**Input format:**
- First line: integer `n` (1 ≤ n ≤ 1000) - array size
- Second line: `n` integers separated by spaces - array elements

**Output format:**
- One integer - index of any peak element (0-based indexing)

**Note:**
- For edge elements, it's sufficient to be greater than the single neighbor
- It's guaranteed that at least one peak element exists

**Example 1:**
Input:
4
1 2 3 1
Output:
2

**Example 2:**
Input:
5
1 3 2 4 5
Output:
1

# Theory

## Find Peak Element

### Definition

An array element $arr[i]$ is called a **peak** if:

- For $i = 0$: $arr[0] > arr[1]$
- For $i = n-1$: $arr[n-1] > arr[n-2]$
- For $0 < i < n-1$: $arr[i] > arr[i-1]$ and $arr[i] > arr[i+1]$

### Efficient Algorithm

We can use a modified **binary search**:

1. **Initialization:**
   $$left = 0, \quad right = n-1$$

2. **Main loop:** while $left < right$
   - Calculate middle index:
     $$mid = \left\lfloor \frac{left + right}{2} \right\rfloor$$
   - If $arr[mid] > arr[mid+1]$ → peak is on the left:
     $$right = mid$$
   - Else → peak is on the right:
     $$left = mid + 1$$

3. **Result:** $left$ (or $right$) - index of peak element

### Complexity

- **Time complexity:** $O(\log n)$
- **Space complexity:** $O(1)$

