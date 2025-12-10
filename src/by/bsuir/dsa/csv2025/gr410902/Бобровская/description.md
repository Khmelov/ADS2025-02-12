# Name

Air Pollution Hotspot Detection and Map Normalization

# Task

**Given:**

- A two-dimensional array airMap[M][N], where airMap[i][j] is the air pollution level at point (i,j) (integer values from 0 to 500).
- An integer K — the size of the side of the square zone to analyze.
- An integer T — the threshold for the average pollution level.

**Required:**

- Find all square zones of size K×K in airMap where the average air pollution level is maximal.
- From the found zones, keep only those where the average pollution is greater than or equal to the threshold T.
- Construct a new map normalizedMap of the same size, normalizing the values of the original airMap to the range [0, 1] using min–max normalization.

**Expected output:**

- A list of coordinates of the top-left corner of all K×K zones that satisfy the condition.
- A two-dimensional array normalizedMap[M][N] with normalized values rounded to two decimal places.

# Theory

---
title: Arithmetic
weight: 2
authors:
- Anna Bobrovskaya
created: 2025
---

## Arithmetic and Bit Representation

### Bit Representation of Integers
All integers can be represented in binary:

5₁₀ = 101₂ = 4 + 1  
42₁₀ = 101010₂ = 32 + 8 + 2  
256₁₀ = 100000000₂ = 2^8  

At the hardware level, computers store numbers as fixed-length bit words: 8 bits (`char`), 16 bits (`short`), 32 bits (`int`), or 64 bits (`long long`).  

---

### Endianness
The order of bytes when storing a number in memory may vary depending on the architecture. This feature is called **endianness**.

- **Little-endian** — least significant bytes are stored first.  
- **Big-endian** — most significant bytes are stored first.  

---

### Bitwise Operations

#### Shifts
Binary representations can be shifted:

- `x << y` — left shift, equivalent to multiplying by 2^y  
- `x >> y` — right shift, equivalent to integer division by 2^y (rounded down)  

Shifts are very fast — usually completed in a single CPU cycle — so multiplication or division by a power of two is often optimized this way.  

#### Logical Bitwise Operations
Bitwise versions of logical operations are applied individually to each bit:

- AND (`&`)  
- OR (`|`)  
- NOT (`~`)  
- XOR (`^`)  

Examples:

13 & 7 = 1101₂ & 0111₂ = 0101₂ = 5  
17 | 10 = 10001₂ | 01010₂ = 11011₂ = 27  
17 ^ 9 = 10001₂ ^ 01001₂ = 11000₂ = 24  

---

### Masks and Subsets

A binary number can be viewed as a representation of a set: if the i-th bit is 1, element i is in the set; otherwise, it is not. Bitwise operations allow efficient manipulation of such sets.

- **Set the i-th bit:** `x |= (1 << i)` — adds the element to the set  
- **Invert the i-th bit:** `x ^= (1 << i)` — toggles the element (removes if present, adds if absent)  

Example:

```cpp
int x = 5;        // 0101₂
bool bit2 = (x >> 2) & 1; // check the 2nd bit -> 1
x |= (1 << 1);    // set the 1st bit -> 0111₂ = 7
x &= ~(1 << 0);   // clear the 0th bit -> 0110₂ = 6
x ^= (1 << 2);    // invert the 2nd bit -> 0010₂ = 2


