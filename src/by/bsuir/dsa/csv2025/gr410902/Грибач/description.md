# Name

Mo`s algorithm with updates and counting of various numberssion

# Task

An array of integers `a` of length `n` is given.  
There are also two types of `q` operations.:

### Type 1 — request

1 l r

It is necessary to determine **the number of different numbers** in the segment `[l, r]` of the array **, taking into account all updates** performed prior to this request.

### Type 2 — Update

2 pos val

Change the value of the array element at the `pos` position to `val'.

> Updates only affect the queries that come after them in the input data.

---

## Required

Process all requests **offline** using the **Mo's algorithm with updates**:

- Move the boundaries of the segment `L` and `R`.
- Apply and roll back updates by "time" `T'.
- Maintain the number of different numbers through the `add(x)` and `remove(x)` functions.
- Compress the coordinates of the array values and updates.
- Sort queries by triple key:
1. Block `L`
2. Block `r`
3. Time `t`

---

## Input data format

n q
a1 a2 ... an
Then q lines — operations:
1 l r
2 pos val


- `1 ≤ n, q ≤ 200000`
- `ai` and `val' are any integers.

---

## Output data format

For each type 1 query, output one number — the number of different elements in the specified segment.  
The order of the output must match the order of the queries in the input data.

---

## Example

### Entrance

5 5
1 2 1 3 2
1 1 5
2 3 10
1 2 4
2 5 1
1 1 5


### Exit

3
3
4

# Theory

## **1. Problem Statement**

You are given an array ( a ) of length ( n ).
There are two types of operations:

1. **Range queries**
   [
   Q(l, r) = \text{number of distinct values on } [l, r].
   ]

2. **Updates**
   [
   U(p, x):\quad a_p := x.
   ]

Queries and updates appear in mixed order, and you must output the result for every query.

This problem is known as **Mo’s algorithm with modifications**
or **Mo’s algorithm with updates**.

---

## **2. Why the classic Mo’s algorithm fails**

Classic Mo requires that the array **never changes**.

But here, updates happen between queries, so the array’s state at query time depends on **how many updates happened before it**.

Therefore, classic Mo cannot solve this problem.

---

## **3. Extending Mo’s Algorithm**

We introduce a third dimension: **time**.

### Each query becomes:

[
(l, r, t),
]
where:

* (l, r) — range boundaries
* (t) — **the number of updates that occurred before this query**

---

## **4. Block Decomposition and Sorting**

We split:

* the coordinate (l) into blocks of size (B)
* the coordinate (r) also into blocks of size (B)
* the time (t) into blocks as well

Sorting order:

1. by block of (l)
2. then by block of (r)
3. then by time (t)

Typical block size:
[
B = n^{2/3}.
]

This gives the well-known complexity:
[
O((n + q),n^{2/3}).
]

---

## **5. Three Types of Movements**

To move from the current state to the next query, the algorithm adjusts three dimensions:

### **1. Move L**

[
\text{add/remove}(a[L])
]

### **2. Move R**

[
\text{add/remove}(a[R])
]

### **3. Move Time T**

If we go from time (t) to (t+1):

* apply the next update

If we go backwards in time:

* revert the update:
  [
  a_{pos} := old_value.
  ]

If the updated position is inside ([L, R]), then:

* remove the old value from the answer
* add the new value back

This ensures correctness.

---

## **6. Counting Distinct Elements**

We maintain a frequency array:

[
cnt[x]
]

When adding a value:

* if (cnt[x] = 0), increment distinct counter
* increment (cnt[x])

When removing:

* decrement (cnt[x])
* if (cnt[x] = 0), decrement distinct counter

Thus, the number of distinct elements in the current range is always known.

---

## **7. Time Complexity**

[
O\left((n + q) \cdot n^{2/3}\right)
]

Memory complexity:

[
O(n)
]

This is significantly faster than naive solutions and handles large inputs well.

---
* Store updates separately from regular queries.
* For each query, record how many updates happened before it.
* When rewinding time, always check whether the updated position is currently inside ([L, R]).
* Updates must store both:

  * the index
  * the previous value
  * the new value


