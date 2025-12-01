# Name

Binary Search: Finding the First Occurrence of an Element

# Task

Binary Search: Finding the First Occurrence of an Element

## Task

A non-empty sequence of integers separated by spaces is given. The first number `N` defines the array length, the next `N` numbers represent a sorted array, and the last number `X` is the target element.<br>
For a given sequence:

1. Implement a binary search algorithm to find the position of the first occurrence of element `X` in the array.
2. If the element is not present in the array, return `-1`.
3. Output the resulting position.

> **Remarks:**
> - the array is guaranteed to be sorted in ascending order;
> - all numbers are integers;
> - array length `N` can range from 1 to 10^5.

## Example

- Input:<br>
`5 1 3 3 5 7 3`
- Expected output:<br>
`1`


# Theory

Binary Search: Finding the First Occurrence of an Element
Theory
Binary search is an efficient algorithm for finding the position of an element in a sorted array. Unlike linear search which works in $O(n)$ time, binary search has $O(\log n)$ complexity.

Algorithm for Finding First Occurrence
Given a sorted array $A$ of length $n$ and a target element $x$, we need to find the smallest index $i$ such that $A[i] = x$.

public class BinarySearch {
    public static int binarySearchFirst(int[] arr, int x) {
        int left = 0;
        int right = arr.length - 1;
        int result = -1;
        
        while (left <= right) {
            int mid = left + (right - left) / 2;  // Overflow protection
            
            if (arr[mid] == x) {
                result = mid;      // Found potential answer
                right = mid - 1;   // Continue searching left
            } else if (arr[mid] < x) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return result;
    }
    
    public static void main(String[] args) {
        // Example usage
        int[] arr = {1, 3, 3, 5, 7};
        int x = 3;
        int position = binarySearchFirst(arr, x);
        System.out.println("First occurrence of element " + x + ": " + position);
    }
}
Complexity Analysis
Time Complexity: $O(\log n)$

Space Complexity: $O(1)$

Complexity Proof: At each algorithm step, the size of the considered subarray is halved. The maximum number of iterations is $\lceil \log_2 n \rceil$.

Execution Example
For array $A = [1, 3, 3, 5, 7]$ and $x = 3$:

Iteration 1: left=0, right=4, mid=2, A[2]=3 → result=2, right=1
Iteration 2: left=0, right=1, mid=0, A[0]=1 < 3 → left=1
Iteration 3: left=1, right=1, mid=1, A[1]=3 → result=1, right=0
Iteration 4: left=1, right=0 → exit
Result: 1
Key Features
Invariant: if $x$ is present in the array, its first occurrence is always within the interval $[left, right]$

Overflow protection: use mid = left + (right - left) / 2 instead of (left + right) / 2

Exit condition: left > right guarantees that the entire array has been checked

