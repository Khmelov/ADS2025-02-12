# Name

Delivery Route Optimization

# Task

Courier Delivery Route Optimization

### Info

You work for a delivery service and need to optimize a courier's route. The courier starts from the warehouse (point 0), must deliver orders to $n$ addresses (points $1..n$), and return back to the warehouse. Find the shortest route that visits all delivery addresses exactly once.

### Input

* $n$ - number of delivery addresses $(1 ≤ n ≤ 15)$
* Distance matrix of size $(n+1)×(n+1)$, where point 0 is the warehouse, points $1..n$ are delivery addresses

### Output

* Single integer - the minimum total route distance


# Theory

## Brief Theory

The Traveling Salesman Problem (TSP) is a classic NP-hard combinatorial optimization problem. We solve it using dynamic programming with bitmask technique.

### Algorithm Overview

* State $dp$[mask][last] stores the minimum distance to visit all points in the $mask$, ending at point $last$

* Mask is a bit vector where the $i$-th bit is 1 if point $i$ has been visited

* Initial state: only warehouse visited (mask = 1, last = 0)

* Transitions: from current state (mask, last) move to all unvisited points $next$

* Answer: minimum value of $dp$[full_mask][last] + $distance$[last][0] for all $last$

* Complexity: $O(2ⁿ × n²)$, efficient for $n ≤ 15$

