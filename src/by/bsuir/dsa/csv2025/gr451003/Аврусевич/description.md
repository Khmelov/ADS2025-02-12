# Name

Warehouse Order Processing Optimization

# Task

Warehouse Order Processing Optimization

A warehouse automation system processes incoming orders. Each order is characterized by:

- Arrival time: `arrivalTime`  
- Priority level: `priority` (1 — highest, 2 — medium, 3 — low)  
- Processing time: `processingTime`

The processing robot can handle only one order at a time. When selecting the next order, the system follows these rules:

1. The order with the highest priority is processed first.  
2. For orders with equal priority, the one that arrived earlier is selected.

Implement an algorithm that determines:

- The order processing sequence  
- Total processing time of all orders  
- Average waiting time of orders

**Input:**
- Array `orders` of size n (1 ≤ n ≤ 1000)
- Each order is represented as `[arrivalTime, priority, processingTime]`
- `0 ≤ arrivalTime ≤ 10^4`
- `priority ∈ {1, 2, 3}`
- `1 ≤ processingTime ≤ 100`

**Output:**

- Array of order indices in processing sequence  
- Total processing time of all orders  
- Average waiting time rounded to 2 decimal places

**Example:**

```
Input: orders = [[0, 2, 5], [2, 1, 3], [4, 3, 2], [6, 1, 4]]
Output:
  order: [0, 1, 3, 2]
  total: 14
  avg_wait: 3.25
```

# Theory

---
title: Priority Scheduling Algorithm
weight: 2
authors:
- Валерия Аврусевич
created: 2025
---

This problem belongs to the class of process scheduling algorithms in operating systems. It uses a modified *Priority Scheduling* algorithm with non-preemptive processing.

**Mathematical model:**

Let $n$ be the number of orders.

For order $i$:  
- $a_i$ — arrival time,  
- $p_i$ — priority,  
- $t_i$ — processing time.

Start time:  
$$
s_i = \max(c_{i-1}, a_i),
$$
where $c_{i-1}$ is completion time of previous order.

Waiting time:  
$$
w_i = s_i - a_i
$$

Total processing time:  
$$
T = \sum_{i=1}^n t_i
$$

Average waiting time:  
$$
\bar{w} = \frac{1}{n} \sum_{i=1}^n w_i
$$

**Algorithm:**

- Sort orders by arrival time — $O(n \log n)$  
- Use priority queue to select next order — $O(n \log n)$  
- Comparator: first compares priorities, then arrival times  

**Complexity:** $O(n \log n)$ due to sorting and priority queue operations.

