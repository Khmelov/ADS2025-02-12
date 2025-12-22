# Name

Analysis-of-Winning-Strategies-in-Pebbles-Game

# Task

Analysis of winning strategies in the game "Pebbles"

## Task condition

We consider a simplified version of the game.  
There is **one pile** containing `n` stones. Two players take turns making moves.  
In one turn, a player can remove **1, 2, or 3 stones** from the pile.  
The player who takes the last stone wins.

**Task:**  
For a given initial number of `n` stones, determine:
1. Whether the position is **winning** or **losing** for the current player (assuming optimal play).
2. If the position is winning, find the **optimal move** (the number of stones to take) that guarantees victory.

Your program should:
- Read the initial number of stones `n` from input.
- Output:
    - The number `n` itself
    - Whether the position is winning (`true`/`false`)
    - The optimal move (1–3), or `-1` if no winning move exists

---

## Requirements

1. Study the theoretical foundations of the game "Pebbles".
2. Prove the criterion for winning a position:
    - A position is **losing** if `n` is divisible by 4.
    - Otherwise, it is **winning**.
3. Develop an algorithm that:
    - Checks if `n % 4 == 0` → losing position.
    - Otherwise, returns `n % 4` as the winning move.
4. Provide examples of how the algorithm works (test cases).

---

## Solution format

- Mathematical proof of the criterion
- Algorithm in pseudocode or Java implementation
- Examples of program output for different values of `n`

---

## Examples

| n   | Winning? | Optimal move |
|-----|----------|--------------|
| 1   | true     | 1            |
| 2   | true     | 2            |
| 3   | true     | 3            |
| 4   | false    | -1           |
| 5   | true     | 1            |
| 6   | true     | 2            |
| 7   | true     | 3            |
| 8   | false    | -1           |
| 9   | true     | 1            |
| 12  | false    | -1           |


# Theory

---
author: Nikolay Kureychik
weight: 1
---

# Analysis of winning strategies in the game "Pebbles"

## Task condition

Consider the following game. Given **one** pile containing `n` stones. Two players take turns making moves. In one turn, the player can remove 1, 2 or 3 stones from the pile **. The player who takes the last stone wins.

## Theoretical analysis

### Basic concepts of combinatorial game theory

Let's introduce a formal classification of positions:

- **P-position** (losing position) - a position from which there is no winning strategy with optimal opponent play
- **N-position** (winning position) - a position from which there is a winning strategy

**Fundamental properties of P- and N-positions:**
1. The terminal position (0 stones) is a P-position
2. There is at least one move from the N-position to the P-position.
3. From the P-position, all moves lead to the N-position

### Formal statement of the problem

Define the winning function `W: ℕ → {True, False}`:
- `W(n) = False` if a position with n stones is a losing one (P-position)
- `W(n) = True` if a position with n stones is a winning one (N-position)

Recurrence relation for arbitrary n ≥ 1:
W(n) = W(n-1) ∨ W(n-2) ∨ W(n-3)

## Conclusion

The game "Pebbles" is a canonical example of a deterministic game with complete information, demonstrating the fundamental principles of combinatorial game theory. The simplicity of characterization of winning positions makes it an ideal object for studying methods of combinatorial game analysis, including the invariant method, symmetric strategies, and the theory of P- and N-positions.

The results obtained are of practical importance in the development of game algorithms, artificial intelligence and the theory of computational complexity of games.

