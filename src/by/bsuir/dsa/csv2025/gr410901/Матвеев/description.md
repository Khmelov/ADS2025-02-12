# Name

Modular Arithmetic Operations

# Task

Modular Arithmetic Operations

Implement a program for computing various operations in modular arithmetic.

The program should support:
- Basic arithmetic operations modulo
- Modular exponentiation
- Inverse element calculation
- Solving systems of linear congruences

## Input Format
Input data is provided in various formats:
1. `a + b mod m`
2. `a * b mod m`
3. `a ^ b mod m` 
4. `a ^-1 mod m` (inverse element)
5. `x ≡ a mod b, x ≡ c mod d, ...` (system of congruences)
6. Simple arithmetic expressions

## Output Format
For each input expression, output the computation result.

# Theory

## Modular Arithmetic

### Definition
For integers $a$, $b$ and $m > 0$:
$$a \equiv b \pmod{m} \iff m \mid (a - b)$$

### Basic Properties
1. **Addition**: $(a + b) \bmod m = ((a \bmod m) + (b \bmod m)) \bmod m$
2. **Multiplication**: $(a \cdot b) \bmod m = ((a \bmod m) \cdot (b \bmod m)) \bmod m$
3. **Exponentiation**: $a^b \bmod m$ computed using fast exponentiation algorithm

### Inverse Element
The inverse element $a^{-1} \pmod{m}$ exists if and only if $\gcd(a, m) = 1$

### Chinese Remainder Theorem
For pairwise coprime moduli $m_1, m_2, \dots, m_k$, the system:
$$
\begin{cases}
x \equiv a_1 \pmod{m_1} \\
x \equiv a_2 \pmod{m_2} \\
\vdots \\
x \equiv a_k \pmod{m_k}
\end{cases}
$$
has a unique solution modulo $M = m_1 \cdot m_2 \cdot \dots \cdot m_k$

