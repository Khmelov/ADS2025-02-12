# Name

Substring search using Z-function

# Task

Substring search using Z-function

## Task condition

Given a string **text** and a substring **pattern**. It is necessary to find all occurrences of the substring **pattern** in the string **text** using the Z-function algorithm.

The **Z-function** of a string `s` is an array `Z`, where `Z[i]` is the length of the longest common prefix of the string `s` and the suffix `s[i..n-1]` starting at position `i`.

**Task:** For given strings `text` and `pattern`, find all positions where `pattern` occurs in `text` using the Z-function.

## Requirements

1. Study the theoretical foundations of the Z-function
2. Prove the correctness of the Z-function computation algorithm
3. Develop an algorithm for substring search using the Z-function
4. Give examples of how the algorithm works

## Solution format

- Mathematical proof of algorithm correctness
- Algorithm for computing Z-function in O(n) time
- Algorithm for finding all substring occurrences in O(n + m) time
- Examples for various input data

---

### Features of this task:

- **Efficient algorithm** - linear time complexity O(n + m)
- **Practical application** - substring search in text
- **Elegant solution** - using Z-function for efficient search
- **Good for learning** - allows you to understand the basics of string algorithms

This task is great for learning substring search algorithms and working with strings.


# Theory

---
author: Mitskevich Nikita
weight: 1
---

# Substring search using Z-function

## Task condition

Given a string **text** and a substring **pattern**. It is necessary to find all occurrences of the substring **pattern** in the string **text** using the Z-function algorithm.

## Theoretical analysis

### Definition of Z-function

The **Z-function** of a string `s` of length `n` is an array `Z[0..n-1]`, where `Z[i]` is the length of the longest common prefix of the string `s` and the suffix `s[i..n-1]` starting at position `i`.

**Formal definition:**
```
Z[i] = max{k : s[0..k-1] = s[i..i+k-1]}
```

**Properties of Z-function:**
1. `Z[0] = n` (by definition, the entire string matches itself)
2. `Z[i] ≤ n - i` for all `i ∈ [1, n-1]`
3. If `Z[i] > 0`, then `s[0..Z[i]-1] = s[i..i+Z[i]-1]`

### Examples of Z-function

**Example 1:** `s = "aaaa"`
```
Z[0] = 4
Z[1] = 3
Z[2] = 2
Z[3] = 1
```

**Example 2:** `s = "abacaba"`
```
Z[0] = 7
Z[1] = 0
Z[2] = 1
Z[3] = 0
Z[4] = 3
Z[5] = 0
Z[6] = 1
```

### Algorithm for computing Z-function

**Algorithm idea:** Use already computed Z-function values to speed up computations.

**Main idea:** Maintain an interval `[l, r]` — the rightmost Z-block (an interval for which we have already computed the Z-function and know that `s[0..r-l] = s[l..r]`).

**Algorithm:**
1. Initialization: `Z[0] = n`, `l = 0`, `r = 0`
2. For each `i` from 1 to n-1:
   - If `i ≤ r`, use already computed value: `Z[i] = min(r - i + 1, Z[i - l])`
   - Expand the Z-block to the right while characters match
   - Update `l` and `r` if we found a more right Z-block

**Pseudocode:**
```
Z[0] = n
l = 0, r = 0
for i = 1 to n-1:
    if i <= r:
        Z[i] = min(r - i + 1, Z[i - l])
    else:
        Z[i] = 0
    
    while i + Z[i] < n and s[Z[i]] == s[i + Z[i]]:
        Z[i]++
    
    if i + Z[i] - 1 > r:
        l = i
        r = i + Z[i] - 1
```

### Correctness proof

**Lemma 1.** If `i ≤ r`, then `Z[i] ≥ min(r - i + 1, Z[i - l])`.

**Proof:** 
Since `s[0..r-l] = s[l..r]`, for position `i` in the interval `[l, r]`:
- If `Z[i - l] < r - i + 1`, then `Z[i] = Z[i - l]`
- If `Z[i - l] ≥ r - i + 1`, we can use the value `r - i + 1` as an initial approximation

**Theorem.** The algorithm computes the Z-function correctly in O(n) time.

**Proof:**
1. **Correctness:** Follows from Lemma 1 and the fact that we expand the Z-block only when characters match.
2. **Time complexity:** Each character of the string is examined at most twice (once when expanding the Z-block, once when updating boundaries), so the total complexity is O(n).

### Application of Z-function for substring search

**Task:** Find all occurrences of substring `pattern` in text `text`.

**Idea:** Create a string `s = pattern + '$' + text`, where `'$'` is a character that does not occur in either `pattern` or `text`. Compute the Z-function for `s`. Then all positions `i` where `Z[i] = |pattern|` correspond to occurrences of `pattern` in `text`.

**Algorithm:**
1. Create string `combined = pattern + '$' + text`
2. Compute Z-function for `combined`
3. Find all positions `i` where `i > |pattern|` and `Z[i] = |pattern|`
4. Position of occurrence in original text: `i - |pattern| - 1`

**Time complexity:** O(|pattern| + |text|) = O(n + m), where `n = |text|`, `m = |pattern|`.

**Space complexity:** O(n + m).

### Example of algorithm execution

**Input:**
- `text = "abacaba"`
- `pattern = "aba"`

**Step 1:** Create `combined = "aba$abacaba"`

**Step 2:** Compute Z-function:
```
Z[0] = 11
Z[1] = 0
Z[2] = 1
Z[3] = 0
Z[4] = 3  ← occurrence (Z[4] = 3 = |pattern|)
Z[5] = 0
Z[6] = 1
Z[7] = 0
Z[8] = 3  ← occurrence (Z[8] = 3 = |pattern|)
Z[9] = 0
Z[10] = 1
```

**Step 3:** Find occurrences:
- Position 4 in `combined` → position 0 in `text`
- Position 8 in `combined` → position 4 in `text`

**Result:** Occurrences at positions 0 and 4.

## Comparison with other algorithms

### Z-function vs. Prefix function (KMP)

- **Z-function:** `Z[i]` — length of longest common prefix of string and suffix starting at position `i`
- **Prefix function:** `π[i]` — length of longest proper suffix of prefix `s[0..i]` that is a prefix of the string

Both algorithms have O(n) complexity, but Z-function is more intuitive to understand.

### Z-function vs. Naive algorithm

- **Naive algorithm:** O(n·m) in worst case
- **Z-function:** O(n + m) in any case

Z-function is significantly more efficient for large texts.

## Generalizations and applications

### Searching for multiple substrings

Z-function can be used to search for multiple substrings simultaneously by combining them into one string with separators.

### Finding periodic substrings

Z-function allows efficient finding of periodic substrings in a string.

### String compression

Z-function is used in string compression algorithms such as LZ77.

## Conclusion

Z-function is a powerful tool for working with strings, allowing efficient solving of substring search problems in linear time. The simplicity of implementation and understanding makes it an ideal object for studying string algorithms.

The results obtained have practical significance in text processing, search engines, bioinformatics, and other areas where efficient substring search is required.


