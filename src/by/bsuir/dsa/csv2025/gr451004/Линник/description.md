# Name

Stock price changes 

# Task

Stock price changes 

## Task condition

There is an array of `n` stocks with initial prices: `price[0..n-1]'. There are some price changes during the `m` days.  Each change is a pair of `(index_ shares, new_ price)`, meaning that the price of the specified share in the corresponding day (its sequence number is equal to the increased by one index in the transmitted array) was changed to a new value.

Boss sends the analyst queries like `(start, end, k)`, where:

- `start`, `end` - stock indexes (range `[start, end]`)
- `k` - number of changes to be taken into account (the first `k` pieces)

It is necessary to consider the state of prices at the end of the day `end`, but for stocks in the range `[start, end]`, only the first `k` changes should be taken into account, which:

- Affected stocks in the segment `[start, end]`
- Occurred between the days of the `start` and the `end`

It is necessary to find the **median price** (e. g. if the array length is odd, it is the central element, and if it is even, it is the arithmetic mean of the two central elements) of stocks with indexes from `start` to `end'.

# Theory

---
title: Stock price changes 
weight: 2
authors:
- Linnik Maria
created: 2025
---
# Query problem

## Problematic

Sometimes tasks require responding to queries as:  
**"What did the data look like at time T?"**. The naive solution is to store full copies of the data for each change, which requires **O(m·n)** memory, which is unacceptable for large values of `m` and `n`.

## Key observation

With successive data changes, each subsequent state differs from the previous one only in **one element**. This allows you to build **persistent data structures**.

### The advantage of persistent structures

- Store all versions using memory **O(n + m log n)** instead of **O(m·n)**

# Persistent segment tree

To solve the problem, a persistent segment tree is suitable that supports:
- **Updating one element** in `O(log n)`
- **Requesting the sum on the segment** in `O(log n)`
- **Storing all versions** with total memory of `O((n + m) log n)`

### Implementation in C++

```cpp
struct Node {
    int sum;
    Node *left, *right;
    Node(int s, Node* l, Node* r) : sum(s), left(l), right(r) {}
};

vector<Node*> versions; // Roots of all versions
``
##k-th ordinal statistics

The technique of k-th ordinal statistics is used to find the median on a segment in a persistent segment tree.:

    1. Compress the price coordinates.
    2. Build a persistent tree of segments, where the `ith` version takes into account the first `i` changes.
    3. For the query `(start, end, k)`, find which version corresponds to the first `k` changes in the segment.
    4. Use binary search to find the median.

