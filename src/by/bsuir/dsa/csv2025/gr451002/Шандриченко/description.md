# Name

Prefix-function

# Task

Minimal String Period

**Given:**  
A string `s` consisting of lowercase Latin letters  
String length: `1 ≤ |s| ≤ 10^6`

**Required:**  
Find the length of the minimal period of string `s`.

A period of a string is a string `t` such that `s` can be represented as a concatenation of one or more copies of `t`.

**Examples:**
- `"abcabcabc"` has period `"abc"` of length `3`
- `"aaaa"` has period `"a"` of length `1`
- `"abcde"` has period `"abcde"` of length `5`

---

**Sample Input:**  
```
abcabcabc
```

**Sample Output:**  
```
3
```

# Theory

Prefix Function

## Definition

The **prefix function** of a string $s$ of length $n$ is an array $\pi[0 \dots n-1]$, where $\pi[i]$ is defined as the length of the longest proper suffix of the substring $s[0 \dots i]$ that is also its prefix.

Formally:
$$
\pi[i] = \max_{0 \leq k \leq i} \{k : s[0 \dots k-1] = s[i-k+1 \dots i]\}
$$

A **proper suffix** means that $k < i+1$ (the suffix is not equal to the entire string).

## Example

For the string $s = "abcabcd"$:
- $\pi[0] = 0$ (for "a")
- $\pi[1] = 0$ (for "ab")
- $\pi[2] = 0$ (for "abc") 
- $\pi[3] = 1$ (for "abca" → suffix "a" = prefix "a")
- $\pi[4] = 2$ (for "abcab" → suffix "ab" = prefix "ab")
- $\pi[5] = 3$ (for "abcabc" → suffix "abc" = prefix "abc")
- $\pi[6] = 0$ (for "abcabcd")

## Computation Algorithm

```java
public static int[] computePrefixFunction(String s) {
    int n = s.length();
    int[] pi = new int[n];
    for (int i = 1; i < n; i++) {
        int j = pi[i - 1];
        while (j > 0 && s.charAt(i) != s.charAt(j)) {
            j = pi[j - 1];
        }
        if (s.charAt(i) == s.charAt(j)) {
            j++;
        }
        pi[i] = j;
    }
    return pi;
}
```

**Complexity**: $O(n)$, despite the nested while loop — amortized analysis shows that the total number of while loop iterations is $O(n)$.

## Properties

### Relation to String Period

A string $s$ has a **period** of length $p$ if:
$$
s[i] = s[i + p] \quad \text{for all } i = 0 \dots n-p-1
$$

**Theorem**: Let $len = n - \pi[n-1]$. Then:
- If $n$ is divisible by $len$, then $len$ is the minimal period of the string
- Otherwise, the string has no period (except itself)

**Example**: $s = "abcabcabc"$, $n=9$, $\pi[8]=6$, $len=9-6=3$, $9\%3=0$ → period of length 3.

### Other Properties

1. $\pi[i] < \pi[i+1] \leq \pi[i] + 1$
2. $\pi[i]$ is the length of the longest border (both prefix and suffix) for $s[0 \dots i]$
3. All values $\pi[i]$ are determined through previous values of the prefix function

## Applications

### 1. Knuth-Morris-Pratt Algorithm
Finding all occurrences of a substring in $O(n + m)$ time.

### 2. Finding String Period
As in our problem — in $O(n)$ time.

### 3. String Compression
A string can be represented as multiple repetitions of its minimal period.

### 4. Counting Distinct Substrings
Using the prefix function, the number of distinct substrings can be found in $O(n^2)$ time.

### 5. Partitioning String into Non-overlapping Occurrences
Finding how many minimal identical pieces a string can be divided into.

## Practical Notes

### Efficient Implementation

```java
public static int[] prefixFunction(String s) {
    int n = s.length();
    int[] pi = new int[n];
    for (int i = 1; i < n; i++) {
        int j = pi[i - 1];
        while (j > 0 && s.charAt(i) != s.charAt(j)) {
            j = pi[j - 1];
        }
        if (s.charAt(i) == s.charAt(j)) {
            j++;
        }
        pi[i] = j;
    }
    return pi;
}
```

### Finding Minimal Period

```java
public static int findMinimalPeriod(String s) {
    int n = s.length();
    int[] pi = computePrefixFunction(s);
    int candidate = n - pi[n - 1];
    return (n % candidate == 0) ? candidate : n;
}
```

### Example Usage

```java
public static void main(String[] args) {
    String s = "abcabcabc";
    int[] pi = computePrefixFunction(s);
    int period = findMinimalPeriod(s);
    
    System.out.println("Prefix function: " + Arrays.toString(pi));
    System.out.println("Minimal period: " + period);
}
```

