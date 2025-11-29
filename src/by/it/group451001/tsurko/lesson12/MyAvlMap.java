package by.it.group451001.tsurko.lesson12;

import java.util.*;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        Integer key;
        String value;
        int height;
        Node left;
        Node right;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    private int height(Node node) {
        return node == null ? 0 : node.height;
    }

    private void fixHeight(Node node) {
        node.height = Math.max(height(node.left), height(node.right)) + 1;
    }

    private int balanceFactor(Node node) {
        return height(node.right) - height(node.left);
    }

    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        fixHeight(p);
        fixHeight(q);
        return q;
    }

    private Node rotateLeft(Node q) {
        Node p = q.right;
        q.right = p.left;
        p.left = q;
        fixHeight(q);
        fixHeight(p);
        return p;
    }

    private Node balance(Node node) {
        fixHeight(node);
        int bf = balanceFactor(node);
        if (bf == 2) {
            if (balanceFactor(node.right) < 0)
                node.right = rotateRight(node.right);
            return rotateLeft(node);
        }
        if (bf == -2) {
            if (balanceFactor(node.left) > 0)
                node.left = rotateLeft(node.left);
            return rotateRight(node);
        }
        return node;
    }

    private Node insert(Node node, Integer key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = insert(node.left, key, value);
        else if (cmp > 0)
            node.right = insert(node.right, key, value);
        else
            node.value = value;
        return balance(node);
    }

    private Node find(Node node, Integer key) {
        while (node != null) {
            int cmp = key.compareTo(node.key);
            if (cmp == 0) return node;
            node = (cmp < 0) ? node.left : node.right;
        }
        return null;
    }

    private Node findMin(Node node) {
        return node.left != null ? findMin(node.left) : node;
    }

    private Node removeMin(Node node) {
        if (node.left == null) return node.right;
        node.left = removeMin(node.left);
        return balance(node);
    }

    private Node remove(Node node, Integer key) {
        if (node == null) return null;
        int cmp = key.compareTo(node.key);
        if (cmp < 0)
            node.left = remove(node.left, key);
        else if (cmp > 0)
            node.right = remove(node.right, key);
        else {
            size--;
            Node left = node.left;
            Node right = node.right;
            if (right == null) return left;
            Node min = findMin(right);
            min.right = removeMin(right);
            min.left = left;
            return balance(min);
        }
        return balance(node);
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        root = insert(root, key, value);
        return old;
    }

    @Override
    public String get(Object key) {
        Node node = find(root, (Integer) key);
        return node == null ? null : node.value;
    }

    @Override
    public String remove(Object key) {
        Node node = find(root, (Integer) key);
        if (node == null) return null;
        String old = node.value;
        root = remove(root, (Integer) key);
        return old;
    }

    @Override
    public boolean containsKey(Object key) {
        return find(root, (Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node node, Object value) {
        if (node == null) return false;
        if (Objects.equals(node.value, value)) return true;
        return containsValueRecursive(node.left, value) || containsValueRecursive(node.right, value);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        inOrderToString(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inOrderToString(Node node, StringBuilder sb) {
        if (node == null) return;
        inOrderToString(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        inOrderToString(node.right, sb);
    }
}
