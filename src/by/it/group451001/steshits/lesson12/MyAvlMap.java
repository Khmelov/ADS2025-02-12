package by.it.group451001.steshits.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {
    private static class Node {
        int key;
        String val;
        Node left, right;
        int height;

        Node(int k, String v) {
            key = k;
            val = v;
            height = 1;
        }
    }

    private Node root;
    private int size;
    private String removedValue;

    private int height(Node n) {
        return n == null ? 0 : n.height;
    }

    private void updateHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    private int balanceFactor(Node n) {
        return n == null ? 0 : height(n.left) - height(n.right);
    }

    private Node rotateRight(Node y) {
        Node x = y.left;
        Node T2 = x.right;
        x.right = y;
        y.left = T2;
        updateHeight(y);
        updateHeight(x);
        return x;
    }

    private Node rotateLeft(Node x) {
        Node y = x.right;
        Node T2 = y.left;
        y.left = x;
        x.right = T2;
        updateHeight(x);
        updateHeight(y);
        return y;
    }

    private Node balance(Node n) {
        updateHeight(n);
        int bf = balanceFactor(n);
        if (bf > 1) {
            if (balanceFactor(n.left) < 0) n.left = rotateLeft(n.left);
            return rotateRight(n);
        }
        if (bf < -1) {
            if (balanceFactor(n.right) > 0) n.right = rotateRight(n.right);
            return rotateLeft(n);
        }
        return n;
    }

    private Node insert(Node node, int key, String value) {
        if (node == null) {
            size++;
            return new Node(key, value);
        }
        if (key < node.key) node.left = insert(node.left, key, value);
        else if (key > node.key) node.right = insert(node.right, key, value);
        else node.val = value;
        return balance(node);
    }

    private Node minNode(Node n) {
        Node cur = n;
        while (cur.left != null) cur = cur.left;
        return cur;
    }

    private Node delete(Node node, int key) {
        if (node == null) return null;
        if (key < node.key) node.left = delete(node.left, key);
        else if (key > node.key) node.right = delete(node.right, key);
        else {
            removedValue = node.val;
            if (node.left == null && node.right == null) {
                size--;
                return null;
            } else if (node.left == null) {
                size--;
                return node.right;
            } else if (node.right == null) {
                size--;
                return node.left;
            } else {
                String originalRemoved = removedValue;
                Node succ = minNode(node.right);
                node.key = succ.key;
                node.val = succ.val;
                node.right = delete(node.right, succ.key);
                removedValue = originalRemoved;
            }
        }
        return balance(node);
    }

    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String old = get(key);
        root = insert(root, key.intValue(), value);
        return old;
    }

    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = ((Integer) key).intValue();
        Node cur = root;
        while (cur != null) {
            if (k < cur.key) cur = cur.left;
            else if (k > cur.key) cur = cur.right;
            else return cur.val;
        }
        return null;
    }

    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(key) != null;
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        return remove((Integer) key);
    }

    public String remove(Integer key) {
        if (key == null) return null;
        removedValue = null;
        root = delete(root, key.intValue());
        return removedValue;
    }

    public int size() {
        return size;
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.val == null : value.equals(n.val)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
    }

    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        toStringRec(root, sb, new boolean[]{true});
        sb.append("}");
        return sb.toString();
    }

    private void toStringRec(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        toStringRec(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append("=").append(n.val);
        first[0] = false;
        toStringRec(n.right, sb, first);
    }

    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

    public Set<Integer> keySet() {
        throw new UnsupportedOperationException();
    }

    public Collection<String> values() {
        throw new UnsupportedOperationException();
    }

    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public boolean equals(Object o) {
        return this == o;
    }

    public int hashCode() {
        return System.identityHashCode(this);
    }
}
