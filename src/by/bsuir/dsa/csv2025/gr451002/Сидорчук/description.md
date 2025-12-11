# Name

Matrix Spiral Transformation

# Task

Problem Statement

Given the dimension `n` of a square matrix and the matrix itself of size `n × n`, perform the following operations:
1. Rotate the matrix 90 degrees clockwise
2. Traverse the rotated matrix in a clockwise spiral pattern starting from the top-left corner
3. Return the resulting array where elements are arranged **in the exact order they were visited during the spiral traversal**

## Input Format

- First line: integer `n` (matrix dimension)
- Next `n` lines: `n` integers each (matrix elements) separated by spaces

## Output Format 

-Single line with `n²` integers separated by spaces - elements in spiral traversal order

# Theory

Matrices: Rotation and Spiral Traversal

## Definition

A **matrix** is a rectangular array of numbers arranged in rows and columns. In the context of this problem, we work with **square matrices** of size $n \times n$.

## Basic Operations

### Matrix Rotation

Rotating a matrix 90° clockwise is a linear transformation that can be expressed through indices:

$$
\text{rotated}[j][n-1-i] = \text{matrix}[i][j]
$$

**Mathematical interpretation:** This rotation is equivalent to sequentially applying two operations:
1. Matrix transposition: $A^T$
2. Reflection about the vertical axis

### Spiral Traversal

Spiral traversal of a matrix is a method of traversing elements through concentric "layers" from the boundary to the center.

**Algorithmic approach:** We use four boundary pointers:
- `top`, `bottom` - row boundaries
- `left`, `right` - column boundaries

Traversal is performed in four stages per iteration:
1. Left to right along the top row
2. Top to bottom along the right column
3. Right to left along the bottom row
4. Bottom to top along the left column

After each complete traversal, the boundaries shrink toward the center.

## Example

**Original matrix:**
[1, 2, 3]
[4, 5, 6]
[7, 8, 9]

**After 90° clockwise rotation:**
[7, 4, 1]
[8, 5, 2]
[9, 6, 3]

