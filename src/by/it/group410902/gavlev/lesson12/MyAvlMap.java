package by.it.group410902.gavlev.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap implements Map<Integer, String> {

    private class Node {
        Integer key;
        String value;
        Node left, right;
        int height;

        Node(Integer key, String value) {
            this.key = key;
            this.value = value;
            this.height = 0;
        }
    }

    private Node root = null;
    private int size = 0;

    private int height(Node n) {
        return n == null ? -1 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    private int balanceFactor(Node n) {
        return height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        y.left = x.right;
        x.right = y;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        x.right = y.left;
        y.left = x;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        if (balanceFactor(n) == 2) {
            if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (balanceFactor(n) == -2) {
            if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node findMin(Node n) {
        return n.left == null ? n : findMin(n.left);
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balance(n);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    private Node treeSearch(Object key) {
        Node cur = root;
        Integer k = (Integer) key;
        while (cur != null) {
            if (k.equals(cur.key)) return cur;
            else if (k < cur.key) cur = cur.left;
            else cur = cur.right;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object key) {
        return treeSearch(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (java.util.Objects.equals(n.value, value)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    @Override
    public String get(Object key) {
        Node n = treeSearch(key);
        return n == null ? null : n.value;
    }

    @Override
    public String put(Integer key, String value) {
        String[] old = new String[1];
        root = insert(root, key, value, old);
        if (old[0] == null) size++;
        return old[0];
    }

    private Node insert(Node node, Integer key, String value, String[] old) {
        if (node == null) return new Node(key, value);
        if (key.equals(node.key)) {
            old[0] = node.value;
            node.value = value;
            return node;
        }
        if (key < node.key) node.left = insert(node.left, key, value, old);
        else node.right = insert(node.right, key, value, old);
        return balance(node);
    }

    @Override
    public String remove(Object key) {
        String[] old = new String[1];
        root = delete(root, (Integer) key, old);
        if (old[0] != null) size--;
        return old[0];
    }

    private Node delete(Node node, Integer key, String[] old) {
        if (node == null) return null;
        if (key.equals(node.key)) {
            old[0] = node.value;
            if (node.right == null) return node.left;
            Node min = findMin(node.right);
            node.key = min.key;
            node.value = min.value;
            node.right = removeMin(node.right);
        } else if (key < node.key) node.left = delete(node.left, key, old);
        else node.right = delete(node.right, key, old);
        return balance(node);
    }

    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    @Override
    public String toString() {
        if (size == 0) return "{}";
        StringBuilder sb = new StringBuilder("{");
        toStringRec(root, sb);
        sb.delete(sb.length() - 2, sb.length()); // убрать последнюю ", "
        sb.append("}");
        return sb.toString();
    }

    private void toStringRec(Node node, StringBuilder sb) {
        if (node == null) return;
        toStringRec(node.left, sb);
        sb.append(node.key).append("=").append(node.value).append(", ");
        toStringRec(node.right, sb);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException();
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
}
