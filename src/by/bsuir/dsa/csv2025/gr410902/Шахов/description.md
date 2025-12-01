# Name

Task management system

# Task

Task management system

## TASK
* Implement a task management system.
* Each task consists of a name and a priority (integer).
## Required:

1) Implement your own dynamic array (without using ArrayList). 
2) Implement an iterator over a dynamic array.
3) Implement binary max-
heap to get the task with the highest priority.  
4) Implement the getNextTask() method, returning the task with the maximum priority as a tuple (key-value): (name, priority).  
5) All tasks must be stored both in a dynamic array and on the heap.  

Use an OOP structure.

# Theory

Basic Data Structures  
## Arrays, Tuples, Iterators, Dynamic Array, and Binary Heap

---

## 1. Arrays

An **array** is an ordered sequence of elements of the same type stored in a *contiguous block of memory*.  
Access by index runs in:

```
O(1)
```

### Properties
- fixed size  
- fast random access  
- minimal overhead  
- expensive insertions/deletions in the middle  

### Operations
| Operation | Time |
|----------|------|
| Read `a[i]` | `O(1)` |
| Write `a[i] = x` | `O(1)` |
| Insert in middle | `O(n)` |
| Remove from middle | `O(n)` |

---

## 2. Tuples

A **tuple** is an immutable structure with a fixed number of elements, potentially of different types.

Used for:
- returning multiple values from a function  
- grouping small sets of related data  

Example:
```java
return new AbstractMap.SimpleEntry<>("task", 5);
```

---

## 3. Iterators

An iterator is an object that provides sequential access to the elements of a data structure *without exposing its internal representation*.

Minimal interface:

```java
boolean hasNext();
T next();
```

### Why iterators?
- abstraction over underlying structure: array, list, tree, etc.  
- enables `for-each` loops  
- supports lazy traversal (elements are produced one at a time)  

---

## 4. Dynamic Array

A **dynamic array** grows automatically when its capacity is exceeded.

### How resizing works
1. A new array (usually ×2 larger) is created.  
2. All elements are copied into it.  
3. The reference is updated to point to the new array.

### Complexity
Appending to the end is amortized:

```
O(1)
```

### Properties
- fast indexed access  
- automatic capacity growth  
- efficient push-back (`add` at end)  
- costly insertions/removals in the middle  

---

## 5. Binary Heap

A **binary heap** is a complete binary tree satisfying the heap property:

- **max-heap**: parent ≥ children  
- **min-heap**: parent ≤ children  

### Array-based representation
Implemented using a regular array.

Index formulas:

```
parent(i) = (i - 1) / 2
left(i)   = 2*i + 1
right(i)  = 2*i + 2
```

### Operations

| Operation | Description | Time |
|-----------|-------------|-------|
| `add(x)` | insert + sift-up | `O(log n)` |
| `extractMax()` | remove root + sift-down | `O(log n)` |
| peek root | return `heap[0]` | `O(1)` |

### Common usage
- priority queues  
- heap sort  
- graph algorithms (Dijkstra, Prim)  

---

## 6. Combining Structures in Practical Tasks

Combining a dynamic array, iterator, and heap allows:

- storing tasks in a growable array  
- iterating through them using an iterator  
- retrieving the highest priority task via a max-heap  

This is the standard foundation for implementing **priority queues** and various scheduling systems.

---


