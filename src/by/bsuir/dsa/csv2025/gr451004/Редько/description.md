# Name

Substring Hashing and String Comparison

# Task

title: Substring Hashing and String Comparison
weight: 3
authors:

* AntonRadzko
  created: 2025

---

# Substring Hashing and String Comparison

You are given a string s of length n. You must process q queries of two types:

1. **`1 l r`** — compute the polynomial hash of substring s[l..r] modulo 2^64.
2. **`2 a b c d`** — determine whether substrings s[a..b] and s[c..d] are equal using only their hashes.

You need to:

* build a prefix hash array of the string,
* precompute powers of the chosen base k,
* be able to compute a substring hash in O(1) after preprocessing.

## Short theory

A polynomial hash is built by iteratively multiplying the current hash by a constant base and adding the next character. The modulo 2^64 is applied automatically via unsigned long long overflow.

Substring hashes can be obtained using prefix hashes and the length of the substring.

## Input format

```
n q
s
<q queries>
```

## Output format

* For queries of type `1` — output the hash of the substring.
* For queries of type `2` — output `YES` if the substrings are equal, otherwise `NO`.

## Task

Implement polynomial hashing and process all queries.


# Theory

## Short theory

A polynomial hash of a string is defined as:

$$
H(s) = (s_1 k^{n-1} + s_2 k^{n-2} + ... + s_n) mod\ m
$$

A substring hash is computed using prefix hashes:

$$
H(l, r) = H[r] - H[l-1] * k^{(r-l+1)}
$$

---

