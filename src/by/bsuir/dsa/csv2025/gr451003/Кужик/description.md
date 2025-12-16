# Name

Real-time Signal Analysis

# Task

Real-time Signal Analysis

A monitoring system receives a continuous digital signal represented as an array of \(N\) integer values. To analyze signal quality and detect anomalies, a sliding window method with width \(K\) is used.

**Technical Requirements:**
For each time interval of length \(K\), determine:
- Minimum signal value in the interval (to detect "dips")
- Maximum signal value in the interval (to detect "spikes")

## Input Format

First line:
- \(N\) — total number of signal measurements
- \(K\) — width of the analysis window

Second line:
- \(N\) integers — consecutive signal values

## Output Format

Two lines:
1. Minimum values for each window (\(N-K+1\) numbers)
2. Maximum values for each window (\(N-K+1\) numbers)

## Example

**Input:**
7 3
2 1 3 4 6 3 8

**Output:**
1 1 3 3 3
3 4 6 6 8

*Explanation:* Signal is analyzed by windows [2,1,3]→[1,3,4]→[3,4,6]→[4,6,3]→[6,3,8]

**Constraints:**
- \(1 \leq K \leq N \leq 10^6\)
- Signal values: integers in range \([-10^9, 10^9]\)

**Solution Requirements:** Efficient algorithm with \(O(N)\) complexity

---
*Section: II. Data Structures → 7. Basic Data Structures*

# Theory

title: Sliding Window Minimum and Maximum
weight: 2
authors:
- Daniil Kuzhik
created: 2025
---

The problem of finding minimum and maximum values in a sliding window appears in many data analysis and signal processing applications where tracking extreme values in data streams is required.

## Naive Solution

The straightforward approach — for each of the $(N-K+1)$ windows, find the minimum and maximum by scanning through $K$ elements. This solution runs in $O(NK)$ time, which is unacceptable for large $N$ and $K$.

## Efficient Solution with Deque

An $O(N)$ solution uses a **monotonic queue** data structure implemented via a deque.

### Algorithm Idea

**For minimums:**
- Maintain a deque of indices where the array elements form a non-decreasing sequence
- When adding a new element, remove from the back all elements greater than or equal to the current one
- The front of the deque always contains the minimum of the current window
- Remove from the front elements that have moved outside the window

**For maximums:**
- Similarly, but maintain a non-increasing sequence
- Remove from the back all elements less than or equal to the current one

