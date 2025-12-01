# Name

Twin Prime Search

# Task

Twin Prime Search

Twin primes are pairs of prime numbers that differ by exactly 2.  

Research on twin primes is important not only in pure mathematics but also in real-world applications:
they appear in cryptography, in the study of prime distributions, in analyzing large numerical sequences, and in optimizing algorithms that rely on prime number structures.
As modern cryptographic systems depend heavily on large prime numbers, understanding properties of closely spaced primes — including twin primes — helps develop more secure protocols and study the behavior of prime-generation algorithms.

Examples:

- (3, 5)
- (5, 7)
- (11, 13)
- (17, 19)

Your task is to use the **Sieve of Eratosthenes** to find all twin prime pairs in the interval from 1 to n.

## Input format

A single integer n.  
Constraints:

1 ≤ n ≤ 10⁷



## Output format

Print all twin prime pairs in ascending order in a single line, separated by spaces, in the format:

p1 p2 p3 p4 ...

If no such pairs exist, print nothing.

## Example

Input:
20



Output:
3 5 5 7 11 13 17 19



# Theory

---
title: Sieve of Eratosthenes
weight: 2
authors:
- Sergey Slotin
created: 2019
---

**Definition.** A positive integer is called *prime* if it has exactly two distinct natural divisors - one and itself. One is not considered a prime number.

**Sieve of Eratosthenes** - an algorithm for finding all prime numbers from $1$ to $n$.

## Algorithm Idea

The main idea corresponds to the name of the algorithm: write down the series of numbers $1, 2, \ldots, n$, and then cross out:

* first, numbers divisible by $2$, except the number $2$ itself,
* then numbers divisible by $3$, except the number $3$ itself,
* with numbers divisible by $4$, we do nothing - we've already crossed them out,
* then we continue crossing out numbers divisible by $5$, except the number $5$ itself,
* …and so on.

The simplest implementation might look like this:

```cpp
vector<bool> sieve(int n) {
    vector<bool> is_prime(n + 1, true);
    for (int i = 2; i <= n; i++)
        if (is_prime[i])
            for (int j = 2 * i; j <= n; j += i)
                is_prime[j] = false;
    return is_prime;            
}
```

This code first marks all numbers except zero and one as prime, and then begins the process of filtering out composite numbers. To do this, we iterate through all numbers from $2$ to $n$ in a loop, and if the current number is prime, we mark all numbers that are multiples of it as composite.

If memory allows, it's better to use `vector<char>` instead of `vector<bool>` for speed optimization - but it will take 8 times more space. Computers cannot directly operate with bits, so when indexing a `vector<bool>`, it first retrieves the necessary byte and then uses bit operations to get the desired value, which takes a considerable amount of time.

## Time Complexity

It's quite easy to show that the asymptotic time complexity of the algorithm is at least no worse than $O(n \log n)$: even if we entered the crossing-out loop for every number without first checking if it's prime, the total number of iterations would be

$$
\sum_k \frac{n}{k} = \frac{n}{2} + \frac{n}{3} + \frac{n}{4} + \ldots + \frac{n}{n} = O(n \log n)
$$

Here we used the asymptotic behavior of the harmonic series.

The original algorithm should have even better complexity. To find it more precisely, we need two facts about prime numbers:

1. The number of primes from $1$ to $n$ is approximately $\frac{n}{\ln n}$.
2. Prime numbers are distributed without large "gaps" and "clusters", meaning the $k$-th prime number is approximately equal to $k \ln k$.

We can roughly assume that a number $k$ is prime with "probability" $\frac{1}{\ln n}$. Then, the algorithm's running time can be more accurately estimated as

$$
\sum_k \frac{1}{\ln k} \frac{n}{k}
\approx
n \int \frac{1}{k \ln k} = n \ln \ln k \Big|_{2}^{n} = O(n \log \log n)
$$

The algorithm's complexity can be improved further, to $O(n)$.

## Linear Sieve

The main problem with the Sieve of Eratosthenes is that we mark some numbers as composite multiple times - as many times as they have distinct prime divisors. To achieve linear time complexity, we need to find a way to consider all composite numbers exactly once.

Let $d(k)$ denote the smallest prime divisor of $k$ and note the following fact: a composite number $k$ has a unique representation $k = d(k) \cdot r$, and the number $r$ has no prime divisors smaller than $d(k)$.

The optimization idea is to iterate through this $r$, and for each one iterate only through the necessary multipliers - namely, all from $2$ to $d(r)$ inclusive.

### Algorithm

Let's slightly generalize the problem - now we want to calculate for each number $k$ on the segment $[2, n]$ its smallest prime divisor $d_k$, not just determine whether it's prime.

Initially, we fill array $d$ with zeros, which means all numbers are assumed to be prime. During the algorithm's execution, this array will gradually be filled. Additionally, we'll maintain a list $p$ of all prime numbers found so far.

Now we iterate through numbers $k$ from $2$ to $n$. If this number is prime, meaning $d_k = 0$, then we assign $d_k = k$ and add $k$ to the list $p$.

Next, regardless of whether $k$ is prime, we begin the process of setting values in array $d$ - we iterate through the found prime numbers $p_i$ that don't exceed $d_k$, and make the assignment $d_{p_i k} = p_i$.

```cpp
const int n = 1e6;

int d[n + 1];
vector<int> p;
 
for (int k = 2; k <= n; k++) {
    if (d[k] == 0) {
        d[k] = k;
        p.push_back(k);
    }
    for (int x : p) {
        if (x > d[k] || x * d[k] > n)
            break;
        d[k * x] = x;
    }
}
```

The algorithm requires at least 32 times more memory than the regular sieve because we need to store a divisor (`int`, 4 bytes) instead of one bit per number. Although the linear sieve has better asymptotic complexity, in practice it also loses in speed to the optimized version of the Sieve of Eratosthenes.

## Applications

The array $d$ allows us to find the factorization of any number $k$ in time proportional to the size of this factorization:

$$
factor(k) = \{d(k)\} \cup factor(k / d(k))
$$

Knowing the factorization of all numbers is very useful information for some problems. The linear sieve is interesting not for its time complexity, but precisely for this array of smallest prime divisors.


