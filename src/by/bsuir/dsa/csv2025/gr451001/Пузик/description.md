# Name

Substring Comparison via Hashing

# Task

## Problem Statement

You are given `n` strings `s1, s2, ..., sn`. You must answer `q` queries of the form: are the substrings `si[l1..r1]` and `sj[l2..r2]` equal? Indices are **1-based** and inclusive.

Implement an efficient solution that answers all queries. Typical approaches for this task use polynomial (rolling) substring hashing. Be aware of hash collisions — to reduce their probability to an acceptable level, use two independent hash functions (different moduli and/or bases) or another collision-resistant strategy.

## Input

The first line contains two integers `n` and `q` (`1 ≤ n, q ≤ 2·10^5`).

The next `n` lines contain the strings `s1, s2, ..., sn`.
The total length of all strings satisfies `∑ |si| ≤ 2·10^6`.
Each string consists only of lowercase Latin letters (`'a'–'z'`).

The following `q` lines contain the queries. Each query consists of six integers:

```
i l1 r1 j l2 r2
```

with
`1 ≤ i, j ≤ n`,
`1 ≤ l1 ≤ r1 ≤ |si|`,
`1 ≤ l2 ≤ r2 ≤ |sj|`.

## Output

For each query, output `YES` if the substrings are equal, and `NO` otherwise.


# Theory

Polynomial hashing allows substring comparison in *O(1)* after *O(n)* preprocessing.
The idea is to interpret a string as a number in a base-(p) numeral system and compute its hash modulo (m).

Hash of a string (s = s_1 s_2 \dots s_k):

[
H(s) = \sum_{i=1}^{k} s_i \cdot p^{,i-1} \bmod m.
]

Prefix hashes are used for efficient substring hashing:

[
H_{\text{pref}}[i] = \sum_{t=1}^{i} s_t \cdot p^{,t-1} \bmod m.
]

Hash of a substring (s[l..r]):

[
H(l, r) = \bigl(H_{\text{pref}}[r] - H_{\text{pref}}[l-1] \cdot p^{,r-l+1}\bigr) \bmod m.
]

Two substrings are equal if their hashes match.
To reduce collision probability, double hashing is commonly used — compute hashes with two different ((p, m)) pairs and compare both results.

