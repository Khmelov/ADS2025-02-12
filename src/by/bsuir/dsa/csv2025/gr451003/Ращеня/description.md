# Name

Bridge Reliability Analysis

# Task

Numerical Methods. Numerical Integration - Bridge Reliability Analysis

## Task

## Problem: "Bridge Reliability Analysis Under Dynamic Load"

Needed to develop a program for analyzing dynamic load on a pedestrian bridge using numerical integration methods and data processing algorithms.
Engineers need to assess the reliability of a pedestrian bridge under dynamic load. It is known that when a group of people passes, a periodic
load occurs described by the function:

f(t) = A * sin(ωt) + B * e^(-γt) * cos(Ωt) + C * t

**Parameters:**
- A = (5000 N) - main load amplitude
- B = (2000 N) - damped oscillations amplitude
- C = (100 N/s) - linear load increase
- ω = (2pi rad/s) - main frequency
- γ = (0.1 s^(-1)) - damping coefficient
- Ω = (4pi rad/s) - damped oscillations frequency

#### Required Implementation:

1. **Numerical Integration using Simpson's Method**
   - Implement a function to compute the load integral on the interval [0, n] seconds
   - Calculate the average load over the observation period

2. **Maximum Load Analysis**
   - Find the maximum load value on the interval

## Input Data
- Lower bound of the integral
- Upper bound of the integral
- Number of subintervals for Simpson's rule
- Step size for scanning maximum load 

## Output Data
The program should output:
- Average load
- Maximum load


# Theory

Theoretical Info

## Numerical Integration

### Simpson's Rule (Parabolas)
Based on approximating the function by quadratic parabolas on each partition segment.
The final formula is as follows:

∫(from a to b) f(x) dx ≈ (h / 3) * [f(x₀) + f(xₙ) + 4*(f(x₁) + f(x₃) + ... + f(xₙ₋₁)) + 2*(f(x₂) + f(x₄) + ... + f(xₙ₋₂))]

> n - even number of segments
> h = (b - a)/n - step size
> x₀ = a, x₁ = a + h, x₂ = a + 2h, ..., xₙ = b - grid nodes

#### Application Algorithm

1. Define the function f(x), integration limits a and b, and an even number of partitions n.
2. Calculate the step size h = (b - a)/n.
3. Create a grid of nodes: x_i = a + i*h, where i = 0, 1, 2, ..., n and compute the function values at these nodes.
4. Substitute the values into Simpson's formula:

sum_odd = f(x₁) + f(x₃) + ... + f(xₙ₋₁)
sum_even = f(x₂) + f(x₄) + ... + f(xₙ₋₂)
integral = (h / 3) * [f(x₀) + f(xₙ) + 4*sum_odd + 2*sum_even]


