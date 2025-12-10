# Name

Substring Search Using Prefix Function

# Task

Substring Search Using Prefix Function

## Task condition

Given a string `s` and a substring `pattern`. It is required to find all occurrences of the substring `pattern` in the string `s` using the Knuth-Morris-Pratt (KMP) algorithm, which is based on the prefix function.

**Task:** For a given string `s` and substring `pattern`:
1. Compute the prefix function for the substring `pattern`
2. Find all positions in the string `s` where the substring `pattern` starts
3. Output the results in the required format

## Requirements

1. Study the theoretical foundations of the prefix function
2. Understand the Knuth-Morris-Pratt algorithm for substring search
3. Develop an algorithm for computing the prefix function
4. Implement the search for all occurrences of a substring
5. Provide examples of how the algorithm works

## Solution format

- Mathematical definition of the prefix function
- Algorithm for computing the prefix function
- Algorithm for substring search using the prefix function
- Examples for various strings and substrings

---

### Features of this task:

- **Fundamental algorithm** - the prefix function is the basis of many string algorithms
- **Efficiency** - the KMP algorithm works in O(n + m) time, where n is the length of the string, m is the length of the substring
- **Practical application** - substring search is used in text editors, search engines, DNA processing
- **Good for learning** - allows you to understand important concepts of working with strings

This task is excellent for studying string algorithms and understanding the principles of substring search optimization.


# Theory

---
author: Стефанович Ярослав
weight: 1
---

# Substring Search Using Prefix Function

## Task condition

Given a string `s` and a substring `pattern`. It is required to find all occurrences of the substring `pattern` in the string `s` using the Knuth-Morris-Pratt (KMP) algorithm, which is based on the prefix function.

## Theoretical analysis

### Basic concepts

**Definition.** For a string `s` of length `n`, the prefix function `π[i]` (0 ≤ i < n) is defined as the length of the longest proper suffix of the substring `s[0..i]` that is simultaneously a prefix of this substring.

**Formally:** 
```
π[i] = max{k : 0 ≤ k < i, s[0..k-1] = s[i-k+1..i]}
```

where `s[0..k-1]` denotes the prefix of string `s` of length `k`, and `s[i-k+1..i]` is the suffix of substring `s[0..i]` of length `k`.

**Important properties:**
- `π[0] = 0` (for a string of one character, there is no proper suffix)
- `π[i] < i` for all `i > 0` (proper suffix is shorter than the entire substring)
- If `s[0..i]` has a period of length `p`, then `π[i] ≥ i - p + 1`

### Recursive computation of the prefix function

**Main idea:** When computing `π[i]`, we use already computed values `π[0], π[1], ..., π[i-1]`.

**Computation algorithm:**

```
π[0] = 0
for i from 1 to n-1:
    j = π[i-1]
    while j > 0 and s[i] ≠ s[j]:
        j = π[j-1]
    if s[i] == s[j]:
        π[i] = j + 1
    else:
        π[i] = 0
```

**Algorithm correctness:**

Suppose we are computing `π[i]`. By definition, `π[i-1]` is the length of the longest proper suffix of `s[0..i-1]` that is a prefix.

If `s[i] == s[π[i-1]]`, then we can simply extend the found prefix-suffix by adding the character `s[i]`. In this case, `π[i] = π[i-1] + 1`.

If `s[i] ≠ s[π[i-1]]`, then we need to find a smaller prefix-suffix. The key observation: the next candidate for a prefix-suffix has length `π[π[i-1]-1]`. This follows from the fact that if `s[0..k-1]` is a suffix of `s[0..i-1]`, then the longest proper prefix-suffix of `s[0..k-1]` is also a suffix of `s[0..i-1]`.

**Time complexity:** O(n), since each iteration of the inner `while` loop decreases the value of `j`, and `j` cannot become negative.

### Knuth-Morris-Pratt algorithm

**Algorithm idea:** Using the prefix function for the substring `pattern`, we can avoid backtracking the pointer in the string `s` when characters don't match.

**Search algorithm:**

```
1. Compute prefix function π for pattern
2. j = 0  // pointer in pattern
3. for i from 0 to |s|-1:
       while j > 0 and s[i] ≠ pattern[j]:
           j = π[j-1]  // shift pattern using prefix function
       if s[i] == pattern[j]:
           j++
       if j == |pattern|:
           // found occurrence at position i - |pattern| + 1
           j = π[j-1]  // continue searching for next occurrence
```

**Correctness:**

When `s[i] ≠ pattern[j]`, instead of backtracking the pointer `i` in the string `s`, we use the prefix function to find the longest prefix of `pattern` that is a suffix of `pattern[0..j-1]`. This allows us to continue comparison from position `j = π[j-1]` without backtracking in the string `s`.

**Time complexity:** O(n + m), where n is the length of string `s`, m is the length of substring `pattern`.

**Space complexity:** O(m) for storing the prefix function.

## Algorithmic analysis

### Computational model

The KMP algorithm can be represented as a finite automaton, where:
- States correspond to positions in `pattern`
- Transitions are determined by the prefix function
- Acceptance occurs when reaching the final state (full match)

### Algorithm optimality

The KMP algorithm is optimal for the substring search problem in the worst case among algorithms that do not preprocess the string `s`.

**Lower bound:** Any substring search algorithm must check each character of the string `s` at least once, which gives a lower bound of Ω(n). The KMP algorithm achieves this bound up to a constant factor.

### Comparison with naive algorithm

**Naive algorithm:**
- Time complexity: O(n·m) in the worst case
- Space complexity: O(1)
- Simple implementation

**KMP algorithm:**
- Time complexity: O(n + m)
- Space complexity: O(m)
- More complex implementation, but significantly faster for long strings

## Theoretical generalizations

### Z-function

An alternative approach to substring search uses the Z-function, which for a string `s` is defined as:
```
Z[i] = max{k : s[0..k-1] = s[i..i+k-1]}
```

The Z-function and prefix function are closely related and can be transformed into each other.

### Automata programming

The KMP algorithm can be interpreted as constructing a deterministic finite automaton (DFA) for substring search. This opens the way to generalization for searching multiple substrings simultaneously (Aho-Corasick algorithm).

### Applications of the prefix function

1. **Substring search** - main application
2. **Finding string period** - a string has period `p` if `π[n-1] ≥ n - p`
3. **String compression** - using the prefix function to detect repeating patterns
4. **DNA processing** - searching for genetic sequences

## Connection with other algorithms

### Rabin-Karp algorithm

The Rabin-Karp algorithm uses hashing for substring search. In the average case, it works in O(n + m), but in the worst case it can degrade to O(n·m). KMP guarantees O(n + m) in the worst case.

### Boyer-Moore algorithm

The Boyer-Moore algorithm uses the "bad character" and "good suffix" heuristics. In practice, it is often faster than KMP, especially for long substrings, but more complex to implement.

### Aho-Corasick algorithm

A generalization of KMP for searching multiple substrings simultaneously. Uses the prefix function in a trie data structure.

## Conclusion

The prefix function and the KMP algorithm are fundamental tools in string algorithms. Understanding these concepts opens the way to studying more complex algorithms, such as the Aho-Corasick algorithm, and has wide practical applications in text processing, bioinformatics, and search systems.

The knowledge gained has practical significance in the development of text editors, search engines, antivirus software, and genetic data processing systems.


