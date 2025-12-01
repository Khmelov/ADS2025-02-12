# Name

Expert Resource Allocation

# Task

Expert Resource Allocation

You are the CEO of the consulting firm **"Company"**. You have $N$ **Specialists** and $M$ **Client Projects** to manage.

Every **Specialist** possesses a specific **Expertise Level** ($A_i$). Every **Project** requires a minimum **Admittance Level** ($B_j$) and has a **Return Parameter** ($C_j$).

To undertake a project, the Expertise Level of the selected Specialist ($A_i$) must be **at least** the project's Admittance Level ($B_j$).

All projects fall into two categories, determined by the **Return Parameter** ($C_j$):

## 1. Development Projects ($C_j > 0$)

These are complex projects related to training and certification.

* **Resource Use:** The Specialist is temporarily assigned to the project.
* **Outcome:** Upon completion, the Specialist is **returned** to the resource pool, but their **Expertise Level** is permanently updated to $A_{\text{new}} = \max(A_{\text{current}}, C_j)$.

## 2. Consumption Projects ($C_j = 0$)

These are one-time tasks involving the expenditure of unique, irreplaceable skills.

* **Resource Use:** The Specialist is permanently committed to the project.
* **Outcome:** Upon completion, the Specialist **leaves** the company (is removed from the resource pool).

---

## Goal

Determine the **maximum number of Projects** the company can complete by optimally allocating its specialists.

# Theory

We will call assigning a **Specialist** to a **Project** operation $i$, and refer to the Specialist with less Expertise Level as "the smaller Specialist."

## Lemma 1: Stage Order

It's optimal to execute **Consumption Projects** ($C_j = 0$) only **after** completing all **Development Projects** ($C_k > 0$).

**Proof:**
Suppose we execute Consumption Project $j$ before Development Project $k$. Since Project $j$ yields no new resources, the Specialist used for Project $k$ must exist before Project $j$. Therefore, we can always defer operation $j$ until operation $k$ is complete.

We call the process of completing $C_j > 0$ projects **Stage 1**, and $C_j = 0$ projects **Stage 2**.

## Lemma 2: Stage 1 Ordering (Development)

In Stage 1, it's optimal to execute Projects in **increasing order** of their minimum **Admittance Level** ($B_j$).

**Proof:**
Sorting by $B_j$ ensures that if a resource can complete the current project, it could have completed all prior projects. This facilitates the earliest possible utilization of the maximum upgrade potential ($C_j$).

## Lemma 3: Specialist Choice in Stage 1

In Stage 1, it's optimal to use the **smallest possible Specialist** whose Expertise Level $A_i \geq B_j$.

**Proof (Rationale):**
Using the smallest $A_i$ guarantees that stronger Specialists are preserved for potentially tougher future projects. Since a Specialist's level is updated to $A_{\text{new}} = \max(A_{\text{old}}, C_j)$, using a smaller $A_{\text{old}}$ (especially when $C_j$ is high) is more likely to result in a superior pool of resources (the multiset of resources after the operation with the smaller Specialist **dominates** the multiset obtained with the larger Specialist).

### Lemma 4 & 5: Stage 2 (Consumption)

In Stage 2 ($C_j=0$ Projects), the order of projects doesn't matter, but it is **optimal to use the smallest possible Specialist** for each project.

**Rationale:**
Since resources are permanently consumed in this Stage, using the smallest Specialist $A_i \geq B_j$ preserves stronger resources for any remaining projects that might have higher requirements.

## The Greedy Algorithm

1.  **Stage 1 (Development):**
    * Use a **Min-Heap** to maintain all available Expertise Levels ($A_i$).
    * Process $C_j > 0$ projects in **increasing order of $B_j$**.
    * For each project: Extract the weakest Specialist from the heap; if $A_i < B_j$, store them for Stage 2; if $A_i \geq B_j$, use them, update $A_i$ to $\max(A_i, C_j)$, and re-insert into the heap.
2.  **Stage 2 (Consumption):**
    * Gather all remaining resources ($A_i$ from the heap + saved resources).
    * Sort the gathered resources and sort the $C_j = 0$ Projects (by $B_j$).
    * Use a **two-pointer method** to greedily match the smallest capable Specialist to each Project.

The time complexity is $O(N \log N)$.

