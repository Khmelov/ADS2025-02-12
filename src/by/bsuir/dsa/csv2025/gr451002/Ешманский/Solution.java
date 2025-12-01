package by.bsuir.dsa.csv2025.gr451002.Ешманский;



import java.util.Random;

public class Solution {
    private static final Random random = new Random(42);
    private Node root;

    private static class Node {
        int value;
        long priority;
        int size;
        Node left, right;

        Node(int value) {
            this.value = value;
            this.priority = random.nextLong();
            this.size = 1;
        }
    }

    private static class SplitResult {
        Node left, right;
        SplitResult(Node left, Node right) { this.left = left; this.right = right; }
    }

    public Solution() {}

    // Строим treap из массива
    public Solution(int[] array) {
        for (int value : array) {
            insert(size(), value); // вставляем каждый элемент в конец
        }
    }

    private int size(Node node) {
        return node == null ? 0 : node.size;
    }

    private void update(Node node) {
        if (node != null) {
            node.size = 1 + size(node.left) + size(node.right);
        }
    }

    private SplitResult split(Node node, int k) {
        if (node == null) return new SplitResult(null, null);

        int leftSize = size(node.left);
        if (k <= leftSize) {
            SplitResult leftSplit = split(node.left, k);
            node.left = leftSplit.right;
            update(node);
            return new SplitResult(leftSplit.left, node);
        } else {
            SplitResult rightSplit = split(node.right, k - leftSize - 1);
            node.right = rightSplit.left;
            update(node);
            return new SplitResult(node, rightSplit.right);
        }
    }

    private Node merge(Node left, Node right) {
        if (left == null) return right;
        if (right == null) return left;

        if (left.priority > right.priority) {
            left.right = merge(left.right, right);
            update(left);
            return left;
        } else {
            right.left = merge(left, right.left);
            update(right);
            return right;
        }
    }

    // ---------------- Основные операции ----------------

    public void insert(int index, int value) {
        if (index < 0 || index > size()) throw new IllegalArgumentException("Index out of bounds");
        SplitResult split = split(root, index);
        Node newNode = new Node(value);
        root = merge(merge(split.left, newNode), split.right);
    }

    public void delete(int index) {
        if (index < 0 || index >= size()) throw new IllegalArgumentException("Index out of bounds");
        SplitResult split1 = split(root, index);
        SplitResult split2 = split(split1.right, 1); // вырезаем узел
        root = merge(split1.left, split2.right);
    }

    public void update(int index, int value) {
        if (index < 0 || index >= size()) throw new IllegalArgumentException("Index out of bounds");
        SplitResult split1 = split(root, index);
        SplitResult split2 = split(split1.right, 1); // нужный узел
        Node newNode = new Node(value);
        root = merge(merge(split1.left, newNode), split2.right);
    }

    public int get(int index) {
        if (index < 0 || index >= size()) throw new IllegalArgumentException("Index out of bounds");
        return get(root, index);
    }

    private int get(Node node, int index) {
        int leftSize = size(node.left);
        if (index < leftSize) {
            return get(node.left, index);
        } else if (index == leftSize) {
            return node.value;
        } else {
            return get(node.right, index - leftSize - 1);
        }
    }

    public int size() {
        return size(root);
    }

    // ---------------- Для тестирования ----------------
    public int[] toArray() {
        int[] result = new int[size()];
        toArray(root, result, 0);
        return result;
    }

    private int toArray(Node node, int[] arr, int index) {
        if (node == null) return index;
        index = toArray(node.left, arr, index);
        arr[index++] = node.value;
        return toArray(node.right, arr, index);
    }
}



