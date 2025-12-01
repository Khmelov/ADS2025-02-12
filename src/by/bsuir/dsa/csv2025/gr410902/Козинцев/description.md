# Name

Calculating critical temperature of chemical reactor

# Task

Calculating Critical Temperature of Chemical Reactor

## Task
For safe operation of an industrial reactor, it is necessary to determine the critical temperature at which the chemical reaction regime changes. This temperature is the root of the cubic equation:

**$f(T) = aT^3 + bT^2 + cT + d = 0$**

where coefficients `a, b, c, d` depend on specific reaction parameters.

## Technical Requirements

### Input Data
The method accepts a string with six space-separated values:
- `a, b, c, d` - equation coefficients
- `T₀` - initial temperature approximation
- `N` - maximum number of iterations

### Output Data
For correct verification, the method must return a string containing an **integer** - the result of multiplying the found temperature by **10000** (rounded to the nearest integer).

## Example

For equation: $T^3 - 2T^2 + 3T - 5 = 0$

```java
// Input data
String input = "1.0 -2.0 3.0 -5.0 2.0 5";

// Output data
String output = "18437";  // 1.8437 × 10000 = 18437

```
## Note
Use Newton's method with iterative formula:

$T_{n+1} = T_n - \frac{f(T_n)}{f'(T_n)}$


# Theory


# Newton's Method (Tangent Method)

**Newton's Method** is an **iterative numerical technique** used to find the roots (zeros) of a nonlinear function $f(x)$. It is one of the most efficient methods, provided a suitable initial guess is chosen.

## Principle of Operation

The principle is based on using the **tangent line** to the function's graph at the current point $x_n$. The new, more accurate approximation $x_{n+1}$ is found as the intersection point of this tangent line with the $x$-axis. 

## Key Formula

The iterative process is described by the following formula:

$$x_{n+1} = x_n - \frac{f(x_n)}{f'(x_n)}$$

Where:
* $x_n$ is the current approximation of the root.
* $f(x_n)$ is the function value at that point.
* $f'(x_n)$ is the value of the **derivative** of the function at that point.


## Convergence

Newton's Method exhibits **Quadratic Convergence**, meaning that it converges very rapidly given a good initial guess.

