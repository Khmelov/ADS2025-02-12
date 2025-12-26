## Name

Almost-palindromic substring

## Task

Given a string `text` and a number `k_len`, find all starting indices `i` such that the substring  
`text[i..i+k_len-1]` can be transformed into a palindrome by changing **at most one character**.

Return indices in increasing order.

## Constraints

0 <= |text| <= 2e5  
1 <= k_len <= |text|  
Characters: lowercase a..z  
Expected complexity: O(n * k_len) or an optimized sliding window approach.

## Examples

text = "abca"  
k_len = 4  
Output: [0]  
Explanation: "abca" → change 'b' to 'c' → "acca", a palindrome.

text = "abcdefg"  
k_len = 3  
Output: [0, 1, 2, 3, 4]

## Theory

Let substring S = text[i..i+m-1], where m = k_len.

S is almost-palindrome if the number of mismatches on symmetric positions is not greater than 1.

That means:  
number of j such that  
S[j] != S[m - 1 - j]  
is at most 1.

## Check

- Use two pointers: left = 0, right = m - 1
- Count mismatches
- If mismatches ≤ 1 → starting index i is valid

## Complexity

Time: O(n * k_len)  
Memory: O(1)
