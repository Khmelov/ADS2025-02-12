# Name

Player rating

# Task

Player rating

## Task Description
You are developing the core of a player rating management system for an online game. Each player has their own unique **id**. Each player must have a non-negative **rating**.

You need to develop the core of a system that manages player ratings and efficiently handles the following queries:
  **1. addPlayer(id, rating)** – adds a player with the given **id** and **rating**.  
If a player with the same **id** already exists, their **rating** is updated.
  **2. removePlayer(id)** - remove the player with the **id** provided in the query.
  **3. updateRating(id, newRating)** - update the rating of the player with the provided **id**, to **newRating**.
  **4. kthPlayer(k)** - return the **id** of the player(s) whose rating ranks **k**-th in the top ratings.
  **5. countPlayersInRange(L, R)** - Return the number of players with ratings in the range **[L, R]**, inclusive.

---

## Input Format

The first line contains an integer **Q** - the number of queries. **(Q <= $1*10^5$)**
The next **Q** lines contain queries in the following format:
  **1 id rating** - query **addPlayer**. **(0 <= id <= $5*10^4$; 0 <= rating <= $1*10^4$)**
  **2 id** - query **removePlayer**.
  **3 id newRating** - query **updateRating**.
  **4 k** - query **kthPlayer**. **(1 <= k <= $5*10^4$)**
  **5 L R** - query **countPlayersInRange**. **(0 <= L < R <= $1*10^4$)**
Ignore queries with invalid data during input, for example, if a non-existent player **id** is provided.

---

## Output Format

For queries of type 4, output the **id** of the player. If multiple players satisfy the condition, output their ids separated by spaces.
For queries of type 5, output a single number - the count of players with **rating** in range **[L, R]**.

---

## Example

**Input**
8
1 1 1500
1 2 1700
1 3 1200
4 2
3 3 1500
4 2
5 1400 1600
2 2
4 1

**Output**
1
1 3
2
1 3

# Theory

AVL Trees

An **AVL tree** is a **self-balancing binary search tree (BST)** in which every node satisfies:

$$
|height(left) - height(right)| \le 1
$$

where \(height(v)\) is the height of the subtree rooted at node \(v\).

---

## Properties of AVL Trees

1. **Binary Search Tree property:**

For any node with value \(k\):

- all values in the left subtree are smaller:  
  $$
  \forall x \in left,\; x < k
  $$
- all values in the right subtree are greater:  
  $$
  \forall x \in right,\; x > k
  $$

2. **Strict balancing:**  
   The height difference between left and right subtrees never exceeds 1.

3. **Tree height:**

$$
h = O(\log n)
$$

4. **Operation complexities:**

| Operation       | Complexity |
|-----------------|------------|
| Search          | \(O(\log n)\) |
| Insert          | \(O(\log n)\) |
| Delete          | \(O(\log n)\) |

---

## Balance Factor

Defined as:

$$
bf(v) = height(v_{left}) - height(v_{right})
$$

Nodes with \(bf = \pm 2\) are unbalanced.

---

## Rotations (Rebalancing)

### 1. Right Rotation (LL case)

If:

$$
bf(v) = 2,\quad bf(v_{left}) \ge 0
$$

→ perform a right rotation.

### 2. Left Rotation (RR case)

If:

$$
bf(v) = -2,\quad bf(v_{right}) \le 0
$$

→ perform a left rotation.

### 3. Left-Right (LR) and Right-Left (RL) Rotations

Used when:

$$
bf(v) = 2,\ bf(v_{left}) < 0 \quad\text{(LR)}
$$

or

$$
bf(v) = -2,\ bf(v_{right}) > 0 \quad\text{(RL)}
$$

---

# Hash Tables

A hash table is a data structure that provides key-value access in **amortized \(O(1)\)** time.

---

## Hash Function

Maps a key \(k\) to an index of the table:

$$
index = h(k)
$$

A good hash function must be:

- fast,
- evenly distributing values,
- minimizing collisions.

---

# Collisions

A collision occurs when:

$$
h(k_1) = h(k_2), \quad k_1 \ne k_2
$$

There are two main strategies to resolve collisions:

---

## 1. Open Addressing

When a collision occurs, probing is used:

### Linear Probing:

$$
h_i(k) = (h(k) + i) \bmod m
$$

### Quadratic Probing:

$$
h_i(k) = (h(k) + c_1 i + c_2 i^2) \bmod m
$$

### Double Hashing:

$$
h_i(k) = (h(k) + i \cdot h'(k)) \bmod m
$$

---

## 2. Separate Chaining

Each table cell contains a list (usually a linked list):

$$
T[h(k)] = \{k_1, k_2, \dots\}
$$

---

## Operation Complexities

| Operation       | Average | Worst |
|-----------------|---------|--------|
| Search          | \(O(1)\) | \(O(n)\) |
| Insert          | \(O(1)\) | \(O(n)\) |
| Delete          | \(O(1)\) | \(O(n)\) |

---

## Load Factor

$$
\alpha = \frac{n}{m}
$$

where:

- \(n\) — number of elements,
- \(m\) — size of the table.

When \(\alpha\) becomes too large → **rehashing** occurs:

$$
m_{new} = 2m
$$


