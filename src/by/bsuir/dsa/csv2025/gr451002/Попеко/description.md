# Name

 Analysis of winning strategies in the game

# Task

Analysis of winning strategies in the game.

## Task condition

The classic version of the Nim game is being considered. There are n piles of stones, where each pile contains an arbitrary number of stones. Two players take turns walking. During his turn, the player chooses one of the piles and removes any number of stones from it (at least one). The player who takes the last remaining stone wins.

**Task:**For an arbitrary initial arrangement of piles, determine which of the players (going first or second) has a winning strategy, provided that both sides play optimally.

## Requirements

1. To study the theoretical foundations of the game.
2. Prove the criterion for winning a position
3. Develop an algorithm for determining a winning strategy
4. Give examples of how the algorithm works

## Solution format

- Mathematical proof of the theorem
- Algorithm for determining the winning strategy
- Examples for different configurations

# Theory

Analysis of winning strategies in the game.

## Game formulation

Consider a game with $n$ piles of stones, where each pile contains an arbitrary number of stones. Players take turns choosing one pile and removing any non-zero number of stones from it. The player who takes the last stone wins.

## The main theorem

** The theorem.** A position is a winning one if and only if the XOR sum of the pile sizes is non-zero:

$$S = a_1 \oplus a_2 \oplus \ldots \oplus a_n \neq 0$$

### Proof

**Base of induction:** In the terminal state (all piles are empty), the XOR amount is $0$, and the position is indeed a losing one.

**The induction step:**

1. ** From a losing position** ($S = 0$), all moves lead to winning positions. When any pile of $a_i$ is reduced to $b_i$ ($b_i < a_i$), the XOR amount changes:

$$S' = 0 \oplus a_i \oplus b_i = a_i \oplus b_i \neq 0$$

2. **From a winning position** ($S\neq 0$), there is a move to a losing position. Let's select the highest single bit of $S$ and find a bunch of $a_i$ in which this bit is also a single bit. Let's say:

$$b_i = S \oplus a_i$$

Then $b_i < a_i$ and the new XOR sum:

$$S' = S \oplus a_i \oplus b_i = S \oplus a_i \oplus (S \oplus a_i) = 0$$

## Optimal strategy

1. Calculate the XOR sum of all heap sizes: $S = a_1 \oplus a_2\oplus\ldots\oplus a_n$
2. If $S = 0$, the position is a losing one.
3. If $S\neq 0$:
- Find a pile of $a_i$ that has the same high unit bit as $S$
- Reduce this pile to the value of $b_i = S\oplus a_i$

## Game Modifications

### Him in the giveaway (Misère Nim)

In this version, the player who takes the last stone loses.

**Strategy:** Similar to the usual nim, but with an exception: if all the piles are $1$ in size, then the payoff is reversed.

### Nim Mura ($k$-nim)

It is allowed to reduce from $1$ to $k$ piles in one turn.

**The winning criterion:** For each bit bit, the sum of the bits modulo $(k+1)$ is calculated. The position is a losing one if all the amounts are zero.

## Application and meaning

The concept of zugzwang (a situation where any move worsens the position) is closely related to the theory of neame games. Many strategy games can be reduced to their equivalent configurations, which makes it possible to analyze their winning strategies using XOR summation.

## Examples

### Example 1
Piles: $3, 4, 5$
$$3 \oplus 4 \oplus 5 = (011)_2 \oplus (100)_2 \oplus (101)_2 = (010)_2 = 2 \neq 0$$
**Conclusion:** The first player wins.

### Example 2
Piles: $1, 2, 3$
$$1 \oplus 2 \oplus 3 = (001)_2 \oplus (010)_2 \oplus (011)_2 = (000)_2 = 0$$
**Conclusion:** The second player wins.

