# Name

Matrix Coefficients Modulo Prime

# Task

Matrix Coefficients Modulo Prime

## Description

In crystallography, the task of computing special matrix coefficients characterizing the symmetry of a crystal lattice often arises. These coefficients are defined through factorials and require computation modulo a prime number to avoid overflow when dealing with large parameter values.

Given an integer `n` and a prime modulus `p = 10^9 + 7`. You need to calculate the matrix coefficient using the formula:

$$\text{coefficient} = \frac{(n!)^2}{(2n)!} \pmod{p}$$

where all operations are performed modulo `p`, including division (which is implemented through multiplication by the modular inverse).

## Input

The first line contains a single integer `n` (1 ≤ n ≤ 100 000).

## Output

Output a single integer — the value of the matrix coefficient modulo `p = 10^9 + 7`.

## Example

**Input:**
```
5
```

**Output:**
```
119813081
```

## Constraints

- 1 ≤ n ≤ 100 000
- Modulus p = 1 000 000 007 (prime number)
- It is guaranteed that the modular inverse always exists for the given constraints

# Theory

Division Modulo in Modular Arithmetic

## Introduction

When solving computational problems modulo some number, the operation of division often arises. However, division in modular arithmetic is defined differently from ordinary mathematics. Direct division modulo is incorrect: the result \(a / b \pmod{p}\) cannot be obtained by simply computing \((a \bmod p) / (b \bmod p)\).

For example: \(10 / 5 = 2\), but \(10 \equiv 1 \pmod{3}\) and \(5 \equiv 2 \pmod{3}\), yet \(1 / 2 \not\equiv 2 \pmod{3}\).

## Modular Inverse

**Definition:** The modular inverse of a number \(a\) modulo \(p\) is a number \(a^{-1}\) such that:

$$a \cdot a^{-1} \equiv 1 \pmod{p}$$

The modular inverse exists only when \(\gcd(a, p) = 1\). For a prime modulus \(p\), the modular inverse exists for all \(a\) where \(1 \leq a < p\).

**Computing division via modular inverse:**

$$\frac{a}{b} \pmod{p} = a \cdot b^{-1} \pmod{p}$$

Thus, the division problem in modular arithmetic reduces to finding the modular inverse.

## Fermat's Little Theorem

**Theorem:** If \(p\) is a prime number and \(\gcd(a, p) = 1\), then:

$$a^{p-1} \equiv 1 \pmod{p}$$

From this, it follows that:

$$a \cdot a^{p-2} \equiv a^{p-1} \equiv 1 \pmod{p}$$

Consequently, the modular inverse can be computed using the formula:

$$a^{-1} \equiv a^{p-2} \pmod{p}$$

## Computing Modular Inverse via Binary Exponentiation

To efficiently compute \(a^{p-2} \pmod{p}\), we use the binary exponentiation algorithm (fast exponentiation), which runs in \(O(\log p)\) operations.

**Algorithm:**

```
binpow(a, n, p):
    result = 1
    while n > 0:
        if n is odd:
            result = (result * a) % p
        a = (a * a) % p
        n = n // 2
    return result

modularInverse(a, p):
    return binpow(a, p - 2, p)
```

**Advantages of this method:**
- Works for any prime modulus
- Simple implementation
- Time complexity \(O(\log p)\)

## Application in Combinatorics

Division modulo is especially common when computing factorials and binomial coefficients.

**Example:** Computing factorial modulo:

$$n! \pmod{p} = (1 \cdot 2 \cdot 3 \cdot ... \cdot n) \pmod{p}$$

If we need to compute several factorials, it makes sense to precompute them:

```
factorial[0] = 1
for i = 1 to n:
    factorial[i] = (factorial[i-1] * i) % p
```

## Inverse Factorials

If division by factorials is frequently required, it is worthwhile to precompute inverse factorials:

1. Compute \(n!\) in the usual way
2. Find the inverse: \((n!)^{-1} \equiv (n!)^{p-2} \pmod{p}\)
3. Compute the remaining inverses: \(((n-1)!)^{-1} = (n!)^{-1} \cdot n \pmod{p}\)

**Algorithm:**
```
factorial[0] = 1
for i = 1 to n:
    factorial[i] = (factorial[i-1] * i) % p

inverse_factorial[n] = binpow(factorial[n], p - 2, p)
for i = n - 1 down to 0:
    inverse_factorial[i] = (inverse_factorial[i+1] * (i+1)) % p
```

## Practical Example

Let us compute \(\frac{10}{3} \pmod{7}\):

1. Find \(3^{-1} \pmod{7}\): \(3^{7-2} = 3^5 = 243 \equiv 5 \pmod{7}\)
2. Verification: \(3 \cdot 5 = 15 \equiv 1 \pmod{7}\) ✓
3. Result: \(10 \cdot 5 = 50 \equiv 1 \pmod{7}\)

Let us verify: \(10 \equiv 3 \pmod{7}\), \(3 \div 3 = 1 \pmod{7}\) ✓

## Why Choose Modulus \(10^9 + 7\)?

- It is a prime number
- Large enough to avoid overflow when adding two 32-bit integers
- Large enough that the product of two 32-bit integers fits in a 64-bit long long
- Easy to code: \(10^9 + 7 = 1000000007\)

## Summary

Division modulo \(p\) reduces to multiplication by the modular inverse. For a prime modulus \(p\), the modular inverse of a number \(a\) can be computed using the formula \(a^{p-2} \pmod{p}\) according to Fermat's Little Theorem. This allows efficient division in modular arithmetic in logarithmic time.

