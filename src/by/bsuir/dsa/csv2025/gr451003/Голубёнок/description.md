# Name
Multiple pattern string matching (Aho–Corasick algorithm)

# Task
Multiple pattern string matching (Aho–Corasick Algorithm)

## Problem Statement
Develop a program to search for multiple substrings (patterns) in a long string (genome) using the **Aho–Corasick algorithm**. The program should find **all occurrences** of each pattern and return their positions in the original string.

Features:
- The genome string consists of characters `A`, `C`, `G`, `T`.
- The set of patterns (mutations) can contain from a few to hundreds of elements.
- Positions should be **zero-based** (0-based indexing).
- The algorithm must work efficiently even for large genomes (e.g., length > 1 million characters).

### Input
1. **Genome:** a string of characters `A`, `C`, `G`, `T`.
2. **Mutations:** an array of strings, each string is a sequence of `A`, `C`, `G`, `T` that needs to be found in the genome.

### Output
For each pattern, return a list of all **starting indices** of occurrences in the genome.

Example:
Genome: "AGCTTTGCAAGCTTCGTAA".

Mutations: ["AGCTT", "TTGCA", "CGTAA"]

Result:
AGCTT → [0, 9]
TTGCA → [4]
CGTAA → [13]

## Theory
Algorithms that use **hashing** have one significant drawback — they are **non-deterministic**.  
If we generate infinite examples, sooner or later a case will occur where two different objects produce the same hash. This is called a **collision**.

In bioinformatics, this can be especially critical. For example, if we are counting distinct DNA fragments in a long genome, a hash collision could mix up two different sequences, leading to incorrect analysis.

To perform a reliable search, we can use the **Aho-Corasick algorithm**, which allows searching for multiple patterns simultaneously and **guarantees** detection of all matches.

---

### Theoretical Introduction
A DNA molecule consists of four types of nucleotides:

$$
\text{Alphabet} = \{ A,\; C,\; G,\; T \}
$$

Suppose we are given two strings:

$$
S_1 \quad \text{and} \quad S_2,
$$

we need to determine whether `S2` is a **mutation** of `S1`:

$$
\text{mutated}(S_1, S_2) =
\begin{cases}
\text{true}, & \text{if the strings differ by at most one operation},\\[2mm]
\text{false}, & \text{otherwise}.
\end{cases}
$$

---

### Allowed Operations
1. **Substitution**  
   One nucleotide is replaced:
   $$
   ACGT \rightarrow AGGT
   $$

2. **Insertion**  
   $$
   ACGT \rightarrow ACGGT
   $$
3. **Deletion**  
   $$
   ACGT \rightarrow AGT
   $$

---

### Aho-Corasick Algorithm: Key Idea
The algorithm builds a **Trie (prefix tree)** for all known patterns (e.g., DNA fragments) and extends it with:

1. **Failure links** — allow quickly jumping to the longest possible matching prefix if the current path fails to match the text.
2. **Automaton transitions** — after reading each character, determine the automaton state (which prefix matches the text).

This enables searching through long texts **in linear time relative to the text length**, independent of the number of patterns:
$$
O(|\text{text}| + \text{total length of patterns})
$$
---

### Main Steps
1. **Building the Trie**  
   All patterns are inserted into the tree. Each node represents a prefix of one or more patterns.

2. **Building Failure Links**  
   For a node `v`, the failure link `link(v)` points to the node corresponding to the **longest prefix** of another pattern that matches a suffix of the string corresponding to `v`.

   Formula:
   $$
   link(v) = \delta(link(v.\text{parent}), v.\text{char})
   $$

3. **Automaton Transitions**  
   The automaton transition `go(v, c)` leads to the node corresponding to the maximal acceptable suffix of the string `v + c`.

    - If there is no direct transition, follow the failure link.
    - If a direct transition exists, follow it.

4. **Text Traversal**  
   Each text character is processed in constant time via automaton transitions.  
   If the current state is terminal, a match is found.

---

### DNA Mutation Check
The logic of the function:

1. If the strings have equal lengths — check for **one substitution**.
2. If the lengths differ by 1 — check for **one insertion or deletion**.
3. Otherwise — immediately `false`.

Formally:

$$
|\,|S_1| - |S_2|\,| > 1 \;\Rightarrow\; \text{false}
$$