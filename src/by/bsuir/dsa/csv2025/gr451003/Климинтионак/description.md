# Name

Hash table with double hashing, deletion and dynamic extension

# Task

Hash Table with Double Hashing, Deletion, and Dynamic Resizing

Implement a hash table that supports:

* insertion `ADD x`
* search `FIND x`
* deletion `DEL x`
* output of internal state `PRINT`

The hash table uses open addressing with double hashing:
$$
h_1(x) = (a \cdot x + b) \bmod m
$$
$$
h_2(x) = 1 + (c \cdot x + d) \bmod (m - 1)
$$
$$
h(x, k) = (h_1(x) + k \cdot h_2(x)) \bmod m
$$
### Required features

1. **Deletion**
   A freed cell must be marked with the special value `DELETED`.

2. **Auto-resizing**
   If the load factor:
[
\text{load factor} = \frac{\text{number of NON-empty cells}}{m} \ge 0.7,
]
then the table size must be increased to the next prime number, and all elements must be rehashed.

3. **Commands**

```
ADD x     – insert x
DEL x     – delete x (ignore if it does not exist)
FIND x    – print its index or -1
PRINT     – print the table
```

### Output Format

* For each `FIND`: print the index or `-1`
* For `PRINT`: print lines of the form

```
i: EMPTY
i: DELETED
i: x
```

# Theory

Brief Theory

## 1. Load Factor

The load factor $(\alpha\\)$ is defined as:

$$
\alpha = \frac{n}{m}
$$

When $(\alpha > 0.7\\)$, the amortized cost of operations increases sharply, prompting a rehash — the creation of a new table.

## 2. Marking Deletion

With open addressing, a cell cannot simply be made empty:

$$
\text{NULL} \neq \text{DELETED}
$$

Otherwise, the search may incorrectly terminate prematurely.

## 3. Double Hashing

A complete table traversal is guaranteed if:

$$
\gcd(h_2(x), m) = 1
$$

Thus, the following hash function is used:

$$
h_2(x) = 1 + \big((cx + d) \bmod (m - 1)\big)
$$

