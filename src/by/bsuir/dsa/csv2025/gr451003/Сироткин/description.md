# Name

Comb sort

# Task

Network Driver: Reordering TCP Packets

You are writing a data processing module for a network gateway. Data arrives as packets, each having a sequence number (`seq_id`).

Due to network jitter and routing inconsistencies, packets often arrive slightly out of order. For example, instead of `1, 2, 3, 4`, you might receive `1, 3, 2, 4`. The array is *almost* sorted.

You need to implement the **Insertion Sort** algorithm.
While generally $O(n^2)$, on *nearly sorted* arrays it runs in $O(n)$, making it faster than QuickSort or MergeSort for this specific scenario.

Write a function that takes a packet buffer (array of `seq_id` integers) and sorts it.

### Example

**Input (Buffer):**
`[101, 102, 104, 103, 105, 107, 106]`

**Output (Stream):**
`[101, 102, 103, 104, 105, 106, 107]`

# Theory

---
title: Insertion Sort
weight: 1
authors:
- Sirotkin Yaroslav
created: 2025
---

Most textbooks describe **Insertion Sort** as a slow algorithm with quadratic complexity $O(n^2)$. This is true for random data. However, in real-world engineering, it is invaluable due to its **adaptivity**.

If an array is partially sorted (the number of inversions is small), the running time approaches $O(n)$. This is why complex hybrid algorithms like Timsort (used in Python and Java) switch to Insertion Sort when processing small or nearly sorted chunks of data.

## The Algorithm (Card Analogy)

Imagine picking up playing cards from a table one by one and arranging them in your hand:

1.  Pick up the first card (packet). It is sorted by itself.
2.  Pick up the next card. Compare it with the cards already in your hand, moving from right to left.
3.  If the new card is smaller than the current one in your hand, shift the current card to the right.
4.  Repeat shifting until you find the correct spot for the new card (or reach the beginning).
5.  Insert the card into the empty slot.

## Formal Steps

1.  Iterate from $i = 1$ to $n-1$.
2.  Store the current element: $key = A[i]$.
3.  Initialize the comparison index: $j = i - 1$.
4.  While $j \ge 0$ and $A[j] > key$:
    *   Shift the element to the right: $A[j+1] = A[j]$.
    *   Decrement the index: $j = j - 1$.
5.  Insert the stored key into the correct position: $A[j+1] = key$.

In the case of our network packets, the inner `while` loop will execute very rarely (only when a packet is out of order).

