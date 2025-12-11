# Name

Lecture schedule optimization

# Task

Lecture schedule optimization
A university has a lecture hall and a set of lectures. Each lecture has:
- Start time `start`
- End time `end`
- Number of attendees `students`
- Priority `priority` (1-high, 2-medium, 3-low)

Lectures cannot overlap in time. It is necessary to create a schedule that maximizes:
1. Total number of attendees
2. Lecture priority (the higher the priority, the lower the number)

**Objective Function**: The total value of `(students * (4 - priority))` for the selected lectures.

# Theory

Mathematical formulation of the problem

## Formal statement

### **Sets and indices**

- $i, j \in \{1, 2, \dots, n\}$ are lecture indices
- $L = \{l_1, l_2, \dots, l_n\}$ is the set of all lectures

### **Parameters**

For each lecture $l_i$, the following are specified:

| Parameter | Designation | Description |
|----------|-------------|------------|
| Start time | $s_i$ | Start time |
| End time | $e_i$ | End time |
| Number of students | $stu_i$ | Number of students |
| Priority | $p_i \in \{1, 2, 3\}$ | Priority level |

### **Decision Variables**

$$
x_i = \begin{cases}
1, & \text{if lecture } l_i \text{ is selected in the schedule} \\
0, & \text{otherwise}
\end{cases}
$$

## Objective Function

**Maximize the total value of the schedule:**

$$
\max Z = \sum_{i=1}^n \left[ x_i \cdot stu_i \cdot (4 - p_i) \right]
$$

### **Objective Function Components:**

- $stu_i$ — contribution of the number of students
- $(4 - p_i)$ — priority weighting coefficient:
- Priority 1: weight = 3
- Priority 2: weight = 2
- Priority 3: weight = 1

## ⛓️ Constraints

### **1. Disjointness of Lectures**

For each pair of lectures $(i, j)$ with intersecting intervals:

$$
x_i + x_j \leq 1, \quad \forall i,j: [s_i, e_i] \cap [s_j, e_j] \neq \emptyset
$$

**Alternative formulation using time slots:**
$$
\sum_{\substack{i \\ s_i \leq t < e_i}} x_i \leq 1, \quad \forall t \in T
$$

### **2. Binary Variables**

$$
x_i \in \{0, 1\}, \quad \forall i \in \{1, 2, \dots, n\}
$$

