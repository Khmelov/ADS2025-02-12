# Name

Maximum Number of Completed Suits in Spider Solitaire After One Move

# Task

Consider simplified 1-suit Spider Solitaire (cards 1–13), starting with 10 piles: first 4 with 6 cards, others with 5 (total 54 cards).

**Rules**:
- A move: transfer any **contiguous decreasing sequence** from top of one pile to another, if top card of destination is +1 greater.
- After move: auto-remove all completed suits `13,12,…,1` located **at the top (beginning)** of any pile.

**Task**:  
Find the **maximum number of completed suits** `13..1` appearing at pile tops after **exactly one move** (including initial state if it already has suits).

## Input  
10 lines:  
```
k c₁ c₂ … cₖ
```  
`k` = pile size (0 ≤ k ≤ 6), `cᵢ` = card (1–13), **top to bottom**.

## Output  
One integer — max number of completed suits.

## Example 1  
Input:
```
13 13 12 11 10 9 8 7 6 5 4 3 2 1
0
0
0
0
0
0
0
0
0
```
Output:
```
1
```

## Example 2  
Input:
```
13 13 12 11 10 9 8 7 6 5 4 3 2 1
13 13 12 11 10 9 8 7 6 5 4 3 2 1
0
0
0
0
0
0
0
0
0
0
```
Output:
```
2
```

# Theory

Spider Solitaire is a **single-player impartial combinatorial game**. A state is defined by an ordered list of piles.

Define the **state invariant**:  
> A *completed suit* is a decreasing sequence `13,12,…,1` located at the **top of a pile**.

Formally, pile $p = [p_0, \dots, p_{\ell-1}]$ contains a suit iff  
$$
\forall i \in [0, 12] \colon p_i = 13 - i.
$$

Let $c(P)$ be the number of suits in state $P$.  
Task reduces to:
$$
\max_{P' \in \mathrm{Next}(P_0)} c(P'),
$$
where $\mathrm{Next}(P_0)$ — states reachable from initial $P_0$ in **one move**.

A move is valid if there exist:
- non-empty pile $i$ with decreasing prefix of length $L$:  
  $$
  \forall j \in [0, L-2] \colon p^{(i)}_j = p^{(i)}_{j+1} + 1,
  $$
- pile $j \ne i$ such that $p^{(j)}_0 = p^{(i)}_{L-1} + 1$ or $p^{(j)} = \varnothing$.

Algorithm enumerates $O(n^2 \cdot m^2)$ moves ($n = 10$, $m = 6$) and checks $c(P')$ in $O(n \cdot 13)$.

See:  
- [Combinatorial Game Theory](https://en.algorithmica.org/cs/games/)

