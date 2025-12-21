# Extended Modular Congruence

You are given three integers `a`, `b`, `m`. Determine whether there exists an integer `x` such that:

\[
a \cdot x \equiv b \pmod{m}
\]

If a solution exists, output **any** valid `x` in the range from `0` to `m-1`.  
If no solution exists — output `NO`.

### Input
Three integers:  
`a` `b` `m`, where  
- \(1 \le m \le 10^9\)  
- \(0 \le a, b \le 10^9\)

### Output
One integer `x` from `0` to `m-1`, or the string `NO`.

### Example
Input:

## Short Theory

The congruence

\[
a x \equiv b \pmod{m}
\]

has a solution if and only if:

\[
\gcd(a, m) \mid b.
\]

Let \(d = \gcd(a, m)\). Then the equation reduces to:

\[
\frac{a}{d} x \equiv \frac{b}{d} \pmod{\frac{m}{d}}.
\]

Since \(\gcd(a/d, m/d) = 1\), the multiplicative inverse exists:

\[
x \equiv \frac{b}{d} \cdot (a/d)^{-1} \pmod{m/d}.
\]

The inverse is obtained using the extended Euclidean algorithm.

