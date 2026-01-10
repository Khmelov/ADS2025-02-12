package by.it.group410901.kliaus.lesson12;

import java.util.*;

public class MyAvlMap<K extends Comparable<K>, V> implements Map<K, V> {

    private class Node {
        K key;
        V value;
        Node left, right;
        int height = 1;

        Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    private Node root;
    private int size = 0;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private int balance(Node n) {
        return (n == null) ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node t2 = x.right;
        x.right = y;
        y.left = t2;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node t2 = y.left;
        y.left = x;
        x.right = t2;
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        return y;
    }

    private Node balanceNode(Node node) {
        int bf = balance(node);
        if (bf > 1) {
            if (balance(node.left) < 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        if (bf < -1) {
            if (balance(node.right) > 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        return node;
    }

    private Node put(Node node, K key, V value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = put(node.left, key, value);
        else if (cmp > 0) node.right = put(node.right, key, value);
        else node.value = value;

        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balanceNode(node);
    }

    @Override
    public V put(K key, V value) {
        V old = get(key);
        root = put(root, key, value);
        return old;
    }

    private Node minNode(Node node) {
        while (node.left != null) node = node.left;
        return node;
    }

    private Node remove(Node node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0) node.left = remove(node.left, key);
        else if (cmp > 0) node.right = remove(node.right, key);
        else {
            size--;
            if (node.left == null) return node.right;
            else if (node.right == null) return node.left;
            Node min = minNode(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = remove(node.right, min.key);
        }
        node.height = 1 + Math.max(height(node.left), height(node.right));
        return balanceNode(node);
    }

    @Override
    public V remove(Object key) {
        V old = get(key);
        root = remove(root, (K) key);
        return old;
    }

    private V get(Node node, K key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return node.value;
        if (cmp < 0) return get(node.left, key);
        return get(node.right, key);
    }

    @Override
    public V get(Object key) {
        return get(root, (K) key);
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private void toString(Node node, StringBuilder sb) {
        if (node == null) return;
        toString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        toString(node.right, sb);
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder("{");
        toString(root, sb);
        if (sb.length() > 2) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    // ===== Неиспользуемые методы интерфейса Map =====

    @Override public void putAll(Map<? extends K, ? extends V> m) { for (var e : m.entrySet()) put(e.getKey(), e.getValue()); }
    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public Set<K> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<V> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<K, V>> entrySet() { throw new UnsupportedOperationException(); }
}
