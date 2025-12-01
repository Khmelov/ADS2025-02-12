# Name

Simple String Hash

# Task

Simple String Hash

You are given a string consisting of the following characters: space (`' '`), digits `0–9`, and Latin letters `A–Z` and `a–z`.

You must compute the **polynomial hash** of this string using the following formula:

\[
h_0 = 0,
\]
\[
h_{i+1} = (h_i \cdot P + \mathrm{code}(s_i)) \bmod M,
\]
\[
\mathrm{hash}(s) = h_n,
\]

where:

- \(P = 131\),
- \(M = 10^9 + 7\),
- \(\mathrm{code}(s_i)\) is the ASCII code of the character.

## Input format

A single line — the string \(s\).  
The string may contain spaces but does not contain newline characters inside.

## Output format

Print a single integer — \(\mathrm{hash}(s)\).

## Examples

1.  
Input: `abc`  
Output: `1677554`

2.  
Input: `Hello world`  
Output: `21160602`


# Theory

## Short theory (English)

### 1. Idea of string hashing

A hash function converts an object (for example, a string) into an integer — a *hash*.

Goals:

- compare strings faster (comparing integers is cheaper than character-by-character comparison);
- store keys in data structures (hash table, set, unordered_map, etc.).

Important: hashing always theoretically allows **collisions** — different strings may have the same hash, but with good parameter choices the probability is low.

### 2. Polynomial hash

For a string \(s = s_0 s_1 \dots s_{n-1}\) we define:

\[
h_0 = 0,
\]
\[
h_{i+1} = (h_i \cdot P + \mathrm{code}(s_i)) \bmod M,
\]
\[
\mathrm{hash}(s) = h_n.
\]

Here:

- \(P\) is the base (typically a small prime or just a “random-looking” number, e.g., \(P = 131\) or \(P = 257\));
- \(M\) is a large modulus (often \(10^9 + 7\) or \(10^9 + 9\));
- \(\mathrm{code}(s_i)\) is the numeric value of character \(s_i\) (e.g., ASCII code).

Intuitively, this is like treating the string as a number in base \(P\), computed modulo \(M\).

The iterative formula is straightforward to implement:

```text
h = 0
for each character c in s:
    h = (h * P + code(c)) mod M
return h


