# Name

Thermal power plant power regulation

# Task

Power Control of a Thermal Power Plant (TPP)

Your task is to find the minimal operating cost of a power plant boiler under the given conditions and output it. At the beginning of the day, you are given an array int D[] specifying the minimum required power output of the boiler for each hour.

#### The boiler has the following characteristics:

- Initially, the boiler power is 0 W (not started)

- Start-up cost: 10 units (does not increase power, only spent when starting from zero)

- Cost of changing boiler power by 1 W: 20 units

- Cost of operating the boiler for 1 hour at power p: p^2 units

- Power changes are instantaneous at the beginning of the hour, but no more than 5 units per hour

#### Data constraints:

- Number of hours: no more than 24 (size of array D[])

- Maximum boiler power: 20 W

- The number 90000 is considered infinity

## Input format

- Input is a string of integers separated by spaces (e.g., "0 5 10")

- Number of hours: 0 < D[].size < 24

- Power values: 0 < D[i] < 20

## Output format

- A string with the number representing the minimal operating cost according to the schedule.

- If the schedule cannot be fulfilled, return infinity, i.e., "90000"

## Requirements

The task must be solved using dynamic programming with layer-by-layer implementation (optimizations are optional).

P.S. The boiler in this problem is abstract. Large costs of changing power should be considered bureaucratic.

```java
import java.util.Scanner;
public class Main {
    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);
        String test_data = sc.nextLine();
        if(test_data == "") return;
        String output_data = StationGraphic(test_data);
        System.out.println(output_data);
        sc.close();
    }
    public static String StationGraphic(String input){

        // Convert input String to int[]
        String[] parts = input.split(" ");
        int[] D = new int[parts.length]; // Input array
        for (int i = 0; i < parts.length; i++) {
            D[i] = Integer.parseInt(parts[i]);
        }
        String result = ""; // Result for output

        // Problem constants
        int time = D.length; // Number of working hours (recommended to add +1 hour before startup)
        int p_max = 20; // Maximum boiler power
        int start_cost = 10; // Cost to start boiler from zero power
        int delta_p_cost = 20; // Cost to change power by 1 W
        // Cost of operating 1 hour at power p: p^2
        int max_delta_p = 5; // Maximum allowed power change at the beginning of the hour
        int INF = 90000; // Representing infinity
        //============Solution==============

        //============End of solution==============
        // Return result as String
        return result;
    }
}
```

Example 1:
- Input: "0 0 5"
- Output:  "135"
- Optimal Solution 0->0->5
Example 2:
- Input: "0 5 0"
- Output: "160"
- Optimal Solution 0->5->5

# Theory

---
title: Layered Dynamic Programming
authors:
- Sergey Slotin
- Konstantin Amelichev
created: 2019
updated: 2021-08-29
---

# Layered Dynamic Programming

Task. Given \(n\) points on a line, sorted by their coordinate \(x_i\). Need to find \(m\) segments covering all points while minimizing the sum of the squares of their lengths.

The basic solution — define the DP state \(f[i,j]\) as the minimal cost to cover the first \(i\) points using no more than \(j\) segments.   It can be recalculated by iterating over all possible last segments:

\[
f[i,j] = \min_{k < i} \{ f[k, j-1] + (x_{i-1} - x_k)^2 \}
\]

The final answer will be \(f[n,m]\), and overall such DP works in \(O(n^2 m)\).

```cpp
// x[] — sorted array of point coordinates, zero-based indexing

// square of the segment length from i-th to j-th point
int cost(int i, int j) {
    return (x[j] - x[i]) * (x[j] - x[i]);
}

for (int i = 0; i <= m; i++)
    f[0][i] = 0; // if nothing needs to be covered, cost is zero
// all other f values are assumed infinite

for (int i = 1; i <= n; i++)
    for (int j = 1; j <= m; j++)
        for (int k = 0; k < i; k++)
            f[i][j] = min(f[i][j], f[k][j - 1] + cost(k, i - 1));
// (Note: loops over i and j can be swapped)
```
---
# Adapting the Theory to an Exam Problem
// this part is written by the student

In this example, the two-dimensional array \(f[n,m]\) effectively operates in a **two-layer mode**.   If the loops over `i` and `j` are swapped, then in one outer loop only **two layers** of the array are involved (`j` and `j-1`).

```cpp
for (int j = 1; j <= m; j++)
    for (int i = j; i <= n; i++)
        for (int k = 0; k < i; k++)
            f[i][j] = min(f[i][j], f[k][j - 1] + cost(k, i - 1));
// (Note: loops over i and j can be swapped.)  <---
```
Theoretically, this allows memory optimization from O(n*m) to 2 * O(n), by creating 2 arrays representing these layers. However, the main idea of this method is that each new state is computed from the previous layer, selecting the optimal value from the last layer. This idea will be useful in implementing the problem of minimizing the operating cost of a power plant.

