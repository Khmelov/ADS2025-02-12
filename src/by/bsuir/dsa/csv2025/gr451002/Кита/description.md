# Name

Expression Parenthesization for Min/Max Value

# Task

Task: Expression Parenthesization for Minimum and Maximum Value

## Task Description

Develop the method `calculateMinMax(String expression)` to find the minimum and maximum value of an arithmetic expression through optimal parenthesis placement.

## Input

A string, which contains an arithmetic expression consisting of:
- Digits (0-9)
- Operators: `+`, `-`, `*`

The expression does not contain spaces or other characters.

## Requirements

Using the **Interval Dynamic Programming** algorithm, find the parenthesis placement that yields:
- The **minimum possible value** of the expression
- The **maximum possible value** of the expression

## Output

- Minimum value of the expression and maximum value of the expression

## Algorithm Reference

For detailed explanation see:  
[Yandex Handbook: Parenthesization Problem](https://education.yandex.ru/handbook/algorithms/article/zadacha-rasstavit-skobki)

## Example

**Sample Input:**
1+2*3-4*5

**Sample Output:**
 -51 25

# Theory

Theory: Theory: Parenthesization Algorithm for finding the Minimum and Maximum Value of an Expression

## The idea of the algorithm

The problem is solved by using **dynamic programming**. The basic idea is that the optimal solution for the entire expression can be constructed from optimal solutions for its parts.

## Algorithm

### Data separation:

An arithmetic expression consists of numbers and operations {+, -, *}. The length of the entire string containing the arithmetic expression is 2k + 1. Then there are k + 1 numbers and k operations in this expression.
It is necessary to fill the array of numbers $numbers$ of size k + 1 with the numbers from the expression and the array of operators $operators$ of size k with the operations of the expression. 

### Creating and initializing DP arrays:

To convert a recursive algorithm to an iterative one, two-dimensional arrays are used in which the minimum and maximum values of all subexpressions are stored: minDP[0..n][0..n] and maxDP[0..n][0..n], where n = k + 1 is the number of numbers in the expression. Define:

- $minDP[i][j]$ - minimum value of a subexpression from $i$ to $j$
- $maxDP[i][j]$ - maximum value of a subexpression from $i$ to $j$

Base case:
$$
minDP[i][i] = maxDP[i][i] = numbers[i]
$$

Then initialize the main diagonal with the numbers from the expression.

### DP filling for segments of increasing length (len from 2 to the number of numbers):
For each beginning of the segment i:
j = i + len - 1 (the end of the segment)
Iterate through all possible partition points k from i to j-1, where the operators[k] operation will be performed last.

The recurrence relation for $i < j$:
$$
By filling in the table data, we need to make sure that by the end of calculating the optimal values for E(i, j), the optimal values are E(i, k) and E(k+1, j) for all 
k has already been calculated (E is a subexpression).

One way to do this is to list all the pairs (i,j) in ascending order of j −i. 
For each operator at position $k$, 4 combinations are calculated:
a = f(minDP[i][k], minDP[k+1][j], operators[k]);
b= f(minDP[i][k], maxDP[k+1][j], operators[k]);
c= f(maxDP[i][k], minDP[k+1][j], operators[k]);
d =f(maxDP[i][k], maxDP[k+1][j], operators[k]);
where $f(x, y, op)$ is the application of the $op$ operator to the values of $x$ and $y$
$$

Updating values:  
Among all the combinations, find the new minDP[i][j] and maxDP[i][j].
$$
minDP[i][j] = min(minDP[i][j], a, b, c, d);
maxDP[i][j] = max(maxDP[i][j], a, b, c, d);
$$

### Result: 
The answer will be in minDP[0][n-1] and maxDP[0][n-1], where n is the number of numbers.

