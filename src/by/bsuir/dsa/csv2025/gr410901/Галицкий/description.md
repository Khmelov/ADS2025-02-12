# Name

Anti-Hash Test

# Task

Anti-Hash Test

Find all occurrences of pattern P in text S.

You must use:
- Double hashing with two large primes, or
- KMP / Z-algorithm, or
- BigInteger 

# Theory

**Brief theory: safe string hashing in Java**

Java's `long` is signed, so natural overflow (like in C++ `unsigned long long`) does not work correctly and leads to wrong hash values.

Fixed bases (31, 131, etc.) + single modulus are vulnerable: there exist crafted anti-hash tests that force collisions.

Reliable approaches in Java:

1. Double hashing  
   Compute two independent hashes with different large prime moduli  
   (e.g. 1_000_000_007 and 1_000_000_009).

2. 2⁶⁴-modulo hashing using `BigInteger` (slower but collision-proof).

3. Deterministic algorithms  
   Z-algorithm or Knuth-Morris-Pratt — O(n+m) time, zero collision risk.

For most competitive programming problems, double hashing is the fastest and sufficiently safe choice.

