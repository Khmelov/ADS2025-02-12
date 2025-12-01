# Name

Palindromic Substrings

# Task

Given a string `s`, return the number of **palindromic substrings** in it.

# Theory

**A palindrome** is a sequence of characters $a_i$ of length $n$ such that $a_i = a_{n-i}$.

We define the dynamic function $f[l, r]$ equal to one if the substring from $l$ to $r$ is a palindrome, and zero otherwise. By calculating this dynamic function, we find all palindromic substrings in the original string, and to get the answer, we can simply sum all the cells of this dynamic function.

The definition of a palindrome can be alternatively formulated as follows: the string $s$ is a palindrome if $s_0 = s_n$ and the string $s_{1\ldots n - 1}$ is a palindrome. We'll use this definition to recalculate the dynamics:

$$
f[l, r] = (s_l = s_r) \land f[l + 1, r - 1]
$$

We'll create a *two-dimensional* array to store the dynamics and iterate through the base cases: substrings of size 1, which are always palindromes, and substrings of size 2, which are palindromes if the first character is equal to the second. For the remaining cases, we'll use the formula above.

The only caveat is the order of traversal. We can't simply iterate through the for loop first in order of increasing $l$, and then increasing $r$, because the recalculation depends on the dynamics with larger $l$. Instead, we'll iterate over the *size* of the substring $d = r - l + 1$, and then iterate through all substrings of that size. Then, for any substring, the dynamics for $f[l + 1][r - 1]$ will already be calculated.

