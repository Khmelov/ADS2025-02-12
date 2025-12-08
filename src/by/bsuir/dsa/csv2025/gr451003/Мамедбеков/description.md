# Name

Guess the number

# Task

#Guess the number
##Task
The program must guess a hidden integer X in the range from 1 to 10^9, asking no more than 30 questions to the interactor. For each question of the form "? Y," the interactor responds with one of the following symbols: '>' (if X > Y), '<' (if X < Y), or '=' (if X = Y). Once the number is guessed, the program should print "! X" and exit.
##Notes:
    1. The program must run in interactive mode: print questions to standard output and read answers from standard input.
    2. Make sure the program does not exceed the 30-question limit.
    3. The range is from 1 to 1,000,000,000, integers.
##Example:
    ? 500,000,000
    <

# Theory

---
title: Binary Search
weight: 1
authors:
- Eldar Mamedbekov
created: 2025
---

Binary search is a fundamental algorithm for searching for an element in a sorted array or range. In the context of number guessing problems, it allows one to effectively narrow the range of possible values, minimizing the number of queries. If the range is too large (for example, from 1 to 10^9), sequential search is inefficient, but binary search solves this in logarithmic time.

In our problem, the hidden number X lies in the range [1, 10^9], and the program can ask questions like "? Y," receiving answers of '>', '<', or '='. The goal is to guess X in no more than 30 questions, which corresponds to log₂(10^9) ≈ 30.

## Choosing a Strategy

*Rule of Thumb:* Always choose the midpoint of the current interval for the next question. This ensures that the interval is halved at each step, minimizing the worst-case scenario.

Avoid using random questions—this can lead to worse times. In programming languages, account for overflow when calculating the midpoint: instead of (left + right) / 2, use left + (right - left) / 2.

For large ranges (like 10^9), binary search is ideal, since linear search would require up to a billion operations, which is unacceptable.

## Logarithmic Complexity

> In a range of N elements, binary search requires no more than ⌈log₂(N)⌉ questions in the worst case.

A more general statement: finding an element in a sorted range of size N requires Θ(log N) operations.

**First proof** (for math enthusiasts). Let's say the interval starts with size N. After the first question, the interval shrinks to N/2. After the second, to N/4, and so on. After k questions, the interval size ≤ N / 2^k. When the size becomes 1, we've found the element. Thus, k ≥ log₂(N).

Formally:

$$
k = \lceil \log_2 N \rceil
$$

For N = 10^9:

$$
\log_2(10^9) \approx 29.897 \implies \lceil \log_2(10^9) \rceil = 30
$$

**Second proof** (for computer science enthusiasts). Each question provides 1 bit of information (more/less), and distinguishing between N options requires log₂(N) bits. In information theory, this is the minimum number of questions in an adaptive strategy.

*Note*: A non-adaptive strategy (all questions pre-assigned) may require more, but here we're using an adaptive strategy.

### Bonus: "Meta-Problem"

You're given a sorted array of 100 elements, but you don't know its contents. You have access to an "oracle" that responds to the query "? i" with the value of the array at index i. You have 10 queries. Find the maximum in the array. (Hint: adapt binary search to find the extremum.)

