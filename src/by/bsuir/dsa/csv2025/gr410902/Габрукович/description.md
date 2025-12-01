# Name

Isomorphic Substrings

# Task

Isomorphic Substrings

## Problem Statement

You are given a string `S` of length up to 2⋅10^5 characters.  
Also, there are `Q` queries. Each query contains four integers: `L1`, `R1`, `L2`, `R2`.

For each query, determine whether the substrings `S[L1..R1]` and `S[L2..R2]` are **isomorphic**.

Two strings are isomorphic if you can transform one into the other by replacing characters according to the following rules:  
- Each character of the first string corresponds to exactly one character of the second string;  
- Different characters of the first string cannot map to the same character of the second string;  
- The order of characters is preserved.

Output `YES` for each query if the substrings are isomorphic, otherwise `NO`.

Implementation Guidelines:
- Use **double polynomial hashing**;  
- Encode each substring as a sequence of numbers based on the first occurrence of each character;  
- Compute hashes using two different moduli;  
- Compare the hashes of the two substrings to determine `YES`/`NO`;  
- Allows handling strings up to length 2⋅10^5 and a large number of queries efficiently.

---

# Theory

Isomorphic Substrings

When checking substring isomorphism, direct character comparison may be too slow for long strings and a large number of queries.  
The solution is to use **pattern-based character hashing**.

## Idea of Hashing

Each substring is encoded into a sequence of numbers based on the first occurrence of each character:
- the first seen character receives code `0`;  
- every new character receives the next code `1, 2, 3 …`;  
- a repeating character gets the code it was assigned earlier.

Then this sequence of codes is converted into a single number — a hash.  
If the hashes of two substrings match, they are very likely isomorphic.

## Hash Collisions

A *collision* occurs when two different substrings produce the same hash.  
- If the string has length `n` and modulus `m` is used, the probability of collision is approximately \(\Theta(n^2/m)\).  
- For practical tasks, it is safe to choose a modulus of about `10 * n^2`.

To reduce the probability of collisions, **double hashing** is used:
- Two hashes with different moduli are computed;  
- Both hash pairs must match.

This makes the collision probability practically zero, even for long strings (up to 2⋅10^5) and a large number of queries.

## Choosing Constants

- Moduli should be large prime numbers to minimize collisions.  
- The hashing base (BASE) should be chosen randomly, fairly large, and not divisible by the moduli.  
- For extra reliability, three moduli may be used, but two are usually enough.

## Example

If we want to check whether substrings `S[1..5]` and `S[6..10]` are isomorphic:
1. Build the code sequences for each substring.  
2. Compute two independent hashes.  
3. Compare the hash pairs: if both match → `YES`, otherwise → `NO`.

---

**Note:** Double polynomial hashing allows efficient isomorphism checking without direct character comparison and works well on large inputs.


