package by.it.group451001.steshits.lesson12;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.Comparator;

public class MyRbMap implements SortedMap<Integer, String> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private static class Node {
        int key;
        String val;
        Node left, right;
        boolean color;

        Node(int k, String v, boolean c) {
            key = k;
            val = v;
            color = c;
        }
    }

    private Node root;
    private int size;
    private boolean removedFlag;

    private boolean isRed(Node x) {
        return x != null && x.color == RED;
    }

    private Node rotateLeft(Node h) {
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private Node rotateRight(Node h) {
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        return x;
    }

    private void flipColors(Node h) {
        h.color = !h.color;
        if (h.left != null) h.left.color = !h.left.color;
        if (h.right != null) h.right.color = !h.right.color;
    }

    private Node balance(Node h) {
        if (isRed(h.right) && !isRed(h.left)) h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right)) flipColors(h);
        return h;
    }

    private Node moveRedLeft(Node h) {
        flipColors(h);
        if (h.right != null && isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

    private Node moveRedRight(Node h) {
        flipColors(h);
        if (h.left != null && isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

    private Node putRec(Node h, int key, String val) {
        if (h == null) return new Node(key, val, RED);
        if (key < h.key) h.left = putRec(h.left, key, val);
        else if (key > h.key) h.right = putRec(h.right, key, val);
        else h.val = val;
        return balance(h);
    }

    public String put(Integer key, String value) {
        if (key == null) throw new NullPointerException();
        String old = get(key);
        root = putRec(root, key.intValue(), value);
        if (root != null) root.color = BLACK;
        if (old == null) size++;
        return old;
    }

    private Node min(Node x) {
        while (x.left != null) x = x.left;
        return x;
    }

    private Node deleteMin(Node h) {
        if (h.left == null) {
            removedFlag = true;
            return null;
        }
        if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
        h.left = deleteMin(h.left);
        return balance(h);
    }

    private Node deleteRec(Node h, int key) {
        if (h == null) return null;
        if (key < h.key) {
            if (h.left != null) {
                if (!isRed(h.left) && !isRed(h.left.left)) h = moveRedLeft(h);
                h.left = deleteRec(h.left, key);
            }
        } else {
            if (isRed(h.left)) h = rotateRight(h);
            if (key == h.key && h.right == null) {
                removedFlag = true;
                return null;
            }
            if (h.right != null) {
                if (!isRed(h.right) && !isRed(h.right.left)) h = moveRedRight(h);
                if (key == h.key) {
                    Node x = min(h.right);
                    h.key = x.key;
                    h.val = x.val;
                    h.right = deleteMin(h.right);
                    removedFlag = true;
                } else {
                    h.right = deleteRec(h.right, key);
                }
            }
        }
        return balance(h);
    }

    public String remove(Object key) {
        if (!(key instanceof Integer)) return null;
        return remove((Integer) key);
    }

    public String remove(Integer key) {
        if (key == null) return null;
        String old = get(key);
        if (old == null) return null;
        removedFlag = false;
        if (!isRed(root.left) && !isRed(root.right)) if (root != null) root.color = RED;
        root = deleteRec(root, key.intValue());
        if (root != null) root.color = BLACK;
        if (removedFlag) size--;
        return old;
    }

    public String get(Object key) {
        if (!(key instanceof Integer)) return null;
        int k = ((Integer) key).intValue();
        Node x = root;
        while (x != null) {
            if (k < x.key) x = x.left;
            else if (k > x.key) x = x.right;
            else return x.val;
        }
        return null;
    }

    public boolean containsKey(Object key) {
        if (!(key instanceof Integer)) return false;
        return get(key) != null;
    }

    public boolean containsValue(Object value) {
        return containsValueRec(root, value);
    }

    private boolean containsValueRec(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.val == null : value.equals(n.val)) return true;
        return containsValueRec(n.left, value) || containsValueRec(n.right, value);
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

    public SortedMap<Integer, String> headMap(Integer toKey) {
        if (toKey == null) throw new NullPointerException();
        MyRbMap m = new MyRbMap();
        collectHead(root, toKey.intValue(), m);
        return m;
    }

    private void collectHead(Node n, int toKey, MyRbMap m) {
        if (n == null) return;
        if (n.key < toKey) {
            collectHead(n.left, toKey, m);
            m.put(n.key, n.val);
            collectHead(n.right, toKey, m);
        } else {
            collectHead(n.left, toKey, m);
        }
    }

    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        if (fromKey == null) throw new NullPointerException();
        MyRbMap m = new MyRbMap();
        collectTail(root, fromKey.intValue(), m);
        return m;
    }

    private void collectTail(Node n, int fromKey, MyRbMap m) {
        if (n == null) return;
        if (n.key >= fromKey) {
            collectTail(n.left, fromKey, m);
            m.put(n.key, n.val);
            collectTail(n.right, fromKey, m);
        } else {
            collectTail(n.right, fromKey, m);
        }
    }

    public Integer firstKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = root;
        while (x.left != null) x = x.left;
        return x.key;
    }

    public Integer lastKey() {
        if (root == null) throw new java.util.NoSuchElementException();
        Node x = root;
        while (x.right != null) x = x.right;
        return x.key;
    }

    public Comparator<? super Integer> comparator() {
        return null;
    }

    public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) {
        throw new UnsupportedOperationException();
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
