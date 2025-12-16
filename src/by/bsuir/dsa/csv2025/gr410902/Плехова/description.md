# Name

Traffic lights on the street

# Task

A new street with shops is being constructed in the city, and traffic lights are planned to be installed along it. Each traffic light provides a safe pedestrian zone represented by a segment along the street (the X-axis). A pedestrian can walk safely only within a zone covered by at least one traffic light.

The street is represented as a single straight line (the X-axis). Each traffic light defines the beginning and end of a safe zone [l, r]. The task is to calculate the total length of the safe zone (without counting overlapping segments more than once).

If the safe zones of different traffic lights overlap, the overlapping part must be counted only once. The solution must use the sweep line algorithm.

## Input
The first number N is the number of traffic lights (1 ≤ N ≤ 100000).  
Then follow N lines, each containing two integers l r (the coordinates of the traffic light zone, with l < r; integers may be negative).

## Output
A single number — the total length of the safe zone.

## Example
Input:  
3  
1 5  
2 6  
8 10  

Output:  
7


# Theory

The scanline method involves sorting points on a coordinate line or abstract "events" by some characteristic and then traversing them.

It is often used to solve data structure problems where all the queries are known in advance, as well as in geometry to find unions of shapes.

