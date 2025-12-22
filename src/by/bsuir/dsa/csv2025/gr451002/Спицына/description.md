# Name

Knights&Coins

# Task

Knight & Coins

## Problem Statement

**Given an 8×8 chessboard with a knight placed on it. There are 8 coins scattered across the board. The goal is to find the minimum number of moves the knight needs to collect any k coins.**

### Input

* **Initial position of the knight (x, y)**
* **Positions of 8 coins (x, y)**
* **Number of coins that need to be collected**

### Input Constraints
1. **All coordinates are in the range [0,7].**
2. **Initially, the knight and a coin cannot occupy the same square.**
3. **The number of coins to be collected is in the range [0,8].**

### Example input
5 3  1 2  2 2  4 5  6 5  1 7  7 5  7 7  3 4  3   

(5,3) - knight’s position   \
(1,2) (2,2) (4,5) (6,5) (1,7) (7,5) (7,7) (3,4) - coins’ positions \
3 - number of coins to collect

### Output example 
4

4 - minimum number of knight moves required to collect 3 coins

## Notes and Clarifications
1. **When solving the problem, it is necessary to ensure that identical states are not revisited. A knight’s state is defined by its coordinates on the board and the set of coins already collected. Therefore, the knight can enter the same square only if the set of collected coins differs from the previous one.** 

2. **The knight moves according to the standard chess rules. Once the knight lands on a square with a coin, that coin is considered collected and “disappears” from the board.**

3. **The problem should be solved using a breadth-first search (BFS) algorithm.**

# Theory

Brief Theory

Breadth-First Search (BFS) is one of the basic graph algorithms that allows finding the shortest paths from a given vertex. It guarantees that the solution will be found with the minimum number of steps if all transitions have the same “cost”.

In this task, we need to determine the minimum number of knight moves required to collect k coins.
Each state of the knight (its coordinates, the set of collected coins, and the number of steps taken to reach this state) can be considered as a vertex of a graph, and each knight move corresponds to an edge.

## Main Ideas of Breadth-First Search
### 1. Use of a Queue
The algorithm places the initial state into a queue. Then it removes the first state from the queue and generates all possible next states that can be reached with one knight move. These new states are added to the end of the queue.

### 2. Layer-by-Layer Processing
First, all vertices at distance 1 from the start (1 knight move) are visited, then all vertices at distance 2 (2 moves), then 3, and so on.

Therefore, the first time we reach a state where “k coins are collected,” the number of moves is guaranteed to be minimal.

## State Transitions
From one state, the algorithm can generate up to 8 new states, since a knight can move in up to eight different ways from a single square. If a move leads to a square containing a coin, the coin is considered collected and added to the set of collected coins.

## End of the Algorithm
The algorithm stops when it first reaches a state where the number of collected coins is equal to k.

