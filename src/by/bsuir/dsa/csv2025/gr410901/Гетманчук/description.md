# Name

HUFFMAN COMPRESSION ALGORITHM

# Task

Huffman compression algorithm

Objective: to implement text encoding and decoding by the method.

Requirements:
- Calculate the character frequencies of the input string.
- Build a prefixed binary Huffman tree using a priority queue.
- Generate binary codes: left junction is `0', right junction is `1'.
- Encode the string by checking the length of the bit sequence.
- Decode the tree and make sure that the result matches the original one.
- Check the prefix: no code is a prefix of another.

Entrance:
- Lines from `data.txt `.
- String `S`.

Exit:
- Bit string `B' (character → code, decoded string `D`).

Evaluation:
- Correctness of the construction of codes (prefix).
- Accurate decoding: `D == S`.

Implementation Tips:
- Use a priority queue for deterministic merging.
- Store the codes in `Map<Character, String>`.
- Traversing the tree to generate codes with prefix accumulation.


# Theory

Huffman compression algorithm

The idea is to replace the fixed encoding (8 bits per character) with *variable length* taking into account frequencies. Frequently occurring symbols are assigned shorter codes, while rare ones are assigned longer ones. In order for decoding to be unambiguous, the *prefix rule* is used: no code is a prefix of another.

Huffman optimality:
- Let the alphabet have the characters \( \{s_i\} \) with probabilities \( p_i \).
- The lengths of the codes \(\ell_i\) are chosen so as to minimize the average length
  [ L = \sum_i p_i \, \ell_i ]

The prefix constraint is formulated by the Kraft–McMillan inequality:
- [ sum_i 2^{-\ell_i} \le 1 ]

Building a Huffman tree:
1. Count the frequencies \(f_i\) of all characters.
2. Place each character in a node of the priority queue with the key \(f_i\).
3. Repeat until there is only one node left:
- Extract two nodes with the minimum frequency \(a, b\).
   - Create a parent with frequency \(a+b\), a left and a right child.
   - Return the parent to the queue.
4. Traversal of the tree: the left transition is bit `0`, the right one is bit `1'.

Features:
- The codes are prefixed, decoding is unambiguous (along the path from the root to the leaf).
- The total length of the encoded string:
  [ |B| = \sum_i f_i \cdot \ell_i ]
- Time complexity of building a tree for \(m\) different characters:
[ O(m\log m)]

