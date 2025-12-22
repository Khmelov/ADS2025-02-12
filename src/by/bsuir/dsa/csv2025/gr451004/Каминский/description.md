# Name

Set with a Hash Table

# Task

Set with a Hash Table

You are given a sequence of queries to a set of integers. You have to support the following operations:

- `add x` — insert integer `x` into the set (if it is already present, do nothing);
- `del x` — erase integer `x` from the set (if it is not present, do nothing);
- `find x` — print `YES` if `x` is in the set, and `NO` otherwise.

All integers `x` are in the range `0 ≤ x < 10^9`.

Input format:

- the first line contains an integer `q` — the number of queries (`1 ≤ q ≤ 2 * 10^5`);
- each of the next `q` lines contains one query in one of the following formats:
  - `add x`
  - `del x`
  - `find x`

For every query of type `find`, print the answer on a separate line.

It is recommended to implement the data structure using a hash table (with chaining or open addressing).


# Theory

A hash table is a data structure that stores elements in an array using an index given by a hash function $h(x)$.

For integers a simple hash function is
$$h(x) = x \bmod m,$$
where $m$ is the table size.

A more robust variant is
$$h(x) = ((a x + b) \bmod p) \bmod m,$$
where $p$ is a large prime and $a, b$ are parameters.

When different keys get the same hash value (a collision), it is handled by

- chaining (a linked list or dynamic array in each bucket), or
- open addressing (probing for the next free position).

With a good hash function the expected time for `add`, `del`, and `find` operations is $O(1)$.


