# Name

Minimal String Splitting into Dictionary Words Using Prefix Tree and Dynamic Programming

# Task

Minimal String Segmentation into Dictionary Words Using Trie and Dynamic Programming

Three lines will be passed to the console:
1. The number of words in the dictionary  
2. Dictionary words separated by spaces  
3. A string to be segmented

Without using built-in classes make a program that performs the following:

- Reads the input data from the console  
- Builds a prefix tree (Trie) from the dictionary words  
- Uses dynamic programming to find the minimal number of segments that divide the input string into valid dictionary words

Print the result to the console — the minimal number of segments.

### Sample Input:
```txt
3
aaa aa b
aaab
```
### Sample Output:
```txt
2
```

# Theory

Prefix Tree (Trie)

**Authors:** Sergey Slotin, Gleb Lobanov  

A prefix tree, or trie, is a data structure for compact string storage. It is organized as a tree where edges between nodes are labeled with characters, and some nodes are marked as terminal.

A trie accepts a string `s` if there exists a terminal node `v` such that the concatenation of all characters along the path from the root to `v` equals `s`.

## Applications

- **String storage:** When many strings share long common prefixes, a trie can use significantly less memory than an array or a set.
- **String sorting:** A depth-first traversal of the trie outputs all stored strings in lexicographic order.
- **Set of strings:** Tries support efficient insertion, deletion, and membership checks.

From the perspective of automata theory, each node is a state, and valid single-character extensions are transitions. Thus, a trie acts as an automaton that checks whether a word belongs to a set.

## Implementation

A trie is typically implemented as a set of interconnected nodes. Each node stores:

- A terminal flag
- Links to child nodes
- Optional metadata (e.g., word count for multisets)

For the Latin alphabet (26 lowercase letters), an empty trie can be initialized as follows:

```cpp
const int k = 26;

struct Vertex {
    Vertex* to[k] = {0}; // null pointer means no transition
    bool terminal = false;
};

Vertex* root = new Vertex();
```

