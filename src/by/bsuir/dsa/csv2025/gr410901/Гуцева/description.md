# Name

Versioned Warehouse Inventory Tracking

# Task

Versioned Warehouse Inventory Tracking

Create a class `WarehouseVersioningC` with the `main` method.

You are given a network of `N` warehouses, each storing some amount of goods.  
The initial state of all warehouses is given as an array of integers and is considered **version 0**.  
Then a sequence of operations is executed; each operation either creates a **new version** of the state or performs a query on already existing versions.

You must support the following operations on versions:

- `ADD v i x` — based on version `v`, create a new version in which the stock at warehouse `i` is increased by `x`.  
- `MOVE v i j x` — based on version `v`, create a new version in which `x` units are removed from warehouse `i` and added to warehouse `j`.  
- `CLONE v` — create a new version that is an exact copy of version `v`.  
- `SUM v l r` — query the sum of stocks of warehouses with indices from `l` to `r` in version `v`.  
- `DELTA v1 v2 l r` — query the difference of sums on the segment `[l, r]` between versions `v2` and `v1`:  
  `SUM(v2, l, r) − SUM(v1, l, r)`.

Each `ADD`, `MOVE`, and `CLONE` operation creates exactly one new version.  
New versions are numbered consecutively: the initial state has number `0`, the first created version has number `1`, the next one `2`, and so on.  
`SUM` and `DELTA` operations do **not** create new versions.

The time complexity of your solution must be at least as good as `O((N + Q) · log N)`.  
It is recommended to use a persistent segment tree.  
You must not use collection classes such as `ArrayList`, `HashMap`, `TreeMap`, etc. for storing versions; plain arrays and your own node classes are sufficient.

***

## Input format

First line:  
`N` — number of warehouses, `1 ≤ N ≤ 100000`.

Second line:  
`N` integers `a[1..N]` — initial stocks at warehouses in version `0`  
(absolute value ≤ `10^9`).

Third line:  
`Q` — number of operations and queries, `1 ≤ Q ≤ 100000`.

Then follow `Q` lines, each describing one operation in one of the formats:

- `ADD v i x`  
- `MOVE v i j x`  
- `CLONE v`  
- `SUM v l r`  
- `DELTA v1 v2 l r`

It is guaranteed that all version numbers `v`, `v1`, `v2` are valid at the moment they are used.

***

## Output format

For each operation:

- `SUM v l r`  
- `DELTA v1 v2 l r`

print one integer on a separate line — the answer to that query.

The order of answers must match the order of these queries in the input.

# Theory

### Persistent Segment Tree

- Needed to efficiently support multiple versions of an array/data structure, allowing access to any previous state.
- All update operations (ADD, MOVE, CLONE) create a **new independent version** of the array of values.

#### Classic Segment Tree

- The segment tree is built on the initial array $$ a[1..N] $$.
- Each node in the segment tree stores the sum for a segment $$ [l; r] $$ of the array.[2][3]

#### Persistence

- A **persistent** segment tree allows keeping all previous states without copying the entire array.
- On every update, only the nodes along the path from the root to the updated leaf are copied; all other nodes are shared between versions.
- The number of new nodes per update is $$ \mathcal{O}(\log N) $$; total memory used is $$ \mathcal{O}((N+Q) \log N) $$.[1][3][4]

#### Update Example

- For version $$ v $$, an operation like ADD or MOVE creates a new version $$ v+1 $$ by partially copying the tree—only nodes along the update path are changed.
- All other versions are unaffected.

#### Queries

- The sum on range $$ [l;r] $$ for version $$ v $$ is computed just like any segment tree:
  $$
  \text{SUM}(v, l, r) = \sum_{k=l}^{r} a^{(v)}_k
  $$
  where $$ a^{(v)}_k $$ is the state of array $$ a $$ in version $$ v $$.[3][2]
- The difference between two versions on a segment $$ [l;r] $$:
  $$
  \text{DELTA}(v_1, v_2, l, r) = \text{SUM}(v_2, l, r) - \text{SUM}(v_1, l, r)
  $$

#### Advantages

- Instant access to any previous version.
- Both updates and queries run in $$ \mathcal{O}(\log N) $$ time, new memory per operation is also $$ \mathcal{O}(\log N) $$.

