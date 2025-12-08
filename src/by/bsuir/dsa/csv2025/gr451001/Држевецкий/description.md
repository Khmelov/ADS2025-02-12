# Name

Minimum Maximum Segmentation Complexity for the Astra-42

# Task

Minimum Maximum Segmentation Complexity for the Astra-42

**Problem.** You are the chief engineer of the exploration ship **Astra-42**, traveling through the Orion Nebula.\
The ship collects fragments of data, each fragment having its own
**processing complexity**.\
Before jumping to hyperspace, you need to **split the data stream** into
several segments so that each server module can handle the load.

If even one segment is too heavy, it will overload, crash,
and the ship will enter a space rift.\
Therefore, you want to minimize the **maximum load of a single
segment**.

## Input Data
The first line contains a single integer n `(1 ≤ n ≤ 200000)` — the length of array a.

The second line contains a single integer k `(1 ≤ k ≤ n)` — the number of segments.

The third line contains n positive integers a1, a2, …, an `(1 ≤ a[i] ≤ 10^9)`.

It is guaranteed that the sum of all `a[i]` ≤ `2 · 10^14`.

## Output

Print a single integer — the minimum possible cost of a partition, where the cost of a partition is the maximum cost of all segments, and the cost of a segment is the sum of the numbers within it.

## Examples

### Input
    5
    2
    7 2 5 10 8
### Output
    18

### Input
    4
    3
    10 20 30 40
### Output
    40

# Theory

In this article, we'll use several examples to examine an important type of binary search—binary search by answer—which involves formulating the problem as "find the maximum $x$ such that such-and-such an easily computable property of $x$ holds" and finding this $x$ using binary search.

### "Cows in Stalls"

> There are $n$ stalls located on a line (their coordinates on the line are given), into which $k$ cows must be placed so that the minimum distance between the cows is as large as possible.
>
> It is guaranteed that $1 < k < n$.

If we were to solve the problem head-on, it would be completely unclear what to do. Instead, we need to solve a simpler problem: suppose we know this distance $x$, closer than which the cows cannot be placed. Then, can we arrange the cows themselves?

The answer is yes, and it's quite simple: we put the very first cow in the leftmost stall, because that's always profitable. The next few stalls should be left empty if they're less than $x$ away, and the second cow should be placed in the leftmost stall of the remaining stalls, and so on.

How to implement this: we need to go left to right through the sorted array of stalls, store the coordinates of the last cow, and depending on the distance to the previous cow, either skip the stall or place a new cow in it.

```cpp
bool check(int x) {
int cows = 1;
int last_cow = coords[0];
for (int c : coords) {
if (c - last_cow >= x) {
cows++;
last_cow = c;
}
}
return cows >= k;
}
```

If at the end of such a greedy algorithm we run out of cows before we run out of safe stalls, then the answer is definitely no less than $x$, and if we fail, then the answer is definitely less than $x$.

Now we can iterate over $x$ and perform $X = \frac{\max x_i - \min x_i}{k}$ checks in $O(n)$, but it could be done even faster.

Let's run a binary search on $x$—after all, for some small $x$, the cows can definitely be placed, but starting with some large $x$, it's impossible, and this is precisely the boundary we're asked to find in the problem.

```cpp
int solve() {
sort(coords.begin(), coords.end());
int l = 0; // since there are fewer cows than stalls, x = 0 will always be enough.
// according to the condition, there are at least 2 cows,
// which we will, at best, send to opposite stalls:
int r = coords.back() - coords[0] + 1;
while (r - l > 1) {
int m = (l + r) / 2;
if (check(m))
l = m;
else
r = m;
}
return l;
}
```

Each check takes $O(n)$, and the outer binary search takes $O(\log n)$ checks, so the asymptotics will be $O(n \log X)$.

### "Printers"

> There are two printers. One prints a sheet every $x$ minutes, the other every $y$ minutes. How many minutes will it take them to print $n$ sheets?
>
> $n > 0$

Here, unlike the previous problem, there seems to be a direct solution with a formula. But instead of thinking about it, we can simply reduce the problem to its inverse. Let's think about how, given the number of minutes $t$ (the answer), we can determine how many sheets will be printed in this time. It's very easy:

$$
\left \lfloor \frac{t}{x} \right \rfloor + \left \lfloor \frac{t}{y} \right \rfloor
$$

It's clear that $n$ sheets cannot be printed in $0$ minutes, and in $x \cdot n$ minutes, the first printer alone will be able to print $n$ sheets. Therefore, $0$ and $xn$ are suitable initial bounds for a binary search.

