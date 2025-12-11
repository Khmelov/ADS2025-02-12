# Name

"Fast" multiplication

# Task

"Fast" multiplication

Multiply two hexadecimal numbers in O(n^log2(3)).

## Input data

Two non-negative hexadecimal up to 50 digits each.

## Output data

Product of these two numbers in hexadecimal. Print "Error" if the input data is invalid.

## Examples

**Input data №1**

```
1e
2F0
```

**Output №1**

```
5820
```

**Input data №2**

```
35DA
2K0
```

**Output №2**

```
Error
```

# Theory

History

In 1960, Andrey Kolmogorov conjectured that classic "trivial" multiplication method that operates in O(n^2)$$ is the asymptotically optimal algorithm for multiplying numbers. Twenty-three-year-old Anatoly Karatsuba disproved this conjecture by creating an algorithm for multiplying numbers in any number system in O(n^{\log_2 3}) \approx O(n^{1.58})$$. This algorithm was the first to overcome the asymptotic complexity of O(n^2)$$.

# Algorithm

The algorithm operates using the "divide and conquer" method. The essence of the algorithm is that instead of multiplying two integers of length $$n$$ it multiplies integers of length $$\frac{n}{2}$$ three times. Thus, the total running time of the algorithm is:

$$T(n) = 3 \cdot T \left( \frac{n}{2} \right) + O(n) = O(n^{\log_2 3})$$

## Step 1

Split the numbers $$a_0q^0 + a_1q^1 + \ldots + a_{n-1}q^{n-1} + a_nq^n$$ and $$b_0q^0 + b_1q^1 + \ldots + b_{n-1}q^{n-1} + b_nq^n$$ in the $$q$$ number system into two polynomials of equal length of the form:

$$a(q) = a_1(q) + q^ka_2(q)$$
$$b(q) = b_1(q) + q^kb_2(q)$$

## Step 2

Calculate the product polynomials $$p_1$$ and $$p_2$$:

$$p_1(q) = a_1(q)b_1(q)$$
$$p_2(q) = a_2(q)b_2(q)$$

## Step 3

Calculate polynomial $$t$$:

$$t(q) = (a_1(q) + a_2(q)) \cdot (b_1(q) + b_2(q))$$

## Step 4

Calculate the product:

$$c(q) = a(q) \cdot b(q) = p_1(q) + q^k \cdot (t(q) - p_1(q) - p_2(q)) + x^{2k} \cdot p_2(q)$$

