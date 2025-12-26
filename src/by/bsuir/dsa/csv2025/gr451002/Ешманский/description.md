# Name

Implicit Treap — Main Operations

# Task

Implicit Treap — Main Operations

## Description

Implement a data structure based on a **Treap (Cartesian tree)** that stores an array of integers and supports efficient modifications.

Unlike a regular treap (which uses the key as BST order), an **implicit treap uses the position in the array as the key**.
In other words:

* The BST invariant is based on **index in sequence**.
* The heap invariant is based on **priority**.
* Each node stores a **subtree size**, allowing us to navigate by index in O(log n).

### Supported operations (all in O(log n) expected time):

* `INSERT pos value` — insert an element `value` at position `pos` (0-based indexing)
* `DELETE pos` — delete the element at position `pos`
* `UPDATE pos value` — change the element at position `pos` to `value`
* `GET pos` — output the element at position `pos`

Operations use:

* `split(root, k)` — splits the treap into `[0..k-1]` and `[k..end]`
* `merge(left, right)` — merges two treaps back into one
* subtree sizes for index navigation

`GET` and `UPDATE` use a recursive “go by index” function.

---

## Input Format

1. First line: integer `N` — initial size of the array
   `1 ≤ N ≤ 2·10⁵`
2. Second line: `N` integers `a_i`
   `|a_i| ≤ 10⁹`
3. Then `Q` lines with queries (`1 ≤ Q ≤ 2·10⁵`):

    * `INSERT pos value`
    * `DELETE pos`
    * `UPDATE pos value`
    * `GET pos`

## Output Format

For each `GET pos` request, output the value stored at position `pos`.


# Theory

Implicit Treap

An **Implicit Treap** is a data structure based on a **Treap** that stores an array and allows efficient operations on elements by their position (index).

Unlike a regular Treap, here **keys are not stored explicitly** — the **element's position in the array** acts as its key. Each node contains:

* `value` — the element's value
* `priority` — a random priority (to maintain the heap property)
* `size` — the size of the subtree
* pointers to left and right children (`left`, `right`)

The element’s index in the sequence is computed using `size(left)`.

---

## Main Operations

### Split

Splits the treap into two subtrees at position `k`:

* `L` — all elements with indices `< k`
* `R` — all elements with indices `≥ k`

We recursively traverse the left or right subtree, updating subtree sizes accordingly.

**Example:**
Array `[10, 20, 30, 40]`, split at `k = 2`:

* `L = [10, 20]`
* `R = [30, 40]`

---

### Merge

Merges two trees `L` and `R`, where all elements in `L` come before elements in `R`.
The root becomes the node with the higher priority, and the remaining nodes are recursively distributed in the left and right subtrees.

---

### Insertion (INSERT)

Insert a value `val` at position `index`:

1. Split at position `index` → `(L, R)`
2. Create a new node `t` with `val` and a random priority
3. Merge back:
   $$
   \text{root} = \operatorname{merge}(L, \operatorname{merge}(t, R))
   $$

**Example:** Insert `25` into `[10, 20, 30, 40]` at position `2` → `[10, 20, 25, 30, 40]`.

---

### Deletion (DELETE)

Remove the element at position `index`:

1. Split at `index` → `(L, midR)`
2. Split `midR` to separate the single element → `(mid, R)`
3. Merge back:
   $$
   \text{root} = \operatorname{merge}(L, R)
   $$

---

### Update (UPDATE)

Update the element at position `index`:

1. Split at `index` → `(L, midR)`
2. Split `midR` to isolate the node → `(mid, R)`
3. Create a new node with the updated value
4. Merge back:
   $$
   \text{root} = \operatorname{merge}(L, \operatorname{merge(mid, R)})
   $$

---

### Get (GET)

To retrieve the value at position `index`:

* If `index < size(left)` → go to the left subtree
* If `index == size(left)` → return the current node’s value
* Else → go to the right subtree with `index - size(left) - 1`

---

## Extensions

Implicit Treaps can be easily extended:

* store **range sums, minimums, maximums**
* persistent version to maintain history of modifications
* work with dynamic arrays and sequences efficiently

---

Implicit Treap is a flexible structure for working with arrays:

* All basic operations (`INSERT`, `DELETE`, `UPDATE`, `GET`) run in (O(\log n)) expected time
* Allows building **dynamic arrays** and sequences with efficient modifications


