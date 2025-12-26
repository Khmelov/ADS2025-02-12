# Name

Moore's K-Nim

# Task



# **Description of the K-Nim Game**



The game consists of several piles of stones.  

On each turn, a player may **choose up to `k` piles** and **decrease each chosen pile by any positive number of stones**.



Let:



-   `piles[i]` — the size of the i-th pile (non-negative integers),

&nbsp;   

-   `k ≥ 1` — the maximum number of piles that can be reduced in a single move.



Players move alternately.  

A player who cannot make a move loses.







## **Moore’s Theorem (Characterization of Winning Positions)**



A position is a **losing position (P-position)** if and only if for every bit position in binary representation:



`the number  of piles with a 1  in that bit  ≡ 0 (mod k+1)` 



If this condition is violated for at least one bit, the position is a **winning position (N-position)**.



----------



##  **Required Implementation**



### Function:



`boolean  isWinningKnim(int[] piles, int k)` 



which returns:



-   `true` — if the position is winning (N-position),

&nbsp;   

-   `false` — if it is losing (P-position).




### Implementation Requirements:



1.  An empty position is considered losing.



2.  `k` must be ≥ 1.



3.  All values in `piles` must be positive.



# Theory


# The Game of Nim

Consider the following game. There are `n` piles, each containing some number of stones. On each move, a player may choose one pile and remove any non-zero number of stones from it. The player who takes the last stone wins.

**Theorem.**  

A position is winning **if and only if** the xor-sum of the pile sizes is non-zero:
```
S = a1 ⊕ a2 ⊕ … ⊕ an ≠ 0
```

**Proof** proceeds by induction.

_For the terminal position_, there are no piles left, the xor-sum is zero, and it is indeed a losing position. This proves the base case — now we prove the transitions.
From any position with xor-sum equal to zero, all moves lead to winning positions, i.e. positions with non-zero xor-sum. Indeed, removing any number of stones from any pile changes the xor-sum from zero to  

`ai ⊕ bi`, where `bi < ai` is the new number of stones in pile `i` after the move.
_The opposite direction is more complex._ We need to show that if the xor-sum is non-zero, then there always exists a number `bi < ai` such that after changing one pile, the xor-sum becomes zero, i.e.
```
S ⊕ ai ⊕ bi = 0
```

To construct such a move, consider the highest set bit of `S` and choose any pile `ai` that also has this bit set. Such `ai` must exist — by the properties of xor, the number of such piles is odd. From the equation above it follows that the required `bi` must be equal to `S ⊕ ai`.

It turns out that this is a valid new pile size, i.e. `bi < ai`. Why? Because all higher bits remain unchanged, the `k`-th bit flips to zero, and what happens to the lower bits does not matter — the number we subtract is guaranteed to be less than `2^k`.

Thus the optimal strategy is: compute the xor-sum `S` of all `ai`, choose a pile `ai` whose highest set bit matches the highest set bit of `S`, and replace it with `S ⊕ ai`.  
(And if the xor-sum `S` is zero, surrender.)


## Moore’s Nim (k-Nim)

**Rules.**  
There are `n` piles of stones with sizes `ai`. A natural number `k` is given. On each move, a player may decrease the sizes of **one to `k` piles** simultaneously (i.e. multiple piles may be changed in a single move). A player loses if they cannot move.

Obviously, when `k = 1`, Moore’s Nim becomes ordinary Nim.

**Solution.**  

Write the size of each pile in binary. Then sum the bits column-wise and take each sum modulo `(k+1)`. If all resulting digits are zero, the position is losing; otherwise, it is winning.

**Proof**, as in ordinary Nim, relies on describing the players’ strategies — we must show that:

1.  from a position with zero value, one can move only to positions with non-zero value, and


2.  from any position with non-zero value, there exists a move to a position with zero value.

The first statement is proved as follows. If all bit-sums modulo `(k+1)` are zero, then after changing from one to `k` elements, obtaining all zeros again requires “balancing” all changes: for every element where a bit is removed, there must be another element where the same bit is added. In particular, this must hold for the highest bit in which a change occurs. But then there must exist an element in which a bit is added in this highest position, while all more significant bits remain unchanged — meaning this element becomes larger than it originally was, contradicting the rules.

Now we show how to move from positions with non-zero sums to positions with zero sums. Call a bit position _problematic_ if its bit-sum modulo `(k+1)` is non-zero. We iterate over the bits from most significant to least significant and fix each problematic bit by decreasing some number of piles.

Let `u` be the number of piles we have already decided to modify; initially, `u = 0`. Since in these `u` piles we already decreased some higher bit, all lower bits may be assigned arbitrarily.

Suppose we consider a bit whose sum modulo `(k+1)` is non-zero. Ideally, we would like to fix this bit using only the already chosen `u` piles. Mentally set the corresponding bit to `1` in all of these `u` piles and recompute the sum; call it `s`. Now consider two cases:

-   If `s ≤ u`, then we can use only the selected piles, clearing the bit in exactly `s` of them.


-   Otherwise (`s > u`), find `(s − u)` additional piles that have a `1` in this bit and decrease them as well.


After each iteration, the number of modified piles becomes
```
u' = max(s, u) ≤ k
```

Therefore, we have shown how to select the set of modified piles and determine which bits to change such that the total number `u` never exceeds `k`. Hence a transition from any position with non-zero value to a zero-value position always exists — which completes the proof.

# The Game of Nim

Consider the following game. There are `n` piles, each containing some number of stones. On each move, a player may choose one pile and remove any non-zero number of stones from it. The player who takes the last stone wins.

**Theorem.**  

A position is winning **if and only if** the xor-sum of the pile sizes is non-zero:

```
S = a1 ⊕ a2 ⊕ … ⊕ an ≠ 0
```

**Proof** proceeds by induction.

_For the terminal position_, there are no piles left, the xor-sum is zero, and it is indeed a losing position. This proves the base case — now we prove the transitions.

From any position with xor-sum equal to zero, all moves lead to winning positions, i.e. positions with non-zero xor-sum. Indeed, removing any number of stones from any pile changes the xor-sum from zero to  
`ai ⊕ bi`, where `bi < ai` is the new number of stones in pile `i` after the move.

_The opposite direction is more complex._ We need to show that if the xor-sum is non-zero, then there always exists a number `bi < ai` such that after changing one pile, the xor-sum becomes zero, i.e.
```
S ⊕ ai ⊕ bi = 0
```
To construct such a move, consider the highest set bit of `S` and choose any pile `ai` that also has this bit set. Such `ai` must exist — by the properties of xor, the number of such piles is odd. From the equation above it follows that the required `bi` must be equal to `S ⊕ ai`.

It turns out that this is a valid new pile size, i.e. `bi < ai`. Why? Because all higher bits remain unchanged, the `k`-th bit flips to zero, and what happens to the lower bits does not matter — the number we subtract is guaranteed to be less than `2^k`.

Thus the optimal strategy is: compute the xor-sum `S` of all `ai`, choose a pile `ai` whose highest set bit matches the highest set bit of `S`, and replace it with `S ⊕ ai`.  

(And if the xor-sum `S` is zero, surrender.)

## Moore’s Nim (k-Nim)



**Rules.**  

There are `n` piles of stones with sizes `ai`. A natural number `k` is given. On each move, a player may decrease the sizes of **one to `k` piles** simultaneously (i.e. multiple piles may be changed in a single move). A player loses if they cannot move.



Obviously, when `k = 1`, Moore’s Nim becomes ordinary Nim.



**Solution.**  

Write the size of each pile in binary. Then sum the bits column-wise and take each sum modulo `(k+1)`. If all resulting digits are zero, the position is losing; otherwise, it is winning.



**Proof**, as in ordinary Nim, relies on describing the players’ strategies — we must show that:



1.  from a position with zero value, one can move only to positions with non-zero value, and

&nbsp;   

2.  from any position with non-zero value, there exists a move to a position with zero value.

 

The first statement is proved as follows. If all bit-sums modulo `(k+1)` are zero, then after changing from one to `k` elements, obtaining all zeros again requires “balancing” all changes: for every element where a bit is removed, there must be another element where the same bit is added. In particular, this must hold for the highest bit in which a change occurs. But then there must exist an element in which a bit is added in this highest position, while all more significant bits remain unchanged — meaning this element becomes larger than it originally was, contradicting the rules.

Now we show how to move from positions with non-zero sums to positions with zero sums. Call a bit position _problematic_ if its bit-sum modulo `(k+1)` is non-zero. We iterate over the bits from most significant to least significant and fix each problematic bit by decreasing some number of piles.

Let `u` be the number of piles we have already decided to modify; initially, `u = 0`. Since in these `u` piles we already decreased some higher bit, all lower bits may be assigned arbitrarily.

Suppose we consider a bit whose sum modulo `(k+1)` is non-zero. Ideally, we would like to fix this bit using only the already chosen `u` piles. Mentally set the corresponding bit to `1` in all of these `u` piles and recompute the sum; call it `s`. Now consider two cases:

-   If `s ≤ u`, then we can use only the selected piles, clearing the bit in exactly `s` of them.


-   Otherwise (`s > u`), find `(s − u)` additional piles that have a `1` in this bit and decrease them as well.

After each iteration, the number of modified piles becomes



```
u' = max(s, u) ≤ k

```


Therefore, we have shown how to select the set of modified piles and determine which bits to change such that the total number `u` never exceeds `k`. Hence a transition from any position with non-zero value to a zero-value position always exists — which completes the proof.











