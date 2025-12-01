# Name

Prefix Function

# Task

A company is developing a system for fast pattern search in large texts.  
To optimize the search, it is required to compute the **prefix function** of a string — an array  
$$
\pi[i],
$$
where each value shows the length of the longest proper suffix of the substring  
$$
s[0..i],
$$  
which is also its prefix.

Write a program that reads a string $$s$$, computes the prefix function array $$\pi$$, and outputs its values separated by spaces.

# Theory

Theory

The prefix function of a string is a key tool in string algorithms, especially in the Knuth–Morris–Pratt (KMP) algorithm. It allows efficient detection of repeating structures in a string and their use for substring search optimization.

Let a string be given:  
$$
s = s_0 s_1 \dots s_{n-1},
$$

and we need to build an array  
$$
\pi = (\pi_0, \pi_1, \dots, \pi_{n-1}),
$$

where each value  
$$
\pi_i
$$  
is the length of the longest proper prefix of the string  
$$
s[0..i],
$$  
which is also its suffix.

That is,  
$$
\pi_i = \max\{k : s[0..k-1] = s[i-k+1..i]\}.
$$

---

## How the Prefix Function is Computed

For each index \(i\), we attempt to extend the previously found border \(\pi[i-1]\).

Let  
$$
j = \pi_{i-1}
$$  
be the length of the longest prefix-suffix for the previous index.

Now check the characters:

### **1) If they match:**

If  
$$
s[i] = s[j],
$$  

then the match can be extended:  
$$
\pi_i = j + 1.
$$

### **2) If they do NOT match:**

If  
$$
s[i] \ne s[j],
$$  
then we "roll back" to:  

$$
j = \pi_{j-1},
$$

and try again.

### **3) If j becomes 0 and no match:**

$$
\pi_i = 0.
$$

Thus, when computing each value of \(\pi\), we only move along previously found borders, which ensures **linear complexity** of the algorithm.

---

## Algorithm Complexity

The prefix function is computed in:  
$$
O(n),
$$

because each character of the string is considered at most twice:  
— once when advancing \(i\),  
— once when rolling back \(j\).

---

## Applications of the Prefix Function

1. **KMP Algorithm (substring search in O(n))**  
   Allows finding a substring \(t\) in a text \(s\) without backtracking.

2. **Finding string periods.**  
   A string has period \(p\) if:  
   $$
   n \bmod (n - \pi_{n-1}) = 0.
   $$

3. **Counting occurrences of a prefix in suffixes.**

4. **Used in dynamic programming** for optimization in string-related problems.

5. **Computing borders** (prefixes that are also suffixes).

