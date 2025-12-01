# Name

Coordinate Compression

# Task

Coordinate Compression

## Formal Statement
Given an array of integers, transform it into an array of indices where each number is replaced by its position in the sorted array of unique elements.

## Real-world Motivation
Coordinate compression is commonly used in data processing tasks when original values have a large range but the number of unique values is limited. This allows efficient use of data structures that require continuous index numbering.

## Input Format
First line: integer n (0 ≤ n ≤ 1000) - number of elements  
Second line: n integers separated by spaces

## Output Format
Array of n integers - compressed coordinates (0-based indexing)

## Example
Input:\
5\
5 10 2 5 2\
Output:\
1 2 0 1 0\
Explanation:  
Unique sorted elements: [2, 5, 10]  
Indices: 2→0, 5→1, 10→2  
Original array: [5, 10, 2, 5, 2] → [1, 2, 0, 1, 0]

# Theory

Coordinate Compression Algorithm

## Mathematical Formulation
For an array $a$ of length $n$:

- $b = \text{sort}(\text{unique}(a))$ - sorted array of unique elements
- For each element $a_i$ find index in $b$:
  $$ \text{index}(a_i) = \text{position}(a_i \in b) $$

## Algorithm
1. **Sorting and duplicate removal:**
   $$ b = \text{sort}(a) $$
   $$ b = \text{remove\_duplicates}(b) $$

2. **Binary search:**
   $$ \text{result}_i = \text{binary\_search}(b, a_i) $$

## Complexity
- **Time:** $O(n \log n)$ - dominated by sorting
- **Space:** $O(n)$ - storing array copy

## Visualization
Original: [5, 10, 2, 5, 2] \
Sorted: [2, 2, 5, 5, 10] \
Unique: [2, 5, 10] \
Indices: 2→0, 5→1, 10→2 \
Result: [1, 2, 0, 1, 0]


