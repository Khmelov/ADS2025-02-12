# Name

Student rating

# Task

Student rating

## Task

Implement selection sorting for an array of students. Each student has a full name and an average grade (an integer from 1 to 5).
Sort the students by grade descending (with the best students first) and display the student ranking.

# Theory

Selection sort is an algorithm that, at each step, finds the minimum element in the unsorted portion of the array and places it at the beginning of that portion. To sort the array, we select the minimum among the unsorted numbers n times and place it in its proper position (namely, in the kth position after the kth iteration). To simplify the implementation, at the kth iteration, we search for the minimum on the interval [k,n−1], swapping it with the current kth element, after which the interval [0,k] will be sorted.

