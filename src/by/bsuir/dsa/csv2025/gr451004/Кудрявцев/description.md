# Name

Advanced Nim with Constraints

# Task

Advanced Nim with Constraints

## Problem Statement
You are given `n` piles of stones, where the `i`-th pile contains `a_i` stones. Additionally, two integers `k` and `m` are given (1 ≤ `k` ≤ `n`, 2 ≤ `m` ≤ 10).

In one move, a player can perform **exactly one** of the following operations:

1. **Standard Nim Move**: Choose one pile and remove any non-zero number of stones from it.

2. **Group Move (k-nim)**: Choose between 1 and `k` piles and remove exactly one stone from each selected pile (only from non-empty piles).

3. **Split Pile (Modular Operation)**: Choose a pile with a number of stones divisible by `m`, and replace it with `m` piles, each containing a number of stones equal to the original count divided by `m`. For example, if `m = 3`, a pile of 9 stones can be replaced with three piles of 3 stones each.
The player who cannot make a move (all piles are empty or none of the operations can be performed) loses.
Determine who will win with optimal play from both players, assuming the first player moves first.

## Constraints
- 1 ≤ `n` ≤ 10
- 1 ≤ `a_i` ≤ 50 for all `i`
- Number of test cases in one file does not exceed 20

## Input Format
- First line: number of test cases
- For each test case:
  - First line: three integers `n`, `k`, `m`
  - Second line: `n` integers `a_1`, `a_2`, ..., `a_n`

## Output Format
For each test case, output one line:
- `"First"` if the first player wins
- `"Second"` if the second player wins

# Theory

Game Theory: Analyzing the Game of "Nim"

## The Classic Game of Nim

Imagine a game where several piles of stones lie before you. Players take turns removing stones from any single pile, with the option to take any number of stones in one move (but at least one). The player who takes the last stone wins.
Mathematically, this game can be described as a set of non-negative integers $a_1, a_2, \ldots, a_n$, where each number represents the count of stones in the corresponding pile. A player's move consists of decreasing one of these numbers, and the terminal position occurs when all numbers equal zero.

**Fundamental Theorem.** A position in this game is winning for the current player if and only if the bitwise XOR of all pile sizes is non-zero.

**Justification** follows the principle of mathematical induction:

1. **Base case:** When no stones remain, the XOR equals zero, and the position is losing (the player cannot make a move).

2. **Transition for zero XOR:** If the current XOR equals zero, any move will lead to a non-zero XOR. This happens because changing any $a_i$ to $b_i$ (where $b_i < a_i$) will alter the overall XOR sum.

3. **Transition for non-zero XOR:** If the current XOR ($S$) is non-zero, there always exists a move that makes the XOR zero. To achieve this, find a pile $a_i$ that has the most significant bit of $S$ set, and reduce it to the value $S \oplus a_i$.

Why is $S \oplus a_i < a_i$? Because this transformation clears the most significant bit where both $S$ and $a_i$ have ones, and changes in lower bits cannot compensate for this reduction.

**Winning strategy:** Calculate the XOR of all piles. If it's non-zero, find a pile with the most significant bit of the XOR sum set, and change its size to $S \oplus a_i$. If the XOR equals zero—you're in a losing position assuming optimal play from your opponent.

## Variations of the Game of Nim

### Reverse Nim (Misère Nim)

In this variation, the player who takes the last stone loses rather than wins.
Surprisingly, the optimal strategy is almost identical to classic Nim. The main difference appears only when all piles contain exactly one stone. In this special case, winning and losing positions are reversed compared to standard Nim.

**How to play:** Follow the standard Nim strategy (aim for zero XOR), but when your move would lead to a situation where all remaining piles contain one stone each, adjust your strategy: make a move that changes the parity of the number of non-empty piles.

**Why this works?** Eventually, the game will reach a state where all non-empty piles contain exactly one stone. If at this moment the number of such piles is even—the current player wins in Reverse Nim. Analyzing the game in reverse direction shows that when there are at least two piles with more than one stone, the strategy aligns with the standard approach.

### Extended Nim (k-nim or Moore's Nim)

In this variation, a player can remove stones from one to $k$ piles simultaneously in a single move (taking at least one stone from each selected pile).

**Solution:** Represent each pile size in binary form. For each bit position, calculate the sum of ones across all piles and take the result modulo $(k+1)$. If all positions yield zero—the position is losing; otherwise—it's winning.

**Strategy justification** parallels classic Nim but with additional complexity due to the ability to modify multiple piles simultaneously:

- From a zero position (where all bit positions yield zero modulo $k+1$), any move leads to a non-zero position.
- From a non-zero position, there's always a move to a zero position by correctly selecting piles to modify.
For the latter assertion, a step-by-step zeroing of problematic bit positions from most to least significant is used, while controlling the total number of modified piles.

## The Significance of Nim in Game Theory

The term "zugzwang" (from German words "zug"—move and "zwang"—compulsion) describes a situation where a player is forced to make a move that worsens their position. Many strategic games contain such elements.
A remarkable fact: a vast number of games based on the zugzwang principle can be reduced to the game of Nim. This allows using mathematical methods developed for Nim to analyze diverse gaming situations.

