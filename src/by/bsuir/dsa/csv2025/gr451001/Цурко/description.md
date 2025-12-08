# Name

Laboratory folding

# Task

Laboratory Folding

In a research lab, long chains of molecules, represented by a string of symbols 'A', 'C', 'G', 'T', are studied.

Each symbol represents one type of molecule.

Scientists have discovered a process called *Folding*, which allows molecular chains to be shortened while preserving their key properties.

---

## Description of the Folding Process

- A cut point between two adjacent molecules is selected.
- The left and right sides must be mirror images of each other up to the nearest end of the chain.
- If the condition is met, the chain "folds" at this point, and the matching molecules merge.

### Example
For the chain 'ATTACC', two cuts are possible:
- 'AT | TACC'
- 'ATTAC | C'

If the first cut is selected, folding will yield a new chain 'CCAT' or 'TACC'.

---

## Problem

Given a molecular chain `s` consisting of the characters `A`, `C`, `G`, `T`.
Determine the **minimum possible chain length** that can be obtained by applying Folding zero or more times.

---

## Input
One string `s` (1 ≤ |s| ≤ 4 10^6), where |s| is the chain length.

---

## Output
A single number—the minimum chain length after optimally applying Folding operations.

---

# Theory

---
title: Manacher's Algorithm (with explanations)
weight: 5
authors:
- Vlad Tsurko
created: 2025
---

## Introduction

Many string problems require finding **palindromes**.

A **palindrome** is a string that reads the same from left to right and from right to left.

Examples: `abba`, `racecar`, `topot`.

A naive algorithm checks every substring (that is, any part of the string) and runs in $O(n^2)$, where $n$ is the string length.
**Asymptotics of $O(n^2)$** means that the running time grows proportionally to the square of the string length. For a string a million characters long, this is too slow.

**Manacher's Algorithm** solves the problem: it finds all palindromic substrings in $O(n)$, that is, linear time.
Linear time is O(n)—this means the number of operations grows directly proportional to the length of the string.

---

## Basic Idea

1. Consider a string `s` of length $n`.

2. For each position $i$, we want to know the maximum palindrome radius.

The palindrome radius is the number of characters by which the palindrome can be extended left and right from the center.

For example, in the string `abba`, the center at `b` has a radius of 2, because we can extend 2 characters left and right to get the palindrome `abba`.
3. If we already know the rightmost palindrome $[L, R]$, then for a new position $i$ within this range, we can use the mirror position $j = L + R - i$.

The mirror position** is the symmetric point about the center of the palindrome.
- The radius of the palindrome at $i$ will be at least equal to the radius at $j$, but is limited by the right boundary of $R$.
4. After this, we try to extend the palindrome around $i$ as long as the characters match.
5. If the new palindrome extends beyond $R$, update $L$ and $R`.
- $L$ and $R$ are the left and right boundaries of the rightmost palindrome found.

---

## Technical Details

To treat **even** and **odd** palindromes equally, the string is transformed:
- A **separator**, such as `#`, is inserted between characters.
- Example: the string `abba` becomes `#a#b#b#a#`.
- Now all palindromes have odd length, and the algorithm works uniformly.

---

## Pseudocode

**Pseudocode** is a simplified notation of an algorithm, close to code, but independent of a specific programming language.

```cpp
vector<int> manager(string s) {
    int n = s.size();
    vector<int> d(n); // palindrome radii
    int L = 0, R = -1;
    for (int i = 0; i < n; i++) {
        int k = (i > R) ? 1 : min(d[L + R - i], R - i + 1);
        while (i - k >= 0 && i + k < n && s[i - k] == s[i + k]) k++;
        d[i] = k;
        if (i + k - 1 > R) {
            L = i - k + 1;
            R = i + k - 1; 
        } 
    } 
    return d;
}

