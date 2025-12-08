# Name

Sorting students with dynamic criteria

# Task

Sorting students with dynamic criteria

# Theory

Full Name (String name)

Grade Point Average (double gpa)

Number of missed classes (int absences)

Group number (String group)

Requirements:

Create an array of several students (at least 6–7 so that sorting is meaningful).

Offer the user a choice of sorting criteria:

By full name (alphabetically, A → Z)

By GPA (in descending order, from higher to lower)

By number of absences (in ascending order, from lower to higher)

By group number (alphabetically)

Sort the array strictly using the bubble sort algorithm, but in the form of a bidirectional variation (cocktail shaker sort):

First pass from left to right — larger elements “bubble up” to the end.

Then pass from right to left — smaller elements “sink” to the beginning.

Implement optimization: if during a full cycle (a forward and backward pass) no swaps were made, the algorithm must terminate early.

The following are NOT allowed:

Arrays.sort, Collections.sort, List.sort

Comparator or lambda expressions for sorting

Only a fully manual bubble sort implementation is allowed.

After sorting, the program must output the sorted list of students.

