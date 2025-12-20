# Name

Dynamic catalog analysis

# Task

Dynamic catalog analysis

## Description

An online store's price monitoring system tracks N products. At the end of the day, the analytics department prepares a set of queries to analyze price changes. You must efficiently process all operations (in offline mode) and, for each query, do the following:

- Find **the median price** among all unique prices in the given range of products.
- Output **the number of products in the range whose price is greater than or equal to the median**.

## Input

- The first line contains an integer N — the number of products, where $1 \leq N \leq 2 \times 10^5$.
- The second line contains N integers — the initial prices of the products in rubles, where $1 \leq price_i \leq 10^6$.
- Next comes a sequence of operations (no more than $2 \times 10^5$), each in one of the following formats:
  - `Update pos new_price` — replace the price of the product at position pos with value new_price ($0 \leq pos < N, 1 \leq new\_price \leq 10^6$).
  - `Query L R` — compute the median price among all unique prices in the segment [L, R] and the number of products in [L, R] with price at least equal to the median ($0 \leq L \leq R < N$).

## Output

For each `Query L R`, output two lines:

- The first line: the median price among all unique prices in segment [L, R] (if the number of unique prices is even, use the integer division/floor of the average of the two middle values; if odd, use the value at index $n // 2$ in the sorted list of unique prices).
- The second line: the count of products in the segment whose price is greater than or equal to the median.

Results must be output in the order the queries appear in the input. Queries are processed offline, but output order matches the input.

**Median Calculation:**

- First, gather all unique prices in the segment and sort them in ascending order.
- If the number of unique prices is odd, the median is the value at index $n // 2$.
- If even, the median is the integer average (floor) of the two central values: $\lfloor(\text{A}[n/2-1] + \text{A}[n/2]) / 2\rfloor$.


# Theory

Mo's Algorithm

Mo’s algorithm is an offline method for efficiently answering a large number of range queries (subarray queries) when all queries are known in advance.

## Core Idea

In standard sqrt decomposition, we precompute answers for each block and learn to efficiently merge them when answering queries. However, for some problems (for example, mode queries — the most frequent element in a range), merging can become prohibitively complicated: each block may require a data structure that's too complex to merge quickly. In such cases, a "lazy" sliding window approach is preferable: we maintain only a single data structure, which always represents the statistics for the current range.

Mo's algorithm is built on these principles:

- All queries are given in advance (*offline*).
- Queries are sorted by their left endpoint (grouped into blocks of length about $\sqrt{N}$), and within each block, by their right endpoint.
- We process queries sequentially, moving a "window" [cur_l, cur_r] through the array, incrementally adding and removing elements at the window borders.
## Specifics

Observe that we never recompute answers from scratch; we only add or remove one element at each step. This makes it possible to maintain complex statistics incrementally. This approach works exceptionally well for queries such as counting unique elements, frequencies, finding the maximum or minimum, sums, and (with tree/set/decomposed frequency arrays) — medians and modes.

## Time Complexity

Let N be the array length, and Q the number of queries.

- Every array element may be added and removed for each block: total O(N * sqrt(N))
- Left pointer movements total O(Q * sqrt(N))

Together, the complexity is:
$O((N + Q)\sqrt{N})$
(assuming add/remove are O(1))

***

## Efficiency Tips

- The block size is usually $\sqrt{N}$, but it’s worth experimenting with the constant.
- Sometimes, "chessboard order" is used: in even-numbered blocks, sort R in increasing order; in odd-numbered blocks — in decreasing order, to minimize window jumps.
- All `add` and `remove` operations must be extremely fast.

***

# Extension: 3D Mo's Algorithm (Mo’s algorithm with updates)

If you allow point updates (*updates* between queries), the classic Mo's algorithm won't work.

- Each query gets a third coordinate — the time t: ([l, r], t), where t is the number of updates that must be applied before that query.
- Queries are sorted by (block of time, block of L, R).
- In addition to moving the left/right boundary, you must carefully "apply" and "undo" updates as you move through queries.
- If the position being updated is inside the current window, you must remove the old value and add the new value.
- Total time complexity:
$O(N^{5/3})$

