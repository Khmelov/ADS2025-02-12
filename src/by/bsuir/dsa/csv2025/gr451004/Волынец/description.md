# Name

Hash function collision detection

# Task

Comparison of Hash Functions. Detecting Hash Collisions

Write a program in **Java** that takes two strings from the user and compares their hashes calculated by two different functions. The program should determine whether a collision occurs (the same hash for different strings).

## Requirements

1. **Implement two hash functions (16-bit):**
   - **Function A:** the sum of ASCII codes of the string characters modulo $2^{16}$.
   - **Function B:** the FNV-1a algorithm, which uses XOR and multiplication by a prime number, with the result limited to 16 bits (`& 0xFFFF`).

2. **Program workflow:**
   - Read **two strings** from the console.
   - Compute the hash of each string using both functions.
   - Print the hashes.
   - Indicate whether a collision occurs for each function.

# Theory

Hashing is the process of converting input data of arbitrary length (string, file, object) into a fixed-length output value (hash code or digest).  
**Purpose:** to create a unique "fingerprint" of the data for quick identification, comparison, or integrity verification.  

Hash codes are used:
- in data structures (e.g., `HashMap`, `HashSet`)
- in cryptography (e.g., `SHA-256`)

---

## Key Properties of a Good Hash Function

- **Determinism**: the same input string must always produce the same hash.  
- **Efficiency**: hash calculation should be fast.  
- **Diffusion (Avalanche effect)**: changing one bit in the input should change about half of the bits in the output hash.  
- **Confusion**: the relationship between input and output should be complex and nonlinear.  
- **Collision resistance**: it should be infeasible to find two different strings $S_1 \neq S_2$ such that $H(S_1) = H(S_2)$.

---

## Collisions and the Birthday Paradox

A collision occurs when two different strings are transformed into the same hash value:

$S_1 \neq S_2 \implies H(S_1) = H(S_2)$

Since the input space is infinite and the output space is finite (e.g., $2^{16}$ or $2^{64}$), collisions are inevitable by the pigeonhole principle.  
The goal of a good hash function is to make them rare and hard to find.

### Birthday Paradox
This principle shows how quickly collisions appear in a random hash space.

- Let $N$ be the size of the hash space (e.g., $N = 2^{16} = 65536$).  
- The probability of finding the first collision exceeds 50% after adding $k$ random elements, where:

$k \approx \sqrt{N}$

**Example:**  
In a 16-bit space ($N=65536$), the first collision is expected after:

$k \approx \sqrt{65536} = 256$

not after 65536.

---

## 🛠️ Comparison of Simple and Advanced Hash Functions

### 1. Simple ASCII Sum (Function A)
**Principle:**

$H = \left(\sum \text{ASCII}(c_i)\right) \pmod{N}$

**Drawbacks:**
- Poor diffusion: order of characters doesn’t matter (e.g., "ab" and "ba" → same hash).  
- Low sensitivity: replacing characters that balance the sum often leads to collisions.  

**Result:** collisions occur much earlier than predicted by $\sqrt{N}$.

---

### 2. FNV-1a (Function B)
**Principle:**  
Uses mixing (XOR) and multiplication by a special prime number (FNV Prime) for each character.

**Advantages:**
- Excellent diffusion: every bit of the previous hash affects the next calculation.  
- Good distribution: results are evenly spread across the hash space.  

**Result:** collisions occur close to the statistically expected value $\sqrt{N}$.

---

## Applications
- **Data structures:** fast access (`HashMap`, `HashSet`).  
- **Cryptography:** SHA-256, SHA-3 — data protection, digital signatures.  
- **Non-cryptographic tasks:** FNV-1a, MurmurHash, xxHash — balance of speed and quality.


