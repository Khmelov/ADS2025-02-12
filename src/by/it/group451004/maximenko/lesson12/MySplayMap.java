package by.it.group451004.maximenko.lesson12;

import java.util.*;

@SuppressWarnings("all")
public class MySplayMap implements NavigableMap<Integer, String> {

    // ================= NODE ==================

    private static class Node {
        Integer key;
        String value;
        Node left, right, parent;

        Node(Integer k, String v) {
            key = k;
            value = v;
        }
    }

    private Node root = null;
    private int size = 0;

    // ================= BASIC TREE OPS =================

    private void rotateRight(Node x) {
        Node p = x.parent;
        Node y = x.left;
        x.left = y.right;
        if (y.right != null) y.right.parent = x;
        y.right = x;
        x.parent = y;
        replaceParent(p, x, y);
    }

    private void rotateLeft(Node x) {
        Node p = x.parent;
        Node y = x.right;
        x.right = y.left;
        if (y.left != null) y.left.parent = x;
        y.left = x;
        x.parent = y;
        replaceParent(p, x, y);
    }

    private void replaceParent(Node parent, Node oldChild, Node newChild) {
        if (parent == null) {
            root = newChild;
        } else if (parent.left == oldChild) {
            parent.left = newChild;
        } else {
            parent.right = newChild;
        }
        if (newChild != null) newChild.parent = parent;
    }

    // Зиг-заг, зиг-зиг: splay
    private void splay(Node x) {
        while (x.parent != null) {
            Node p = x.parent;
            Node g = p.parent;
            if (g == null) {
                if (x == p.left) rotateRight(p);
                else rotateLeft(p);
            } else {
                boolean zigzig = (x == p.left && p == g.left) || (x == p.right && p == g.right);
                if (zigzig) {
                    if (x == p.left) { rotateRight(g); rotateRight(p); }
                    else { rotateLeft(g); rotateLeft(p); }
                } else {
                    if (x == p.left) { rotateRight(p); rotateLeft(g); }
                    else { rotateLeft(p); rotateRight(g); }
                }
            }
        }
    }

    private Node findNode(Integer key) {
        Node cur = root;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp == 0) return cur;
            cur = (cmp < 0) ? cur.left : cur.right;
        }
        return null;
    }

    // ================= PUT =================

    @Override
    public String put(Integer key, String value) {
        if (root == null) {
            root = new Node(key, value);
            size = 1;
            return null;
        }
        Node cur = root, parent = null;
        int cmp = 0;
        while (cur != null) {
            parent = cur;
            cmp = key.compareTo(cur.key);
            if (cmp == 0) {
                String old = cur.value;
                cur.value = value;
                splay(cur);
                return old;
            }
            cur = (cmp < 0) ? cur.left : cur.right;
        }

        Node newNode = new Node(key, value);
        if (cmp < 0) parent.left = newNode;
        else parent.right = newNode;
        newNode.parent = parent;

        splay(newNode);
        size++;
        return null;
    }

    // ================= GET =================

    @Override
    public String get(Object key) {
        Node n = findNode((Integer) key);
        if (n != null) {
            splay(n);
            return n.value;
        }
        return null;
    }

    // ================= REMOVE =================

    @Override
    public String remove(Object keyObj) {
        Integer key = (Integer) keyObj;
        Node n = findNode(key);
        if (n == null) return null;

        splay(n);

        String old = n.value;

        if (n.left == null) {
            replaceParent(n.parent, n, n.right);
        } else if (n.right == null) {
            replaceParent(n.parent, n, n.left);
        } else {
            // max in left
            Node m = n.left;
            while (m.right != null) m = m.right;
            splay(m);
            m.right = n.right;
            n.right.parent = m;
            root = m;
        }

        size--;
        return old;
    }

    // ================= UTILITIES =================

    @Override
    public void putAll(Map<? extends Integer, ? extends String> m) {
        for (Map.Entry<? extends Integer, ? extends String> e : m.entrySet()) {
            put(e.getKey(), e.getValue());
        }
    }


    @Override
    public boolean containsKey(Object key) {
        return findNode((Integer) key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        return containsValueRecursive(root, value);
    }

    private boolean containsValueRecursive(Node n, Object value) {
        if (n == null) return false;
        if (value == null ? n.value == null : value.equals(n.value)) {
            return true;
        }
        return containsValueRecursive(n.left, value) ||
                containsValueRecursive(n.right, value);
    }


    @Override
    public int size() { return size; }

    @Override
    public void clear() { root = null; size = 0; }

    @Override
    public boolean isEmpty() { return size == 0; }

    // ================= NAVIGABLE OPS =================

    private Node firstNode() {
        if (root == null) return null;
        Node n = root;
        while (n.left != null) n = n.left;
        return n;
    }

    private Node lastNode() {
        if (root == null) return null;
        Node n = root;
        while (n.right != null) n = n.right;
        return n;
    }

    @Override
    public Integer firstKey() {
        Node n = firstNode();
        return (n == null ? null : n.key);
    }

    @Override
    public Integer lastKey() {
        Node n = lastNode();
        return (n == null ? null : n.key);
    }

    private Integer lowerOrHigherKey(Integer key, boolean lower, boolean strict) {
        Node cur = root;
        Integer best = null;
        while (cur != null) {
            int cmp = key.compareTo(cur.key);
            if (cmp > 0) {
                if (lower) best = cur.key;
                cur = cur.right;
            } else if (cmp < 0) {
                if (!lower) best = cur.key;
                cur = cur.left;
            } else {
                if (strict)
                    cur = lower ? cur.left : cur.right;
                else
                    return cur.key;
            }
        }
        return best;
    }

    @Override public Integer lowerKey(Integer key) { return lowerOrHigherKey(key, true, true); }
    @Override public Integer floorKey(Integer key) { return lowerOrHigherKey(key, true, false); }
    @Override public Integer ceilingKey(Integer key) { return lowerOrHigherKey(key, false, false); }
    @Override public Integer higherKey(Integer key) { return lowerOrHigherKey(key, false, true); }

    // ================= headMap & tailMap =================

    @Override
    public SortedMap<Integer, String> headMap(Integer toKey) {
        MySplayMap m = new MySplayMap();
        inOrderCopy(root, m, null, toKey);
        return m;
    }

    @Override
    public SortedMap<Integer, String> tailMap(Integer fromKey) {
        MySplayMap m = new MySplayMap();
        inOrderCopy(root, m, fromKey, null);
        return m;
    }

    private void inOrderCopy(Node n, MySplayMap m, Integer from, Integer to) {
        if (n == null) return;
        inOrderCopy(n.left, m, from, to);
        boolean ok = true;
        if (from != null && n.key < from) ok = false;
        if (to != null && n.key >= to) ok = false;
        if (ok) m.put(n.key, n.value);
        inOrderCopy(n.right, m, from, to);
    }

    // ================= ENTRY SUPPORT =================

    private static class SimpleEntry implements Map.Entry<Integer,String> {
        private final Integer k;
        private String v;
        SimpleEntry(Integer k, String v) { this.k = k; this.v = v; }
        @Override public Integer getKey() { return k; }
        @Override public String getValue() { return v; }
        @Override public String setValue(String nv) { String o = v; v = nv; return o; }
    }

    @Override
    public Entry<Integer,String> firstEntry() {
        Node n = firstNode();
        return (n == null) ? null : new SimpleEntry(n.key, n.value);
    }

    @Override
    public Entry<Integer,String> lastEntry() {
        Node n = lastNode();
        return (n == null) ? null : new SimpleEntry(n.key, n.value);
    }

    @Override
    public Entry<Integer,String> lowerEntry(Integer key) {
        Integer k = lowerKey(key);
        return (k == null) ? null : new SimpleEntry(k, get(k));
    }

    @Override
    public Entry<Integer,String> floorEntry(Integer key) {
        Integer k = floorKey(key);
        return (k == null) ? null : new SimpleEntry(k, get(k));
    }

    @Override
    public Entry<Integer,String> ceilingEntry(Integer key) {
        Integer k = ceilingKey(key);
        return (k == null) ? null : new SimpleEntry(k, get(k));
    }

    @Override
    public Entry<Integer,String> higherEntry(Integer key) {
        Integer k = higherKey(key);
        return (k == null) ? null : new SimpleEntry(k, get(k));
    }

    @Override
    public Entry<Integer,String> pollFirstEntry() {
        Integer k = firstKey();
        if (k == null) return null;
        String v = remove(k);
        return new SimpleEntry(k, v);
    }

    @Override
    public Entry<Integer,String> pollLastEntry() {
        Integer k = lastKey();
        if (k == null) return null;
        String v = remove(k);
        return new SimpleEntry(k, v);
    }

    // ================= toString =================

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        boolean[] first = { true };
        toStringDFS(root, sb, first);
        sb.append("}");
        return sb.toString();
    }

    private void toStringDFS(Node n, StringBuilder sb, boolean[] first) {
        if (n == null) return;
        toStringDFS(n.left, sb, first);
        if (!first[0]) sb.append(", ");
        sb.append(n.key).append("=").append(n.value);
        first[0] = false;
        toStringDFS(n.right, sb, first);
    }

    // ===================== UNUSED REQUIRED METHODS ======================

    @Override public Comparator<? super Integer> comparator() { return null; }

    @Override public NavigableSet<Integer> navigableKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableSet<Integer> descendingKeySet() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> descendingMap() { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> subMap(Integer fromKey, boolean fromInclusive, Integer toKey, boolean toInclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> headMap(Integer toKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public NavigableMap<Integer, String> tailMap(Integer fromKey, boolean inclusive) { throw new UnsupportedOperationException(); }
    @Override public SortedMap<Integer, String> subMap(Integer fromKey, Integer toKey) { throw new UnsupportedOperationException(); }

    @Override public Set<Integer> keySet() { throw new UnsupportedOperationException(); }
    @Override public Collection<String> values() { throw new UnsupportedOperationException(); }
    @Override public Set<Map.Entry<Integer, String>> entrySet() { throw new UnsupportedOperationException(); }
}
