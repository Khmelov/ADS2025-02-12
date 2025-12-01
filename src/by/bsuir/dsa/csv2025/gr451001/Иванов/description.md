# Name

Space Mission: Titanium Collection

# Task

Space Mission: Titanium Collection

## Problem Statement

To repair the orbital station, the crew of a spaceship must collect titanium from several planets.  
There are `n` planets, and the i‑th planet contains `titan[i]` tons of titanium.  
It takes `travel[i]` flight hours to get to and return from the planet.  
The crew has no more than `h` hours to complete the mission, including both flights and mining.
After arriving at a planet, the astronauts can mine titanium at a rate of `k` tons per hour.  
If the remaining titanium on a planet is less than `k` tons, they take all of it and spend the rest of that hour resting.
The crew aims to work at the slowest possible rate, but must still collect all the titanium 
and return to the base before the `h` hours run out.

Task: Determine the minimum mining speed `k` (tons per hour) at which the mission can be successfully completed.

## Input

- The first line contains an integer `n` — the number of planets.
- The second line contains an integer `h` — the total time (in hours) available for the mission.
- The third line contains `n` integers `titan[i]`, where each number represents the amount of titanium (in tons) on the i‑th planet.
- The fourth line contains `n` integers `travel[i]`, where each number represents the flight time (in hours) to the corresponding planet.

## Output

- A single integer — the minimum mining speed `k` (tons per hour) at which the crew can collect all titanium and return to the base within `h` hours.

## Notes

The mission time `h` includes both flights and mining.  
All input values are positive integers. Ranges:
- `1 ≤ n ≤ 100`
- `n ≤ h ≤ 1000`
- `1 ≤ titan[i] ≤ 1000`
- `1 ≤ travel[i] ≤ 100`

## Example

### Input:

    4
    15
    3 6 7 11
    1 2 1 3

### Output:

    4

# Theory

Binary search

## The idea
The algorithm divides the search segment in half and finds the element or boundary of the property in `O(log n)` steps.

## Algorithm
1. Set the boundaries of `l`, `r`.
2. Find the middle of `m = (l+r)/2`.
3. Check the condition.
4. Shift the `l` or `r`.
5. Repeat until `l != r`.

## In arrays
- Unsorted array → search for `O(n)'.
- Sorted array → binary search in `O(log n)`.

## Application
- Search for an item.
- Counting the number of occurrences.
- Finding the boundary of a monotonic property.

