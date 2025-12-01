# Name

Nim Game

# Task

Analysis of Winning Strategies in Nim Game

## Problem Statement

Consider the classical Nim game. There are n piles, each containing some number of stones. Two players take turns making moves. In one move, a player can choose one pile and remove any non-zero number of stones from it. The player who takes the last stone wins.

Task: For a given pile configuration, determine which player has a winning strategy when both players play optimally.

## Requirements

1. Study the theoretical foundations of the Nim game
2. Prove the criterion for a winning position
3. Develop an algorithm for determining the winning strategy
4. Provide examples of the algorithm's operation

## Solution Format

- Mathematical proof of the theorem
- Algorithm for determining the winning strategy
- Examples for various pile configurations

# Theory

author : Osadchy Kirill


weight : 1
 

Consider the following game. There are $n$ piles, each containing some number of stones. In one move, a player can choose one pile and remove any non-zero number of stones from it. The player who takes the last stone wins.


Let's reformulate the condition slightly. The game state is uniquely described by an unordered set of non-negative numbers — let's number them and denote the number of stones in the $i$-th pile as $a_i$. Now, in one move, it is allowed to strictly decrease any of these numbers. The terminal state is when all numbers become zero.

Theorem. A game state is winning if and only if the XOR-sum of the pile sizes is non-zero:

$$
S = a_1 \oplus a_2 \oplus \ldots \oplus a_n \ne 0
$$

Proof We will prove by induction.

For the terminal state, there are no piles left, and the XOR-sum is zero, and it is indeed a losing state. The base case is proven — now let's prove the transitions.

From a state with zero XOR-sum, all transitions lead to winning states, that is, states with non-zero sum. Indeed, it is sufficient to remove any number of stones from any pile — the XOR-sum will change from zero to $a_i \oplus b_i$, where $b_i < a_i$ is the number of stones in the $i$-th pile after our move.

The opposite case is more complicated. We need to show that if the XOR-sum is non-zero, then there always exists $b_i < a_i$ such that the XOR-sum becomes zero, that is

$$
S \oplus a_i \oplus b_i = 0
$$

To do this, let's look at the highest set bit of $S$ and take any $a_i$ that also has this bit set. Such an $a_i$ will be found at least one — by the properties of xor, there must be an odd number of them. From the condition above, it follows that the desired $b_i$ must be equal to $S \oplus a_i$.

It turns out that this is a valid new pile size, that is $b_i < a_i$. Why is that? Because all higher bits in the expression remained unchanged, the $k$-th bit changed to one, and what happened with the further bits doesn't matter to us, because these changes are certainly no more than $2^k$.

So, the optimal strategy is: calculate the XOR-sum of all $a_i$, find an $a_i$ whose highest set bit is raised, and replace it with $S \oplus a_i$. (And if the XOR-sum $S$ turns out to be zero, then surrender.)

## Modifications

### Misère Nim

In contrast to ordinary Nim, there also exists "misère Nim": when the player who makes the last move does not win, but loses.

The solution to such Nim is surprisingly simple. The winningness of a position is determined in the same way as in ordinary Nim — by the XOR-sum of the pile sizes — but with one exception: if the sizes of all piles are equal to one, then winningness and losingness are swapped.

Taking this exception into account, we make moves as in ordinary Nim, transitioning to a position with zero XOR-sum, but if such a move leads to a position where the sizes of all piles are equal to one, then this move must be changed so that the number of remaining non-empty piles changes its parity.

Proof. Consider some course of the game: choose an arbitrary starting position and write down the players' moves until the game ends. In any game of two optimal players, sooner or later there comes a moment when the sizes of all non-empty piles are equal to one. Denote by $k$ the number of non-empty piles at this moment — then for the current player this position is winning if and only if $k$ is even.

Now let's go back one move. We find ourselves in a position where exactly one pile has size $a_i > 1$, and all other piles (possibly there were zero of them) have size $1$. This position is winning, since we can always make a move after which an odd number of piles of size $1$ remain:

- If the total number of non-empty piles is odd, then replace $a_i$ with $1$.
- Otherwise, replace $a_i$ with $0$.
Further, if we continue to go back through the game, then for all states the winningness will also coincide with "normal" Nim — simply because when we have more than one pile of size $>1$, all transitions lead to states with one or more piles of size $>1$, and for all of them, as we have already shown, nothing has changed compared to "normal" Nim.

Thus, the changes in misère Nim affect only states where all piles have size equal to one — which is what was required to prove.

### Moore's Nim (k-Nim)

Condition. There are $n$ piles of stones of size $a_i$. Also given is a natural number $k$. In one move, a player can reduce the sizes of from one to $k$ piles (that is, simultaneous moves in several piles are now allowed). The player who cannot make a move loses.

Obviously, when $k=1$, Moore's Nim turns into ordinary Nim.

Solution. Write the size of each pile in the binary numeral system. Then sum these zeros and ones along each digit and take this sum modulo $(k+1)$. If in all digits the result is zero, then the current position is losing, otherwise — winning.

Proof, as for ordinary Nim, consists in describing the strategy of the players — it is necessary to show that

1. from a game with zero value we can only transition to games with non-zero values, and
2. from any game with non-zero value there is a move to a game with zero value.

The first point is proved by the following reasoning. If for all digits the sum of bits modulo $(k+1)$ was equal to zero, then after changing from one to $k$ elements, obtaining a zero sum again is possible only by "balancing" all changes: for all elements where a certain bit is removed, there must be another element where this bit is added. In particular, this must hold for the highest digit, for which at least one bit of any number changes. But then this will mean that there exists some element for which a one was added in this digit, and in all higher digits nothing changed — meaning this element will be greater than the original, which violates the rules.

It remains to learn how to transition from states with non-zero sums to zero ones. Let's call *problematic* those positions for which the sum of bits turned out to be non-zero. We will iterate over all bits from highest to lowest and sequentially make each problematic sum zero, reducing some number of elements.

Denote by $u$ the number of piles that we have already started to change; initially, $u = 0$. Note that since in these $u$ piles we were reducing some of the previous, higher bits, then all lower bits we can set arbitrarily.

Suppose we are considering the current bit, in which the sum modulo $(k+1)$ turned out to be non-zero. Ideally, we want to make it zero, changing in this digit only those $u$ elements that we already planned to change. Mentally set ones in the corresponding digit in all of these $u$ elements and recalculate the sum, denoting it by $s$. Now consider two cases:

- If $s \le u$, then we can manage with the already selected elements, removing the unit bit in $s$ of them.
- Otherwise, if $s > u$ we find $(s - u)$ additional piles that have the considered bit set, and we will reduce them together with the $u$ already available.

After each iteration, the number $u$ of changed piles becomes equal to $u' = \max(s, u) \le k$.

Thus, we have shown a way to choose the set of piles to change and which bits to change in them, so that their total number $u$ never exceeds $k$. Consequently, we have proved that the desired transition from a state with non-zero sum to a state with zero sum always exists, which is what was required to prove.

## Why is this needed at all?

*Zugzwang* (from German *zug* — move, and *zwang* — compulsion) in chess and many other strategic games is called a situation when a player has run out of good moves, and if the rules allowed, they would simply stand still and pass the turn to the opponent.
 It turns out that there are very many games based on the idea of bringing the opponent to zugzwang, and they are all equivalent to Nim — in the sense that their graphs, from the point of view of the winningness of the sum of individual games, work exactly the same as the graphs of Nim.

