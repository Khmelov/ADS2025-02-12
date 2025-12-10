# Name

Maximum Height Difference with Distance Constraint

# Task

Maximum Height Difference with Distance Constraint

A PE teacher lined up all students in a row. Each student's height is recorded in the `heights` array. The teacher wants to select two students such that the difference in their heights is maximized, but the students must be at least `k` apart in the row (there must be at least `k-1` other students between them).

# Theory

Merge Sort

## Algorithm
Merge sort uses the "divide and conquer" strategy:
1. **Divide**: The array is recursively divided into two halves until subarrays of size 1 remain
2. **Conquer**: Sorted subarrays are merged into one sorted array

## How It Works
- Recursively split the array into left and right parts
- Sort each part separately
- Merge sorted parts by comparing elements and placing the smaller one in the result array

## Complexity
- **Time**: O(n log n) in all cases (worst, average, best)
- **Space**: O(n) - additional memory required for temporary arrays

## Advantages
- Stable sort (preserves order of equal elements)
- Predictable performance
- Efficient for large arrays

## Disadvantages
- Requires additional memory
- Not adaptive (doesn't speed up on partially sorted arrays)

