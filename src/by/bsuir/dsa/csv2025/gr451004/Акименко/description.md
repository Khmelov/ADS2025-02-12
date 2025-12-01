# Name

Merging Two BSTs into a Balanced Tree

# Task

#Merging Two BSTs into a Balanced Tree
You are given two non-empty Binary Search Trees (BST). Your task is to merge them into a single balanced Binary Search Tree.
A balanced tree in this context is defined as a tree where the depths of the left and right subtrees of every node differ by at most 1.
Write a function:
public TreeNode mergeTreesToBalanced(TreeNode root1, TreeNode root2) {
    // Your implementation
}
Input:
root1: The root of the first BST.
root2: The root of the second BST.
Output:
The root of a new balanced BST that contains all nodes from both input trees.
Notes:
The input trees may be unbalanced.
The trees may contain duplicate values. In this case, all duplicates must be preserved in the resulting tree.
The TreeNode structure is defined as:
public class TreeNode {
    int val;
    TreeNode left;
    TreeNode right;
    TreeNode() {}
    TreeNode(int val) { this.val = val; }
    TreeNode(int val, TreeNode left, TreeNode right) {
        this.val = val;
        this.left = left;
        this.right = right;
    }
}
Example:
Input:
Tree 1:      2     Tree 2:      5
            / \                /
           1   3              4

Output (one possible balanced structure):
        3
       / \
      2   5
     /   /
    1   4



# Theory

Бинарное дерево поиска (BST)

Бинарное дерево поиска — это структура данных, в которой каждый узел имеет не более двух потомков и выполняется свойство упорядоченности:

    Все узлы в левом поддереве имеют значения меньшие, чем значение родительского узла

    Все узлы в правом поддереве имеют значения большие, чем значение родительского узла

In-order обход

Для BST in-order обход (левый-корень-правый) возвращает узлы в отсортированном порядке:
text

void inOrder(TreeNode node, List<Integer> list) {
    if (node == null) return;
    inOrder(node.left, list);
    list.add(node.val);
    inOrder(node.right, list);
}

Сбалансированные деревья

Дерево считается сбалансированным, если для каждого узла выполняется:
∣height(left)−height(right)∣≤1
∣height(left)−height(right)∣≤1

где heightheight — высота поддерева.

