# Name

Misère Nim

# Task

---
title: Misère Nim
weight: 3
authors:
  - You
created: 2025-11-24
---
# Misère Nim

n piles with aᵢ stones each.  
A move: remove any positive number of stones from one pile.  
The player who takes the last stone loses.

Determine the winner with optimal play.

Input: t (≤10⁵) tests, each with n (≤2·10⁵ total) and a₁…aₙ (0≤aᵢ≤10⁹).

Output: First or Second for each test.

## Short theory

Let S = a₁ ⊕ … ⊕ aₙ, mx = max(aᵢ).

First player wins iff  
• S ≠ 0 and mx > 1, or  
• mx ≤ 1 and the number of piles of size 1 is odd.

Equivalently: same as normal Nim, but flip the winner when every pile has size ≤ 1.

# Theory

---
title: Misère Nim: winning move
weight: 4
authors:
  - You
created: 2025-11-24
---
# Misère Nim: winning move

Same rules as Misère Nim.

If the position is winning, output the index (1-based) of a pile and the new number of stones in it (strictly smaller).  
Any valid winning move is accepted.  
If the position is losing, output -1.

Constraints are the same as in the basic problem.

## Short theory

- If max(aᵢ) > 1 → play as in normal Nim: change some aᵢ to aᵢ ⊕ S.  
- If all aᵢ ≤ 1 and the position is winning (odd number of 1-s) → remove one stone from any pile of size 1.

Special case: never make a move that leaves exactly one stone if the opponent would then lose in normal Nim (i.e. avoid leaving an odd number of 1-s when it would be losing in misère).

