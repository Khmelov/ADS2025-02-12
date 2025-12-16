# Name

Number of Distinct Values in Subarrays

# Task

An array of nums integers of length n and a list of queries are given. Each query is a pair of indexes (l, r). For each query, it is necessary to find the number of different numbers in the nums[l...r] subarray (including boundaries).
Write a function that returns an array of answers to all queries.

Input:

int[] nums: the input array (1 <= n <= 10^5, -10^9 <= nums[i] <= 10^9)

int[][] queries: the array of queries where queries[i] = [l_i, r_i] (0 <= l_i <= r_i < n, 1 <= number of queries m <= 10^5)

Output:

int[]: an array of answers where ans[i] is the number of distinct values in the subarray for queries[i].

# Theory

Mo's algorithm efficiently processes multiple range queries when we can quickly update the answer when adding/removing an element.

Main Idea
Given $m$ queries $Q_i = [l_i, r_i]$. Instead of processing each query separately in $O(n)$, we:

Sort queries in a special way:
Divide array into blocks of size $k \approx \sqrt{n}$
Sort queries by left endpoint's block: $bucket(l_i) = \lfloor \frac{l_i}{k} \rfloor$
Within the same block, sort by right endpoint $r_i$

Use two pointers $L$ and $R$ that we move between queries:
Movement complexity: $O((n + m) \cdot \sqrt{n})$
Mathematical Foundation

Algorithm Complexity:
Left pointer movement: inside block - $O(k)$, between blocks - $O(n)$
Right pointer movement: monotonic within block - $O(n)$

Total complexity: $O(m \cdot k + n \cdot \frac{n}{k})$

