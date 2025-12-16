# Name

CustomBitSet Data Structure Implementation

# Task


# CustomBitSet Data Structure Implementation

Implement a `CustomBitSet` data structure in Java that represents a dynamic set of bits with efficient set operations.

## Implementation Requirements

### Basic Functionality:

- **Constructors**:
    
    - Default constructor creating a bitset of standard size
        
    - Constructor with initial number of bits specification
        

### Core Operations:

- `set(int bitIndex)` - set the bit at specified position
    
- `set(int bitIndex, boolean value)` - set the bit to the specified value
    
- `clear(int bitIndex)` - clear the bit
    
- `clear()` - clear all bits
    
- `get(int bitIndex)` - get the bit value
    
- `flip(int bitIndex)` - flip the bit
    

### Bitwise Operations:

- `and(CustomBitSet other)` - logical AND
    
- `or(CustomBitSet other)` - logical OR
    
- `xor(CustomBitSet other)` - logical XOR
    
- `andNot(CustomBitSet other)` - set difference
    

### Utility Methods:

- `cardinality()` - number of set bits
    
- `isEmpty()` - check if empty
    
- `size()` - total size in bits
    
- `length()` - logical length (index of highest set bit + 1)
    
- `intersects(CustomBitSet other)` - check for intersection
    
- `copy()` - create a copy
    
- `equals(Object obj)` - compare with another object
    
- `toString()` - string representation
    

### Technical Requirements:

- Use `long` array for bit storage (64 bits per element)
    
- Implement dynamic expansion when needed
    
- Handle edge cases and exceptions properly
    
- Optimize operation performance

# Theory

Bit sets (BitSet) are one of the fundamental data structures in computer science, enabling efficient work with sets of bits and performing logical operations on them. However, the standard implementation may not always be suitable for specific tasks, which necessitates creating custom solutions.

## Bit Set Architecture

The core idea of `CustomBitSet` is storing bits in an array of 64-bit words (`long[]`), where each array element contains 64 consecutive bits. This allows efficient utilization of modern processors' hardware capabilities for bitwise operations.

### Index Calculation

Bit operations are used to access a specific bit:

```java
private static int wordIndex(int bitIndex) {
    return bitIndex >> ADDRESS_BITS_PER_WORD; // ADDRESS_BITS_PER_WORD = 6
}
```

Since $2^6 = 64$, the right shift operation by 6 is equivalent to integer division by 64.

### Bit Masking

Masking is used to work with individual bits within a word:

```java
// Setting a bit
words[wordIndex] |= (1L << bitIndex);

// Clearing a bit  
words[wordIndex] &= ~(1L << bitIndex);

// Checking a bit
(words[wordIndex] & (1L << bitIndex)) != 0
```

## Memory Management Strategies

### Dynamic Expansion

When performing operations with bits beyond the current size, expansion becomes necessary. The implementation uses two strategies:

- **Automatic expansion** (`sizeIsSticky = false`) - the array doubles when needed
- **Fixed size** (`sizeIsSticky = true`) - the size remains unchanged

### Performance Optimization

Using `long[]` instead of `int[]` or `byte[]` provides significant benefits due to:

- **Memory alignment** - 64-bit operations are more efficient on modern processors
- **Cache locality** - sequential access to adjacent bits in the same word
- **Vectorization** - potential use of SIMD instructions

## Optimizations for Specific Cases

### Counting Set Bits

The `cardinality()` method uses the built-in `Long.bitCount()` function, which compiles into a specialized processor instruction on most modern architectures:

```java
public int cardinality() {
    int count = 0;
    for (long word : words) {
        count += Long.bitCount(word);  // May use the POPCNT instruction
    }
    return count;
}
```

### Finding Logical Length

The `length()` method finds the last set bit using efficient operations:

```java
public int length() {
    // Find the last non-zero word
    int lastNonZeroWord = words.length - 1;
    while (lastNonZeroWord >= 0 && words[lastNonZeroWord] == 0) {
        lastNonZeroWord--;
    }
    
    if (lastNonZeroWord < 0) return 0;
    
    long lastWord = words[lastNonZeroWord];
    return (lastNonZeroWord + 1) * BITS_PER_WORD - Long.numberOfLeadingZeros(lastWord);
}
```

## Practical Recommendations

### Initial Size Selection

*Practical rule*: If you need to store $n$ bits, initialize `CustomBitSet` with a buffer of $1.5 \cdot n$ bits to avoid frequent expansions.

### Operation Performance

- **Setting/clearing a bit**: $O(1)$ amortized time
- **Bitwise operations**: $O(\lceil n/64 \rceil)$
- **Counting bits**: $O(\lceil n/64 \rceil)$, but with a high constant factor

### Memory Consumption

Each `CustomBitSet` instance consumes:
- Object header: 16 bytes
- `words` field: 4-8 bytes
- `sizeInBits` field: 4 bytes
- `sizeIsSticky` field: 1 byte
- Alignment: ~3 bytes
- `long[]` array: $8 \cdot \lceil n/64 \rceil$ bytes

**Total**: approximately $24 + 8 \cdot \lceil n/64 \rceil$ bytes.

## Mathematical Model of Bit Operations

Consider a bit set as a bit vector $B = (b_0, b_1, \ldots, b_{n-1})$, where $b_i \in \{0, 1\}$.

### Algebraic Properties

Bit operations form a Boolean algebra:

- **Commutativity**: $A \land B = B \land A$, $A \lor B = B \lor A$
- **Associativity**: $(A \land B) \land C = A \land (B \land C)$
- **Distributivity**: $A \land (B \lor C) = (A \land B) \lor (A \land C)$
- **De Morgan's laws**: $\neg(A \land B) = \neg A \lor \neg B$

### Probabilistic Analysis

For a random bit set of size $n$, where each bit is set independently with probability $p$:

- **Expected number of set bits**: $E[|B|] = n \cdot p$
- **Probability that two sets intersect**:

$$
P(A \cap B \neq \emptyset) = 1 - (1 - p_A p_B)^n
$$

where $p_A$ and $p_B$ are the probabilities of setting a bit in sets $A$ and $B$ respectively.

