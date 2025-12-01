# Name

N-Queens

# Task

N-Queens

## Task

The n-queens puzzle is the problem of placing n queens on an **n x n** chessboard such that no two queens attack each other.

Given an integer **n**, return the *number* of distinct solutions to the n-queens puzzle.


> **Remarks:**
> - A queen attacks any square on the same row, column, or diagonal.

## Example 1

-   Input:
    `4`
-   Expected output:
    `2`
-   Explanation: There are two distinct solutions to the 4-queens puzzle as shown.

## Example 2

-   Input:
    `1`
-   Expected output:
    `1`

## Constraints

-   `1 <= n <= 9`


# Theory


# Theoretical Info

## Backtracking

### Definition

**Backtracking** is a general algorithmic technique for solving problems that systematically searches for all possible candidates for a solution. It is used for problems that require finding *all* (or *some*) solutions, particularly for constraint satisfaction problems.


The main idea is to build a solution step-by-step (incrementally). At each step, we check if the current partial candidate can be extended to a complete solution.

1.  If the candidate can be extended, we take the next step (recursive call).
2.  If at some step we find that the current candidate cannot lead to a valid solution (it violates constraints), we **"backtrack"** one step and try another option.
3.  If the candidate is a complete and valid solution (the base case of recursion is reached), we count it.

### Application to the N-Queens Problem

In the N-Queens problem, we try to place one queen in each row, starting from `row = 0`.

-   **Step:** Try to place a queen in `row` (current row).
-   **Candidates:** Iterate through all columns `j` from `0` to `n-1` for this row.
-   **Constraint Check:** Before placing a queen at `(row, j)`, we must check if it is "safe". That is, no previously placed queen (in rows `0`...`row-1`) should attack this cell.

### Safety Check Optimization

For a fast safety check (instead of re-scanning all previously placed queens every time), we can use auxiliary arrays to track occupied lines:

-   `col[n]`: Stores information about occupied **columns**.
    -   `col[j] == 1` if column `j` is occupied.
-   `topRight[2*n-1]`: Stores information about occupied **anti-diagonals**.
    -   For a cell `(i, j)`, the diagonal index is `i+j`.
-   `topLeft[2*n-1]`: Stores information about occupied **main diagonals**.
    -   For a cell `(i, j)`, the diagonal index is `i-j`.
    -   To avoid negative indices, an offset is used: `i-j + n-1`.

In the recursive call `help(i, ...)` (where `i` is the row number):

1.  We iterate through columns `j` from `0` to `n-1`.
2.  **Check** if the cell is free:
    `if ((col[j] == 0) && (topLeft[i-j+n-1] == 0) && (topRight[i+j] == 0))`
3.  If yes:
    -   "Place" the queen (mark all three arrays as `1`).
    -   Recursively call `help(i+1, ...)` for the next row.
    -   Add the result to `count`.
    -   "Remove" the queen (**backtrack**): mark all three arrays back to `0` to try the next column `j`.
4.  **Base Case:** If `i == n` (we have successfully placed queens in all `n` rows), we have found one valid solution and return `1`.


