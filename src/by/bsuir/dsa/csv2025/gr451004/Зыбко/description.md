# Name

Automated Text Censorship System using the Aho-Corasick Algorithm

# Task

Automated Text Censorship System Using the Aho-Corasick Algorithm

## Assignment Objective

Develop an automated text censorship system that efficiently finds and masks forbidden words using the Aho-Corasick algorithm for multiple pattern matching in linear time.

## Technical Specification

### Functional Requirements

1. **Adding forbidden words** to the system dictionary
2. **Building an automaton** based on added words using BFS approach
3. **Searching for forbidden words** in arbitrary text
4. **Masking found words** by replacing them with '-' character
5. **Support for Latin alphabet** (a-z)

### Algorithmic Requirements

- Implementation of a prefix tree (trie) for storing forbidden words
- Calculation of suffix links for efficient transitions
- Implementation of automaton transitions for handling mismatches
- Ensuring time complexity of O(n + m), where:
  - n - text length
  - m - total length of all forbidden words

# Theory

---
title: Aho-Corasick Algorithm
weight: 2
author:
- Andrey Zybko
created: 2025
---

The Aho-Corasick algorithm solves the problem of searching for multiple substrings in a text. If the problem is solved "naively" by checking each word separately, the complexity is $O(m \cdot n)$, where $m$ is the total length of all words and $n$ is the length of the text. The Aho-Corasick algorithm solves this problem in $O(n + m + k)$, where $k$ is the total number of occurrences.

The event when the algorithm finds a substring occurrence in the text is called a *match*. In the censorship task, each such match leads to replacing the corresponding word with asterisks.

## Automaton Construction

*Practical rule:* for an alphabet of size $k$, each vertex contains $k$ pointers to children. In my implementation, I use the English alphabet ($k = 26$).

The main idea is to build a *prefix tree* (trie) from the set of forbidden words, and then augment it with *suffix links* and *automaton transitions*.

### Suffix Links

For a vertex $v$ corresponding to string $s$, the suffix link $link(v)$ leads to the vertex corresponding to the **longest proper suffix** of $s$ that is also a prefix of some forbidden word.

**Recurrence formula:**
$$
link(v) = 
\begin{cases} 
root & \text{if } v = root \text{ or } parent(v) = root \\
\delta(link(parent(v)), char(v)) & \text{otherwise}
\end{cases}
$$

### Automaton Transitions

The function $\delta(v, c)$ returns the vertex corresponding to the longest suffix of string $v + c$ that is accepted by the trie:

$$
\delta(v, c) = 
\begin{cases}
to[v][c] & \text{if the transition exists} \\
root & \text{if } v = root \\
\delta(link(v), c) & \text{otherwise}
\end{cases}
$$

## Optimization via BFS

Although suffix links can be computed lazily (through memoization), my implementation uses a BFS approach:

```java
public void buildAutomatonBFS() {
    Queue<Vertex> queue = new LinkedList<>();
    root.link = root;
    for (int i = 0; i < 26; i++) {
        if (root.to[i] != null) {
            root.to[i].link = root;
            queue.add(root.to[i]);
        } else {
            root.go[i] = root;
        }
    }
    while (!queue.isEmpty()) {
        Vertex v = queue.poll();
        for (int i = 0; i < 26; i++) {
            if (v.to[i] != null) {
                Vertex child = v.to[i];
                child.link = go(v.link, i);
                queue.add(child);
            }
        }
    }
}

