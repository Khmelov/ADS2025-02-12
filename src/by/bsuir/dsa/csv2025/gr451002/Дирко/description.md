# Name

Eratosthenes's Sieve

# Task

Sieve of Eratosthenes

Write the function `sieve(n)` to find prime numbers using the ["Sieve of Eratosthenes"](https://en.wikipedia.org/wiki/Sieve_of_Eratosthenes).

- The sieve(n) function takes one argument, n, which is the upper limit for finding numbers. 
- The function returns a **string** containing the product of the prime numbers in the range [2, n). 
- If n < 2, the function should return "0". 
- Tip: The product may exceed the range of int values.

## Examples
- Input: `14`  
 Output: `"30030"`

- Input: `1`  
 Output: `"0"`

- Input: `-3`  
 Output: `"0"`

# Theory

Theory

The Sieve of Eratosthenes algorithm is one of the most famous and effective methods for finding all prime numbers up to a given number n. It was invented by the ancient Greek mathematician Eratosthenes of Cyrene around 250 BC.

The idea behind the algorithm is simple: we gradually "cross out" composite numbers, leaving only prime numbers.

## Basic Idea

1. Write down all the numbers from 2 to n.

2. Take the first uncrossed number—it's prime.

3. Cross out all its multiples.

4. Repeat steps 2–3 for the next uncrossed number.

5. When the square of the current number exceeds n, the algorithm is complete—all remaining numbers will be prime.

## Example

Let n = 30.
- Start with 2: cross out all multiples (4, 6, 8, …).
- The next prime is 3: cross out multiples (6, 9, 12, …).
- Next is 5: cross out multiples (10, 15, 20, …).
- Then 7: cross out multiples (14, 21, 28).
- We stop, since \(7^2 > 30\).

The remaining numbers are: **2, 3, 5, 7, 11, 13, 17, 19, 23, 29**.

## Practical Application

- Fast prime number finding for cryptography.
- Use in number theory problems.
- Optimization of algorithms that require checking the primality of numbers.

