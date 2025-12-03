# Name

Wildcard Anagram Substring Search

# Task

Wildcard Anagram Substring Search

---

## Problem Statement

You are given:

* a string **s** — the text (`1 ≤ |s| ≤ 2 * 10^5`);
* a string **p** — the pattern (`1 ≤ |p| ≤ 2 * 10^5`).

The pattern may include three types of elements:

* regular letters `'a'`–`'z'` — **all such letters together form a single anagram group**, meaning their order does not matter, only their multiset must match;
* the wildcard symbol `?` — matches **any single character**, but positionally (only at that exact index in the pattern);
* set constraints `{abc}` — a set of allowed characters for that exact position.

You must find all indices `i` (0‑based) such that the substring `s[i … i + |p| − 1]` matches the pattern `p` under the following strict rules:

1. **Anagram group rule.** All plain letters of the pattern (all `a`–`z` outside of `{}` and not equal to `?`) form a multiset. The characters of the substring on the corresponding *anagram positions* must form exactly the same multiset, in **any order**.
2. **Positional matching.** Every position in `p` that contains `?` or a `{...}` set must match the corresponding position in the substring:

   * `?` allows any character;
   * `{...}` requires the character to belong to the given set.
3. Letters in the anagram group are **not tied to specific positions** — they may appear in any of the positions not occupied by positional constraints.

Output all starting indices of matches in increasing order. If there are no matches, output an empty line.

---

## Input Format

Two lines:

1. `s` — the text;
2. `p` — the pattern.

---

## Constraints

* `1 ≤ |s|, |p| ≤ 2 * 10^5`;
* Characters in `s` and inside `p` (including inside `{}`) are lowercase English letters;
* Pattern is guaranteed to be correctly formatted (every `{` has a matching `}`).

---

## Example

Input:

```
s = "cbaebabacd"
p = "abc"
```

Here, the pattern contains only the letters `a`, `b`, `c`. All of them belong to the **anagram group** with frequencies:

```
a:1, b:1, c:1
```

We look for any permutation of these characters in substrings of length 3.

Output:

```
0 6
```

---

## Explanation

The substrings `cba` (starting at index 0) and `bac` (starting at index 6) contain exactly one `a`, one `b`, and one `c` in any order. All other length‑3 windows contain extra or missing characters.

---

## Output

Print all 0‑based starting indices of substrings matching the pattern, space‑separated. If no matches exist, print an empty line.

---


# Theory

## Pattern parsing

The pattern is parsed into a list of *groups*, each representing one position:

* `WILDCARD` — `?`;
* `SET` — `{...}` with a boolean array of allowed characters;
* `LETTER` — plain letters, which are added to the global **anagram multiset**.

Let the pattern length be `m`, and text length be `n`.
If `m > n`, no matches are possible.

### Matching logic (fixed-window check)

For each window `s[i … i+m−1]`:

* All `SET` positions must match exactly.
* `?` positions always match.
* Characters at all remaining positions form the **anagram window**, and their frequencies must equal the frequencies of the anagram group.

The window matches if and only if:

```
have[c] == need[c] for all characters c
and all positional constraints are satisfied
```


