# Name

Binary Search Tree (BST)

# Task

Binary Search Tree

Implement element search in a binary search tree.

**Input:**

- First line: `n` and `n` numbers to build the tree
    
- Second line: `k` and `k` numbers to search for
    

**Output:**  
For each target number output:

- `"found"` - if element is found
    
- `"not found"` - if element is missing
    

**Example:**

text

Input:
7 5 3 7 2 4 6 8
5 4 9 5 2 10

Output:
found
not found
found
found
not found

**Requirements:**

- Implement `insert` and `search` methods for BST
    
- Handle all edge cases
    
- All tree elements are guaranteed to be distinct

# Theory

title: Binary Search Trees  
weight: 2  
authors:

- Galuzo Pavel 
    created: 2025
    

---

**Binary Search Tree (BST)** is a data structure that allows efficient storage and retrieval of elements. The key property of BST: for any node, all elements in the left subtree are smaller, and all elements in the right subtree are larger than the node's value.

## Basic Operations

### Search

The search algorithm in BST recursively compares the target value with the current node:

- If values are equal — element found
    
- If target value is smaller — search continues in left subtree
    
- If target value is larger — search continues in right subtree
    

python

def search(node, value):
    if node is None:
        return False
    if node.value == value:
        return True
    elif value < node.value:
        return search(node.left, value)
    else:
        return search(node.right, value)

### Insertion

New elements are always added as leaves, preserving the BST property:

python

def insert(node, value):
    if node is None:
        return Node(value)
    if value < node.value:
        node.left = insert(node.left, value)
    elif value > node.value:
        node.right = insert(node.right, value)
    return node

## Time Complexity

**Tree height** determines operation efficiency:

- **Best case** (balanced tree): $O(\log n)$
    
- **Worst case** (degenerate tree): $O(n)$
    

BST height depends on insertion order. Minimum height: $\lfloor \log_2 n \rfloor$, maximum height: $n-1$.

## Balancing

To prevent degeneration into a list, balanced BST variants are used:

- **AVL trees** — strict balancing (height difference ≤ 1)
    
- **Red-black trees** — less strict balancing but more efficient for frequent modifications
    

## Tree Traversals

- **In-order**: left subtree → node → right subtree  
    _For BST, returns elements in sorted order_
    
- **Pre-order**: node → left subtree → right subtree
    
- **Post-order**: left subtree → right subtree → node
    

## Practical Applications

BSTs are used in:

- Associative array implementations (`std::map` in C++, `TreeMap` in Java)
    
- Database indexing
    
- Algorithms requiring fast search and dynamic insertion
    

**Advantages**: simple implementations, efficient search when balanced  
**Disadvantages**: performance depends on insertion order, balancing required

