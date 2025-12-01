# Name

Navigator for warehouse robots

# Task

A rectangular grid (warehouse) of size N×M is given, consisting of four types of cells:

· free cells ('.')

· obstacles ('#')

· starting position of the robot ('R')

· target position ('C')

The robot can move one cell up, down, left or right in one step, but it cannot:

· go beyond the boundaries of the grid

· go through obstacles

Required: find the minimum number of steps in which the robot can get from the starting position 'R' to the target position 'C'. If this way is not possible, return -1.

# Theory

Task: Navigator for warehouse robots

Category

**Graphs → Graph bypasses (BFS)**

Complexity

Average

Task description

You are developing a navigation system for an automated warehouse. Robot loaders move around the warehouse, which is represented as a two-dimensional grid of size `N × M`.

Symbols on the map:

- `'.'` — free passable cell

- `'#'` — obstacle (rack that cannot be bypassed)

- `'R'` — the starting position of the robot

- `'C'` — the target cell where the cargo should be delivered

The robot can move one cell **up, down, left or right** in one move. You can't go beyond the grid and move through obstacles.

Task

Write a function that determines **the minimum number of steps** required by the robot to get from the start of `'R'` to the goal of `'C'`.

**If the path is not possible, return `-1`. **

Input data format

- Two-dimensional list (matrix) of symbols `List[List[str]]`

- It is guaranteed that there is exactly one "R" and at least one "C" in the grid

Output format

- Integer - minimum number of steps or `-1`

Example 1

**Entrant:**

```python

Grid = [

['.', '.', '.', '#', '.'],

['.', '#', '.', '#', '.'],

['.', '#', '.', '.', '.'],

['R', '.', '#', '#', 'C'],

['.', '.', '.', '.', '.']

]

