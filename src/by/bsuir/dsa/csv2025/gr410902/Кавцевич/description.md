# Name

Megalopolis monitoring system

# Task

Megalopolis monitoring system

Use the **square root decomposition (SQRT-DECOMPOSITION)** technique.

A city consists of **N districts**.  
Each district has an integer value representing its **air pollution level**.

The city control center receives **Q queries** of four types:

- **1 l r** — output the *sum of pollution levels* on the segment `[l, r]`.  
- **2 i x** — *update* the pollution level of district `i` to `x`.  
- **3 l r** — output the *minimum pollution level* on the segment `[l, r]`.  
- **4 l r** — output the *maximum pollution level* on the segment `[l, r]`.

## Input

- The first line contains two integers: `N` and `Q`.
- The second line contains `N` integers — initial pollution levels.
- Then follow `Q` query lines.
- `1 ≤ N, Q ≤ 200000`.

## Output

For each query of type **1, 3, and 4**,  
print the result — **one number per line**.
## Example

### Sample Input:
5 4
1 2 3 4 5
1 1 5
3 2 4
4 1 3
2 3 10
### Sample Output:
15
2
3



# Theory

Root Structures

---

## Root Decomposition on Arrays

Let's assume we don't know about segment trees and consider the following problem.

**Problem.** Given an array `a` of length `n`, answer `q` queries of two types:

1. Find the sum on the interval `[l, r]`.
2. Increase all elements on the interval `[l, r]` by `x`.

We divide the array into blocks of size `c ≈ sqrt(n)` and compute the sum for each block. Since the blocks don't overlap, the total work will be `O(n)`.

```cpp
// c is both the number of blocks and their size; it should be slightly larger than the square root
const int maxn = 1e5, c = 330;
int a[maxn], b[c], add[c];

for (int i = 0; i < n; i++)
    b[i / c] += a[i];
```

We also create an `add` array of block size, which we will use for lazy addition: the actual value of the i-th element is `a[i] + add[i / c]`.

Now we can answer sum queries in `O(n)` per query:

* For all blocks fully inside the query, simply take the precomputed sums.
* For blocks partially overlapping the query (at most two — left and right), iterate through the elements and add individually.

One possible implementation style is:

```cpp
int sum(int l, int r) {
    int res = 0;
    while (l <= r) {
        // if we are at the start of a block and it is fully inside the query
        if (l % c == 0 && l + c - 1 <= r) {
            res += b[l / c];
            l += c; // jump to the next block
        }
        else {
            res += a[l] + add[l / c];
            l += 1;
        }
    }
    return res;
}
```

Updates are written similarly — for fully covered blocks, update `add` and the sum; for others, update elements individually:

```cpp
void upd(int l, int r, int x) {
    while (l <= r) {
        if (l % c == 0 && l + c - 1 <= r) {
            b[l / c] += c * x;
            add[l / c] += x;
            l += c;
        }
        else {
            b[l / c] += x;
            a[l] += x;
            l++;
        }
    }
}
```

Both operations run in `O(sqrt(n))` on average because the number of full blocks is at most `n / c`, and the boundary blocks have at most `2c` elements in total.

---

## Internal Structures

Within root decomposition blocks, we can store not only function values for subarrays but also various data structures.

For example, a hash table in each block can answer queries like "the number of elements equal to `x` in a range," and a sorted array can solve problems like "the number of elements less than `x` in a range" or "sum in a rectangle."

The block size greatly affects performance. Previously, for simplicity, we used the same constant for both the number and size of blocks, but in practice, it often needs tuning.

We need to consider not just the asymptotics of block and per-element operations, but also the actual relative time of their execution — accessing a treap, for example, can be slower than a well-vectorized linear scan. For this reason, the root in root heuristics is often smaller than a logarithm.

---

## Dynamic Elements

**Problem.** Given an array `a` of length `n`, answer `q` queries of three types:

1. Insert element `x` at position `k` (so there are exactly `k` elements to its left).
2. Remove the element at position `k`.
3. Find the minimum on interval `[l, r]`.

The previous approach cannot be applied here because insertions and deletions change indices of all right-hand elements. We need a more flexible structure not tied to static indices.

Divide all elements into root blocks. In each block, store a list (`vector` or other container) of all elements and the block minimum.

For insertion, iterate through blocks, find the block where the element belongs (so that the total elements before it are fewer than `k`), and insert the element, rebuilding the block from scratch.

For deletion, find the block and element and remove it. For the minimum query, take the minimum of fully covered blocks, and for the boundary blocks, find the minimum in the prefix or suffix linearly.

To locate boundary blocks, we can no longer simply divide by a constant — block sizes are dynamic, and to find the start index of a block, we need to sum sizes of all previous blocks.

```cpp
vector< vector<int> > blocks;

// returns the block index and index within the block
pair<int, int> find_block(int pos) {
    int idx = 0;
    while (blocks[idx].size() <= pos)
        pos -= blocks[idx++].size();
    return {idx, pos};
}
```

This solution works well initially, when each operation touches at most `O(sqrt(n))` blocks and `O(sqrt(n))` elements individually. Over time, either the number of blocks may grow too large or some blocks may become too big. Two solutions to this problem are:

1. After each insertion or deletion, check the affected block. If its size exceeds `2 * B` (where `B` is the target block size), split it into two. If a block and its neighbor together are smaller than `B`, merge them.
2. Maintain a global operation counter and rebuild the entire structure every `q` queries (flatten into an array and divide into equal blocks again).

The first approach may cost `O(n)` for splits and merges but preserves invariants; the second is exactly `O(n)` times the cost of building.

The second option is easier to implement, as the structure needs initial construction anyway. Sometimes maintaining balanced blocks is less efficient than occasional full rebuilds, depending on merge versus rebuild cost.

