# Name

Number of non-overlapping occurrences

# Task

Number of non-overlapping occurrences.
You are given two strings: S (the text) and P (the pattern).
Determine how many non-overlapping occurrences of P appear in S.

# Theory

The Knuth-Morris-Pratt (KMP) algorithm allows substring searching in O(n + m) time, where:
n is the length of S,
m is the length of P.
Key components of the algorithm:
The LPS array (Longest Prefix Suffix) stores, for each position, the length of the longest prefix of P that matches a suffix ending at that position.
When a mismatch occurs, the algorithm does not move backward in S, but instead uses the LPS array to skip unnecessary comparisons.
To count non-overlapping occurrences, after each full match of length m, you should not fall back according to LPS. Instead, continue the search starting immediately after the matched segment.

