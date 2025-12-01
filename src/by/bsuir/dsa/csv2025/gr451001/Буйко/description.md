# Name

Consistent investment strategy

# Task

Consistent investment strategy

## Task condition

You are a financial analyst at an investment company. A new type of asset has appeared on the market — "extreme stocks". The peculiarity of these assets is that only stocks located on the "edges" of the current list are available for purchase on a daily basis.

### The bidding mechanism
- Only two promotions are available for purchase every day: the first and the last in the current list
- After purchase, the selected promotion is removed from the list
- New "extreme" promotions become available the next day
- The bidding takes turns, your company goes first

**Objective:** To determine whether your company can be guaranteed to receive a combined share price not less than that of a competitor, provided that both companies act optimally.

## Formal statement

An array of integers `prices` with length `n` is given, where `prices[i]` represents the value of the ith share in millions of rubles.

It is necessary to determine whether the first player (your company) can guarantee that his total amount will ** not be less** than the amount of the second player (competitor), provided that both players act optimally.

## Examples

### Example 1
```
n = 3
prices = [1, 5, 2]
result = False
```
*Explanation:* Regardless of the first player's choice, the second player will be able to choose a share worth 5 million.

### Example 2
```
n = 4
prices = [1, 5, 233, 7]
result = True
```
*Explanation:* The first player can choose 1, then regardless of the choice of the second player, the first one will take the share for 233 million.

## Restrictions
- 1 ≤ `prices.length` ≤ 20
- 0 ≤ `prices[i]` ≤ 10^7e

# Theory

Theory for the task of "Consistent Investment Strategy"

## Mathematical basis

### Game theory
This task belongs to **antagonistic games with full information**:

- **Two players** with opposing interests
- **Full details** - all moves and costs are known to both players
- **Consecutive moves** - players take turns
- **Zero amount** - One person's gain is equal to the other's loss

### Key concepts

####1. Minimax strategy
Each player chooses a move that **maximizes his minimum guaranteed winnings** provided that his opponent plays optimally.

#### 2. The value of the game
A result that is guaranteed to be obtained by the first player with optimal play on both sides.

## Formal model

### Definitions
Let `F(i, j)` be the maximum advantage that the current player can get in the `prices[i...j]` subarray.

### Recurrence relations

#### The base case
``
F(i, i) = prices[i] # One share left
```

#### The general case
```
F(i, j) = max(
    prices[i] - F(i + 1, j), # Took the left share
prices[j] - F(i, j - 1)    # Took the right action
)
``

### Explanation of the formula
- `prices[i] - F(i + 1, j)' - we took the left action, the opponent will play optimally for the remaining segment
- `prices[j] - F(i, j - 1)' - took the right action, the opponent will play optimally for the remaining segment

## Complexity analysis

### Time complexity
- **Recursion**: O(2ⁿ) without memoization, O(n2) with memoization
- **Dynamic programming**: O(n2)

### Spatial complexity
- **Recursion with memoization**: O(n2)
- **Dynamic programming**: O(n2), can be optimized to O(n)

## Properties of the optimal strategy

### The symmetry of the game
The problem is symmetrical - if the first player can win with an advantage of X, then the second player, starting first, can also win with an advantage of X.

### Critical cases
1. **Even array length** - the first player has a strategic advantage
2. **Large range of values** - the presence of "jackpot" stocks strongly affects the result
3. **Palindromic arrays** - the result depends on the specific values

## Practical insights

### When is the first player guaranteed to win?
- When he can control access to "expensive" stocks
- When the sum of the values on the even positions is significantly different from the sum on the odd ones
- When it can create a fork situation for an opponent

### Tactical principles
1. **Access control** - restrict your opponent's access to valuable stocks
2. **Balancing** - sometimes it is better to take a smaller share, but maintain a positional advantage
3. **Foresight** - plan your moves a few steps ahead

## The criterion of victory

The first player can guarantee victory if:
``
F(0, n-1) ≥ 0
```

This means that with optimal play, the difference in sums (player 1 - player 2) will be non-negative.

