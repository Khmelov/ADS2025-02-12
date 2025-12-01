# Name

Gift wishlist

# Task

Gift wishlist

University students love giving each other gifts. This year, they decided to create gift wish lists of M types for the sake of an experiment.
There are N such lists, but there is only one of each gift type.

What is the maximum number of students who will receive at least one of their desired gifts this year?

## Sample Data and Answer
*Input (3 gift lists, 2 gifts each; 5 gift types)*
5
3 2
4 5
3 2
5 3

*Output*
3

# Theory

Theory: reducing the matching problem to a maximum Flow

To solve the gift distribution problem, we use the maximum matching problem in a bipartite graph and solve it using a maximum flow algorithm.

## 1. Definitions

Let's assume a bipartite graph $G = (U \cup V, E)$, where:
- $U = \{u_1, u_2, \dots, u_N\}$ is the set of students (left part).
- $V = \{v_1, v_2, \dots, v_M\}$ is the set of gift types (right part).
- $E$ is the set of edges. An edge $(u_i, v_j) \in E$ exists if student $u_i$ wants gift $v_j$.

*A matching* $M \subseteq E$ is a set of edges in which no two edges share a common vertex.
*Goal:* Find a matching $M$ of maximum size $|M|$.

## 2. Constructing a transportation network

To apply flow algorithms, construct a new network $G' = (V', E')$:

1. *Add dummy vertices:*
- Source $S$.
- Sink $T$.
- Vertex set: $V' = U \cup V \cup \{S, T\}$.

2. *Construct edges and assign capacities $c(x, y)$:*

- *From the source to students ($S \to U$):*
- For each student $u_i \in U$, draw an edge $S \to u_i$.
- $$c(S, u_i) = 1$$
- _Physical meaning:_ The system receives 1 unit of "need" for each student (they can only take 1 gift).

- *Preferences ($U \to V$):*
- For each preference edge $(u_i, v_j) \in E$, draw a directed edge $u_i \to v_j$.
- $$c(u_i, v_j) = 1 \quad (\text{or } \infty)$$
- _Physical meaning:_ The flow can proceed if the student agrees to this gift.

* *From gifts to the sink ($V \to T$):*
- For each gift $v_j \in V$, draw an edge $v_j \to T$.
- $$c(v_j, T) = 1$$
- _Physical meaning:_ There is only one copy of a given gift.

## 3. Main theorem

The maximum flow $f_{max}$ in the constructed network $G'$ is numerically equal to the size of the maximum matching in the original graph $G$.

$$|M_{max}| = |f_{max}|$$

