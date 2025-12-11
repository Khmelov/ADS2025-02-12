# Name

Design Dynamic Range Sum Array

# Task

Design Dynamic Range Sum Array

You need to develop a data structure called `DynamicStream` that simulates the behavior of a dynamic array but is optimized for frequent insertions, deletions, and range sum queries. The standard `ArrayList` in Java performs insertion and deletion in $O(N)$ time, which is too slow for large data volumes with mixed operations.

# Theory

Sqrt Decomposition and Unrolled Lists

**Weight**: 2  
**Authors**:
- Sinyutin Nazar
**Created**: 2025

------

Standard dynamic arrays (like `ArrayList` in Java or `std::vector` in C++) have one unpleasant drawback: linear shift complexity. If we simply insert elements at the beginning of the array, we have to shift the entire contents to the right each time. On LeetCode and CodeForces, problems often arise where a naive solution catches **Time Limit Exceeded (TLE)** — tests can be specifically generated consisting solely of insertions at the beginning (`index = 0`). There are even [guides](https://en.wikipedia.org/wiki/Unrolled_linked_list) on how to bypass this limitation by splitting data into parts.

The event when one block overflows and requires splitting is called a *split*. Suppose we are solving the problem of maintaining an array with insertions — we store elements as a linked list of small arrays (blocks). It's clear that if the block size is chosen incorrectly, we will either end up with a very long list of blocks or overly heavy operations inside a block, leading to TLE. How large should the block size $B$ be to avoid this?

## Choosing Constants

*Practical rule:* if you need to store $N$ elements, the optimal block size is a number on the order of $\sqrt{N}$. Justification — see the section on balance optimization.

This size cannot always be chosen statically — if the number of elements $N$ changes significantly (from 0 to $10^5$), a fixed constant might be inefficient. Instead, dynamic balancing (merging and splitting blocks) can be done.

You can also use a fixed "size" of around 300-500 (for $N=10^5$). This has several advantages:

- It's simple — no need to recalculate the root at every step.
- With this size, we fit into the processor cache (L1/L2 cache) — iterating over the small array (`ArrayList`) inside a block is extremely fast.
- Memory allocation is less frequent — Java/C++ don't like creating a million objects with 1 element each; it's better to create a thousand objects with 100 elements each.

Everything was fine with this approach until "sawtooth" load tests (many insertions and deletions at block boundaries) were invented. However, they are not added to all problems — keep this in mind.

The restrictions on choosing a specific $B$ (Block Size) are not so strict:

- It should not be less than $\sqrt{N}$ — otherwise, the list of blocks becomes too long, and finding the required block will take $O(N)$.
- It should not be too large — otherwise, insertion inside a block (shifting the tail) becomes linear again.

The main thing is to ensure your solution does not degenerate into a regular `LinkedList` (where block = 1 element) or a regular `ArrayList` (where block = $N$ elements).

## Balance Optimization

> To minimize the execution time of insertion and search operations in a block-based structure, the size of each block should approach the square root of the total number of elements.

A more general statement: for a structure of size $N$, the sum of the costs for finding a block and working inside the block is minimized when the block size is $\Theta(\sqrt{N})$.

**First proof** (for calculus enthusiasts). Let $T(N, B)$ be the operation time, where $N$ is the total number of elements, and $B$ is the size of one block.

We assume that on average, we need to traverse half the list of blocks to find the required one and shift half the elements inside the block.

The number of blocks is $N / B$.

$$T(N, B) \approx C_1 \cdot \frac{N}{B} + C_2 \cdot B$$

Let's try to find the minimum of the function $T$ with respect to the variable $B$. Take the derivative with respect to $B$ and set it to zero:

$$\begin{aligned}
    T'(B) & = -\frac{C_1 \cdot N}{B^2} + C_2 \\
    0 & = -\frac{C_1 \cdot N}{B^2} + C_2 \\
    \frac{C_1 \cdot N}{B^2} & = C_2 \\
    B^2 & = \frac{C_1}{C_2} \cdot N \\
    B & \approx \sqrt{N} \cdot \sqrt{\frac{C_1}{C_2}} \\
\end{aligned}$$

From the last expression, it's more or less clear that the optimum is achieved at $B \approx \sqrt{N}$ (adjusted for operation constants). If $C_1 \approx C_2$, then $B = \sqrt{N}$.

**Second proof** (via the inequality of means). Introduce two quantities: "navigation cost" $X = \frac{N}{B}$ and "shift cost" $Y = B$. We want to minimize their sum $X + Y$.

It is known that the product $X \cdot Y = \frac{N}{B} \cdot B = N$ is constant.

According to the Cauchy inequality (of arithmetic and geometric means), the sum of two positive numbers with a fixed product is minimized when these numbers are equal.

$$\frac{X + Y}{2} \ge \sqrt{XY} \implies X + Y \ge 2\sqrt{N}$$

Equality (the minimum sum) is achieved when $X = Y$, i.e., $\frac{N}{B} = B$, whence $B^2 = N \Rightarrow B = \sqrt{N}$.

*Note*: Formally, in practice, $C_1$ (traversing the list of references) is more expensive than $C_2$ (traversing the array in cache), so blocks are often made slightly larger than the pure $\sqrt{N}$.

### Bonus: "Meta-Problem"

You are given a compiled class `BlackBoxList` implementing this algorithm. You cannot see the code, but you know that it uses Sqrt decomposition with a fixed block size $B$. The problem has a time limit of 2 seconds. You have the ability to call the `insert` and `get` methods.

"Hack" the structure: write code that, through experimental means (measuring operation execution time), determines the hidden constant $B$ with an accuracy of 10%, using no more than $2 \cdot 10^5$ operations.

