# Name

Quality control for transcripts via the prefix-function

# Task

Quality control for transcripts via the prefix-function

## Task condition

The transcription team of the “Listen.Online” hotline converts thousands of emergency calls into text. As soon as the phrase `PASSPORT BLOCKED` appears anywhere in a transcript, a fraud specialist must jump on the call. Manual scanning and naive substring checks do not scale, so the engineers adopt the prefix-function workflow described at https://ru.algorithmica.org/cs/string-searching/prefix-function/.

**Task:** given a pattern and a text (both may include spaces and punctuation), return every starting index where the pattern occurs inside the text. Implement the linear-time solution based on the prefix-function and justify its correctness.

## Requirements

1. Restate the definition of the prefix-function `pi[i]` and the idea of reusing matched prefix/suffix borders (refer to the Algorithmica article).
2. Prove why `pi[i] ≤ pi[i-1] + 1` and how this bound limits backtracking during the scan.
3. Explain why we build `pattern + "#" + text`, why the separator must be unique, and how `pi` values reveal matches in the text segment.
4. Argue about the `O(n + m)` time and `O(1)` extra memory (beyond the concatenated string) relative to pattern and text lengths.
5. Provide console I/O samples that cover zero matches, a single match, and multiple overlapping matches rooted in the transcript-monitoring scenario.

## Solution format

- A theoretical note that walks through prefix-function computation and links to the source material.
- Working code (or pseudocode) that computes `pi` and enumerates all match positions.
- Examples that reflect the fraud-alert phrase used by the quality-control team.

---

### Features of this task:

- **Realistic narrative:** stream processing for critical transcript triggers.
- **String-centric practice:** focuses on linear substring search rather than games.
- **Bridge to KMP:** mastering the prefix-function paves the way for the full KMP algorithm.
- **Behavioral tests:** covers empty outputs and overlapping detections.

This task targets developers who want industry motivation for string algorithms and a fast track toward production-grade pattern recognition.

# Theory

---
author: Nikolay Kureychik
weight: 1
---

# Prefix-function as a transcript monitoring primitive

The following note summarizes https://ru.algorithmica.org/cs/string-searching/prefix-function/ and tailors it to the Listen.Online hotline, where analysts scan live transcripts for the phrase `PASSPORT BLOCKED`.

## Problem statement

Given a pattern `P` of length `m` and a text `T` of length `n`, report every index `i` such that `T[i..i+m-1] = P`. The system ingests thousands of lines per minute, so we need a linear-time solution that never rewinds the stream.

## Prefix-function definition

For a string `S`, the prefix-function `pi[i]` equals the length of the longest proper prefix of `S` that matches the suffix ending at `i`. Intuitively, `pi[i]` encodes how many characters we already matched before hitting `i`. As Algorithmica proves, the values obey

```
pi[i] ≤ pi[i-1] + 1
```

Indeed, the current border can only grow by one character. If the next symbol does not match, we jump to the border of the border (`pi[j-1]`), which guarantees amortized constant work per character.

## Computing `pi`

Single pass procedure:

1. Initialize `pi[0] = 0`.
2. For each position `i`, copy the previous border `j = pi[i-1]`.
3. While `j > 0` and `S[i] ≠ S[j]`, shrink the border: `j = pi[j-1]`.
4. If the symbols coincide, increment `j`.
5. Assign `pi[i] = j`.

Thanks to the monotone behavior of `j`, the total cost is `O(|S|)`.

## Using it for substring search

Concatenate

```
S = P + "#" + T
```

The separator `#` must not show up in `P` or `T`, otherwise it could create false borders. After computing `pi` over `S`, any position `i` with `pi[i] = m` signals that the pattern ended in the text segment. The starting index inside `T` equals `i - 2m`.

This technique lets the hotline monitor read the text once while still reusing the partial matches from the prefix-function table.

## Correctness sketch

1. `pi[i]` never exceeds `m`, so a value of `m` implies the latest symbol belongs to `T`.
2. A full border of size `m` matches the entire pattern, hence we discovered an occurrence.
3. Whenever the match fails, we jump to the largest possible smaller border, ensuring we do not miss any occurrences.

## Complexity

- Time: `O(n + m)` — one scan of the concatenated string.
- Memory: `O(n + m)` to store `S` and its `pi` array; auxiliary counters use `O(1)` space.

## Example

Let `P = "PASSPORT BLOCKED"` and `T = "agent wrote PASSPORT BLOCKED twice"`. After building `S` and its prefix-function, we will see `pi[i] = |P|` at the locations where the trigger phrase ends. Each such match triggers an automated alert for the fraud specialist.

## Embedding into the product workflow

1. Buffer the latest transcript slice, append it to the pattern with a separator, and extend the `pi` computation.
2. Emit alerts whenever `pi[i] = m`.
3. Carry metadata (timestamp, operator id) alongside the index to aid investigations.

Consequently, the prefix-function moves from textbook theory to a practical guardrail for real-time transcript quality control.

