# Name

Finding a substring with errors

# Theory

## Bitap Algorithm (Baeza-Yates-Gonnet)

### Main Idea

The algorithm uses bits to compactly represent the matching state. Each bit corresponds to one position in the pattern. If a bit is set (equal to 1), it means we can reach that position in the pattern after processing the current character of the text.

Instead of storing a full DP table of size `text × pattern × errors`, we store only one number (a bitmask) for each error level.

### Preparation: Character Mask Table

Before starting the search, we create a `charMask` table where for each unique character, we store a mask of its positions in the pattern.

For example, for the pattern "cat":
- charMask['c'] = 001 (character 'c' is at position 0)
- charMask['a'] = 010 (character 'a' is at position 1)
- charMask['t'] = 100 (character 't' is at position 2)

If a character appears multiple times, its mask will contain multiple set bits.

### Search Process

For each character in the text, we update the `dp` array, where `dp[e]` is a bitmask representing the state after processing the current character with exactly e errors.

Initially: `dp[0] = 0` (no matches).

For each character in the text, four types of transitions occur:

**1. Character Match (no error)**

If the current character matches the expected one in the pattern:
- Shift the current state left by one bit (move forward one position in the pattern)
- Add 1 to the least significant bit (start a new potential match from the current position)
- Apply the character mask using bitwise AND to keep only positions where this character actually appears

**2. Character Substitution (error)**

If we want to treat the current character as a substitution:
- Perform the same shift and add 1 as in a match
- But place the result in `dp[e+1]` instead of `dp[e]` (spend one error)

**3. Insertion in Text (error)**

If we skip the current character in the text (treat it as an insertion):
- Shift the state left and add 1
- Place in `dp[e+1]` without applying the character mask

**4. Deletion from Pattern (error)**

If we skip a character in the pattern:
- Simply copy the current state without shifting to `dp[e+1]`

### Match Detection

After processing each character of the text, check if the last bit (bit m-1, where m is the pattern length) is set in any `dp[e]` where e ≤ maxErrors.

If yes — we found a match at the current position in the text (with e errors).

# Problem Description

You are given a text and a pattern to search for. You need to find all positions in the text where the pattern appears with a tolerance of k errors.

Errors include:
- Substitution — a character does not match the expected one
- Insertion — an extra character in the text
- Deletion — a missing character in the text

For each found match, output the position and the found substring in one line.

## Input

One line containing three parameters separated by a space:
`text pattern maxErrors`

- `text` — the source text (length up to 10^4, only letters and digits)
- `pattern` — the search pattern (length from 1 to 64, only letters and digits)
- `maxErrors` — maximum allowed number of errors (0 ≤ maxErrors < len(pattern))

## Output

If no matches are found, output:
```
none
```

If matches are found, output in one line pairs of `position substring` separated by spaces:
```
pos1 substr1 pos2 substr2 ...
```