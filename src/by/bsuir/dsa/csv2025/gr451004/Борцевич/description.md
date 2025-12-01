# Name

Matrices

# Task

#Matrices
It is necessary to write an algorithm that will find the determinant of a square matrix.

# Theory

---
title: Matrices

weight: 5

authors:

- Sergey Slotin

- Maxim Ivanov

created: 2019

---

## Linear Functions

**Definition.** A function $f: \mathbb{R}^n \to \mathbb{R}^m$ is called *linear* if it satisfies two conditions:

- $f(x + y) = f(x) + f(y)$ (additivity)

- $f(ax) = af(x); a \in \mathbb{R}$ (homogeneity)

**Examples of linear functions:**

- A function that "stretches" a vector $k$ times: $f(x) = kx$

- A function that rotates a vector on a plane by an angle $\theta$

- A function that projects a three-dimensional vector onto some plane

- The scalar product $f(x, y) = x \cdot y = \sum x_k y_k$ is also linear with respect to both parameters

**Properties derived from the definition:**

- The sum of linear functions is a linear function

- The composition of linear functions $f(g(x)) = (f \circ g)(x)$ is a linear function

- The sum of linear functions is commutative: $f + g = g + f$

- The sum of linear functions is associative: $(f + g) + h = f + (g + h)$

- The composition of linear functions is associative: $(f \circ g) \circ h = f \circ (g \circ h) = f \circ g \circ h$

- Composition is generally not commutative

**Conclusion:** Linear algebra deals with the study of linear functions.

### Matrices

Any linear function $f: \mathbb{R}^n \to \mathbb{R}^m$ can be represented as a system of linear equations:

$$

f(x) = \begin{pmatrix}

a_{11} \cdot x_1 + a_{12} \cdot x_2 + \dots + a_{1n} \cdot x_n \\

a_{21} \cdot x_1 + a_{22} \cdot x_2 + \dots + a_{2n} \cdot x_n \\

\vdots \\

a_{m1} \cdot x_1 + a_{m2} \cdot x_2 + \dots + a_{mn} \cdot x_n

\end{pmatrix}

$$

Matrices are simply a very compact notation for these coefficients $a_{ij}$:

$$

A = \begin{pmatrix}

a_{11} & a_{12} & \dots & a_{1n} \\

a_{21} & a_{22} & \dots & a_{2n} \\

\vdots & \vdots & \ddots & \vdots \\

a_{m1} & a_{m2} & \dots & a_{mn}

\end{pmatrix}

$$

Each linear function $f$ from $\mathbb{R}^n$ to $\mathbb{R}^m$ corresponds to some matrix $A$ of size $m \times n$ and vice versa. The number $m$ equals the number of rows, and $n$ equals the number of columns. The element at the intersection of the $i$-th row and $j$-th column will be denoted as $A_{ij}$.

The original expression for $f(x)$ can now be compactly written as $f(x) = Ax$ instead of $m$ equations with $n$ terms in each.

### Connection with Vectors

If a vector is an ordered set of scalars, then a matrix can be viewed as a vector of vectors. A vector, in particular, can be represented as a matrix where one of its dimensions is equal to one — then it is called a column vector or a row vector.

```cpp

typedef vector<vector<int>> matrix;

```

There are also tensors — these are all objects of even higher order: vectors of matrices (a three-dimensional tensor), matrices of matrices (a four-dimensional tensor), and vectors of matrices of matrices, and so on. Tensors have their own interesting algebra, but in contexts where an ordinary programmer encounters them, no algebra is usually implied, and this term is used simply because the phrase "multidimensional array" has too many letters.

### Matrix Multiplication

Let a linear function $f$ correspond to matrix $A$, and a function $g$ correspond to matrix $B$. Then the composition of these functions $h = f \circ g$ will correspond to the product $C$ of matrices $A$ and $B$, defined as follows:

$$

C = AB : C_{ij} = \sum_{k=1}^{k} A_{ik}B_{kj}

$$

The reader can verify this by carefully writing out the substitution of formulas for $f$ into $g$.

When multiplying matrices manually, it's convenient to think of it this way: the element at the intersection of the $i$-th row and $j$-th column is the scalar product of the $i$-th row of $A$ and the $j$-th column of $B$. Note that this imposes a restriction on the dimensions of the matrices being multiplied: if the first matrix has size $n \times k$, then the second must have size $k \times m$, meaning the "middle" dimensions must match.

**Matrix multiplication implementation:**

```cpp

const int n, k, m;



matrix matmul(matrix a, matrix b) {

    matrix c(n, vector<int>(m, 0));

    for (int i = 0; i < n; i++)

        for (int j = 0; j < m; j++)

            for (int t = 0; t < k; t++)

                c[i][j] += a[i][t] * b[t][j];

    return c;

}

```

This implementation, though the simplest, is not optimal: at each iteration, we move the pointer for $B$ $m$ steps forward, which leads to unnecessary cache line loads and prevents the compiler from applying auto-vectorization. However, this is easy to fix if $B$ is transposed before all loops, i.e., by swapping each of its $(i, j)$-th elements with its $(j, i)$-th element — such an implementation will work 5-10 times faster.

There are ways to optimize matrix multiplication much further — 50-100 times compared to the naive approach — but they are beyond the scope of this article. Science also knows ways to multiply matrices asymptotically faster than $O(n^3)$, but in practice, they become effective only for matrices of several thousand elements.

### Properties of Matrices

Matrices should not be treated as tables containing some numbers. Each matrix corresponds to some linear function that transforms vectors. The central objects of linear algebra are precisely linear functions, not matrices.

Thanks to this one-to-one correspondence, all previously mentioned properties of linear functions are transferred to matrices as well:

- The sum of matrices $A$ and $B$ is also a matrix: $C = A + B : C_{ij} = A_{ij} + B_{ij}$

- Matrix addition is commutative: $A + B = B + A$

- Matrix addition is associative: $(A+B) + C = A + (B+C)$

- Matrix multiplication is associative: $(AB)C = A(BC) = ABC$

- Matrix multiplication is generally not commutative

Matrices do not necessarily have to be considered only for real numbers — all these properties extend to arbitrary fields: sets for which $*$ and $+$ are defined with certain restrictions on operations.

The most popular class of such fields is residues modulo a prime. In the special case when $p = 2$, the field will have only two elements — zero and one — as well as XOR for addition and AND for multiplication. This allows matrices to be efficiently stored as bit sequences.

### Examples of Matrices

**Matrix "double everything":**

$$

\begin{pmatrix}

2 & 0 & 0 \\

0 & 2 & 0 \\

0 & 0 & 2

\end{pmatrix}

$$

**Matrix "swap x and y":**

$$

\begin{pmatrix}

0 & 1 \\

1 & 0

\end{pmatrix}

$$

**Rotation matrix by angle $\alpha$ in a plane:**

$$

\begin{pmatrix}

\cos \alpha & -\sin \alpha \\

\sin \alpha & \cos \alpha

\end{pmatrix}

$$

**Projection matrix onto the $xy$-plane in three-dimensional space:**

$$

\begin{pmatrix}

1 & 0 & 0 \\

0 & 1 & 0 \\

0 & 0 & 0

\end{pmatrix}

$$

**Matrix "do nothing", also known as the identity matrix:**

$$

I_3 = \begin{pmatrix}

1 & 0 & 0 \\

0 & 1 & 0 \\

0 & 0 & 1

\end{pmatrix}

$$

The identity matrix is usually denoted as $I$ or $E$. On its main diagonal there are always ones, and outside — zeros.

