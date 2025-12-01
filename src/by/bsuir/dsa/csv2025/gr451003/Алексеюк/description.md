# Name

Counting Sort for a String

# Task

Counting Sort for a String

## Task

You are given a string `s` consisting of lowercase Latin letters (`'a'`–`'z'`).  
You must sort the characters of the string in ascending order of their ASCII codes (i.e., alphabetically) using the **counting sort algorithm**.

### Input format

- A single string `s` of length `n` (`1 ≤ n ≤ 10^6`), containing only lowercase Latin letters.

### Output format

- A single string containing the characters of the original string `s`, sorted in alphabetical order.

> **Remarks:**
> - Counting sort runs in time `O(n + k)`, where `k` is the size of the alphabet (here `k = 26`).
> - Algorithm:
>   1. Create an array `count` of size `k`, initialized to zeros.
>   2. Traverse the string and increment the counter for each character.
>   3. Traverse the `count` array and output each character as many times as it occurred.
> - Using built-in sorting functions is not allowed.

---

## Example 1

- Input:  
  `counting`

- Output:  
  `cginnotu`

---

## Example 2

- Input:  
  `algorithm`

- Output:  
  `aghilmort`



# Theory

Theoretical Info

## Counting Sort

### Definitions

**Counting Sort** is a non-comparison based sorting algorithm that works in linear time under certain conditions.

**Value range** — the set of all possible values that can appear in the data to be sorted. In our task, this is the 26 lowercase Latin letters.

**Frequency array** — an array where each element stores the count of occurrences of the corresponding value in the input data.

### Applicability Conditions

Counting sort is efficient when:
1. The range of possible values `k` is not too large compared to the number of elements `n`
2. The data are integers or can be mapped to integers

In our task:
- Value range: 26 letters (`k = 26`)
- Number of elements: up to 10^6 (`n ≤ 10^6`)
- Ratio `k ≪ n` makes the algorithm optimal

### Counting Sort Algorithm

#### Step 1: Initialize Frequency Array
Create an array `count` of size 26 (for letters 'a'-'z'), filled with zeros.

#### Step 2: Count Frequencies
Iterate through all characters in the input string and increment the corresponding counter

#### Step 3: Build Sorted String
Iterate through the frequency array and build the result

### Time Complexity

- **Counting frequencies:** O(n) — one pass through the string
- **Building result:** O(k) — pass through the alphabet
- **Total complexity:** O(n + k)

Since k = 26 is constant, the final complexity is O(n).

### Advantages and Limitations

**Advantages:**
- Linear time complexity
- Stability (preserves order of equal elements)
- Simple implementation

**Limitations:**
- Requires additional memory O(k)
- Efficient only with small value ranges

