package by.it.group451003.yepanchuntsau.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    // ===== Узел AVL-дерева =====
    private static class Node {
        Integer key;
        String value;
        Node left, right;
        int height = 1;

        Node(Integer k, String v) {
            key = k;
            value = v;
        }
    }

    private Node root = null;
    private int size = 0;

    // ===== Вспомогательные методы =====

    private int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    // --- Правый поворот ---
    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;

        // Поворот
        x.right = y;
        y.left = T2;

        // высоты
        updateHeight(y);
        updateHeight(x);

        return x;
    }

    // --- Левый поворот ---
    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;

        // поворот
        y.left = x;
        x.right = T2;

        updateHeight(x);
        updateHeight(y);

        return y;
    }

    // --- Балансировка ---
    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);

        // LL
        if (bf > 1 && balanceFactor(n.left) >= 0)
            return rotateRight(n);

        // LR
        if (bf > 1 && balanceFactor(n.left) < 0) {
            n.left = rotateLeft(n.left);
            return rotateRight(n);
        }

        // RR
        if (bf < -1 && balanceFactor(n.right) <= 0)
            return rotateLeft(n);

        // RL
        if (bf < -1 && balanceFactor(n.right) > 0) {
            n.right = rotateRight(n.right);
            return rotateLeft(n);
        }

        return n;
    }

    // ===== Вставка =====
    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }

        if (key < node.key)
            node.left = insert(node.left, key, value);
        else if (key > node.key)
            node.right = insert(node.right, key, value);
        else {
            // обновление значения
            node.value = value;
            return node;
        }

        return balance(node);
    }

    // ===== Удаление =====
    private Node removeNode(Node node, Integer key) {
        if (node == null) return null;

        if (key < node.key)
            node.left = removeNode(node.left, key);
        else if (key > node.key)
            node.right = removeNode(node.right, key);
        else {
            // нашли
            size--;

            // 0 или 1 ребёнок
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            // 2 ребёнка → ищем минимальный справа
            Node min = getMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = deleteMin(node.right);
        }

        return balance(node);
    }

    private Node getMin(Node n) {
        while (n.left != null) n = n.left;
        return n;
    }

    private Node deleteMin(Node n) {
        if (n.left == null) return n.right;
        n.left = deleteMin(n.left);
        return balance(n);
    }

    // ===== Поиск =====
    private Node find(Node n, Integer key) {
        while (n != null) {
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else return n;
        }
        return null;
    }

    // ===== Реализация интерфейса Map =====

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        inorder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        inorder(n.right, sb);
    }

    @Override
    public String put(Integer key, String value) {
        Node found = find(root, key);
        String old = (found == null ? null : found.value);
        root = insert(root, key, value);
        return old;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Node found = find(root, (Integer) key);
        if (found == null) return null;
        String old = found.value;
        root = removeNode(root, (Integer) key);
        return old;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find(root, (Integer) key);
        return (n == null ? null : n.value);
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return find(root, (Integer) key) != null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // ==== Остальные методы Map — заглушки (как и требовалось) ====

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
