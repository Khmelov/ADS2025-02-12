package by.it.group451001.drzhevetskiy.lesson12;

import java.util.Map;
import java.util.Set;
import java.util.Collection;

public class MyAvlMap implements Map<Integer, String> {

    private static class Node {
        int key;
        String value;
        Node left;
        Node right;
        int height;

        Node(int key, String value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private Node root;
    private int size = 0;

    private int height(Node n) {
        return (n == null) ? 0 : n.height;
    }

    private int balance(Node n) {
        return height(n.right) - height(n.left);
    }

    private void fixHeight(Node n) {
        n.height = Math.max(height(n.left), height(n.right)) + 1;
    }

    private Node rotateRight(Node p) {
        Node q = p.left;
        p.left = q.right;
        q.right = p;
        fixHeight(p);
        fixHeight(q);
        return q;
    }

    private Node rotateLeft(Node p) {
        Node q = p.right;
        p.right = q.left;
        q.left = p;
        fixHeight(p);
        fixHeight(q);
        return q;
    }

    private Node balanceNode(Node n) {
        fixHeight(n);

        int b = balance(n);

        if (b == 2) {
            if (balance(n.right) < 0)
                n.right = rotateRight(n.right);
            return rotateLeft(n);
        }

        if (b == -2) {
            if (balance(n.left) > 0)
                n.left = rotateLeft(n.left);
            return rotateRight(n);
        }

        return n;
    }

    @Override
    public String put(Integer key, String value) {
        String old = get(key);
        root = insert(root, key, value);
        if (old == null) size++;
        return old;
    }

    private Node insert(Node n, int key, String value) {
        if (n == null) return new Node(key, value);

        if (key < n.key)
            n.left = insert(n.left, key, value);
        else if (key > n.key)
            n.right = insert(n.right, key, value);
        else {
            n.value = value;
            return n;
        }

        return balanceNode(n);
    }

    @Override
    public String get(Object key) {
        Node n = root;
        int k = (Integer) key;

        while (n != null) {
            if (k < n.key) n = n.left;
            else if (k > n.key) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public String remove(Object key) {
        String old = get(key);
        if (old != null) {
            root = removeNode(root, (Integer) key);
            size--;
        }
        return old;
    }

    private Node findMin(Node n) {
        return (n.left == null) ? n : findMin(n.left);
    }

    private Node removeMin(Node n) {
        if (n.left == null) return n.right;
        n.left = removeMin(n.left);
        return balanceNode(n);
    }

    private Node removeNode(Node n, int key) {
        if (n == null) return null;

        if (key < n.key)
            n.left = removeNode(n.left, key);
        else if (key > n.key)
            n.right = removeNode(n.right, key);
        else {
            Node L = n.left;
            Node R = n.right;

            if (R == null) return L;

            Node min = findMin(R);
            min.right = removeMin(R);
            min.left = L;
            return balanceNode(min);
        }

        return balanceNode(n);
    }

    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
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


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        inorder(root, sb);
        if (sb.length() > 1) sb.setLength(sb.length() - 2);
        sb.append("}");
        return sb.toString();
    }

    private void inorder(Node n, StringBuilder sb) {
        if (n == null) return;
        inorder(n.left, sb);
        sb.append(n.key).append("=").append(n.value).append(", ");
        inorder(n.right, sb);
    }

    @Override public boolean containsValue(Object value) { throw new UnsupportedOperationException(); }
    @Override public void putAll(Map<? extends Integer, ? extends String> m) { throw new UnsupportedOperationException(); }
    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
