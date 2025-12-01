# Name

Parallel Merge Sort

# Task

## Task
Given an array of integers and the number of threads. Implement a parallel version of merge sort in Java.
The program should:
1. Divide the array into the specified number of parts (approximately equal in size).
2. Sort each part in a separate thread using a standard sort (e.g., Arrays.sort).
3. Then, in the main thread, merge the sorted parts into one sorted array using the merge algorithm.
4. Output the sorted array.
### Input format
- First line: two integers `K N`, where `K` is the number of elements in the array (1 ≤ K ≤ 10^5), `N` is the number of threads (1 ≤ N ≤ 16, N ≤ K).
- Second line: `K` integers — array elements (-10^9 ≤ a_i ≤ 10^9).
### Output format
- Single line: sorted array elements separated by spaces.
> **Remarks:**
> - Use the Thread class or Runnable implementation to create threads.
> - After starting threads, use join() to wait for their completion.
> - Perform merging of parts sequentially in the main thread.
> - Recommended:
> - Split the array into subarrays.
> - Sort each subarray in its thread.
> - Then repeatedly merge pairs of sorted arrays.
> - Example code snippet in Java:
> ```java
> class Sorter extends Thread {
>     int[] arr;
>     Sorter(int[] arr) { this.arr = arr; }
>     public void run() {
>         Arrays.sort(arr);
>     }
> }
> // In main: create threads, start, join, then merge.
> ```
---
## Example 1
- Input:
  `3 1`
  `3 1 2`
- Expected output:
  `1 2 3`
---
## Example 2
- Input:
  `4 2`
  `4 3 2 1`
- Expected output:
  `1 2 3 4`


# Theory

## Multithreading in Java
### Definitions
A **thread** is an independent unit of execution within a process. In Java, threads enable concurrent code execution.
**Runnable** is an interface for defining a task that a thread can execute.
**Synchronization** is a mechanism for coordinating access to shared resources, but in this task it is not required as threads work on separate data parts.
The task uses threads for parallel sorting of array parts.
### Conditions for Using Threads
In Java, threads are created by extending Thread or implementing Runnable.
To wait for threads to finish, use the join() method.
This allows distributing computations across multiple CPU cores for speedup.
### Parallel Merge Sort Algorithm
1. Divide the array into N parts.
2. For each part, create a thread that sorts it (e.g., Arrays.sort).
3. Start the threads and wait for completion (join).
4. Merge the sorted parts:
   - Use recursive or iterative merging of array pairs.
   - Merging two sorted arrays: compare elements and copy to a new array.
     The resulting array will be fully sorted.
```

