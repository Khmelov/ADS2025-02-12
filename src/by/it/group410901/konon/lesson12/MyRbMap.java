package by.it.group410901.konon.lesson12;

import java.util.*;

public class MyRbMap implements SortedMap<Integer, String> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String value;
        Node left, right, parent;
        boolean color;

        Node(int key, String value, boolean color, Node parent) {
            this.key = key;
            this.value = value;
            this.color = color;
            this.parent = parent;
        }
    }

    private Node root;
    private int size = 0;

    private int compare(int a, int b) {
        return Integer.compare(a, b);
    }

    private void rotateLeft(Node x) {
        Node y = x.right;
        if (y == null) return;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.parent = x.parent;
        if (x.parent == null) root = y;
        else if (x == x.parent.left) x.parent.left = y;
        else x.parent.right = y;
        y.left = x;
        x.parent = y;
    }

    private void rotateRight(Node y) {
        Node x = y.left;
        if (x == null) return;
        y.left = x.right;
        if (x.right != null) x.right.parent = y;
        x.parent = y.parent;
        if (y.parent == null) root = x;
        else if (y == y.parent.left) y.parent.left = x;
        else y.parent.right = x;
        x.right = y;
        y.parent = x;
    }

    private void fixAfterInsertion(Node z) {
        z.color = RED;
        while (z != null && z != root && z.parent.color == RED) {
            if (z.parent == z.parent.parent.left) {
                Node y = z.parent.parent.right;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.right) {
                        z = z.parent;
                        rotateLeft(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateRight(z.parent.parent);
                }
            } else {
                Node y = z.parent.parent.left;
                if (y != null && y.color == RED) {
                    z.parent.color = BLACK;
                    y.color = BLACK;
                    z.parent.parent.color = RED;
                    z = z.parent.parent;
                } else {
                    if (z == z.parent.left) {
                        z = z.parent;
                        rotateRight(z);
                    }
                    z.parent.color = BLACK;
                    z.parent.parent.color = RED;
                    rotateLeft(z.parent.parent);
                }
            }
        }
        if (root != null) root.color = BLACK;
    }

    @Override
    public String put(Integer keyObj, String value) {
        if (keyObj == null) throw new NullPointerException("MyRbMap does not support null keys");
        int key = keyObj;
        if (root == null) {
            root = new Node(key, value, BLACK, null);
            size = 1;
            return null;
        }
        Node current = root;
        Node parent = null;
        int cmp = 0;
        while (current != null) {
            parent = current;
            cmp = compare(key, current.key);
            if (cmp < 0) current = current.left;
            else if (cmp > 0) current = current.right;
            else {
                String old = current.value;
                current.value = value;
                return old;
            }
        }
        Node newNode = new Node(key, value, RED, parent);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        fixAfterInsertion(newNode);
        size++;
        return null;
    }

    @Override
    public String get(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        int key = (Integer) keyObj;
        Node n = root;
        while (n != null) {
            int cmp = compare(key, n.key);
            if (cmp < 0) n = n.left;
            else if (cmp > 0) n = n.right;
            else return n.value;
        }
        return null;
    }

    @Override
    public boolean containsKey(Object keyObj) {
        if (!(keyObj instanceof Integer)) return false;
        return get(keyObj) != null;
    }

    @Override
    public boolean containsValue(Object valueObj) {
        return containsValueRec(root, valueObj);
    }

    private boolean containsValueRec(Node node, Object valueObj) {
        if (node == null) return false;
        if (node.value == null) {
            if (valueObj == null) return true;
        } else {
            if (node.value.equals(valueObj)) return true;
        }
        return containsValueRec(node.left, valueObj) || containsValueRec(node.right, valueObj);
    }

    @Override
    public String remove(Object keyObj) {
        if (!(keyObj instanceof Integer)) return null;
        Integer key = (Integer) keyObj;
        String old = get(key);
        if (old == null) return null;
        MyRbMap rebuilt = new MyRbMap();
        rebuildWithout(root, rebuilt, key);
        this.root = rebuilt.root;
        this.size = rebuilt.size;
        return old;
    }

    private void rebuildWithout(Node node, MyRbMap target, Integer skipKey) {
        if (node == null) return;
        rebuildWithout(node.left, target, skipKey);
        if (!Integer.valueOf(node.key).equals(skipKey)) target.put(node.key, node.value);
        rebuildWithout(node.right, target, skipKey);
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
        ToStringState st = new ToStringState();
        inorderToString(root, sb, st);
        sb.append("}");
        return sb.toString();
    }

    private static class ToStringState { boolean first = true; }

    private void inorderToString(Node node, StringBuilder sb, ToStringState st) {
        if (node == null) return;
        inorderToString(node.left, sb, st);
        if (!st.first) sb.append(", ");
        sb.append(node.key).append("=").append(node.value);
        st.first = false;
        inorderToString(node.right, sb, st);
    }

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap();
        collectHead(root, res, toKey);
        return res;
    }

    private void collectHead(Node node, MyRbMap res, Integer toKey) {
        if (node == null) return;
        collectHead(node.left, res, toKey);
        if (node.key < toKey) res.put(node.key, node.value);
        collectHead(node.right, res, toKey);
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap res = new MyRbMap();
        collectTail(root, res, fromKey);
        return res;
    }

    private void collectTail(Node node, MyRbMap res, Integer fromKey) {
        if (node == null) return;
        collectTail(node.left, res, fromKey);
        if (node.key >= fromKey) res.put(node.key, node.value);
        collectTail(node.right, res, fromKey);
    }

    @Override
    public Integer firstKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.left != null) n = n.left;
        return n.key;
    }

    @Override
    public Integer lastKey() {
        if (root == null) throw new NoSuchElementException();
        Node n = root;
        while (n.right != null) n = n.right;
        return n.key;
    }

    @Override
    public Comparator<? super Integer> comparator() {
        throw new UnsupportedOperationException("comparator not supported");
    }

    @Override
    public Set<Integer> keySet() {
        throw new UnsupportedOperationException("keySet not supported");
    }

    @Override
    public Collection<String> values() {
        throw new UnsupportedOperationException("values not supported");
    }

    @Override
    public Set<Entry<Integer, String>> entrySet() {
        throw new UnsupportedOperationException("entrySet not supported");
    }

    @Override
    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException("subMap not supported");
    }

    public boolean containsValue(String value) {
        return containsValueRec(root, value);
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        throw new UnsupportedOperationException("putAll not supported");
    }

    @Override
    public boolean equals(Object o) {
        throw new UnsupportedOperationException("equals not supported");
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode not supported");
    }
}
