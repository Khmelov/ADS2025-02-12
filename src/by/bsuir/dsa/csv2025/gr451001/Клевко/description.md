# Name

Robin

# Task

Robin has guests — his brother and his mom. You need to choose the start day of each visit.

Days available for visits are numbered from **1** to **n**.

Robin and his team have **k** risky tasks scheduled.  
Each task **i** occupies a range of days from **lᵢ** to **rᵢ** inclusive.

Each guest stays with Robin for **d** consecutive days. All these days must lie within the range from **1** to **n**.

A visit is considered overlapping with a task if **at least one day of the visit coincides with any day of that task**.

**Goal:**

- For the **brother**, choose a start day of the visit such that it overlaps with the **maximum possible number of tasks**.
    
- For the **mom**, choose a start day where the number of overlaps is **minimal**.
    
- If multiple days are possible, choose the **earliest** one.
    

---

## Input

- The first line contains the number **t** — the number of test cases  
    (**1 ≤ t ≤ 10⁴**).
    

Each test case consists of:

1. A line with three numbers:
    
    - **n** — total number of days
        
    - **d** — length of the visit
        
    - **k** — number of tasks (**1 ≤ n ≤ 10⁵**, **1 ≤ d, k ≤ n**)
        
2. Then **k** lines, each containing two numbers **lᵢ** and **rᵢ** — the start and end days of the i-th task  
    (**1 ≤ lᵢ ≤ rᵢ ≤ n**).
    

---

## Output

For each test case, put in a **List** two integers: the best start days for the visits of Robin’s brother and mom, respectively. Both visits must fit within the range from day 1 to n inclusive.

It is guaranteed that the total sum of **n** across all test cases does not exceed **2 · 10⁵**.

# Theory

Sliding Window

Used to calculate the sum or aggregate over a subsequence of fixed length.

Allows finding the maximum or minimum in linear time.

Interval Intersections

Often, it is necessary to count how many segments intersect with a given range.

Can be efficiently solved using a difference array and a sliding window.


