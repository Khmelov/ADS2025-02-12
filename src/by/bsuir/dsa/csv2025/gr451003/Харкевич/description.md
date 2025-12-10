# Name

Finding Ancestor-Descendant Pairs in Tree
 
# Task

A non-empty sequence of character groups separated by spaces is given. Each group defines some binary code. Also given integers K and D.

For a given sequence:

1. Create a binary search tree where keys are binary codes (lexicographical comparison).
2. For each pair of vertices (A, B), where A is an ancestor of B, compute the XOR of values on the path from A to B and output all such pairs with their XOR values.
3. Find the number of such vertex pairs where:
   - the XOR result contains exactly K set bits
   - the distance between A and B in the tree does not exceed D
   - the sum of the counts of set bits in codes A and B is a prime number
4. Output the count of valid pairs and the pairs themselves.

> **Remarks:**
> - distance between vertices is measured by the number of edges on the path between them;
> - the path includes all vertices from A to B inclusive;
> - 1 is not considered a prime number;
> - pairs are output in format (A,B):XOR.

## Example

- Input:<br>
  `0001 1000 1010 0010 0101 2 3`<br>
- Expected output:<br>
`1`<br>

**Explanation:**
- All ancestor-descendant pairs with their XOR values
- Valid pairs: (1,2) and (1,8), because:
  - XOR contains exactly 2 set bits (3=11₂, 9=1001₂)
  - Distance ≤ 3
  - Sum of bits A and B is prime (1+1=2, 1+1=2)

# Theory

## Binary Search Tree

Binary search tree — a data structure with ordered keys where for each node:
- All keys in left subtree are less than node's key
- All keys in right subtree are greater than node's key

## Bitwise XOR

XOR (exclusive OR) — bitwise operation:
- 0 XOR 0 = 0
- 0 XOR 1 = 1
- 1 XOR 0 = 1
- 1 XOR 1 = 0

**Properties:**
- Associativity: (a XOR b) XOR c = a XOR (b XOR c)
- a XOR a = 0
- a XOR 0 = a

## Prime Numbers

Prime number — a natural number greater than 1 that has exactly two divisors: 1 and itself.

## Solution Algorithm

1. Build BST from binary codes
2. Recursive traversal to find all ancestor-descendant pairs
3. Compute path XOR for each pair
4. Check conditions: XOR bit count, distance, sum of bits primality

