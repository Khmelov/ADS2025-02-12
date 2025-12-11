# Name

Find All Anagrams in a String

# Task

Find All Anagrams in a String

## Problem Statement

Write a program that finds all starting positions of anagrams of a given word in a text.

**Input Format:**  
In a single line, separated by space, two strings are provided:
- `text` - the string to search in
- `pattern` - the string whose anagrams to find

**Output Format:**  
Output all starting indices (0-based) in the string `text` where anagrams of the string `pattern` begin. Indices should be output separated by spaces in ascending order. If no anagrams are found, output the symbol `?`.

**Constraints:**
- Strings contain only lowercase English letters
- 1 ≤ length(`pattern`) ≤ length(`text`) ≤ 10^5

## Examples

### Example 1
**Input:**
cbaebabacd abc

**Output:**
0 6

**Explanation:**
- At index 0: substring "cba" is an anagram of "abc"
- At index 6: substring "bac" is an anagram of "abc"

### Example 2
**Input:**
abab ab

**Output:**
0 1 2

**Explanation:**
- Index 0: "ab" - anagram of "ab"
- Index 1: "ba" - anagram of "ab"
- Index 2: "ab" - anagram of "ab"

### Example 3
**Input:**
hello world

**Output:**
?

**Explanation:** No anagrams of "world" found in string "hello"

## Note

Two strings are anagrams if one can be obtained from the other by rearranging letters. For example, "abc" and "cba" are anagrams, while "abc" and "abd" are not.

# Theory

---
title: Finding anagramms in a string
weight: 2
authors:
- Burshtyn Petr
created: 2025
---

# Theory: Finding Anagrams Using Frequency Analysis and Sliding Window

## What is an Anagram?

**Anagram** - a word or phrase formed by rearranging the letters of another word or phrase, using all the original letters exactly once.

**Examples of anagrams:**
- "listen" and "silent"
- "triangle" and "integral"
- "abc" and "cba"

## Key Solution Idea

### Fundamental Observation

Two words are anagrams if and only if:
1. They have the same length
2. Each letter appears the same number of times in both

### Frequency Analysis

Instead of checking all possible permutations (which would be very slow), we can use **frequency analysis**.

**Frequency array** - an array where each element shows how many times the corresponding letter appears in the string.

For the English alphabet of 26 letters, we create an array of 26 elements:
Letters: a b c d e f ... z
Index: 0 1 2 3 4 5 ... 25

**Example:** For word "abc" the frequency array would be:
[1, 1, 1, 0, 0, 0, ..., 0]
↑ ↑ ↑
a b c

### Sliding Window Algorithm

Since we're looking for substrings of fixed length (equal to pattern length), we can use the efficient **sliding window** algorithm.

**Algorithm steps:**

1. **Initialization:**
   - Create frequency array for `pattern`
   - Create frequency array for first window in `text` (first L characters, where L = pattern length)

2. **Check first window:**
   - Compare the two frequency arrays
   - If they are equal - found an anagram, remember index 0

3. **Move the window:**
   - For each next position i from 1 to (n - L):
     - Remove character text[i-1] from window frequency array
     - Add character text[i+L-1] to window frequency array
     - Compare frequency arrays
     - If equal - remember index i

4. **Form result:**
   - If indices found - output them separated by spaces
   - If no indices found - output symbol `?`

### Algorithm Visualization

Consider example `text = "cbaebabacd"`, `pattern = "abc"`:
Step 0: Window [0-2] = "cba"
patternFreq: [a:1, b:1, c:1, ...]
windowFreq: [a:1, b:1, c:1, ...] → MATCHES! Index 0 to answer

Step 1: Window [1-3] = "bae"
Remove 'c', add 'e'
windowFreq: [a:1, b:1, c:0, e:1, ...] → NO MATCH

...
Step 6: Window [6-8] = "bac"
windowFreq: [a:1, b:1, c:1, ...] → MATCHES! Index 6 to answer

Result: found indices 0 and 6 → output "0 6"

### Output Format

- If anagrams found: indices output separated by spaces in ascending order
- If no anagrams found: output symbol `?`

### Algorithm Complexity

- **Time:** O(n), where n is text length
- **Space:** O(1) - we use fixed arrays of size 26

This is significantly more efficient than the naive O(n × L) approach that would check every substring and verify if it's an anagram.

