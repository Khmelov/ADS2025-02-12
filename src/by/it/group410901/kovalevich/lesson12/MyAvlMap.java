package by.it.group410901.kovalevich.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class MyAvlMap implements Map<Integer, String> {

    private static final class Node {
        int key;
        String value;
        int height;
        Node left, right;
        Node(int key, String value) { this.key = key; this.value = value; this.height = 1; }
    }

    private Node root;
    private int size;

    private static int h(Node n) { return n == null ? 0 : n.height; }
    private static void fix(Node n) { n.height = Math.max(h(n.left), h(n.right)) + 1; }
    private static int bf(Node n) { return n == null ? 0 : h(n.right) - h(n.left); }

    private static Node rotR(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        fix(p); fix(q);
        return q;
    }

    private static Node rotL(Node q) {
        Node p = q.right;
        q.right = p.left;
        p.left = q;
        fix(q); fix(p);
        return p;
    }

    private static Node balance(Node n) {
        if (n == null) return null;
        fix(n);
        int b = bf(n);
        if (b == 2) {
            if (bf(n.right) < 0) n.right = rotR(n.right);
            return rotL(n);
        }
        if (b == -2) {
            if (bf(n.left) > 0) n.left = rotL(n.left);
            return rotR(n);
        }
        return n;
    }

    private static Node min(Node n) { while (n.left != null) n = n.left; return n; }

    private Node putNode(Node n, int key, String val, Holder old) {
        if (n == null) { size++; return new Node(key, val); }
        if (key < n.key) n.left = putNode(n.left, key, val, old);
        else if (key > n.key) n.right = putNode(n.right, key, val, old);
        else { old.s = n.value; n.value = val; return n; }
        return balance(n);
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balance(n);
    }

    private Node removeNode(Node n, int key, Holder old) {
        if (n == null) return null;
        if (key < n.key) n.left = removeNode(n.left, key, old);
        else if (key > n.key) n.right = removeNode(n.right, key, old);
        else {
            old.s = n.value;
            size--;
            if (n.left == null) return n.right;
            if (n.right == null) return n.left;
            Node m = min(n.right);
            n.key = m.key; n.value = m.value;
            n.right = removeMin(n.right);
        }
        return balance(n);
    }

    private Node find(Node n, int key) {
        while (n != null) {
            if (key < n.key) n = n.left;
            else if (key > n.key) n = n.right;
            else return n;
        }
        return null;
    }

    private static final class Holder { String s; }

    @Override
    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        Holder old = new Holder();
        root = putNode(root, key, value, old);
        return old.s;
    }

    @Override
    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        Holder old = new Holder();
        int before = size;
        root = removeNode(root, (Integer) key, old);
        return size == before ? null : old.s;
    }

    @Override
    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        Node n = find(root, (Integer) key);
        return n == null ? null : n.value;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return find(root, (Integer) key) != null;
    }

    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        if (sb.length() > 1) sb.append(", ");
        sb.append(n.key).append('=').append(n.value);
        inorder(n.right, sb);
    }

    @Override
    public String toString() {
        if (root == null) return "{}";
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        inorder(root, sb);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public boolean containsValue(Object value) { return containsValue(root, (String) value); }

    private boolean containsValue(Node n, String v) {
        if (n == null) return false;
        if (v == null ? n.value == null : v.equals(n.value)) return true;
        return containsValue(n.left, v) || containsValue(n.right, v);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Entry<? extends Integer, ? extends String> e : m.entrySet()) put(e.getKey(), e.getValue());
    }

    @Override
    public Set<Integer> keySet() { throw new UnsupportedOperationException(); }

    @Override
    public Collection<String> values() { throw new UnsupportedOperationException(); }

    @Override
    public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
